# 1. Generate `Sitekey`
## Setup a check for a new page
1. Create an account at [hCaptcha](https://www.hcaptcha.com/) and log in
2. Create a new site
	1. Name: `localhost` (if in development)
	2. `Save`
	3. Copy the `Sitekey`
## Create a new Secret to validate `hCaptcha`-Tokens
1. Create an account at [hCaptcha](https://www.hcaptcha.com/) and log in
2. `Settings`
3. `Generate New Secret`
# 2. Frontend Integration 
## Explanation
The `hCaptcha`-Widget gets added to the register page and with the register request to the backend, the `hCaptcha` token gets sent.
## Implementation
Here the frontend integration is done for a `js/react` project
1. Install the `hcpatcha`-library
```sh
npm install @hcaptcha/react-hcaptcha
```
2. Add these dependencies and variables:
```jsx
import HCaptcha from '@hcaptcha/react-hcaptcha';

const [captchaToken, setCaptchaToken] = useState(null);
```
3. Add this widget inside the form above the submit button. **Don't forget to add the `sitekey`**!
```jsx
<HCaptcha
    sitekey="<your-site-key-here>"
    onVerify={setCaptchaToken}
    onExpire={() => setCaptchaToken(null)}
/>
```
4. Extend the `handleSubmit`-function to check for the CAPTCHA
```jsx
if (!captchaToken) {
    setErrorMessages(['Please complete the CAPTCHA']);
    return;
}
```
5. Modify the `postUser` function so that it also sends the CAPTCHA to the backend
# 3. Backend Integration
## Explanation
Add the top of the `POST: user` endpoint check if the captcha is valid with a service from `hCaptcha`.
For that i have to modify the `DTO`, add a new service and update the controller a bit. I also have to add my secret key from `hCaptcha` to `application.properties`.
This catches a request without / with an invalid the token right away and rejects it.
## Implementation
Here the backend integration is done for a `java/spring`-backend.
1. Update the `RegisterUser.java` DTO with a Attribute for the token:
```java
@JsonProperty("captchaToken")
@NotEmpty (message="Captcha token is required.")
private String captchaToken;
```
2. Add a `CaptchaService.java` that looks something like this:
```java
...

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.*;

@Service
public class CaptchaService {

    @Value("${hcaptcha.secret}")
    private String secretKey;

    public boolean verifyToken(String token) {
        if (token == null || token.isEmpty()) return false;

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("secret", secretKey);
        params.add("response", token);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(
                "https://hcaptcha.com/siteverify", request, Map.class);
            Map<String, Object> body = response.getBody();
            return (Boolean) body.get("success");
        } catch (Exception e) {
            System.out.println("Captcha verification failed: " + e.getMessage());
            return false;
        }
    }
}
```
3. Update the controller where the user registers. At the top of the class add this:
```java
@Autowired
private CaptchaService captchaService;
```
4. At the start of the `POST: user` endpoint add this:
```java
if (!captchaService.verifyToken(registerUser.getCaptchaToken())) {
    System.out.println("UserController.createUser: captcha failed");
    JsonObject obj = new JsonObject();
    obj.addProperty("message", "CAPTCHA verification failed");
    String json = new Gson().toJson(obj);
    return ResponseEntity.badRequest().body(json);
}

System.out.println("UserController.createUser: captcha passed.");
```
5. In `application.properties` add this line:
```application.properties
hcaptcha.secret=<you-secret-generated-in-step-1>
```