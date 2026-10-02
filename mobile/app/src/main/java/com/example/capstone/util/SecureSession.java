package com.example.capstone.util;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.nio.charset.StandardCharsets;
import java.security.KeyStore;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
public class SecureSession {
  //consts
  private static final String KEYSTORE_NAME = "AndroidKeyStore";
  private static final String KEY_ALIAS  = "session_key";
  private static final String KEY_ACCESS_TOKEN_IV = "access_iv";
  private static final String KEY_ACCESS_TOKEN = "token";
  private static final String KEY_PREF_NAME = "session";
  private static final String KEY_SESSION_EXPIRATION = "session_expiration";
  private static final String KEY_REFRESH_TOKEN ="refresh_token";
  private static final String KEY_REFRESH_TOKEN_IV = "refresh_iv";
  private static final String KEY_REFRESH_EXPIRATION = "refresh_expiration";
  private final MutableLiveData<Boolean> loggedIn =
      new MutableLiveData<>();

  private final SharedPreferences preferences;

  //creates sessionClass and context is needed for shared prefs
  public SecureSession(Context context){
   preferences = context.getApplicationContext().getSharedPreferences(KEY_PREF_NAME,MODE_PRIVATE);
  }

  public LiveData<Boolean> getLoggedIn() {
    return loggedIn;
  }

  public void notifySessionExpired() {
    loggedIn.postValue(false);
  }

  public void setSessionExpiration(long expiration) {
    preferences.edit()
        .putLong(KEY_SESSION_EXPIRATION, expiration)
        .apply();
  }

  public long getSessionExpiration() {
    return preferences.getLong(KEY_SESSION_EXPIRATION, 0L);
  }

  public void setRefreshExpiration(long expiration) {
    preferences.edit()
        .putLong(KEY_REFRESH_EXPIRATION, expiration)
        .apply();
  }

  public long getRefreshExpiration() {
    return preferences.getLong(KEY_REFRESH_EXPIRATION, 0L);
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

    //generate the key
    return keyGenerator.generateKey();
  }

  private void saveEncrypted(
      String value,
      String valueKey,
      String ivKey
  ) throws Exception {
    //get key
    SecretKey key = getOrCreateKey();
    //get cipher
    Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
    cipher.init(Cipher.ENCRYPT_MODE,key);
    //encrypt and get iv
    byte[] encrypted = cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));
    byte[] iv = cipher.getIV();

    //save in preferences
    preferences.edit().
        putString(valueKey,Base64.encodeToString(encrypted,Base64.NO_WRAP))
        .putString(ivKey,Base64.encodeToString(iv,Base64.NO_WRAP))
        .apply();
  }

  public void saveAccessToken(String token)
      throws Exception {

    saveEncrypted(
        token,
        KEY_ACCESS_TOKEN,
        KEY_ACCESS_TOKEN_IV
    );
  }

  public void saveRefreshToken(String token)
      throws Exception {

    saveEncrypted(
        token,
        KEY_REFRESH_TOKEN,
        KEY_REFRESH_TOKEN_IV
    );
  }

  private String getDecrypted(
      String valueKey,
      String ivKey
  ) throws Exception {
    //get token and iv from prefs
    String encryptedString =
        preferences.getString(valueKey, null);

    String ivString =
        preferences.getString(ivKey, null);

    //if empty dont continue and return null key
    if(encryptedString==null || ivString==null){
      return null;
    }

    //decode
    SecretKey key = getOrCreateKey();
    byte[] encrypted = Base64.decode(encryptedString, Base64.NO_WRAP);
    byte[] iv = Base64.decode(ivString,Base64.NO_WRAP);

    ///set cipher to decrypt
    Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
    GCMParameterSpec spec = new GCMParameterSpec(128,iv);
    cipher.init(Cipher.DECRYPT_MODE,key,spec);

    //decrypt

    byte[] decrypted = cipher.doFinal(encrypted);
    //return encoded string
    return new String(decrypted,StandardCharsets.UTF_8);

  }

  public String getAccessToken() {
    try {
      return getDecrypted(
          KEY_ACCESS_TOKEN,
          KEY_ACCESS_TOKEN_IV
      );
    } catch (Exception e) {
      return null;
    }
  }

  public String getRefreshToken() {
    try {
      return getDecrypted(
          KEY_REFRESH_TOKEN,
          KEY_REFRESH_TOKEN_IV
      );
    } catch (Exception e) {
      return null;
    }
  }

  public void clear(){
    //clear preferences
    preferences.edit().clear().apply();

    //try to clear keystore
    try{
      KeyStore keyStore = KeyStore.getInstance(KEYSTORE_NAME);
      keyStore.load(null);
      keyStore.deleteEntry(KEY_ALIAS);
    } catch (Exception ignored){}
  }
}
