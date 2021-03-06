package br.org.otus.security;

import static org.junit.Assert.assertEquals;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Base64.Encoder;

import org.ccem.otus.exceptions.webservice.security.EncryptedException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({EncryptorResources.class, Base64.class})
public class EncryptorResourcesTest {
  private static final String VALUE_IRREVERSIBLE = "Otus1234";
  private static final String VALUE_REVERSIBLE = "Otus1234";
  private static final String VALUE_DECRYPTER = "T3R1czEyMzQ=";
  @Mock
  private MessageDigest messageDigest;
  @Mock
  private Encoder base64encoder;
  private EncryptorResources encryptorResources;
  private byte[] digest;

  @Test
  public void method_EncryptIrreversible_should_return_messageDigestEncode64String()
    throws NoSuchAlgorithmException, EncryptedException {
    messageDigest = MessageDigest.getInstance("SHA");
    digest = messageDigest.digest(VALUE_IRREVERSIBLE.getBytes());
    assertEquals(encryptorResources.encryptIrreversible(VALUE_IRREVERSIBLE), Base64.getEncoder().encodeToString(digest));
  }

  @Test
  public void method_encryptReversible_should_encode_StringParameter()
    throws UnsupportedEncodingException, EncryptedException {
    byte[] valueBytes = VALUE_REVERSIBLE.getBytes();
    byte[] encryptedArray = Base64.getEncoder().encode(valueBytes);
    assertEquals(encryptorResources.encryptReversible(VALUE_REVERSIBLE), new String(encryptedArray, "UTF-8"));
  }

  @Test
  public void method_decrypt_should_decrypter_StringParameter()
    throws UnsupportedEncodingException, EncryptedException {
    assertEquals(encryptorResources.decrypt(VALUE_DECRYPTER), VALUE_REVERSIBLE);
  }

  @Test(expected = EncryptedException.class)
  public void method_EncryptReversible_should_throw_UnsupportedEncodingException() throws Exception {
    byte[] encryptedArray = Base64.getEncoder().encode(VALUE_REVERSIBLE.getBytes());
    whenNew(String.class).withArguments(encryptedArray, "UTF-8").thenThrow(UnsupportedEncodingException.class);
    encryptorResources.encryptReversible(VALUE_REVERSIBLE);
  }

  @Test(expected = EncryptedException.class)
  public void method_decrypt_should_throw_UnsupportedEncodingException() throws Exception {
    byte[] encryptedArray = Base64.getDecoder().decode(VALUE_DECRYPTER.getBytes());
    whenNew(String.class).withArguments(encryptedArray, "UTF-8").thenThrow(UnsupportedEncodingException.class);
    encryptorResources.decrypt(VALUE_DECRYPTER);
  }

}
