1. I added the `SecurityConfig.java` file to implement basic auth
	1. Firstly, I implemented the basic auth
	2. I realized that basic auth isn't practical for the app and I would need to change a lot of things after implementing it so i directly implemented JWT
2. I created the `JwtUtil.java` class
3. I Updated the `SecurityConfig.java` and created the `UserDetailsServiceImpl.java` so that the actual Users can get used instead of the in memory Users
4. I tested if the Security Config is working in the frontend
5. Afterwards i updated the login endpoint to check if i can generate a JWT Token successfully
6. Then I added the token to the `localStore` so that I can use it when performing requests