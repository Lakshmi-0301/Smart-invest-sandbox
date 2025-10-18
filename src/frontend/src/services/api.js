//import axios from 'axios';
//
//const API_BASE = 'http://localhost:8080/api';
//
//export const authAPI = {
//    register: async (username, password, email) => {
//        const response = await axios.post(`${API_BASE}/register`, {
//            username,
//            password,
//            email
//        });
//        return response.data;
//    },
//
//    login: async (username, password) => {
//        const response = await axios.post(`${API_BASE}/login`, {
//            username,
//            password
//        });
//        return response.data;
//    },
//
//    getUser: async (username) => {
//        const response = await axios.get(`${API_BASE}/user?username=${username}`);
//        return response.data;
//    }
//};
//
//// NEW: Tutorial API functions
//export const tutorialAPI = {
//    // Get all tutorials
//    getAllTutorials: async () => {
//        try {
//            const response = await axios.get(`${API_BASE}/tutorials`);
//            return response.data;
//        } catch (error) {
//            console.error('Error fetching tutorials:', error);
//            // Fallback to local data if API fails
//            return getLocalTutorials();
//        }
//    },
//
//    // Get specific tutorial
//    getTutorial: async (id) => {
//        try {
//            const response = await axios.get(`${API_BASE}/tutorials/${id}`);
//            return response.data;
//        } catch (error) {
//            console.error(`Error fetching tutorial ${id}:`, error);
//            const localTutorials = getLocalTutorials();
//            return localTutorials.find(tutorial => tutorial.id === id) || localTutorials[0];
//        }
//    },
//
//    // Validate exercise answer
//    validateExercise: async (tutorialId, answer) => {
//        try {
//            const response = await axios.post(`${API_BASE}/tutorials/${tutorialId}/validate`, {
//                answer: answer
//            });
//            return response.data.correct;
//        } catch (error) {
//            console.error('Error validating exercise:', error);
//            return validateLocalExercise(tutorialId, answer);
//        }
//    },
//
//    // Health check
//    checkHealth: async () => {
//        try {
//            const response = await axios.get(`${API_BASE}/health`);
//            return response.data;
//        } catch (error) {
//            console.error('Health check failed:', error);
//            return { status: 'ERROR', service: 'Tutorial API' };
//        }
//    }
//};
//
//// Local fallback data
//const getLocalTutorials = () => {
//    return [
//        {
//            id: 'basics',
//            title: 'Stock Market Basics',
//            description: 'Learn the fundamental concepts of stock market trading',
//            content: `<h3>What are Stocks?</h3>
//                <p>Stocks represent ownership in a company. When you buy a stock, you become a shareholder.</p>
//
//                <h3>Key Concepts:</h3>
//                <ul>
//                <li><strong>Bull Market:</strong> Rising prices</li>
//                <li><strong>Bear Market:</strong> Falling prices</li>
//                <li><strong>Portfolio:</strong> Your collection of investments</li>
//                <li><strong>Diversification:</strong> Spreading investments to reduce risk</li>
//                </ul>`,
//            exerciseQuestion: 'If a company has 1 million shares and you own 10,000, what percentage do you own?',
//            exerciseAnswer: '1%',
//            hint: 'Calculate: (Your shares / Total shares) × 100'
//        },
//        {
//            id: 'buying',
//            title: 'How to Buy Stocks',
//            description: 'Step-by-step guide to purchasing stocks',
//            content: `<h3>Steps to Buy Stocks:</h3>
//                <ol>
//                <li>Research the company</li>
//                <li>Check financial metrics (P/E ratio, earnings, debt)</li>
//                <li>Decide on order type (Market vs Limit)</li>
//                <li>Place your order</li>
//                <li>Monitor your investment</li>
//                </ol>
//
//                <h3>Order Types:</h3>
//                <ul>
//                <li><strong>Market Order:</strong> Buy at current market price</li>
//                <li><strong>Limit Order:</strong> Buy only at specified price or better</li>
//                <li><strong>Stop Order:</strong> Becomes market order when price hits certain level</li>
//                </ul>`,
//            exerciseQuestion: 'You want to buy ABC stock, but only if it drops to $50. What order type should you use?',
//            exerciseAnswer: 'Limit Order',
//            hint: 'This order type lets you set a maximum purchase price'
//        },
//        {
//            id: 'selling',
//            title: 'When to Sell Stocks',
//            description: 'Learn strategic approaches to selling stocks',
//            content: `<h3>Selling Strategies:</h3>
//                <ul>
//                <li><strong>Profit Taking:</strong> Sell when you've reached your target gain</li>
//                <li><strong>Stop Loss:</strong> Sell automatically if price drops too much</li>
//                <li><strong>Rebalancing:</strong> Sell to maintain your target asset allocation</li>
//                <li><strong>Fundamental Change:</strong> Sell if company fundamentals deteriorate</li>
//                </ul>
//
//                <h3>Tax Considerations:</h3>
//                <p>Hold investments for over a year for favorable long-term capital gains tax rates.</p>`,
//            exerciseQuestion: 'You bought XYZ stock at $100 and it\'s now $150. You want to protect your profits if it drops to $140. What order type should you use?',
//            exerciseAnswer: 'Stop Loss Order',
//            hint: 'This order triggers a sale when the price falls to a specified level'
//        },
//        {
//            id: 'analysis',
//            title: 'Stock Analysis Methods',
//            description: 'Learn fundamental and technical analysis techniques',
//            content: `<h3>Fundamental Analysis:</h3>
//                <ul>
//                <li>Examine financial statements</li>
//                <li>Analyze P/E ratio, EPS, revenue growth</li>
//                <li>Evaluate management team</li>
//                <li>Study industry trends</li>
//                </ul>
//
//                <h3>Technical Analysis:</h3>
//                <ul>
//                <li>Study price charts and patterns</li>
//                <li>Use indicators like Moving Averages, RSI, MACD</li>
//                <li>Identify support and resistance levels</li>
//                <li>Analyze trading volume</li>
//                </ul>`,
//            exerciseQuestion: 'A company has earnings of $5 per share and its stock price is $100. What is its P/E ratio?',
//            exerciseAnswer: '20',
//            hint: 'P/E ratio = Price per share / Earnings per share'
//        }
//    ];
//};
//
//// Local validation fallback
//const validateLocalExercise = (tutorialId, userAnswer) => {
//    const tutorials = {
//        basics: '1%',
//        buying: 'Limit Order',
//        selling: 'Stop Loss Order',
//        analysis: '20'
//    };
//    return tutorials[tutorialId]?.toLowerCase() === userAnswer.toLowerCase().trim();
//};

