package com.payment.simplepaymentgateway.util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class AESUtil {

    public static String encryptAES(String plaintext, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decryptAES(String ciphertext, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(ciphertext));
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    public static void main(String[] args) throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128); // You can change the key size as needed
        SecretKey secretKey = keyGenerator.generateKey();

        // Example text
        String plaintext1 = "user";
        String plaintext2 = "password";
        String plaintext3 = "USER";

        // Encrypt
        String encryptedText = encryptAES(plaintext1, secretKey);
        System.out.println("Encrypted 1: " + encryptedText);

        String encryptedText2 = encryptAES(plaintext2, secretKey);
        System.out.println("Encrypted 2: " + encryptedText2);

        String encryptedText3 = encryptAES(plaintext3, secretKey);
        System.out.println("Encrypted 3: " + encryptedText3);

    }

}
