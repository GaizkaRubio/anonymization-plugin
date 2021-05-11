package org.example.hydrator.plugin.tink;

import com.google.crypto.tink.*;
import com.google.crypto.tink.aead.AeadConfig;

import java.util.Base64;


public class AeadHelper implements TinkHelper {

  String base64Key = "CICY4KkHEmQKWAowdHlwZS5nb29nbGVhcGlzLmNvbS9nb29nbGUuY3J5cHRvLnRpbmsuQWVzR2NtS2V5EiIaIAhE8ZwSBQM8sbcZ9wTtYDDIoLenNaSkvgW4pR/JKnXXGAEQARiAmOCpByAB";

  public byte[] encrypt(byte[] base64Key, String text) throws Exception {
    AeadConfig.register();
    KeysetReader reader = BinaryKeysetReader.withBytes(Base64.getDecoder().decode(this.base64Key));
    KeysetHandle clearSetHandle = CleartextKeysetHandle.read(reader);
    Aead aead = clearSetHandle.getPrimitive(Aead.class);
    byte[] ciphertext = aead.encrypt(text.getBytes(), "".getBytes());
    return ciphertext;
  }

  public byte[] decrypt(byte[] base64Key, String text) throws Exception {
    AeadConfig.register();
    KeysetReader reader = BinaryKeysetReader.withBytes(Base64.getDecoder().decode(base64Key));
    KeysetHandle clearSetHandle = CleartextKeysetHandle.read(reader);
    Aead aead = clearSetHandle.getPrimitive(Aead.class);
    byte[] ciphertext = aead.decrypt(text.getBytes(), "".getBytes());
    return ciphertext;
  }
}
