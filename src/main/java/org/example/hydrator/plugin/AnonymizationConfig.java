package org.example.hydrator.plugin;

import io.cdap.cdap.api.annotation.Description;
import io.cdap.cdap.api.annotation.Macro;
import io.cdap.cdap.api.annotation.Name;
import io.cdap.cdap.api.plugin.PluginConfig;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nullable;

@Getter
@Setter
public class AnonymizationConfig extends PluginConfig {

  @Name("schema")
  @Nullable
  @Macro
  @Description("Specifies the schema of the records outputted from this plugin.")
  private final String schema;

  @Name("anonymizationMode")
  @Macro
  @Description("Specifies whether the plugin should anonymise or de-anonymise the data.")
  private final String anonymizationMode;

  @Name("encryptFunction")
  @Macro
  @Description("Specifies whether the plugin should anonymise or de-anonymise the data.")
  private final String encryptFunction;

  public AnonymizationConfig(@Nullable String schema, String anonymizationMode, String encryptFunction) {
    this.schema = schema;
    this.anonymizationMode = anonymizationMode;
    this.encryptFunction = encryptFunction;
  }
}
