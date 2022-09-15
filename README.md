# pw_manager

This is a local password manager that ultilizes AES allowing for viewing and storing of encrypted passwords inside a MySQL table. The programs requires a table named "secrets" to be created and it also needs for the database for which the table is in to be specified (default is 'sList'). 


The code for the table and trigger generation is included in the database.sql file inside 'src/main/java/com'
![alt text](https://i.ibb.co/HTMtnbF/table.png)


By default the program assumes the database the table is contained in is named 'sList', but that can be changed by changing 'sList' in the following line in the 'sqlConnector.java' file to the name of your database.

![alt text](https://i.ibb.co/MPvMtcS/db-name.png)


When first launched you will be asked to input your key for use in the encryption and decryption process. If it's your first time launching the application you can enter anything you desire for your key, but be sure to remember it for next time you log in.

![alt text](https://i.ibb.co/2dQ4nbq/key-entry.png)


After the key entry prompt you are asked to enter your mysql login information.

![alt text](https://i.ibb.co/6rpSvnR/mysql-login.png)

Once succesfully logged into the application you are met with a gui that includes three buttons - 'get', 'insert' and 'exit'.
    
    -Get = Retrieves the data stored in the database and displays it
    
    -Insert = Opens a prompt to enter a domain/username/password combo into the database
    
    -Exit = Quits the program


![alt text](https://i.ibb.co/Fs39Fh2/pw-manager.png) ![alt text](https://i.ibb.co/vvxksZL/insert-shot.png)

After some information is entered into the database you can use to get button to retreive and display the table entries. The username and password values are encrypted by default and can be decrypted by clicking on the desired row in the table. Once clicked the row will turn green and will display the usernames and passwords in plaintext until the row is clicked a second time and is encrypted once again.


![alt text](https://i.ibb.co/Gn93Rh0/encrypted.png) ![alt text](https://i.ibb.co/54vFb9v/unencrypted.png)



