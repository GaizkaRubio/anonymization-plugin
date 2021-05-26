package org.example.hydrator.plugin.tink;

import io.cdap.cdap.api.data.format.StructuredRecord;
import io.cdap.cdap.api.data.schema.Schema;
import io.cdap.cdap.etl.mock.common.MockEmitter;
import org.example.hydrator.plugin.AnonymizationConfig;
import org.example.hydrator.plugin.AnonymizationPlugin;
import org.junit.Test;

import static org.example.hydrator.plugin.constants.Constants.*;
import static org.example.hydrator.plugin.tink.constants.Constants.DETERKEY;

public class DeterministicAeadAnonimizationPluginTest {

  private static final Schema INPUT = Schema.recordOf("input",
      Schema.Field.of("a", Schema.of(Schema.Type.STRING)),
      Schema.Field.of("b", Schema.of(Schema.Type.STRING)),
      Schema.Field.of("c", Schema.of(Schema.Type.STRING)),
      Schema.Field.of("d", Schema.of(Schema.Type.STRING)),
      Schema.Field.of("e", Schema.of(Schema.Type.STRING)));
  private static String categoryMapping = "a:prueba, d:prueba, e:prueba";
  private static String categoryKey = "prueba:" + DETERKEY;


  @Test
  public void testDeterministicAeadAnonymizationPlugin() throws Exception {
    AnonymizationConfig config =
        new AnonymizationConfig(INPUT.toString(), ENCRYPT, DAEAD, categoryMapping, categoryKey);
    AnonymizationPlugin plugin = new AnonymizationPlugin(config);
    plugin.initialize(null);
    MockEmitter<StructuredRecord> emitter = new MockEmitter<>();
    plugin.transform(StructuredRecord.builder(INPUT)
      .set("a", "test").set("b", "test")
      .set("c", "test").set("d", "test")
      .set("e", "test").build(), emitter);
    StructuredRecord out = emitter.getEmitted().stream().findFirst().get();
    Schema outputSchema = out.getSchema();
    for (Schema.Field field : outputSchema.getFields()) {
      String encrypt = out.get(field.getName());
      System.out.println(field.getName() + " : " + encrypt);
    }
  }

  @Test
  public void testDeterministicAeadDecryptPlugin() throws Exception {
    AnonymizationConfig config =
        new AnonymizationConfig(INPUT.toString(), DECRYPT, DAEAD, categoryMapping, categoryKey);
    AnonymizationPlugin plugin = new AnonymizationPlugin(config);
    plugin.initialize(null);
    MockEmitter<StructuredRecord> emitter = new MockEmitter<>();
    plugin.transform(StructuredRecord.builder(INPUT)
        .set("a", "ARWOCCTOFO+efQEgoelDta6eOLOrdsP67A==")
        .set("b", "test")
        .set("c", "test")
        .set("d", "ARWOCCTOFO+efQEgoelDta6eOLOrdsP67A==")
        .set("e", "ARWOCCTOFO+efQEgoelDta6eOLOrdsP67A==").build(), emitter);
    StructuredRecord out = emitter.getEmitted().stream().findFirst().get();
    Schema outputSchema = out.getSchema();
    for (Schema.Field field : outputSchema.getFields()) {
      String encrypt = out.get(field.getName());
      System.out.println(field.getName() + " : " + encrypt);
    }
  }
}
