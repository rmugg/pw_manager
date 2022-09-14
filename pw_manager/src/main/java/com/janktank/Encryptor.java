package com.janktank;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class Encryptor {
    private static SecretKey finalKey;

    Encryptor(String userInput) throws NoSuchAlgorithmException, InvalidKeySpecException{
        
        finalKey = Encryptor.getKeyFromPassword(buildKey(userInput), "*you-can-replace-this-value*");
    }

    private String buildKey(String input) throws NoSuchAlgorithmException {
        return Hasher.toHexString(Hasher.getSHA(input));
    }


    //returns the secretKey  for decryption/ encryption
    private static SecretKey getKeyFromPassword(String password, String salt)
        throws NoSuchAlgorithmException, InvalidKeySpecException {
        
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);
        SecretKey secret = new SecretKeySpec(factory.generateSecret(spec)
            .getEncoded(), "AES");
        return secret;
    }

    // returns the IV parameter for decryption/ encryption
    public static IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        IvParameterSpec ivPa = new IvParameterSpec(iv);
        return ivPa;

    }

    //overloaded generateIV that takes a string and uses that
    public static IvParameterSpec generateIv(String input) {
        byte[] decoded = Base64.getDecoder().decode(input);
        IvParameterSpec ivPa = new IvParameterSpec(decoded);
        return ivPa;
    }

    //encrypt the data return the encrypted string
    public static String encrypt(String algorithm, String input,
        IvParameterSpec iv) throws NoSuchPaddingException, NoSuchAlgorithmException,
        InvalidAlgorithmParameterException, InvalidKeyException,
        BadPaddingException, IllegalBlockSizeException {
        
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, finalKey, iv);
        byte[] cipherText = cipher.doFinal(input.getBytes());
        return Base64.getEncoder()
            .encodeToString(cipherText);
    }

    //decrypt the data returning the actual string value
    public static String decrypt(String algorithm, String cipherText,
        IvParameterSpec iv) throws NoSuchPaddingException, NoSuchAlgorithmException,
        InvalidAlgorithmParameterException, InvalidKeyException,
        BadPaddingException, IllegalBlockSizeException {
        
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, finalKey, iv);
        byte[] plainText = cipher.doFinal(Base64.getDecoder()
            .decode(cipherText));
        return new String(plainText);
    }

}

