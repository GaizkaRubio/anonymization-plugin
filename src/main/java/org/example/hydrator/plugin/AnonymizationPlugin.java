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

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiFunction;

import static org.example.hydrator.plugin.constants.Constants.AEAD;

@Plugin(type = Transform.PLUGIN_TYPE)
@Name("AnonymizationPlugin")
@Description("A plugin that uses Aead algorithm to anonymise or de-anonymise data.")
public class AnonymizationPlugin extends Transform<StructuredRecord, StructuredRecord> {

  private final AnonymizationConfig config;
  private Schema outputSchema;
  private HashMap<String, byte[]> keyTypeMap = new HashMap<>();
  private TinkHelper aeadHelper = new AeadHelper();
  private BiFunction<byte[], String, Try<byte[]>> encryptFunction;

  public AnonymizationPlugin(AnonymizationConfig config) {
    this.config = config;
  }

  public void configurePipeline(PipelineConfigurer pipelineConfigurer) {

  }

  public void initialize(TransformContext context) throws Exception {
    if (this.config.getEncryptFunction().equals(AEAD)) {
      this.aeadHelper = new AeadHelper();
    } else {
      this.aeadHelper = new DeterAeadHelper();
    }
    this.encryptFunction = (key, text) -> Try.of(() -> aeadHelper.encrypt(key,text));
    keyTypeMap.put("prueba", Constants.deterKey.getBytes());
  }

  @Override
  public void transform(StructuredRecord structuredRecord, Emitter<StructuredRecord> emitter) throws Exception {
    List<Field> fields = structuredRecord.getSchema().getFields();
    StructuredRecord.Builder builder = StructuredRecord.builder(structuredRecord.getSchema());
    for(Field field : fields) {
      String name = field.getName();
      String value = structuredRecord.get(name);
      byte[] newValue = this.encryptFunction.apply(keyTypeMap.get("prueba"), value).get();
      builder.set(name, Base64.getEncoder().encodeToString(newValue));
    }
    emitter.emit(builder.build());
  }
}
