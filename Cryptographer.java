/*******************************************************************************
file:       Login App - Cryptographer class
author:     Jacobson, David
course:     CEN 4078
assignment: Create a program that allows a user to login
                - Part 3 create a class to encrypt and decrypt stored passwords and usernames
*******************************************************************************/

import java.security.SecureRandom;

public class Cryptographer {
    private static final String ALPHAKEY = "ARGOSROCK";
    private static final String NUMBERKEY = "1963";
    private final SecureRandom secureRandom = new SecureRandom(); // SecureRandom for MFA generation

    // Method 1 - Encrypt using Vigenere for alphabetic input

    public String encryptVigenere(String clearText) {
        char[] cypherString = new char[clearText.length()];
    
        for (int i = 0; i < clearText.length(); i++) {
            char currentChar = clearText.charAt(i);
            char keyChar = ALPHAKEY.charAt(i % ALPHAKEY.length());
    
            if (Character.isLetter(currentChar)) {
                // Encrypts only letters
                if (Character.isUpperCase(currentChar)) {
                    cypherString[i] = (char) ((currentChar - 'A' + (keyChar - 'A')) % 26 + 'A');
                } else {
                    cypherString[i] = (char) ((currentChar - 'a' + (keyChar - 'A')) % 26 + 'a');
                }
            } else {
                // Encrypts characters 
                char numKeyChar = NUMBERKEY.charAt(i % NUMBERKEY.length());
                cypherString[i] = (char) ((currentChar + (numKeyChar - '0')) % 127);
            }
        }
    
        return new String(cypherString);
    }


    // Method 2 - Decrypt using Vigenere for alphabetic input
    public String decryptVigenere(String cipherText) {
        char[] clearString = new char[cipherText.length()];
    
        for (int i = 0; i < cipherText.length(); i++) {
            char currentChar = cipherText.charAt(i);
            char keyChar = ALPHAKEY.charAt(i % ALPHAKEY.length());
    
            if (Character.isLetter(currentChar)) {
                // Decrypt uppercase letters
                if (Character.isUpperCase(currentChar)) {
                    clearString[i] = (char) (((currentChar - 'A') - (keyChar - 'A') + 26) % 26 + 'A');
                } 
                // Decrypt lowercase letters
                else {
                    clearString[i] = (char) (((currentChar - 'a') - (keyChar - 'A') + 26) % 26 + 'a');
                }
            } else {
                // For non-alphabetic characters
                char numKeyChar = NUMBERKEY.charAt(i % NUMBERKEY.length());
                // Handle non-alphabetic characters
                clearString[i] = (char) ((currentChar - (numKeyChar - '0') + 127) % 127);
            }
        }
        return new String(clearString);
    }
    

        // Method 3 - Encrypt using Vigenere for numeric input
        public String encryptNumber(String clearText) {
            char[] cypherString = new char[clearText.length()];
    
            // for loopo to input clearText into array
            for (int i = 0; i < clearText.length(); ++i) {
                if (Character.isDigit(clearText.charAt(i))) {
                    cypherString[i] = clearText.charAt(i);
                }
            }
    
            // for loop to encrypt the array. 
            for (int i = 0; i < clearText.length(); ++i) {
                if (Character.isDigit(cypherString[i])) {
                    char keyChar = NUMBERKEY.charAt(i % NUMBERKEY.length());
                    int encryptedChar = (((cypherString[i] - '0') + (keyChar - '0')) % 10) + '0';
                    cypherString[i] = (char) encryptedChar;
                }
            }
            return new String(cypherString);
        }
    
        // Method 4 - Decrypt using Vigenere for numeric input
        public String decryptNumber(String cipherText) {
            char[] clearString = new char[cipherText.length()];
    
            //for loop to copy into the array
            for (int i = 0; i < cipherText.length(); i++) {
                if (Character.isDigit(cipherText.charAt(i))) {
                    clearString[i] = cipherText.charAt(i);
                }
            }
    
            // for loop to decrypt array
            for (int i = 0; i < cipherText.length(); i++) {
                if (Character.isDigit(clearString[i])) {
                    char keyChar = NUMBERKEY.charAt(i % NUMBERKEY.length());
                    int decryptedChar = (((clearString[i] - '0') - (keyChar - '0') + 10) % 10) + '0';
                    clearString[i] = (char) decryptedChar;
                }
            }
            return new String(clearString);
        }
    
        // Generates a random number between 1,000,000,000 and 2,147,483,647
    public String generateMFA() {
        int mfaCode = 1_000_000_000 + secureRandom.nextInt(1_147_483_647 - 1_000_000_000); // Generates a 10-digit number
        return String.valueOf(mfaCode); // Convert to String
    }

}