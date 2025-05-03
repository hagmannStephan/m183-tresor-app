# Encrypt Util
## General
I added these attributes to the class:
```java
   private final SecretKeySpec secretKeySpec;
   private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
   private static final int IV_LENGTH = 16;
```
## EncryptUtil
The EncryptUtil constructor initializes an AES encryption key from a given secret string:
```java
   public EncryptUtil(String secretKey) {
      try {
         MessageDigest sha = MessageDigest.getInstance("SHA-256");
         byte[] key = sha.digest(secretKey.getBytes(StandardCharsets.UTF_8));
         // Use first 16 bytes (128-bit AES)
         this.secretKeySpec = new SecretKeySpec(key, 0, 16, "AES");
      } catch (Exception e) {
         throw new RuntimeException("Error initializing secret key", e);
      }
   }
```
## Encrypt
The encrypt method in the `EncryptUtil` class performs AES encryption on a given string and returns the encrypted result as a Base64-encoded string.
`IV` is the initial Vector that acts a bit like a salt.
```java
   public String encrypt(String data) {
      try {
         Cipher cipher = Cipher.getInstance(ALGORITHM);
         byte[] iv = new byte[IV_LENGTH];
         new SecureRandom().nextBytes(iv); // generate random IV
         IvParameterSpec ivSpec = new IvParameterSpec(iv);

         cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivSpec);
         byte[] encrypted = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));

         // IV + encrypted data -> Base64
         byte[] encryptedWithIv = new byte[IV_LENGTH + encrypted.length];
         System.arraycopy(iv, 0, encryptedWithIv, 0, IV_LENGTH);
         System.arraycopy(encrypted, 0, encryptedWithIv, IV_LENGTH, encrypted.length);

         return Base64.getEncoder().encodeToString(encryptedWithIv);
      } catch (Exception e) {
         throw new RuntimeException("Error while encrypting", e);
      }
   }
```
## Decrypt
The job of this method is to decrypt secrets that got encrypted with the method above.
```java
   public String decrypt(String data) {
      try {
         byte[] encryptedWithIv = Base64.getDecoder().decode(data);
         byte[] iv = new byte[IV_LENGTH];
         byte[] encrypted = new byte[encryptedWithIv.length - IV_LENGTH];

         System.arraycopy(encryptedWithIv, 0, iv, 0, IV_LENGTH);
         System.arraycopy(encryptedWithIv, IV_LENGTH, encrypted, 0, encrypted.length);

         Cipher cipher = Cipher.getInstance(ALGORITHM);
         IvParameterSpec ivSpec = new IvParameterSpec(iv);
         cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec);

         byte[] original = cipher.doFinal(encrypted);
         return new String(original, StandardCharsets.UTF_8);
      } catch (Exception e) {
         throw new RuntimeException("Error while decrypting", e);
      }
   }
```