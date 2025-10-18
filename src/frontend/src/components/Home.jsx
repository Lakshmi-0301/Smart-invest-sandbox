import React from 'react';
import '../styles/Home.css';
const Home = ({ user, onLogout, onNavigateToTutorials }) => {
    return (
        <div className="home-container">
            <div className="blur-background">
                <div className="blur-circle blur-circle-1"></div>
                <div className="blur-circle blur-circle-2"></div>
            </div>

            <div className="content-wrapper">
                <header className="home-header">
                    <div className="header-left">
                        <h1 className="header-title">Smart Invest Sandbox</h1>
                        <p className="header-subtitle">Welcome back, {user?.username}!</p>
                    </div>
                    <button onClick={onLogout} className="logout-btn">
                        <span className="logout-icon">↪</span>
                        Logout
                    </button>
                </header>

                <main className="home-main">
                    <div className="section-intro">
                        <p>Get started with our platform</p>
                    </div>

                    <div className="feature-cards">
                        <div className="feature-card card-blue" onClick={onNavigateToTutorials}>
                            <div className="card-icon-box">📚</div>
                            <h3 className="card-title">Learning Tutorials</h3>
                            <p className="card-description">Master stock trading with our interactive tutorials and exercises</p>
                            <button className="card-btn btn-blue">Start Learning</button>
                        </div>

                        <div className="feature-card card-emerald">
                            <div className="card-icon-box">📈</div>
                            <h3 className="card-title">Virtual Trading</h3>
                            <p className="card-description">Practice trading with virtual money in real-time market conditions</p>
                            <button className="card-btn btn-emerald">Start Trading</button>
                        </div>

                        <div className="feature-card card-purple">
                            <div className="card-icon-box">📊</div>
                            <h3 className="card-title">Market Analysis</h3>
                            <p className="card-description">Analyze real market data and identify key trends in depth</p>
                            <button className="card-btn btn-purple">View Analysis</button>
                        </div>
                    </div>
                </main>
            </div>
        </div>
    );
};

export default Home;