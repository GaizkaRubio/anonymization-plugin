package org.example.hydrator.plugin.tink;

public interface TinkHelper {
  byte[] encrypt(String base64Key, String text) throws Exception;

  byte[] decrypt(String base64Key, String text) throws Exception;
}
