// import React, { useState } from 'react';
// import '../styles/TutorialPage.css';
// const TutorialPage = () => {
//   const [activeSection, setActiveSection] = useState('basics');
//   const [completedSections, setCompletedSections] = useState(new Set());
//   const [showHint, setShowHint] = useState({});
//
//   const tutorials = {
//     basics: {
//       title: "Stock Market Basics",
//       content: `
//         <h3>What are Stocks?</h3>
//         <p>Stocks represent ownership in a company. When you buy a stock, you become a shareholder.</p>
//
//         <h3>Key Concepts:</h3>
//         <ul>
//           <li><strong>Bull Market:</strong> Rising prices</li>
//           <li><strong>Bear Market:</strong> Falling prices</li>
//           <li><strong>Portfolio:</strong> Your collection of investments</li>
//           <li><strong>Diversification:</strong> Spreading investments to reduce risk</li>
//         </ul>
//       `,
//       exercise: {
//         question: "If a company has 1 million shares and you own 10,000, what percentage do you own?",
//         answer: "1%",
//         hint: "Calculate: (Your shares / Total shares) × 100"
//       }
//     },
//     buying: {
//       title: "How to Buy Stocks",
//       content: `
//         <h3>Steps to Buy Stocks:</h3>
//         <ol>
//           <li>Research the company</li>
//           <li>Check financial metrics (P/E ratio, earnings, debt)</li>
//           <li>Decide on order type (Market vs Limit)</li>
//           <li>Place your order</li>
//           <li>Monitor your investment</li>
//         </ol>
//
//         <h3>Order Types:</h3>
//         <ul>
//           <li><strong>Market Order:</strong> Buy at current market price</li>
//           <li><strong>Limit Order:</strong> Buy only at specified price or better</li>
//           <li><strong>Stop Order:</strong> Becomes market order when price hits certain level</li>
//         </ul>
//       `,
//       exercise: {
//         question: "You want to buy ABC stock, but only if it drops to $50. What order type should you use?",
//         answer: "Limit Order",
//         hint: "This order type lets you set a maximum purchase price"
//       }
//     },
//     selling: {
//       title: "When to Sell Stocks",
//       content: `
//         <h3>Selling Strategies:</h3>
//         <ul>
//           <li><strong>Profit Taking:</strong> Sell when you've reached your target gain</li>
//           <li><strong>Stop Loss:</strong> Sell automatically if price drops too much</li>
//           <li><strong>Rebalancing:</strong> Sell to maintain your target asset allocation</li>
//           <li><strong>Fundamental Change:</strong> Sell if company fundamentals deteriorate</li>
//         </ul>
//
//         <h3>Tax Considerations:</h3>
//         <p>Hold investments for over a year for favorable long-term capital gains tax rates.</p>
//       `,
//       exercise: {
//         question: "You bought XYZ stock at $100 and it's now $150. You want to protect your profits if it drops to $140. What order type should you use?",
//         answer: "Stop Loss Order",
//         hint: "This order triggers a sale when the price falls to a specified level"
//       }
//     },
//     analysis: {
//       title: "Stock Analysis Methods",
//       content: `
//         <h3>Fundamental Analysis:</h3>
//         <ul>
//           <li>Examine financial statements</li>
//           <li>Analyze P/E ratio, EPS, revenue growth</li>
//           <li>Evaluate management team</li>
//           <li>Study industry trends</li>
//         </ul>
//
//         <h3>Technical Analysis:</h3>
//         <ul>
//           <li>Study price charts and patterns</li>
//           <li>Use indicators like Moving Averages, RSI, MACD</li>
//           <li>Identify support and resistance levels</li>
//           <li>Analyze trading volume</li>
//         </ul>
//       `,
//       exercise: {
//         question: "A company has earnings of $5 per share and its stock price is $100. What is its P/E ratio?",
//         answer: "20",
//         hint: "P/E ratio = Price per share / Earnings per share"
//       }
//     }
//   };
//
//   const handleExerciseSubmit = (section, userAnswer) => {
//     const correctAnswer = tutorials[section].exercise.answer.toLowerCase();
//     if (userAnswer.toLowerCase() === correctAnswer) {
//       setCompletedSections(prev => new Set([...prev, section]));
//       alert('Correct! Well done!');
//     } else {
//       alert('Not quite right. Try again or use the hint!');
//     }
//   };
//
//   const toggleHint = (section) => {
//     setShowHint(prev => ({
//       ...prev,
//       [section]: !prev[section]
//     }));
//   };
//
//   return (
//     <div className="tutorial-page">
//       <header className="tutorial-header">
//         <h1>Stock Trading Tutorials</h1>
//         <p>Learn how to trade stocks like a pro with our interactive lessons</p>
//       </header>
//
//       <div className="tutorial-container">
//         <nav className="tutorial-nav">
//           {Object.entries(tutorials).map(([key, tutorial]) => (
//             <button
//               key={key}
//               className={`nav-item ${activeSection === key ? 'active' : ''} ${
//                 completedSections.has(key) ? 'completed' : ''
//               }`}
//               onClick={() => setActiveSection(key)}
//             >
//               {tutorial.title}
//               {completedSections.has(key) && <span className="checkmark">✓</span>}
//             </button>
//           ))}
//         </nav>
//
//         <div className="tutorial-content">
//           <div className="content-section">
//             <h2>{tutorials[activeSection].title}</h2>
//             <div
//               className="lesson-content"
//               dangerouslySetInnerHTML={{ __html: tutorials[activeSection].content }}
//             />
//           </div>
//
//           <div className="exercise-section">
//             <h3>Try It Yourself</h3>
//             <div className="exercise-card">
//               <p>{tutorials[activeSection].exercise.question}</p>
//
//               <div className="exercise-controls">
//                 <input
//                   type="text"
//                   id={`exercise-${activeSection}`}
//                   placeholder="Your answer..."
//                   className="answer-input"
//                 />
//                 <button
//                   onClick={() => {
//                     const input = document.getElementById(`exercise-${activeSection}`);
//                     handleExerciseSubmit(activeSection, input.value);
//                     input.value = '';
//                   }}
//                   className="submit-btn"
//                 >
//                   Check Answer
//                 </button>
//               </div>
//
//               <div className="hint-section">
//                 <button
//                   onClick={() => toggleHint(activeSection)}
//                   className="hint-btn"
//                 >
//                   {showHint[activeSection] ? 'Hide Hint' : 'Show Hint'}
//                 </button>
//                 {showHint[activeSection] && (
//                   <div className="hint-content">
//                     {tutorials[activeSection].exercise.hint}
//                   </div>
//                 )}
//               </div>
//             </div>
//           </div>
//
//           <div className="progress-section">
//             <h4>Your Progress</h4>
//             <div className="progress-bar">
//               <div
//                 className="progress-fill"
//                 style={{
//                   width: `${(completedSections.size / Object.keys(tutorials).length) * 100}%`
//                 }}
//               ></div>
//             </div>
//             <p>{completedSections.size} of {Object.keys(tutorials).length} sections completed</p>
//           </div>
//         </div>
//       </div>
//     </div>
//   );
// };
//
// export default TutorialPage;

