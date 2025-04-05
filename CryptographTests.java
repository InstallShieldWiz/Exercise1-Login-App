/*******************************************************************************
file:       Login App - CryptographTests class
author:     Jacobson, David
course:     CEN 4078
assignment: class to test the Cryptographer class.
*******************************************************************************/

public class CryptographTests {
    public static void main(String[] args) {
        Cryptographer crypto = new Cryptographer();

        // Test cases for letter encryption
        String[] letterTests = {
            "Hello",
            "TESTING",
            "Cryptography"
        };

        // Test cases for number encryption
        String[] numberTests = {
            "12345",
            "9876543210",
            "2024"
        };

        System.out.println("\nTesting Letter Encryption/Decryption:");
        System.out.println("------------------------------------");
        for (String input : letterTests) {
            String encrypted = crypto.encryptVigenere(input);
            String decrypted = crypto.decryptVigenere(encrypted);
            System.out.println("Original: " + input);
            System.out.println("Encrypted: " + encrypted);
            System.out.println("Decrypted: " + decrypted);
            System.out.println("------------------------------------");
        }

        System.out.println("\nTesting Number Encryption/Decryption:");
        System.out.println("------------------------------------");
        for (String input : numberTests) {
            String encrypted = crypto.encryptNumber(input);
            String decrypted = crypto.decryptNumber(encrypted);
            System.out.println("Original: " + input);
            System.out.println("Encrypted: " + encrypted);
            System.out.println("Decrypted: " + decrypted);
            System.out.println("------------------------------------");
        }
    }
}
