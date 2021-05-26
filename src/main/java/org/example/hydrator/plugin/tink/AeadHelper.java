package org.example.hydrator.plugin.tink;

import com.google.crypto.tink.*;
import com.google.crypto.tink.aead.AeadConfig;

import java.util.Base64;


public class AeadHelper implements TinkHelper {

  public byte[] encrypt(String base64Key, String text) throws Exception {
    AeadConfig.register();
    KeysetReader reader = BinaryKeysetReader.withBytes(Base64.getDecoder().decode(base64Key));
    KeysetHandle clearSetHandle = CleartextKeysetHandle.read(reader);
    Aead aead = clearSetHandle.getPrimitive(Aead.class);
    return aead.encrypt(text.getBytes(), "".getBytes());
  }

  public byte[] decrypt(String base64Key, String base64Text) throws Exception {
    AeadConfig.register();
    KeysetReader reader = BinaryKeysetReader.withBytes(Base64.getDecoder().decode(base64Key));
    KeysetHandle clearSetHandle = CleartextKeysetHandle.read(reader);
    Aead aead = clearSetHandle.getPrimitive(Aead.class);
    byte[] decodeText = Base64.getDecoder().decode(base64Text);
    return aead.decrypt(decodeText, "".getBytes());
  }
}
