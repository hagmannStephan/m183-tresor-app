1. I added the `SecurityConfig` and checked if the pages still work alright. I already liked it to the DB because it made it easier for me
2. I wrote the `jwtAuthFilter` that authenticates the user and checks for the role
3. In the `/login` endpoint I made sure that the user receives a JWT token, for that i wrote the `JwtUtil` Class
4. I stored the JWT Token in local store and used it like this as a request header: 
```js
'Authorization': `Bearer ${localStorage.getItem('token')}`
```
