I chose Google `oAuth` because it is the most common one and a bit easier than the one from `Apple`, etc. to implement.
# Register App with Google
1. Go to the [Google Cloud Console](https://console.cloud.google.com)
2. `Select a Project` > `New project` > `Create` > `Select`
3. `APIs & Services` > `Credentials` > `Create credentials` > `OAuth client ID` > `Configure Constent Screen` > `Get started`
4. `APIs &  Services`  > `Credentials` > `Create credentials` > `OAuth client ID` > `Web Application`
5. Set `Name` and `Authorized redirect URIs` (where the user gets direct to after auth, somethin like `https://your-backend.com/oauth2/authorization/google` (Auto-Registered by Spring)), I can keep `Authorized JavaScript origins` empty because by backend handles the oAuth flow
6. `Create`
7. Make sure to copy the `Client ID` and `Client secret`
# Configure oAuth in Backend
1. I created the two classes `CustomOAuth2UserServiceImpl` (Defines how a user gets loaded from oAuth) and `CustomOAuth2SuccessHandler` used as beans in the `SecurityConfig`
2.  Extend `SecurityConfig` to accept oAuth2 Login
3. Add config to `application.properties`
4. It is advised to change the DB Model to store the unique google id called `sub`, also remove database constraints for password required (however make sure, that in email login, users with no password in DB get rejected)
# Modify Frontend
1. Added new Page `OAuth2RedirectHandler.js`, that gets called while user logs in with google account, to make sure that user gets redirected to here after finishing the verification with the correct params from the backend
2. Add a button to the login and register page. To make it look good you can style it like this:
```jsx
{/* Google Register Button */}
<button
	onClick={() => window.location.href = 'http://localhost:8080/oauth2/authorization/google'}
	style={{
		display: 'flex',
		alignItems: 'center',
		justifyContent: 'center',
		width: '200px',
		padding: '12px 20px',
		backgroundColor: '#ffffff',
		border: '1px solid #dadce0',
		borderRadius: '8px',
		fontSize: '14px',
		fontWeight: '500',
		color: '#3c4043',
		cursor: 'pointer',
		boxShadow: '0 1px 2px rgba(60, 64, 67, 0.3), 0 1px 3px rgba(60, 64, 67, 0.15)',
		transition: 'all 0.15s ease',
		fontFamily: 'arial, sans-serif',
		outline: 'none'
	}}
	onMouseEnter={(e) => {
		e.target.style.boxShadow = '0 1px 3px rgba(60, 64, 67, 0.3), 0 4px 8px rgba(60, 64, 67, 0.15)';
		e.target.style.backgroundColor = '#f8f9fa';
	}}
	onMouseLeave={(e) => {
		e.target.style.boxShadow = '0 1px 2px rgba(60, 64, 67, 0.3), 0 1px 3px rgba(60, 64, 67, 0.15)';
		e.target.style.backgroundColor = '#ffffff';
	}}
>
	{/* Google Logo SVG */}
	<svg
		width="18"
		height="18"
		viewBox="0 0 24 24"
		style={{ marginRight: '8px' }}
>
		<path fill="#4285F4" d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z"/>
		<path fill="#34A853" d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z"/>
		<path fill="#FBBC05" d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.07H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.93l2.85-2.22.81-.62z"/>
		<path fill="#EA4335" d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.07l3.66 2.84c.87-2.6 3.3-4.53 6.16-4.53z"/>
	</svg>
	Sign in with Google
</button>
```
