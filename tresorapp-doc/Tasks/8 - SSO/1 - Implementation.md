I chose Google `oAuth` because it is the most common one and a bit easier than the one from `Apple`, etc. to implement.
# Register App with Google
1. Go to the [Google Cloud Console](https://console.cloud.google.com)
2. `Select a Project` > `New project` > `Create` > `Select`
3. `APIs & Services` > `Credentials` > `Create credentials` > `OAuth client ID` > `Configure Constent Screen` > `Get started`
4. `APIs &  Services`  > `Credentials` > `Create credentials` > `OAuth client ID` > `Web Application`
5. Set `Name` and `Authorized redirect URIs` (where the user gets direct to after auth), I can keep `Authorized JavaScript origins` empty because by backend handles the oAuth flow
6. `Create`
7. Make sure to copy the `Client ID` and `Client secret`
# Configure oAuth in Backend
1. I created the two classes `CustomOAuth2UserServiceImpl` (Defines how a user gets loaded from oAuth) and `CustomOAuth2SuccessHandler` used as beans in the `SecurityConfig`
2.  Extend `SecurityConfig` to accept oAuth2 Login
3. 