package com.example.whattodo;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class EncryptDecrypt {

    public SecretKey generateKey (String encryptionType)
    {
        try
        {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(encryptionType);
            SecretKey myKey = keyGenerator.generateKey();
            return myKey;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public String encryptingString(String dataToEncrypt, SecretKey key)
    {
        try
        {
            //SecretKey key = generateKey("AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encVal = cipher.doFinal(dataToEncrypt.getBytes());
            String encryptedValue = Base64.encodeToString(encVal, Base64.DEFAULT);

            return encryptedValue;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public String decryptingString(String dataToDecrypt, SecretKey key)
    {
        try
        {
            //SecretKey key = generateKey("AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decodeValue = Base64.decode(dataToDecrypt, Base64.DEFAULT);
            byte[] textDecrypted = cipher.doFinal(decodeValue);
            String result = textDecrypted.toString();

            return result;
        }
        catch (Exception e)
        {
            System.out.println(e);
            return null;
        }
    }
}