import axios from 'axios';

const API_BASE = 'http://localhost:8080/api';

export const authAPI = {
    register: async (username, password, email) => {
        const response = await axios.post(`${API_BASE}/register`, { username, password, email });
        return response.data;
    },
    login: async (username, password) => {
        const response = await axios.post(`${API_BASE}/login`, { username, password });
        return response.data;
    },
    getUser: async (username) => {
        const response = await axios.get(`${API_BASE}/user?username=${username}`);
        return response.data;
    }
};

export const tutorialAPI = {
    getAllTutorials: async () => {
        try {
            const response = await axios.get(`${API_BASE}/tutorials`);
            return response.data;
        } catch (error) {
            console.error('Error fetching tutorials:', error);
            return getLocalTutorials();
        }
    },
    getTutorial: async (id) => {
        try {
            const response = await axios.get(`${API_BASE}/tutorial/${id}`);
            return response.data;
        } catch (error) {
            console.error(`Error fetching tutorial ${id}:`, error);
            const localTutorials = getLocalTutorials();
            return localTutorials.find(tutorial => tutorial.id === id) || localTutorials[0];
        }
    },
    validateExercise: async (tutorialId, answer) => {
        try {
            const response = await axios.post(`${API_BASE}/tutorial/${tutorialId}/validate`, { answer });
            return response.data.correct;
        } catch (error) {
            console.error('Error validating exercise:', error);
            return validateLocalExercise(tutorialId, answer);
        }
    },
    checkHealth: async () => {
        try {
            const response = await axios.get(`${API_BASE}/health`);
            return response.data;
        } catch (error) {
            console.error('Health check failed:', error);
            return { status: 'ERROR' };
        }
    }
};

