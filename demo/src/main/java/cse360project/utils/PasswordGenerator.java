package cse360project.utils;

import java.util.Random;

public class PasswordGenerator {
    static Random rand = new Random();
    public static String generate() {
        final int length = 8;
        char characters[] = new char[length];
        characters[0] = generateLowercaseCharacter();
        characters[1] = generateUppercaseCharacter();
        characters[2] = generateNumericCharacter();
        characters[3] = generateSpecialCharacter();

        for (int i = 4; i < length; i++) {
            int choice = rand.nextInt(6);
            if (choice >= 0 && choice < 2) {
                characters[i] = generateLowercaseCharacter();
            } else if (choice >= 2 && choice < 4) {
                characters[i] = generateUppercaseCharacter();
            } else if (choice == 4) {
                characters[i] = generateNumericCharacter();
            } else if (choice == 5) {
                characters[i] = generateSpecialCharacter();
            }
        }

        shuffleCharArray(characters);

        return String.valueOf(characters);
    }

    static char generateUppercaseCharacter() {
        return (char)randInclusive('A', 'Z');
    }

    static char generateLowercaseCharacter() {
        return (char)randInclusive('a', 'z');
    }

    static char generateNumericCharacter() {
        return (char)randInclusive('0', '9');
    }

    static char generateSpecialCharacter() {
        String specialCharacters = "~`!@#$%^&*()_-+={}[]|\\:;\"'<>,.?/";
        return specialCharacters.charAt(randInclusive(0, specialCharacters.length()-1));
    }

    static int randInclusive(int a,  int b) {
        return rand.nextInt(b - a + 1) + a;
    }

    static void shuffleCharArray(char[] array) {
        for (int i = 0; i < array.length; i++) {
            int randomIndexToSwap = rand.nextInt(array.length);
            char temp = array[randomIndexToSwap];
            array[randomIndexToSwap] = array[i];
            array[i] = temp;
        }
    }

    public static void main(String[] args) {
        System.out.println(generate());
        System.out.println(generate());
        System.out.println(generate());
        System.out.println(generate());
    }
}