import React, { useState, useEffect } from 'react';
import { tutorialAPI } from '../services/api'; // CHANGED: Use centralized API
import '../styles/TutorialPage.css';

const TutorialPage = ({ onBackToHome, user }) => { // CHANGED: Accept navigation props
    const [activeSection, setActiveSection] = useState('basics');
    const [completedSections, setCompletedSections] = useState(new Set());
    const [showHint, setShowHint] = useState({});
    const [tutorials, setTutorials] = useState({});
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        loadTutorials();
    }, []);

    const loadTutorials = async () => {
        try {
            const tutorialList = await tutorialAPI.getAllTutorials();
            const tutorialMap = {};
            tutorialList.forEach(tutorial => {
                tutorialMap[tutorial.id] = tutorial;
            });
            setTutorials(tutorialMap);
        } catch (error) {
            console.error('Failed to load tutorials:', error);
        } finally {
            setLoading(false);
        }
    };

    const handleExerciseSubmit = async (section, userAnswer) => {
        if (!userAnswer.trim()) {
            alert('Please enter an answer!');
            return;
        }

        try {
            const isCorrect = await tutorialAPI.validateExercise(section, userAnswer);
            if (isCorrect) {
                setCompletedSections(prev => new Set([...prev, section]));
                alert('Correct! Well done!');
            } else {
                alert('Not quite right. Try again or use the hint!');
            }
        } catch (error) {
            console.error('Error validating answer:', error);
            alert('Error validating answer. Please try again.');
        }
    };

    const toggleHint = (section) => {
        setShowHint(prev => ({
            ...prev,
            [section]: !prev[section]
        }));
    };

    if (loading) {
        return <div className="tutorial-page loading">Loading tutorials...</div>;
    }

    return (
        <div className="tutorial-page">
            {/* NEW: Header with back button and user info */}
            <header className="tutorial-header">
                <div className="header-content">
                    <button onClick={onBackToHome} className="back-btn">
                        ← Back to Home
                    </button>
                    <div className="header-text">
                        <h1>Stock Trading Tutorials</h1>
                        <p>Learn how to trade stocks like a pro with our interactive lessons</p>
                        {user && <span className="user-welcome">Welcome, {user.username}!</span>}
                    </div>
                </div>
            </header>

            <div className="tutorial-container">
                <nav className="tutorial-nav">
                    {Object.entries(tutorials).map(([key, tutorial]) => (
                        <button
                            key={key}
                            className={`nav-item ${activeSection === key ? 'active' : ''} ${
                                completedSections.has(key) ? 'completed' : ''
                            }`}
                            onClick={() => setActiveSection(key)}
                        >
                            {tutorial.title}
                            {completedSections.has(key) && <span className="checkmark">✓</span>}
                        </button>
                    ))}
                </nav>

                <div className="tutorial-content">
                    {tutorials[activeSection] && (
                        <>
                            <div className="content-section">
                                <h2>{tutorials[activeSection].title}</h2>
                                <div
                                    className="lesson-content"
                                    dangerouslySetInnerHTML={{ __html: tutorials[activeSection].content }}
                                />
                            </div>

                            <div className="exercise-section">
                                <h3>Try It Yourself</h3>
                                <div className="exercise-card">
                                    <p>{tutorials[activeSection].exerciseQuestion}</p>

                                    <div className="exercise-controls">
                                        <input
                                            type="text"
                                            id={`exercise-${activeSection}`}
                                            placeholder="Your answer..."
                                            className="answer-input"
                                        />
                                        <button
                                            onClick={() => {
                                                const input = document.getElementById(`exercise-${activeSection}`);
                                                handleExerciseSubmit(activeSection, input.value);
                                                input.value = '';
                                            }}
                                            className="submit-btn"
                                        >
                                            Check Answer
                                        </button>
                                    </div>

                                    <div className="hint-section">
                                        <button
                                            onClick={() => toggleHint(activeSection)}
                                            className="hint-btn"
                                        >
                                            {showHint[activeSection] ? 'Hide Hint' : 'Show Hint'}
                                        </button>
                                        {showHint[activeSection] && (
                                            <div className="hint-content">
                                                {tutorials[activeSection].hint}
                                            </div>
                                        )}
                                    </div>
                                </div>
                            </div>

                            <div className="progress-section">
                                <h4>Your Progress</h4>
                                <div className="progress-bar">
                                    <div
                                        className="progress-fill"
                                        style={{
                                            width: `${(completedSections.size / Object.keys(tutorials).length) * 100}%`
                                        }}
                                    ></div>
                                </div>
                                <p>{completedSections.size} of {Object.keys(tutorials).length} sections completed</p>
                            </div>
                        </>
                    )}
                </div>
            </div>
        </div>
    );
};

export default TutorialPage;