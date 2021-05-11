package org.example.hydrator.plugin.tink;

import io.cdap.cdap.api.data.format.StructuredRecord;
import io.cdap.cdap.api.data.schema.Schema;
import io.cdap.cdap.etl.mock.common.MockEmitter;
import org.example.hydrator.plugin.AnonymizationConfig;
import org.example.hydrator.plugin.AnonymizationPlugin;
import org.junit.Test;

import static org.example.hydrator.plugin.constants.Constants.*;

public class DeterministicAeadAnonimizationPluginTest {

  private static final Schema INPUT = Schema.recordOf("input",
      Schema.Field.of("a", Schema.of(Schema.Type.STRING)),
      Schema.Field.of("b", Schema.of(Schema.Type.STRING)),
      Schema.Field.of("c", Schema.of(Schema.Type.STRING)),
      Schema.Field.of("d", Schema.of(Schema.Type.STRING)),
      Schema.Field.of("e", Schema.of(Schema.Type.STRING)));
  private static final AnonymizationConfig config = new AnonymizationConfig(INPUT.toString(), ENCRYPT, DAEAD);

  private String base64Key = "CKSQuKwBEoQBCngKMHR5cGUuZ29vZ2xlYXBpcy5jb20vZ29vZ2xlLmNyeXB0by50aW5rLkFlc1NpdktleRJCEkDCwP1yIKBD54/kMi9ijYZAgRicJgQimVoUMAGGaPKqs33608DiAEEvMp1z3qCo3oB6QKYrHHbJlxaZq0P5DTriGAEQARikkLisASAB";

  @Test
  public void testDeterministicAeadAnonymizationPlugin() throws Exception {
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
}
