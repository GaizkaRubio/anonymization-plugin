package org.example.hydrator.plugin.tink;

import com.google.crypto.tink.BinaryKeysetWriter;
import com.google.crypto.tink.CleartextKeysetHandle;
import com.google.crypto.tink.KeysetHandle;
import com.google.crypto.tink.KeysetWriter;
import com.google.crypto.tink.aead.AeadConfig;
import com.google.crypto.tink.aead.AeadKeyTemplates;
import com.google.crypto.tink.aead.AesGcmKeyManager;
import com.google.crypto.tink.aead.AesGcmSivKeyManager;
import com.google.crypto.tink.daead.DeterministicAeadConfig;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Base64;

public class GenerateKeysTest {

  @Test
  public void generateKey() throws Exception {
    AeadConfig.register();
    KeysetHandle keysetHandle = KeysetHandle.generateNew(AesGcmKeyManager.aes256GcmTemplate());
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    KeysetWriter writer = BinaryKeysetWriter.withOutputStream(out);
    CleartextKeysetHandle.write(keysetHandle, writer);
    System.out.println(Base64.getEncoder().encodeToString(out.toByteArray()));
  }
}