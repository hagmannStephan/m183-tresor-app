1. I added the `SecurityConfig` and checked if the pages still work alright. I already liked it to the DB because it made it easier for me
2. I added a JWT-Mechanism that certain routes just can get accessed with a JWT-Token that has the correct rights
3. I stored the JWT Token in local store and used it like this as a request header: 
```js
'Authorization': `Bearer ${localStorage.getItem('token')}`
```
