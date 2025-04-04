package ch.bbw.pr.tresorbackend.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class PasswordEncryptionService {

   @Value("${app.security.pepper}")   // Aus application.properties
   private String pepper;

   private static final int BCRYPT_COST = 12;

   /**
    * Hashes a password using bcrypt with salt and pepper.
    *
    * @param password The plaintext password.
    * @return The hashed password.
    */
   public String hashPassword(String password) {
      String salt = generateSalt();
      
      String passwordWithPepper = password + pepper;
      
      return BCrypt.hashpw(passwordWithPepper, BCrypt.gensalt(BCRYPT_COST) + salt);
   }

   /**
    * Verifies if a plaintext password matches the stored hashed password.
    *
    * @param password       The plaintext password.
    * @param hashedPassword The stored hashed password.
    * @return True if passwords match, false otherwise.
    */
   public boolean verifyPassword(String password, String hashedPassword) {        
      // Combine input password with pepper and salt
      String passwordWithPepper = password + pepper;
      
      // Verify using bcrypt
      return BCrypt.checkpw(passwordWithPepper, hashedPassword.substring(0, hashedPassword.length() - 22));
   }

   /**
    * Generates a secure random salt.
    *
    * @return A base64 encoded salt.
    */
   private String generateSalt() {
      byte[] saltBytes = new byte[16];
      new SecureRandom().nextBytes(saltBytes);
      return Base64.getEncoder().encodeToString(saltBytes);
   }

   /**
    * Checks if the provided password matches the hashed password.
    *
    * @param password       The plaintext password.
    * @param hashedPassword The hashed password.
    * @return True if passwords match, false otherwise.
    */
   public boolean doPasswordMatch(String password, String hashedPassword) {
      return verifyPassword(password, hashedPassword);
   }
}
