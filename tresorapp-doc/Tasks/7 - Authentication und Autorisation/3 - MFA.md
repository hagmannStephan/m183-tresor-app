# Backend Modifications
1. I added the `TOTPSecretGenerator` with the method `generateSecret()` and `verifyToken(String secret, int code)`
2. I adjusted the DB and DB-Models so that they can safe a nullable `mfa-token`
3. In the POST `/user` endpoint i return the formatted totp-uri, in POST `/login` I now also require the code of the user to log in
# Frontend Modifications
1. Run `npm install qrcode.react` to install plugin for qr-code generation
2. After registration display the qr-code sent from the backend so that the user can safe it in his auth-app
3. With login ask for mfa code
# Sequence Diagram
As seen in [[2 - Implementation JWT]] (Login Success) Workflow