const getLocalTutorials = () => {
    return [
        {
            id: 'basics',
            title: 'Stock Market Basics',
            description: 'Learn the fundamental concepts of stock market trading',
            content: `<h3>What are Stocks?</h3><p>Stocks represent ownership in a company. When you buy a stock, you become a shareholder.</p><h3>Key Concepts:</h3><ul><li><strong>Bull Market:</strong> Rising prices</li><li><strong>Bear Market:</strong> Falling prices</li><li><strong>Portfolio:</strong> Your collection of investments</li><li><strong>Diversification:</strong> Spreading investments to reduce risk</li></ul>`,
            exerciseQuestion: 'If a company has 1 million shares and you own 10,000, what percentage do you own?',
            exerciseAnswer: '1%',
            hint: 'Calculate: (Your shares / Total shares) × 100'
        },
        {
            id: 'buying',
            title: 'How to Buy Stocks',
            description: 'Step-by-step guide to purchasing stocks',
            content: `<h3>Steps to Buy Stocks:</h3><ol><li>Research the company</li><li>Check financial metrics (P/E ratio, earnings, debt)</li><li>Decide on order type (Market vs Limit)</li><li>Place your order</li><li>Monitor your investment</li></ol><h3>Order Types:</h3><ul><li><strong>Market Order:</strong> Buy at current market price</li><li><strong>Limit Order:</strong> Buy only at specified price or better</li><li><strong>Stop Order:</strong> Becomes market order when price hits certain level</li></ul>`,
            exerciseQuestion: 'You want to buy ABC stock, but only if it drops to $50. What order type should you use?',
            exerciseAnswer: 'Limit Order',
            hint: 'This order type lets you set a maximum purchase price'
        },
        {
            id: 'selling',
            title: 'When to Sell Stocks',
            description: 'Learn strategic approaches to selling stocks',
            content: `<h3>Selling Strategies:</h3><ul><li><strong>Profit Taking:</strong> Sell when you've reached your target gain</li><li><strong>Stop Loss:</strong> Sell automatically if price drops too much</li><li><strong>Rebalancing:</strong> Sell to maintain your target asset allocation</li><li><strong>Fundamental Change:</strong> Sell if company fundamentals deteriorate</li></ul><h3>Tax Considerations:</h3><p>Hold investments for over a year for favorable long-term capital gains tax rates.</p>`,
            exerciseQuestion: 'You bought XYZ stock at $100 and it\'s now $150. You want to protect your profits if it drops to $140. What order type should you use?',
            exerciseAnswer: 'Stop Loss Order',
            hint: 'This order triggers a sale when the price falls to a specified level'
        },
        {
            id: 'analysis',
            title: 'Stock Analysis Methods',
            description: 'Learn fundamental and technical analysis techniques',
            content: `<h3>Fundamental Analysis:</h3><ul><li>Examine financial statements</li><li>Analyze P/E ratio, EPS, revenue growth</li><li>Evaluate management team</li><li>Study industry trends</li></ul><h3>Technical Analysis:</h3><ul><li>Study price charts and patterns</li><li>Use indicators like Moving Averages, RSI, MACD</li><li>Identify support and resistance levels</li><li>Analyze trading volume</li></ul>`,
            exerciseQuestion: 'A company has earnings of $5 per share and its stock price is $100. What is its P/E ratio?',
            exerciseAnswer: '20',
            hint: 'P/E ratio = Price per share / Earnings per share'
        }
    ];
};

const validateLocalExercise = (tutorialId, userAnswer) => {
    const tutorials = {
        basics: '1%',
        buying: 'Limit Order',
        selling: 'Stop Loss Order',
        analysis: '20'
    };
    return tutorials[tutorialId]?.toLowerCase() === userAnswer.toLowerCase().trim();
};