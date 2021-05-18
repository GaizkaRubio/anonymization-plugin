package org.example.hydrator.plugin.tink;

import io.cdap.cdap.api.data.format.StructuredRecord;
import io.cdap.cdap.api.data.schema.Schema;
import io.cdap.cdap.api.data.schema.Schema.Field;
import io.cdap.cdap.etl.mock.common.MockEmitter;
import org.example.hydrator.plugin.AnonymizationConfig;
import org.example.hydrator.plugin.AnonymizationPlugin;
import org.example.hydrator.plugin.utils.StringUtils;
import org.junit.Test;

import java.util.Base64;

import static org.example.hydrator.plugin.constants.Constants.AEAD;
import static org.example.hydrator.plugin.constants.Constants.ENCRYPT;

public class AeadAnonymizationPluginTest {

  private static final Schema INPUT = Schema.recordOf("input",
      Field.of("a", Schema.of(Schema.Type.STRING)),
      Field.of("b", Schema.of(Schema.Type.STRING)),
      Field.of("c", Schema.of(Schema.Type.STRING)),
      Field.of("d", Schema.of(Schema.Type.STRING)),
      Field.of("e", Schema.of(Schema.Type.STRING)));
  private static String categoryMapping = "d:prueba, e:prueba";
  private static final AnonymizationConfig config = new AnonymizationConfig(INPUT.toString(), ENCRYPT, AEAD, categoryMapping);

  @Test
  public void testAeadAnonymizationPlugin() throws Exception {
    AnonymizationPlugin plugin = new AnonymizationPlugin(config);
    plugin.initialize(null);
    MockEmitter<StructuredRecord> emitter = new MockEmitter<>();
    plugin.transform(StructuredRecord.builder(INPUT)
      .set("a", "1").set("b", "2")
      .set("c", "3").set("d", "test")
      .set("e", "test").build(), emitter);
    StructuredRecord out = emitter.getEmitted().stream().findFirst().get();
    Schema outputSchema = out.getSchema();
    for (Field field : outputSchema.getFields()) {
      String encrypt = out.get(field.getName());
      System.out.println(field.getName() + " : " + encrypt);
    }
  }
}
