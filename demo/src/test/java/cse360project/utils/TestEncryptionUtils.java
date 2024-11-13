package cse360project.utils;

import cse360project.utils.EncryptionUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestEncryptionUtils {

    @Test
    public void testEncryptionDecryption_HelloWorld() {
        String inputText = "HelloWorld";
        String encryptedText = EncryptionUtils.encryptString(inputText);
        assertNotNull(encryptedText, "Encrypted text should not be null");
        assertNotEquals(inputText, encryptedText, "Encrypted text should differ from input");

        String decryptedText = EncryptionUtils.decryptString(encryptedText);
        assertEquals(inputText, decryptedText, "Decrypted text should match the original input");
    }

    @Test
    public void testEncryptionDecryption_LongText() {
        String inputText = "This is a longer text with spaces and special characters!@#";
        String encryptedText = EncryptionUtils.encryptString(inputText);
        assertNotNull(encryptedText, "Encrypted text should not be null");
        assertNotEquals(inputText, encryptedText, "Encrypted text should differ from input");

        String decryptedText = EncryptionUtils.decryptString(encryptedText);
        assertEquals(inputText, decryptedText, "Decrypted text should match the original input");
    }

    @Test
    public void testEncryptionDecryption_EmptyString() {
        String inputText = "";
        String encryptedText = EncryptionUtils.encryptString(inputText);
        assertNotNull(encryptedText, "Encrypted text should not be null");
        assertNotEquals(inputText, encryptedText, "Encrypted text should differ from input");

        String decryptedText = EncryptionUtils.decryptString(encryptedText);
        assertEquals(inputText, decryptedText, "Decrypted text should match the original input");
    }

    @Test
    public void testInvalidDecryption() {
        String invalidCipherText = "InvalidCipherText";
        assertThrows(RuntimeException.class, () -> {
            EncryptionUtils.decryptString(invalidCipherText);
        }, "Expected RuntimeException for invalid cipher text");
    }
}
