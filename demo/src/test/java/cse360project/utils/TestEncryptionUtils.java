package cse360project.utils;

import static org.junit.Assert.*;
import org.junit.Test;

public class TestEncryptionUtils {

    @Test
    public void testEncryptionDecryption_HelloWorld() {
        String inputText = "HelloWorld";
        String encryptedText = EncryptionUtils.encryptString(inputText);
        assertNotNull("Encrypted text should not be null", encryptedText);
        assertNotEquals("Encrypted text should differ from input", inputText, encryptedText);

        String decryptedText = EncryptionUtils.decryptString(encryptedText);
        assertEquals("Decrypted text should match the original input", inputText, decryptedText);
    }

    @Test
    public void testEncryptionDecryption_LongText() {
        String inputText = "This is a longer text with spaces and special characters!@#";
        String encryptedText = EncryptionUtils.encryptString(inputText);
        assertNotNull("Encrypted text should not be null", encryptedText);
        assertNotEquals("Encrypted text should differ from input", inputText, encryptedText);

        String decryptedText = EncryptionUtils.decryptString(encryptedText);
        assertEquals("Decrypted text should match the original input", inputText, decryptedText);
    }

    @Test
    public void testEncryptionDecryption_EmptyString() {
        String inputText = "";
        String encryptedText = EncryptionUtils.encryptString(inputText);
        assertNotNull("Encrypted text should not be null", encryptedText);
        assertNotEquals("Encrypted text should differ from input", inputText, encryptedText);

        String decryptedText = EncryptionUtils.decryptString(encryptedText);
        assertEquals("Decrypted text should match the original input", inputText, decryptedText);
    }

    @Test
    public void testInvalidDecryption() {
        String invalidCipherText = "InvalidCipherText";
        assertThrows("Expected RuntimeException for invalid cipher text", RuntimeException.class, () -> {
            EncryptionUtils.decryptString(invalidCipherText);
        });
    }
}
