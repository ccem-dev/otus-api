package br.org.otus.security;

import org.ccem.otus.exceptions.webservice.security.EncryptedException;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class EncryptorResources {

  public static String encryptIrreversible(String value) throws EncryptedException {
    try {
      MessageDigest messageDigest = MessageDigest.getInstance("SHA");
      byte[] digest = messageDigest.digest(value.getBytes());

      return Base64.getEncoder().encodeToString(digest);
    } catch (NoSuchAlgorithmException exception) {
      throw new EncryptedException(exception);
    }
  }

  public static String encryptReversible(String value) throws EncryptedException {
    try {
      byte[] valueBytes = value.getBytes();
      byte[] encryptedArray = Base64.getEncoder().encode(valueBytes);
      return new String(encryptedArray, "UTF-8");

    } catch (UnsupportedEncodingException e) {
      throw new EncryptedException(e);
    }
  }

  public static String decrypt(String value) throws EncryptedException {
    try {
      byte[] valueBytes = value.getBytes();
      byte[] encryptedArray = Base64.getDecoder().decode(valueBytes);
      return new String(encryptedArray, "UTF-8");
    } catch (UnsupportedEncodingException e) {

      throw new EncryptedException(e);
    }
  }
}
