package com.capstone.myapplication.utils;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;

import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.spec.RSAKeyGenParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
public class SecureSession {
  private static final String KEYSTORE_NAME = "AndroidKeyStore";
  private static final String KEY_ALIAS  = "session_key";
  private static final String KEY_IV = "iv";
  private static final String KEY_TOKEN = "token";
  private static final String KEY_PREF_NAME = "session";

  private static SharedPreferences preferences;

  public SecureSession(Context context){
   preferences = context.getApplicationContext().getSharedPreferences(KEY_PREF_NAME,MODE_PRIVATE);
  }
  private SecretKey getOrCreateKey() throws Exception{
    //instanciate and load keystore
    KeyStore keyStore = KeyStore.getInstance(KEYSTORE_NAME);
    keyStore.load(null);

    //key already exists
    if(keyStore.containsAlias(KEY_ALIAS)){
      return (SecretKey) keyStore.getKey(KEY_ALIAS,null);
    }

    //key not exists
    KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES,KEYSTORE_NAME);
    keyGenerator.init(new KeyGenParameterSpec.Builder(
        KEY_ALIAS,
        KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT
        )
        .setKeySize(256)
        .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
        .build()
    );


    return keyGenerator.generateKey();
  }

  public void saveToken(String token) throws Exception{
    SecretKey key = getOrCreateKey();
    Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
    cipher.init(Cipher.ENCRYPT_MODE,key);
    byte[] encrypted = cipher.doFinal(token.getBytes(StandardCharsets.UTF_8));
    byte[] iv = cipher.getIV();

    preferences.edit().
        putString(KEY_TOKEN,Base64.encodeToString(encrypted,Base64.NO_WRAP))
        .putString(KEY_IV,Base64.encodeToString(iv,Base64.NO_WRAP))
        .apply();
  }

  public String getToken() throws Exception{
    String encryptedString =
        preferences.getString(KEY_TOKEN, null);

    String ivString =
        preferences.getString(KEY_IV, null);

    if(encryptedString==null || ivString==null){
      return null;
    }
    SecretKey key = getOrCreateKey();
    byte[] encrypted = Base64.decode(encryptedString, Base64.NO_WRAP);
    byte[] iv = Base64.decode(ivString,Base64.NO_WRAP);

    Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
    GCMParameterSpec spec = new GCMParameterSpec(128,iv);
    cipher.init(Cipher.DECRYPT_MODE,key,spec);

    byte[] decrypted = cipher.doFinal(encrypted);
    return new String(decrypted,StandardCharsets.UTF_8);
  }
  public void clear(){
    preferences.edit().clear().apply();

    try{
      KeyStore keyStore = KeyStore.getInstance(KEYSTORE_NAME);
      keyStore.load(null);
      keyStore.deleteEntry(KEY_ALIAS);
    } catch (Exception ignored){}
  }
}
