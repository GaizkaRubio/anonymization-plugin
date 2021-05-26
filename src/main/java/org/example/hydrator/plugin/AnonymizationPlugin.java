package org.example.hydrator.plugin;

import io.cdap.cdap.api.annotation.Description;
import io.cdap.cdap.api.annotation.Name;
import io.cdap.cdap.api.annotation.Plugin;
import io.cdap.cdap.api.data.format.StructuredRecord;
import io.cdap.cdap.api.data.schema.Schema;
import io.cdap.cdap.api.data.schema.Schema.Field;
import io.cdap.cdap.etl.api.Emitter;
import io.cdap.cdap.etl.api.PipelineConfigurer;
import io.cdap.cdap.etl.api.Transform;
import io.cdap.cdap.etl.api.TransformContext;
import io.vavr.control.Try;
import org.example.hydrator.plugin.constants.Constants;
import org.example.hydrator.plugin.tink.AeadHelper;
import org.example.hydrator.plugin.tink.DeterAeadHelper;
import org.example.hydrator.plugin.tink.TinkHelper;
import org.example.hydrator.plugin.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import static org.example.hydrator.plugin.constants.Constants.AEAD;
import static org.example.hydrator.plugin.constants.Constants.ENCRYPT;

@Plugin(type = Transform.PLUGIN_TYPE)
@Name("AnonymizationPlugin")
@Description("A plugin that uses Aead algorithm to anonymize or de-anonymize data.")
public class AnonymizationPlugin extends Transform<StructuredRecord, StructuredRecord> {

  private static final Logger LOG = LoggerFactory.getLogger(AnonymizationPlugin.class);

  private final AnonymizationConfig config;

  private Schema outputSchema;
  private TinkHelper aeadHelper;
  private Map<String, String> keyTypeMap;
  private Map<String, String> categoriesConfig;
  private BiFunction<String, String, Try<byte[]>> encryptFunction;


  public AnonymizationPlugin(AnonymizationConfig config) {
    this.config = config;
  }

  public void configurePipeline(PipelineConfigurer pipelineConfigurer) {
    try {
      outputSchema = Schema.parseJson(config.getSchema());
      pipelineConfigurer.getStageConfigurer().setOutputSchema(outputSchema);
    } catch (IOException e) {
      LOG.error(e.getMessage());
    }
  }

  public void initialize(TransformContext context) throws Exception {
    this.categoriesConfig = StringUtils.createKeyValuePairs(this.config.getCategoryConfig());
    this.keyTypeMap = StringUtils.createKeyValuePairs(this.config.getCategoryKeyConfig());
    if (this.config.getEncryptFunction().equals(AEAD)) {
      this.aeadHelper = new AeadHelper();
    } else {
      this.aeadHelper = new DeterAeadHelper();
    }

    if (this.config.getAnonymizationMode().equals(ENCRYPT)) {
      this.encryptFunction = (key, text) -> Try.of(() -> aeadHelper.encrypt(key, text));
    } else {
      this.encryptFunction = (key, text) -> Try.of(() -> aeadHelper.decrypt(key, text));
    }
  }

  @Override
  public void transform(StructuredRecord structuredRecord, Emitter<StructuredRecord> emitter) throws Exception {
    List<Field> fields = structuredRecord.getSchema().getFields();
    StructuredRecord.Builder builder = StructuredRecord.builder(structuredRecord.getSchema());
    for(Field field : fields) {
      String name = field.getName();
      String value = structuredRecord.get(name);
      String newValue = value;
      if (this.categoriesConfig.containsKey(name)) {
        String category = this.categoriesConfig.get(name);
        byte[] encrypted = this.encryptFunction.apply(keyTypeMap.get(category), value)
            .getOrElse(newValue.getBytes());
        newValue = Base64.getEncoder().encodeToString(encrypted);
      }
      builder.set(name, newValue);
    }
    emitter.emit(builder.build());
  }
}
