# 1. Figure out how to Connect via SMTP to Mail
I tested the connection with thunderbird and after a bit of tweaking it finally worked with the config like this:
![[Pasted image 20250519220107.png]]
I want to set the password reset emails, with my new email `contact@stephanhagmann.ch`.
# 2. Configure `pom.xml` with SMTP Settings
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
