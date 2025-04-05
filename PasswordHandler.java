/*******************************************************************************
file:       Login App - PasswordHandler class
author:     Jacobson, David
course:     CEN 4078
assignment: V5.0class to handle password related operations.
*******************************************************************************/

import java.io.IOException;

public class PasswordHandler {
    
    private final Cryptographer cryptographer;
    private final DefaultPassword defaultPassword;
    private final Validation validation; // Instance of Validation for validation methods

    // default constructor
    public PasswordHandler() {
        cryptographer = new Cryptographer(); // Instantiate Cryptographer class
        defaultPassword = new DefaultPassword(); // Instantiate DefaultPassword class
        validation = new Validation(); // Instantiate Validation class
    }

    // Calls the DefaultPassword class instantiation and constructor
    public String generateDefaultPassword(String username) throws DefaultPasswordException {
        try {
            return defaultPassword.defaultPassword(username);
        } catch (IOException e) {
            throw new DefaultPasswordException("Failed to generate default password.");
        }
    }

    // Encrypts a password using the Cryptographer class
    public String encryptPassword(String password) throws PasswordPolicyException {
        if (!validation.validPassword(password)) { // Call validation method
            throw new PasswordPolicyException("Password does not meet policy requirements.");
        }
        return cryptographer.encryptVigenere(password);
    }

    public String encryptNumber(String number) {
        return cryptographer.encryptNumber(number);
    }

    public String decryptNumber(String number) {
        return cryptographer.decryptNumber(number);
    }

    // decrypts a password using the cryptographer class
    public String decryptPassword(String password) {
        return cryptographer.decryptVigenere(password);
    }

    // Validate the password according to specified rules
    public boolean validatePassword(String password) throws PasswordValidationException {
        if (!validation.validPassword(password)) { // Call validation method
            throw new PasswordValidationException("Password does not meet policy requirements.");
        }
        return true;
    }

    // Generate MFA code
    public String generateMFA() {
        return cryptographer.generateMFA(); // Use Cryptographer to generate MFA
    }

    public boolean passwordValidation (String password) throws PasswordValidationException {
        if (!validation.seqValidation(password) || !validation.validPassword(password)) {   
            throw new PasswordValidationException("Password does not meet policy requirements.");
        }
        return true;
    }

    public boolean seqValidation (String password) {
        return validation.seqValidation(password);
    }

    public boolean iOverflow(String strToCheck) {
        return validation.iOverflow(strToCheck);
    }

    //Exception Classes
    public static class PasswordPolicyException extends Exception {
        public PasswordPolicyException(String message) {
            super(message);
        }
    }

    public static class PasswordValidationException extends Exception {
        public PasswordValidationException(String message) {
            super(message);
        }
    }

    public static class DefaultPasswordException extends Exception {
        public DefaultPasswordException(String message) {
            super(message);
        }
    }
}


