package org.example.hydrator.plugin.tink;

public interface TinkHelper {
  byte[] encrypt(byte[] base64Key, String text) throws Exception;

  byte[] decrypt(byte[] base64Key, String text) throws Exception;
}
