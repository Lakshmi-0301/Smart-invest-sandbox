import React, { useState } from 'react';
import { authAPI } from '../services/api';
import '../styles/Login.css'; // Import the enhanced CSS

const Login = ({ onSwitchToRegister, onLogin }) => {
    const [formData, setFormData] = useState({
        username: '',
        password: ''
    });
    const [message, setMessage] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        setMessage('');

        try {
            const result = await authAPI.login(formData.username, formData.password);

            if (result.success) {
                setMessage('✅ Login successful!');
                onLogin(result.data);
            } else {
                setMessage('❌ ' + result.message);
            }
        } catch (error) {
            setMessage('❌ Login failed. Please try again.');
        }
    };

    const handleChange = (e) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
    };

    return (
        <div className="auth-container">
            <div className="auth-card">
                <div className="auth-header">
                    <h1>Welcome Back</h1>
                    <p>Sign in to your account</p>
                </div>

                <form onSubmit={handleSubmit} className="auth-form">
                    <div className="form-group">
                        <label htmlFor="username">Username:</label>
                        <input
                            type="text"
                            id="username"
                            name="username"
                            value={formData.username}
                            onChange={handleChange}
                            placeholder="Enter your username"
                            required
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="password">Password:</label>
                        <input
                            type="password"
                            id="password"
                            name="password"
                            value={formData.password}
                            onChange={handleChange}
                            placeholder="Enter your password"
                            required
                        />
                    </div>

                    <button type="submit" className="btn-primary">
                        Sign In
                    </button>

                    <button type="button" className="btn-secondary" onClick={onSwitchToRegister}>
                        Create Account
                    </button>

                    {message && (
                        <div className={`message ${message.includes('✅') ? 'success' : 'error'}`}>
                            {message}
                        </div>
                    )}
                </form>
            </div>
        </div>
    );
};

export default Login;