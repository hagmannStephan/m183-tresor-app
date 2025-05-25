# Figure out how to Connect via SMTP to Mail
I tested the connection with thunderbird and after a bit of tweaking it finally worked with the config like this:
![[Pasted image 20250519220107.png]]
I want to set the password reset emails, with my new email `contact@stephanhagmann.ch`.
# 1. Backend Config
## 1.1. Configure `pom.xml` with SMTP Settings
Add this config to your `pom.xml`. Make sure to add it to the `.gitignore`, so that the password doesn't get pushed to the remote repo.
```application.properties
# SMTP server settings
spring.mail.host=mail.infomaniak.com
spring.mail.port=465
spring.mail.username=contact@stephanhagmann.ch
spring.mail.password=<your-email-password>

# Protocol and encoding
spring.mail.protocol=smtps
spring.mail.default-encoding=UTF-8

# Optional but recommended
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.ssl.enable=true
spring.mail.properties.mail.smtp.ssl.trust=mail.infomaniak.com
```
## 1.2. Add API Endpoints
### 1.2.1. `POST: /password-reset-request`
1. I added the table `password_reset_token` to the DB, here the tokens for the password reset get stored, if a user requests a password reset
```sql
-- Table: Password Reset Token
-- Used to store the token for password reset if requested by user
CREATE TABLE password_reset_token (
    token VARCHAR(255) PRIMARY KEY,
    user_id int NOT NULL,
    expiration TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES `user`(id) ON DELETE CASCADE
) ENGINE=InnoDB;
```
2. Create the `PasswordResetTokenRepository`
	1. Create the `PasswordResetToken.java`-Class . This defines how the token should look like.
	2. Create the `PasswordResetTokenRepository.java`-Interface. It tells Spring how to access the database table. The interface lets you find, update and delete tokens without writing SQL-queries.
	3. 