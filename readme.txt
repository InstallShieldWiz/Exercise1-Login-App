Thank you for using my login app!

This app was designed to be run in command line via powershell, terminal, or through an IDE such as VS Code. 
Upon start up the app will load the user databse from the database text file, greet the user and begin the 
login process asking for a username and subsequent password which are held in seperate 2d arrays in the code after
being loaded from the text file. 

** Usernames and password scan be found in cleartext in the code submitted (I considered encrypting them but for this 
assignment decided to keep it simple. ) ** 

The app will automatically lock out an account after 3 failed login attempts and will continue denying access to that account
until the app is re run OR the "String" located in the 3rd spot in each 2d array is reset to a capital 'U'. 

Upon a successful login attempt a message like "*** Login Successful for Engineer!! ***" (with 'Engineer' replaced with the 
user account attempting to be logged into) and the app will exit. 

** more usernames and passwords can be added through the txt file should a larger database be desired. **

Version 2.0

NEW FEATURES
new features added in version 2
- MFA - Please review the MFA.txt document to find the MFA codes

    *** MFA's ARE REWRITTEN TO THE MFA's.txt FILE EVERYTIME THE CODE IS RUN! ***
        - You can find the codes for any given instance in the MFA's.txt file generated

- Password Validation method and implementation
- MFA validation method and implementation
- SQL validation sanitization method and implementation  

Version 3.0

Added a new class (Cryptographer) to encrypt and decrypt stored usernames adn passwords.  

changed order of operations on load method to validate input before encryption 

added development only clear text info output for grading purposes (ClearTextMFA.txt)

Version 4.0
Because of the addition of the default password requirement, I rewrote my code to allow for adding new users 
which means i had to dynamically allocate a new array every time a new user was added.  And also meant I needed
 to change how the program functioned at its top menu

 So i added a switch statement to handle the choice to register a new user login or exit the program.

 The login choice has the same functinoality as previous versions but the register new users choice
 now allows for a new user to be added.  stores the user name in clear text and encrypts the password before
  its allocated to the new array. It also adds the MFA and encrypts it before storing it in the new array. 

  I added a prompt when entering a new password with the requirements set forth in previous versions. 

I added some error handling through try catch statements. 

I also made sure that as a new user its added the EncryptedDatabase.txt and ClearTExtMFA.txt files are updated

** as a side note when entering a new username the program does verify that a user name exists in the database
which admitedly is a vulnearbility to compromising username account data and could be exploited through
some type of rainbow table attack.  Im not sure if I should work around this feature or if the exposure is acceptable.

I did some minor reformatting on my code because I was feeling it was getting sloppy and convoluted and may still
think of a better way to format the program in future revisions. 

Created a default password method that creates a text file with the email a user WOULD receive 
with the default password if they failed to enter a valid password 2 times. 

added a password change method to change passwords along with a password rest operations

Change password method kicks user back to main menu after even 1 unsuccessful attempt 
at MFA or username or password validation.  I designed it this way as a Security
over convenience method.  

     ** found bug that successfully changes password on a nonexistent or incorrect username**

     **fixed bug** lol

MOved loginsuccess boolean to the login method from the MFAPrompt method to keep from setting the value true
prematurely as the change password method is called and uses the MFAprompt method exiting the progrm early

Version 5.0

Added the Password handler class. 

edited some comment headers for file information and identificaion.

moved the MFA geneartion to the cryptographer class.

Changed all validation methods to only be called through the passwordHandler instatiation. 

implemented special exceptions to throw inside of passwordhandler class and implemented try catch statements for all methods
that utilize the passwordhandler methods inside of hte loginApp class. 

Had brilliant idea to make a program in Java to let my 2 kids fight each other wit pokemon using GUI with drop down menus
and some buttons.  I'm not sure if I will actually do this or not. 


David Jacobson
