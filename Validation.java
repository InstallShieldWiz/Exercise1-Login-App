/*******************************************************************************
file:       Login App - Validation class
author:     Jacobson, David
course:     CEN 4078
assignment: Create a program that allows a user to login
                - Part 2 create a validation class. 
*******************************************************************************/

public class Validation {

    // constructor 
    public Validation () {
    }

    public boolean seqValidation(String userInput) {
        // Return false if input contains space, forward slash, hyphen, semicolon, or quotes
        // uses regex to match any char  /;\"- with any char before or after it.
        return !userInput.matches(".*[ /;\"-].*");
    }


    public boolean validPassword(String userInput) {
        // return true if password is between 8-12 char 
        // 1 upper case 
        // 1 lower case
        // and 1 #
        if (userInput.length() < 8 || userInput.length() > 12) return false;
        else {
            return userInput.matches(".*[A-Z].*")
            && userInput.matches(".*[a-z].*")
            && userInput.matches(".*[0-9].*");
        }
    }

    public boolean iOverflow(String strToCheck) {
        try {
            int value = Integer.parseInt(strToCheck);
            // Return true if the value is within the valid range for an int
            return value >= -2147483648 && value <= 2147483647;
        } catch (NumberFormatException e) {
            return false;  // Return false if parsing fails
        }
    }

}