# Anforderungen
- Definieren Sie eine gute Regel, wie das Passwort zusammengesetzt sein soll.
- Das Passwort muss auch im Backend geprüft werden.
- Eine Regular-Expressen kann das Ganze vereinfachen.
# Backend
1. Define the requirements, I chose this:
	- Has to have at least on letter, one number and one special character
	- Has to have at least one uppercase and one lowercase letter
	- Has to be at least 11 characters long
2. Identify the relevant parts in the frontend: `RegisterUser.js`
3. Identify the relevant parts in the backend: The `POST`-Endpoint in `UserController.java`
4. Firstly I added the File `PasswordValidationService.java` that checks if a password meets the requirements
5. Secondly I updated the `UserController.java` to use the new Service.
6. Lastly, i updated the `RegisterUser.js` file to display proper errors, if the password doesn't meet the requirements
# Sequence Diagram
Already modeled in the Sequence Diagram in [[1 - Password Hash]].