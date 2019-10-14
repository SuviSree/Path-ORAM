/** Source: https://gist.github.com/itarato/abef95871756970a9dad */

package com.company;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * This example program shows how AES encryption and decryption can be done in Java.
 * Please note that secret key and encrypted text is unreadable binary and hence
 * in the following program we display it in hexadecimal format of the underlying bytes.
 * @author Jayson
 */
public class AES {

    /**
     * 1. Generate a plain text for encryption
     * 2. Get a secret key (printed in hexadecimal form). In actual use this must
     * by encrypted and kept safe. The same key is required for decryption.
     * 3.
     */
    public static void main(String[] args) throws Exception {
        String plainText = "-1";
        String plainText2 = "-1";
        String secKey = getSecretEncryptionKey();
        String cipherText = encryptText(plainText, secKey);
        String decryptedText = decryptText(cipherText, secKey);
        String cipherText2 = encryptText(plainText2, secKey);
        String decryptedText2 = decryptText(cipherText2, secKey);

        System.out.println("Original Text:" + plainText);
        System.out.println("Encrypted Text:"+cipherText);
        System.out.println("Decrypted Text:"+decryptedText);

        System.out.println("Original Text:" + plainText2);
        System.out.println("Encrypted Text:"+cipherText2);
        System.out.println("Decrypted Text:"+decryptedText2);

    }

    private static AES single_instance = null;
    public String secretKey;

    private AES() {
        try {
            secretKey = getSecretEncryptionKey();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static AES getInstance()
    {
        if (single_instance == null)
            single_instance = new AES();

        return single_instance;
    }

    /**
     * gets the AES encryption key. In your actual programs, this should be safely
     * stored.
     * @return
     * @throws Exception
     */
    public static String getSecretEncryptionKey() throws Exception{
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(128); // The AES key size in number of bits
        SecretKey secKey = generator.generateKey();
        return secKey.toString();
    }

    /**
     * Encrypts plainText in AES using the secret key
     * @param plainText
     * @param key
     * @return
     * @throws Exception
     */
    public static String encryptText(String plainText, String key) throws Exception {
        byte[] clean = plainText.getBytes();

        // Generating IV.
        int ivSize = 16;
        byte[] iv = new byte[ivSize];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        // Hashing key.
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(key.getBytes("UTF-8"));
        byte[] keyBytes = new byte[16];
        System.arraycopy(digest.digest(), 0, keyBytes, 0, keyBytes.length);
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");

        // Encrypt.
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] encrypted = cipher.doFinal(clean);

        // Combine IV and encrypted part.
        byte[] encryptedIVAndText = new byte[ivSize + encrypted.length];
        System.arraycopy(iv, 0, encryptedIVAndText, 0, ivSize);
        System.arraycopy(encrypted, 0, encryptedIVAndText, ivSize, encrypted.length);

        return Base64.getEncoder().encodeToString(encryptedIVAndText);
    }

    /**
     * Decrypts encrypted byte array using the key used for encryption.
     * @param cipherText
     * @param key
     * @return
     * @throws Exception
     */
    public static String decryptText(String cipherText, String key) throws Exception {
        byte[] encryptedIvTextBytes = Base64.getDecoder().decode(cipherText);
        int ivSize = 16;
        int keySize = 16;

        // Extract IV.
        byte[] iv = new byte[ivSize];
        System.arraycopy(encryptedIvTextBytes, 0, iv, 0, iv.length);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        // Extract encrypted part.
        int encryptedSize = encryptedIvTextBytes.length - ivSize;
        byte[] encryptedBytes = new byte[encryptedSize];
        System.arraycopy(encryptedIvTextBytes, ivSize, encryptedBytes, 0, encryptedSize);

        // Hash key.
        byte[] keyBytes = new byte[keySize];
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(key.getBytes());
        System.arraycopy(md.digest(), 0, keyBytes, 0, keyBytes.length);
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");

        // Decrypt.
        Cipher cipherDecrypt = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipherDecrypt.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] decrypted = cipherDecrypt.doFinal(encryptedBytes);

        return new String(decrypted);
    }
}