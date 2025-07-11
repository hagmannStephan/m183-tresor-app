import { useNavigate } from 'react-router-dom';
import { useState } from 'react';

/**
 * LoginUser
 * @author Peter Rutschmann
 */
function LoginUser({ loginValues, setLoginValues }) {
    const navigate = useNavigate();
    const [errorMessage, setErrorMessage] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        console.log('Submitting login data:', loginValues);

        try {
            const response = await fetch('http://localhost:8080/api/users/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    email: loginValues.email,
                    password: loginValues.password,
                    mfaToken: loginValues.mfaToken
                })
            });

            console.log('Response status:', response.status);
            const data = await response.json();

            if (response.ok) {
                console.log('Login successful:', data);

                localStorage.setItem('userId', data.userId);
                localStorage.setItem('userEmail', data.email);
                localStorage.setItem('token', data.token);
                localStorage.setItem('password', loginValues.password); // Note: as per instruction
                localStorage.setItem('isLoggedIn', 'true');
                localStorage.setItem('role', data.role);

                navigate('/');
            } else {
                console.error('Login failed:', data);
                setErrorMessage(data.message || 'Login failed. Please check your credentials and MFA token.');
            }
        } catch (error) {
            console.error('Error during login:', error);
            setErrorMessage('An error occurred during login. Please try again.');
        }
    };

    return (
        <div>
            <h2>Login user</h2>
            {errorMessage && (
                <div className="error-message" style={{ color: 'red', marginBottom: '10px' }}>
                    {errorMessage}
                </div>
            )}

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
                <svg width="18" height="18" viewBox="0 0 24 24" style={{ marginRight: '8px' }}>
                    <path fill="#4285F4" d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z"/>
                    <path fill="#34A853" d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z"/>
                    <path fill="#FBBC05" d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.07H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.93l2.85-2.22.81-.62z"/>
                    <path fill="#EA4335" d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.07l3.66 2.84c.87-2.6 3.3-4.53 6.16-4.53z"/>
                </svg>
                Sign in with Google
            </button>

            <form onSubmit={handleSubmit}>
                <section>
                    <aside>
                        <div>
                            <label>Email:</label>
                            <input
                                type="email"
                                value={loginValues.email}
                                onChange={(e) =>
                                    setLoginValues(prev => ({ ...prev, email: e.target.value }))}
                                required
                                placeholder="Please enter your email *"
                            />
                        </div>
                        <div>
                            <label>Password:</label>
                            <input
                                type="password"
                                value={loginValues.password}
                                onChange={(e) =>
                                    setLoginValues(prev => ({ ...prev, password: e.target.value }))}
                                required
                                placeholder="Please enter your password *"
                            />
                        </div>
                        <div>
                            <label>MFA Token:</label>
                            <input
                                type="text"
                                value={loginValues.mfaToken || ''}
                                onChange={(e) =>
                                    setLoginValues(prev => ({ ...prev, mfaToken: e.target.value }))}
                                required
                                placeholder="Enter 6-digit MFA code *"
                            />
                        </div>
                    </aside>
                </section>

                <button type="submit">Login</button>
                <br />
                <button
                    onClick={() => navigate('/reset-password-request')}
                    style={{
                        background: 'none',
                        border: 'none',
                        color: '#007bff',
                        cursor: 'pointer',
                        textDecoration: 'underline',
                        padding: 0,
                        fontSize: '0.9rem'
                    }}>
                    Forgot Password?
                </button>
            </form>
        </div>
    );
}

export default LoginUser;