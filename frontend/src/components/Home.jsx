import React from 'react';

const Home = ({ user, onLogout }) => {
    return (
        <div className="home-container">
            <div className="home-card">
                <div className="welcome-section">
                    <div className="success-icon">🎉</div>
                    <h1>Dashboard</h1>
                    <h2>Welcome back, {user?.username}!</h2>
                    <div className="user-info">
                        <p><strong>Email:</strong> {user?.email}</p>
                        <p><strong>Balance:</strong> ${user?.balance?.toLocaleString()}</p>
                    </div>
                    <button onClick={onLogout} className="btn-secondary">
                        Logout
                    </button>
                </div>
            </div>
        </div>
    );
};

export default Home;