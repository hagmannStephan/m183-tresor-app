# The Bug
1. Start the app
2. Create an Account
3. Log into the App
4. `Secrets` > `New Credential`
5. Input data > `Save secret`
## The Error
### Console Error
```js
Object { email: "alfred@escher.ch", password: "123" }

[NewCredential.js:25](c:/Users/Schule/temp/m183_code/183_12_2_tresorfrontend_rupe-master/src/pages/secret/NewCredential.js "View source in Debugger → c:/Users/Schule/temp/m183_code/183_12_2_tresorfrontend_rupe-master/src/pages/secret/NewCredential.js:25")  

undefined [FetchSecrets.js:14](c:/Users/Schule/temp/m183_code/183_12_2_tresorfrontend_rupe-master/src/comunication/FetchSecrets.js "View source in Debugger → c:/Users/Schule/temp/m183_code/183_12_2_tresorfrontend_rupe-master/src/comunication/FetchSecrets.js:14")  

Error posting secret: loginValues is undefined [FetchSecrets.js:38](c:/Users/Schule/temp/m183_code/183_12_2_tresorfrontend_rupe-master/src/comunication/FetchSecrets.js "View source in Debugger → c:/Users/Schule/temp/m183_code/183_12_2_tresorfrontend_rupe-master/src/comunication/FetchSecrets.js:38")  

Failed to fetch to server: Failed to save secret.
```
## Network Tab
It doesn't seem like anything gets sent.
## Debugging
1. Checkout `FetchSecrets.js`
2. Look where `loginValues` gets used
3. Added debugging in the `postSecret`-Method (`console.log("Login values in post Secret:", loginValues)`)
4. Run again -> Still undefined
5. Checkout `NewCredential.js`
6. `loginValues` gets provided to `NewCredential` as props, so we are going to add debugging there (`console.log("Login values in NewCredential:", loginValues)`)
7. Run again -> The console log in `NewCredential` gets executed twice with this content:
```js
Object { email: "alfred@escher.ch", password: "123" }
```
8. Also add debugging to `newSecret`, the final object (`console.log("FINAL: New secret in NewCredential:", newSecret)`)
```js
FINAL: New secret in NewCredential:

Object { content: {…}, kind: "credential", kindid: 1, title: "123", email: "alfred@escher.ch", encryptPassword: undefined }
```
9. I found out with `ChatGPT` that the object gets destructured but not i the way i expect it. I changed the `postSecret`-Method like this so that it will hopefully work with all secrets:
```js
export const postSecret = async (newSecret) => {
    console.log("New secret in postSecret:", newSecret);
    // Use newSecret.content, newSecret.email, etc.
}
```
10. Now we get another error, that the request didn't work:
```js
...
Initial state in NewCredential:
Object { kindid: 1, kind: "credential", userName: "", password: "", url: "" }

XHRPOST[http://localhost:8080/api/secrets](http://localhost:8080/api/secrets "http://localhost:8080/api/secrets")[HTTP/1.1 400 3ms]  

Error posting secret: encryptPassword: encryption password id is required. [FetchSecrets.js:38]
...
```
It seems like password doesn't get provided correctly. We already see that problem in every console.log before. As a simple debugging method i just set it hardcoded always to the same value `password123!`.
11. Now i get an internal server error, lets checkout the logs of the spring app:
```java
2025-05-11T18:38:55.215+02:00  WARN 27988 --- [tresorbackend] [io-8080-exec-10] o.h.engine.jdbc.spi.SqlExceptionHelper   : SQL Error: 4025, SQLState: 23000
2025-05-11T18:38:55.215+02:00 ERROR 27988 --- [tresorbackend] [io-8080-exec-10] o.h.engine.jdbc.spi.SqlExceptionHelper   : (conn=34) CONSTRAINT `secret.content` failed for `tresordb`.`secret`      
2025-05-11T18:38:55.231+02:00 ERROR 27988 --- [tresorbackend] [io-8080-exec-10] o.a.c.c.C.[.[.[/].[dispatcherServlet]    : Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception [Request processing failed: org.springframework.dao.DataIntegrityViolationException: could not execute statement [(conn=34) CONSTRAINT `secret.content` failed for `tresordb`.`secret`] [insert into secret (content,user_id) values (?,?)]; SQL [insert into secret (content,user_id) values (?,?)]; constraint [null]] with root cause

java.sql.SQLIntegrityConstraintViolationException: (conn=34) CONSTRAINT `secret.content` failed for `tresordb`.`secret`
```
I asked ChatGPT what this means: I told me that the contend doesn't seem to be what the DB is expecting. Maybe it is either null or it could be that the datatype doesn't fit.

I changed the datatype of the content from `json` to `varchar(255)` in the hopes, that it will clear things up. I got a hint from my teacher that this could be the issue.
12. Still get a server error but now another one:
```java
2025-05-11T18:44:39.745+02:00 ERROR 26904 --- [tresorbackend] [nio-8080-exec-3] o.a.c.c.C.[.[.[/].[dispatcherServlet]    : Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception [Request processing failed: org.springframework.dao.InvalidDataAccessResourceUsageException: could not execute statement [(conn=47) Data too long for column 'content' at row 1] [insert into secret (content,user_id) values (?,?)]; SQL [insert into secret (content,user_id) values (?,?)]] with root cause

java.sql.SQLSyntaxErrorException: (conn=47) Data too long for column 'content' at row 1
```
I changed the data type of content again, to `LONGTEXT` so that it doesn't have any length restrictions.
13. Yay now the secret saved, however it cant get any secrets. AAAAAAAAA! This is a problem for another day!
```js
Secret successfully posted:

Object { answer: "Secret saved" }

[FetchSecrets.js:35](c:/Users/Schule/temp/m183_code/183_12_2_tresorfrontend_rupe-master/src/comunication/FetchSecrets.js "View source in Debugger → c:/Users/Schule/temp/m183_code/183_12_2_tresorfrontend_rupe-master/src/comunication/FetchSecrets.js:35")  

XHRPOST[http://localhost:8080/api/secrets/byemail](http://localhost:8080/api/secrets/byemail "http://localhost:8080/api/secrets/byemail")[HTTP/1.1 500 71ms]  

XHRPOST[http://localhost:8080/api/secrets/byemail](http://localhost:8080/api/secrets/byemail "http://localhost:8080/api/secrets/byemail")[HTTP/1.1 500 68ms]  

Failed to get secrets: Server response failed. [FetchSecrets.js:72](c:/Users/Schule/temp/m183_code/183_12_2_tresorfrontend_rupe-master/src/comunication/FetchSecrets.js "View source in Debugger → c:/Users/Schule/temp/m183_code/183_12_2_tresorfrontend_rupe-master/src/comunication/FetchSecrets.js:72")  

Failed to fetch to server: Failed to get secrets. [Secrets.js:25](c:/Users/Schule/temp/m183_code/183_12_2_tresorfrontend_rupe-master/src/pages/secret/Secrets.js "View source in Debugger → c:/Users/Schule/temp/m183_code/183_12_2_tresorfrontend_rupe-master/src/pages/secret/Secrets.js:25")  

Failed to get secrets: Server response failed. [FetchSecrets.js:72](c:/Users/Schule/temp/m183_code/183_12_2_tresorfrontend_rupe-master/src/comunication/FetchSecrets.js "View source in Debugger → c:/Users/Schule/temp/m183_code/183_12_2_tresorfrontend_rupe-master/src/comunication/FetchSecrets.js:72")  

Failed to fetch to server: Failed to get secrets.
```
