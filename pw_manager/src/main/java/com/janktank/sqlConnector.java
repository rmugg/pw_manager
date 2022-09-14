package com.janktank;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;

public class sqlConnector {

    Connection conn;
    IvParameterSpec ivPa = Encryptor.generateIv();
    IvParameterSpec recon;
    String algorithm = "AES/CBC/PKCS5Padding";

    public sqlConnector(String user, String pass) throws NoSuchAlgorithmException, InvalidKeySpecException {

        try {
            
            conn =
            DriverManager.getConnection("jdbc:mysql://localhost/sList?" +
            "user="+user+"&password="+pass);

        } catch (SQLException ex) {
            // handle any errors
            System.out.println("Database 'sList' does not exist or incorrect username/password combination.");
            System.exit(0);
        }
    }

    public ArrayList<secretObject> retrieveAll()
            throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException {
        ArrayList<secretObject> output = new ArrayList<secretObject>();
        String query = "select secret_id, secret_domain, secret_user,secret_value, who, submission_date, iv from secrets ORDER BY secret_domain";

        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String secretID = rs.getString("secret_id");
                String secretDomain = rs.getString("secret_domain");
                String secretUser = rs.getString("secret_user");
                String secretValue = rs.getString("secret_value");
                String date = rs.getString("submission_date");

                byte[] decoded = Base64.getDecoder().decode(rs.getString("iv"));
                recon = new IvParameterSpec(decoded);

                String plainTextDomain = Encryptor.decrypt(algorithm, secretDomain, recon);
                secretObject secret = new secretObject(secretID, plainTextDomain, secretUser, secretValue,
                        rs.getString("iv"), date);
                output.add(secret);
            }
        } catch (SQLException e) {
            // e.printStackTrace();
            System.out.println("Secret key is invalid.");
            System.exit(0);
        }
        return output;
    }

    public void insertEntry(String domain, String userName, String secretValue)
            throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, NoSuchPaddingException,
            InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException {
        ivPa = Encryptor.generateIv();

        String cipherTextDomain = Encryptor.encrypt(algorithm, domain, ivPa);
        String cipherTextUser = Encryptor.encrypt(algorithm, userName, ivPa);
        String cipherTextSecret = Encryptor.encrypt(algorithm, secretValue, ivPa);

        String query = "INSERT INTO secrets(secret_domain, secret_user, secret_value, iv) VALUES(?,?,?,?)";
        String iv = Base64.getEncoder().encodeToString(ivPa.getIV());

        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, cipherTextDomain);
            stmt.setString(2, cipherTextUser);
            stmt.setString(3, cipherTextSecret);
            stmt.setString(4, iv);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
