import React, { useState } from 'react';
import { authAPI } from '../services/api';

const Register = ({ onSwitchToLogin, onRegister }) => {
    const [formData, setFormData] = useState({
        username: '',
        email: '',
        password: ''
    });
    const [message, setMessage] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        setMessage('');

        // Email validation
        const emailRegex = /^[A-Za-z0-9+_.-]+@(.+)$/;
        if (!emailRegex.test(formData.email)) {
            setMessage('⚠️ Invalid email format');
            return;
        }

        try {
            const result = await authAPI.register(formData.username, formData.password, formData.email);

            if (result.success) {
                setMessage('✅ Registered successfully!');
                setTimeout(() => {
                    onRegister(result.data);
                }, 1000);
            } else {
                setMessage('⚠️ ' + result.message);
            }
        } catch (error) {
            setMessage('⚠️ Registration failed. Please try again.');
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
                    <h1>Create Account</h1>
                    <p>Join us today</p>
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
                            placeholder="Choose a username"
                            required
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="email">Email:</label>
                        <input
                            type="email"
                            id="email"
                            name="email"
                            value={formData.email}
                            onChange={handleChange}
                            placeholder="Enter your email"
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
                            placeholder="Create a password"
                            required
                        />
                    </div>

                    <button type="submit" className="btn-primary">
                        Create Account
                    </button>

                    <button type="button" className="btn-secondary" onClick={onSwitchToLogin}>
                        Back to Login
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

export default Register;