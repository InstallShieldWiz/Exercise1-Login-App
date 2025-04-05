/*******************************************************************************
file:       Login App - DefaultPassword class
author:     Jacobson, David
course:     CEN 4078
assignment: class to generate a default password for a user.
*******************************************************************************/

import java.io.FileWriter;
import java.io.IOException;

public class DefaultPassword {
    

    public String defaultPassword(String username) throws IOException {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder defaultPassword = new StringBuilder();
    
        for (int i = 0; i < 12; i++) {
            int index = (int) (Math.random() * chars.length());
            defaultPassword.append(chars.charAt(index));
        }

        // writes a text file as a users email with default password
        try (FileWriter writer = new FileWriter(username + "'s email.txt")) {
            writer.write("""
                    ***TEMPORARY LOGIN CREDENTIALS***
                    Please change your password upon first login.
                    
                    Username: %s
                    Temporary Password: %s

                    ** If you did not request this please contact your administrator. **
                    """.formatted(username, defaultPassword));
        }
        
        return defaultPassword.toString();
    }


}


