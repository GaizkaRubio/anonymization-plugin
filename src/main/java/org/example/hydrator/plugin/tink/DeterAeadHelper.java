package org.example.hydrator.plugin.tink;

import com.google.crypto.tink.*;
import com.google.crypto.tink.aead.AeadConfig;
import com.google.crypto.tink.daead.DeterministicAeadConfig;

import java.util.Base64;

public class DeterAeadHelper implements TinkHelper {

  public byte[] encrypt(byte[] base64Key, String text) throws Exception {
    DeterministicAeadConfig.register();
    KeysetReader reader = BinaryKeysetReader.withBytes(Base64.getDecoder().decode(base64Key));
    KeysetHandle clearSetHandle = CleartextKeysetHandle.read(reader);
    DeterministicAead deterministicAead = clearSetHandle.getPrimitive(DeterministicAead.class);
    return deterministicAead.encryptDeterministically(text.getBytes(), "".getBytes());
  }

  public byte[] decrypt(byte[] base64Key, String text) throws Exception {
    DeterministicAeadConfig.register();
    KeysetReader reader = BinaryKeysetReader.withBytes(Base64.getDecoder().decode(base64Key));
    KeysetHandle clearSetHandle = CleartextKeysetHandle.read(reader);
    DeterministicAead deterministicAead = clearSetHandle.getPrimitive(DeterministicAead.class);
    return deterministicAead.decryptDeterministically(text.getBytes(), "".getBytes());
  }
}
