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
import org.example.hydrator.plugin.tink.AeadHelper;
import org.example.hydrator.plugin.tink.TinkHelper;

import java.util.HashMap;
import java.util.List;

@Plugin(type = Transform.PLUGIN_TYPE)
@Name("AnonymizationPlugin")
@Description("A plugin that uses Aead algorithm to anonymise or de-anonymise data.")
public class AnonymizationPlugin extends Transform<StructuredRecord, StructuredRecord> {

  private final AnonymizationConfig config;
  private Schema outputSchema;
  private HashMap<String, byte[]> keyTypeMap = new HashMap<>();
  private TinkHelper aeadHelper = new AeadHelper();

  public AnonymizationPlugin(AnonymizationConfig config) {
    this.config = config;
  }

  public void configurePipeline(PipelineConfigurer pipelineConfigurer) {

  }

  public void initialize(TransformContext context) throws Exception {
    String key = "CICY4KkHEmQKWAowdHlwZS5nb29nbGVhcGlzLmNvbS9nb29nbGUuY3J5cHRvLnRpbmsuQWVzR2NtS2V5EiIaIAhE8ZwSxBQM8sbcZ9wTtYDDIoLenNaSkvgW4pR/JKnXXGAEQARiAmOCpByAB";
    keyTypeMap.put("prueba", key.getBytes());
  }

  @Override
  public void transform(StructuredRecord structuredRecord, Emitter<StructuredRecord> emitter) throws Exception {
    List<Field> fields = structuredRecord.getSchema().getFields();
    StructuredRecord.Builder builder = StructuredRecord.builder(structuredRecord.getSchema());
    for(Field field : fields) {
      String name = field.getName();
      String value = structuredRecord.get(name);
      String newValue = aeadHelper.encrypt(keyTypeMap.get("prueba"), value).toString();
      builder.set(name, newValue);
    }
    emitter.emit(builder.build());
  }
}
