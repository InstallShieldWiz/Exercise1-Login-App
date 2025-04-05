/*******************************************************************************
file:       Login App
author:     Jacobson, David
course:     CEN 4078
assignment: Create a program that allows a user to login
    v.20 - added MFA, password and username validation to prevent SQL Injection attackes
*******************************************************************************/
import java.io.Console;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class LoginApp {
    private boolean loginSuccess = false;
    Scanner scanner = new Scanner(System.in);
    // 2d arrays holding username/value pairs.
    private String[][] Users; // to be initialized using the txt file Database.txt

    Validation initLoadVal = new Validation();
    DefaultPassword initDefaultPassword = new DefaultPassword();
    PasswordHandler passwordHandler = new PasswordHandler();
    
    // default constructor
    public LoginApp() {
      if(load());
        // loads database file into 2d arrays 
        // during load execution MFA's need to be generated and associated with users
    }



    // load method to load database from txt file into 2d arrays 
    private boolean load() {
       
        ArrayList<String[]> userList = new ArrayList<>();
        File file = new File("StartupDatabase.txt");

        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                String[] parts = line.split(",");

                if (parts.length < 2) continue; // Ensure proper format

                // validates entries prior to encryption.
                if (!initLoadVal.seqValidation(parts[0]) || 
                !initLoadVal.seqValidation(parts[1]) || 
                !initLoadVal.validPassword(parts[1])) {
                System.out.println("Validation failed for user: " + parts[0]);
                return false;
            }

            String mfa = String.valueOf(passwordHandler.generateMFA());
            if (!initLoadVal.iOverflow(mfa)) {
                System.out.println("MFA validation failed for user: " + parts[0]);
                return false;
            }
     
                // Encrypt stored passwords and MFA codes
                String encryptedPassword = passwordHandler.encryptPassword(parts[1]);
                String encryptedMFA = passwordHandler.encryptNumber(mfa);


                // Add encrypted data to the user array
                String[] userWithMFA = new String[]{parts[0], encryptedPassword, "U", encryptedMFA};
                userList.add(userWithMFA);
            }


        Users = userList.toArray(new String[userList.size()][]);

        // exports users with newly generated and encrypted information to EncryptedDatabase.txt
        saveUsers();
        // for development purposes only - decrypts and saves information in CleartextMFA.txt file
        clearTextMFA();

        } catch (Exception e) {
            System.out.println("Error: StartupDatabase.txt could not be found or it could not be opened");
            return false;
        }
        return true;
    }


    
    // saves users to MFA's.txt file for reference when entering codes
    public void saveUsers() throws IOException {
      try (FileWriter writer = new FileWriter("EncryptedDatabase.txt")) {
        if (Users == null || Users.length == 0) {
            System.out.println("No users to save.");
            return;
        }

        for (String[] user : Users) {
            writer.write(String.join(",", user));
            writer.write(System.lineSeparator());
        }
        writer.close();
        
        }catch (IOException e) {
            System.out.println("Error writing to EncryptedDatabase.txt: " + e.getMessage());
        }
    }



    //for development purposes only. ***DELETE PRIOR TO PRODUCTION***
    public void clearTextMFA() throws IOException {
        FileWriter writer = new FileWriter("CleartextMFA.txt");
         for (String[] user : Users) {
                writer.write(user[0] + "," + passwordHandler.decryptPassword(user[1]) + "," + passwordHandler.decryptNumber(user[3]));
                writer.write(System.lineSeparator());
         }
         writer.close();
    }



    // Method to run the app
    public void runApp() {
        while (loginSuccess == false) {
            System.out.println("Welcome, Please choose an option: ");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Reset Password");
            System.out.println("4. Change Password");
            System.out.println("5. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice){

                case 1:
                    login();
                    break;
                case 2:
                    try {
                        createUser(scanner);
                    } catch (IOException e) {
                        System.out.println("Error creating user: " + e.getMessage());
                    }
                    break; 
                case 3:
                    try {
                        resetPassword();
                    } catch (IOException e) {
                        System.out.println("Error creating default password: " + e.getMessage());
                    }
                    break;
                case 4:
                    try {
                        changePassword();
                    } catch (IOException e) {
                        System.out.println("Error changing password: " + e.getMessage());
                    }
                    break;
                case 5:
                    System.out.println("Exiting program...");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    } 



    //method to login existing user - previously login method
    public void login() {
        System.out.println("Welcome,Please enter your username: ");

        String username = scanner.nextLine();
        boolean userFound = false; 
        if (passwordHandler.seqValidation(username)) {
            for (int j = 0; j < Users.length; ++j) {  // cycles thorugh usernames and find the correct one.
                if (username.equals(Users[j][0])) {
                    userFound = true; 
                    if(passwordPrompt(j)) { // calls seperate password method
                        loginSuccess = true;
                        return;
                    }
                }
            }
        }
        
        if (!userFound) {  
            System.out.println("Username not found"); // only if user is not found
        }
    }


    

    // method to register a new user.
    public void createUser(Scanner scanner) throws IOException {
        String username = null;
        int createPasswordAttempts = 0;

        do{
            System.out.println("Welcome, Please enter a new username: ");
            username = scanner.nextLine();
            if (!passwordHandler.seqValidation(username)){
                System.out.println("Invalid username. Please try again.");
            }
        }while (!passwordHandler.seqValidation(username));

        
        while (createPasswordAttempts < 2){
            Console console = System.console();
            System.out.println("Please enter a password: ");
            System.out.println(passwordPolicy());

            char[] newPasswordChars = console.readPassword();
            String password = new String(newPasswordChars);

            try {
                if (passwordHandler.passwordValidation(password)) {

                    //creates new array to copy Users array
                    String[][]newUsers = new String[Users.length + 1][4];

                    // for loop to copy Users objects to newUsers array
                    for (int i = 0; i < Users.length; ++i) {
                        newUsers[i] = Users[i];
                    }

                    // creates, stores, andencrypts new user information appended to the Users array.
                    newUsers[Users.length] = new String[]{
                        username, 
                            passwordHandler.encryptPassword(password), 
                        "U", 
                        passwordHandler.encryptNumber(String.valueOf(passwordHandler.generateMFA()))};
                    
                    // assignes origional Users array with newUsers values. 
                    Users = newUsers;

                    //saves users to EncryptedDatabase.txt
                    saveUsers();

                    //for development purposes only - decrypts and saves information in CleartextMFA.txt file
                    clearTextMFA();
                    
                    System.out.println("User created successfully!");
                    break;
                }
            } catch (PasswordHandler.PasswordValidationException | PasswordHandler.PasswordPolicyException e) {
                System.out.println("Invalid password: " + e.getMessage());
                createPasswordAttempts += 1;
            }
            if (createPasswordAttempts == 2){
                    password = initDefaultPassword.defaultPassword(username);
                    try {
                        if (passwordHandler.passwordValidation(password)) {

                            //creates new array to copy Users array
                            String[][]newUsers = new String[Users.length + 1][4];
                
                            // for loop to copy Users objects to newUsers array
                            for (int i = 0; i < Users.length; ++i) {
                                newUsers[i] = Users[i];
                            }
                
                            // creates, stores, andencrypts new user information appended to the Users array.
                            newUsers[Users.length] = new String[]{
                                username, 
                                passwordHandler.encryptPassword(password), 
                                "U", 
                                passwordHandler.encryptNumber(String.valueOf(passwordHandler.generateMFA()))};
                            
                            // assignes origional Users array with newUsers values. 
                            Users = newUsers;
                
                            //saves users to EncryptedDatabase.txt
                            saveUsers();
                
                            //for development purposes only - decrypts and saves information in CleartextMFA.txt file
                            clearTextMFA();
                            
                            System.out.println("User created successfully!");


                        System.out.println("""
                                ***Too many invalid password attempts.***
                                An email will be sent to you with a temporary default password.""");
                   
                        }
                    } catch (PasswordHandler.PasswordValidationException | PasswordHandler.PasswordPolicyException e) {
                        System.out.println("Invalid password: " + e.getMessage());
                    }
                }  
        }
    }



    // method to take care of password entry and verification with the input j to denote the array for that specific user
    public boolean passwordPrompt(int j) {
        int attempts = 0;
        Console console = System.console();

        if (Users[j][2].equals("U")) { // skips the login process if account is locked to avoid multiple login attempts
            while (attempts < 3) { 
                attempts += 1; // adds 1 to attempts every loop
                System.out.println("Welcome " + Users[j][0] +  ", enter your password: ");
                char[] passwordChars = console.readPassword(); // uses characters to hide the whole password and stitching them together to make a "hidden password String"
                String password = new String(passwordChars);

                try {
                    if(passwordHandler.passwordValidation(password)){
                        String encryptedPassword = passwordHandler.encryptPassword(password);

                        if (encryptedPassword.equals(Users[j][1]) && MFAPrompt(j)) { // calls for MFA check
                            return true;
                        }
                    }
                } catch (PasswordHandler.PasswordValidationException | PasswordHandler.PasswordPolicyException e) {
                    System.out.println("Invalid password: " + e.getMessage());
                }
            }
        }
        if (attempts == 3) { // if 3 i ever reached the account is locked and the 3rd item in that users array is changed to "L" from "U"
            Users[j][2] = "L";
        }
        System.out.println("This account has been locked for security reasons.  Please contact your administrator or press Ctrl + C to exit");
        return false;
    }
    


    // method to take care of password entry and verification with the input j to denote the array for that specific user
    public boolean MFAPrompt(int j) {
        Console console = System.console();
            System.out.println("Please enter you MFA token: ");

            char[] MFAChars = console.readPassword(); // uses characters to hide the whole password and stitching them together to make a "hidden password String"
            String strMFA = new String(MFAChars);

            // correct password check
            if (passwordHandler.iOverflow(strMFA)) {
                String encryptedstrMFA = passwordHandler.encryptNumber(strMFA);
                if (encryptedstrMFA.equals(Users[j][3])) {
                    System.out.println("*** Login Successful for " + Users[j][0] + "!! ***");
                    return true;
                }
            }    
        return false;
    }



    public String passwordPolicy() {
        return """
               NOTE: **Password must be 8-12 characters with at least:
               1 uppercase letter,
               1 lowercase letter,
               and 1 number.**""";
    }



    public void resetPassword() throws IOException {
        System.out.println("Please enter your username"); 
        String resetUsername = scanner.nextLine();
        
        if (resetUsername != null && passwordHandler.seqValidation(resetUsername)) {
            for (int i = 0; i < Users.length; ++i) {
                if (Users[i][0].equals(resetUsername)) {
                    try {
                        Users[i][1] = passwordHandler.encryptPassword(initDefaultPassword.defaultPassword(resetUsername));
                        System.out.println("Password reset.");
                        //populates EncryptedDatabase.txt with new value. 
                        saveUsers();
                        //for dev puroposed only - delete before prod
                        clearTextMFA();
                    } catch (PasswordHandler.PasswordPolicyException e) {
                        System.out.println("Error encrypting password: " + e.getMessage());
                    }
                }
                else {
                    System.out.println("Username not found");
                }
            }
        }
        
    }



    public void changePassword() throws IOException {
        int userIndex = -1;
        Console console = System.console(); 
        
        System.out.println("Please enter your username: "); 
        String username = scanner.nextLine();

        if (passwordHandler.seqValidation(username)) {
            for (int i = 0; i < Users.length; ++i) {
                if (Users[i][0].equals(username)) {
                    userIndex = i;
                }
            }
        }
        else {
            System.out.println("Invalid username. Please try again.");
            return;  // Return to main menu
        }

        if (userIndex > 0 && username != null) {
            System.out.println("Please enter your current password: ");
            char[] currentPasswordChars = console.readPassword();
            String currentPassword = new String(currentPasswordChars);

            if (currentPassword.equals(passwordHandler.decryptPassword(Users[userIndex][1]))) {
                if (MFAPrompt(userIndex)) {
                    System.out.println("Please enter your new password: ");
                    System.out.println(passwordPolicy());
                    char[] newPasswordChars = console.readPassword();
                    String newPassword = new String(newPasswordChars);

                    try {
                        if (passwordHandler.passwordValidation(newPassword)) {   
                            System.out.println("Please re-enter your new password: ");
                            char[] confirmPasswordChars = console.readPassword();
                            String confirmPassword = new String(confirmPasswordChars);

                            if (newPassword.equals(confirmPassword)) {
                                try {
                                    Users[userIndex][1] = passwordHandler.encryptPassword(newPassword);
                                    saveUsers();
                                    clearTextMFA();
                                    System.out.println("Password changed successfully.");
                                } catch (PasswordHandler.PasswordPolicyException e) {
                                    System.out.println("Invalid password: " + e.getMessage());
                                }
                            }
                            else {
                                System.out.println("New passwords do not match. Please try again.");
                            }
                        }
                    } catch (PasswordHandler.PasswordValidationException e) {
                        System.out.println("Invalid password: " + e.getMessage());
                    }
                }
            }
            else {
                System.out.println("Invalid current password. Please try again.");
            }
        }
        else {
            System.out.println("Invalid username. Please try again.");
        }  
 
    }




    // main prgoram method
    public static void main(String[] args) {
        LoginApp AppInstance = new LoginApp();
        AppInstance.runApp();
       
    }
}