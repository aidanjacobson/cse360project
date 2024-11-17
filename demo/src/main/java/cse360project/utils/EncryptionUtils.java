package cse360project.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * Utility class for encrypting and decrypting strings
 */
public class EncryptionUtils {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES";
    private static final SecretKey SECRET_KEY = generateKey();

    /**
     * Generate a secret key for encryption and decryption
     * @return the secret key
     */
    private static SecretKey generateKey() {
        byte[] keyBytes = new byte[] {
            0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
            0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f,
            0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17 };
        return new SecretKeySpec(keyBytes, ALGORITHM);
    }

    /**
     * Encrypt a string
     * @param input the string to encrypt
     * @return the encrypted string
     */
    public static String encryptString(String input) {
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, SECRET_KEY);
            byte[] encryptedBytes = cipher.doFinal(input.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting string", e);
        }
    }

    /**
     * Decrypt a string
     * @param input the encrypted string
     * @return the decrypted string
     */
    public static String decryptString(String input) {
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, SECRET_KEY);
            byte[] decodedBytes = Base64.getDecoder().decode(input);
            byte[] decryptedBytes = cipher.doFinal(decodedBytes);
            return new String(decryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting string", e);
        }
    }
}

