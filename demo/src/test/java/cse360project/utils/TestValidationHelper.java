package cse360project.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestValidationHelper {

    @Test
    public void testValidUsername() {
        assertTrue(ValidationHelper.isValidUsername("validUser_123"), "Valid username should pass");
        assertTrue(ValidationHelper.isValidUsername("user.name"), "Valid username with period should pass");
    }

    @Test
    public void testInvalidUsername() {
        assertFalse(ValidationHelper.isValidUsername(""), "Empty username should fail");
        assertFalse(ValidationHelper.isValidUsername("ab"), "Username less than 3 characters should fail");
        assertFalse(ValidationHelper.isValidUsername("user@name"), "Username with invalid character '@' should fail");
    }

    @Test
    public void testValidPassword() {
        assertTrue(ValidationHelper.isValidPassword("Passw0rd!".toCharArray()), "Valid password should pass");
        assertTrue(ValidationHelper.isValidPassword("P@ssw0rd".toCharArray()), "Valid password with special characters should pass");
    }

    @Test
    public void testInvalidPassword() {
        assertFalse(ValidationHelper.isValidPassword("short".toCharArray()), "Password less than 6 characters should fail");
        assertFalse(ValidationHelper.isValidPassword("password".toCharArray()), "Password missing at least 3 character types should fail");
        assertFalse(ValidationHelper.isValidPassword("123456".toCharArray()), "Password with only numeric characters should fail");
    }

    @Test
    public void testPasswordMatch() {
        char[] password = "Password123".toCharArray();
        char[] confirmPassword = "Password123".toCharArray();
        assertTrue(ValidationHelper.doPasswordsMatch(password, confirmPassword), "Passwords should match");

        confirmPassword = "WrongPassword123".toCharArray();
        assertFalse(ValidationHelper.doPasswordsMatch(password, confirmPassword), "Passwords should not match");
    }

    @Test
    public void testValidName() {
        assertTrue(ValidationHelper.isValidName("John Doe"), "Valid name should pass");
        assertTrue(ValidationHelper.isValidName("O'Neil"), "Name with apostrophe should pass");
        assertTrue(ValidationHelper.isValidName("Mary-Jane"), "Name with hyphen should pass");
    }

    @Test
    public void testInvalidName() {
        assertFalse(ValidationHelper.isValidName(""), "Empty name should fail");
        assertFalse(ValidationHelper.isValidName("   "), "Name with only spaces should fail");
        assertFalse(ValidationHelper.isValidName("John#Doe"), "Name with invalid character '#' should fail");
    }

    @Test
    public void testValidEmail() {
        assertTrue(ValidationHelper.isValidEmail("test@example.com"), "Valid email should pass");
        assertTrue(ValidationHelper.isValidEmail("user.name@example.co"), "Valid email with domain extension should pass");
    }

    @Test
    public void testInvalidEmail() {
        assertFalse(ValidationHelper.isValidEmail("invalidemail.com"), "Email missing '@' should fail");
        assertFalse(ValidationHelper.isValidEmail("user@domain"), "Email missing domain extension should fail");
        assertFalse(ValidationHelper.isValidEmail("user@domain@com"), "Email with multiple '@' should fail");
    }
}