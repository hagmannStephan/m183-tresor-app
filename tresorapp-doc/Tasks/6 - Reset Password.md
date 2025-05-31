Checkout the implementation in [this Repo](https://github.com/hagmannStephan/m183-tresor-app/tree/main).
# Prerequisites
## Figure out how to Connect via SMTP to Mail
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
    id INT NOT NULL AUTO_INCREMENT,
    user_id INT NOT NULL,
    token VARCHAR(255) NOT NULL UNIQUE,
    expires_at TIMESTAMP NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
) ENGINE=InnoDB;

```
2. Create the `PasswordResetTokenRepository`
	1. Create the `PasswordResetToken.java`-Class . This defines how the token should look like.
	2. Create the `PasswordResetTokenRepository.java`-JPA-Interface. It tells Spring how to access the database table. The interface lets you find, update and delete tokens without writing SQL-queries.
3. Add the `EmailService.java` and the `EmailServiceImpl.java` that lets you send a email with the body as a param.
4. Add the `PasswordResetService.java` and the `PasswordResetServiceImpl.java` that lets you create a new token
5. Add the endpoint to the `UserController` that calls the `PasswordResetService`. 
   Make sure that also if the User doesn't get found also a **200 response gets send, so that hackers don't get a hint** (did not do it in this project for debugging).
### 1.2.2 `POST: /reset-password`
1. Create a DTO (Data Transfer Object -> an object just with getters/setters but without business logic, used to carry request or response data) for the `ResetPasswordRequest`
2. Modify `PasswordResetService` and `PasswordResetServiceImpl` so that you can also reset the password
3. In the `UserController` add the `/reset-password` endpoint with the correct calls to the service method.
# 2. Frontend Modifications
1. Add `ResetPasswordRequest.js` in the Frontend, where the user can enter his email if the user forgot the password. Also add a link to login that redirects to this page:
	- Form with email input.
    - Sends POST /password-reset-request.
2. Add a `ResetPassword.js` in the Frontend that lets you reset the password. Does the following:
	- Accessed via link from email.
	- Reads token from ?token=... in the URL.
	- Shows new password form.
	- Sends POST /reset-password with token + password.
# Sequence Diagram
## Request Reset Password
![[Pasted image 20250531215213.png]]
## Reset Password
![[Pasted image 20250531221424.png]]