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
    },
    updateBalance: async (username, balance) => {
        const response = await axios.post(`${API_BASE}/user/balance`, { username, balance });
        return response.data;
    }
};

export const tutorialAPI = {
    // Get all tutorials
    getAllTutorials: async () => {
        try {
            const response = await axios.get(`${API_BASE}/tutorials`);
            return response.data.data;
        } catch (error) {
            console.error('Error fetching tutorials:', error);
            return getLocalTutorials();
        }
    },

    // Get tutorials by level
    getTutorialsByLevel: async (level) => {
        try {
            const response = await axios.get(`${API_BASE}/tutorials/level/${level}`);
            return response.data.data;
        } catch (error) {
            console.error(`Error fetching ${level} tutorials:`, error);
            return [];
        }
    },

    // Get tutorials by category
    getTutorialsByCategory: async (category) => {
        try {
            const response = await axios.get(`${API_BASE}/tutorials/category/${category}`);
            return response.data.data;
        } catch (error) {
            console.error(`Error fetching ${category} tutorials:`, error);
            return [];
        }
    },

    // Search tutorials
    searchTutorials: async (query) => {
        try {
            const response = await axios.get(`${API_BASE}/tutorials/search?q=${encodeURIComponent(query)}`);
            return response.data.data;
        } catch (error) {
            console.error('Error searching tutorials:', error);
            return [];
        }
    },

    // Get specific tutorial
    getTutorial: async (id) => {
        try {
            const response = await axios.get(`${API_BASE}/tutorials/${id}`);
            return response.data.data;
        } catch (error) {
            console.error(`Error fetching tutorial ${id}:`, error);
            const localTutorials = getLocalTutorials();
            return localTutorials.find(tutorial => tutorial.id === id) || localTutorials[0];
        }
    },
/// Submit quiz - FIXED VERSION
 submitQuiz: async (tutorialId, answers, username) => {
     try {
         console.log('🔄 Submitting quiz...');
         console.log('Tutorial ID:', tutorialId);
         console.log('Username:', username);
         console.log('Answers:', answers);

         const requestData = {
             tutorialId: tutorialId,
             username: username,
             answers: JSON.stringify(answers) // Convert object to JSON string
         };

         console.log('Request data:', requestData);

         const response = await axios.post(`${API_BASE}/tutorial/quiz`,
             requestData,
             {
                 headers: {
                     'Content-Type': 'application/json',
                     'Accept': 'application/json'
                 }
             }
         );

         console.log('✅ Quiz submitted successfully:', response.data);
         return response.data.data;
     } catch (error) {
         console.error('❌ Error submitting quiz:', error);
         console.error('Error details:', error.response?.data);
         console.error('Error status:', error.response?.status);
         return {
             success: false,
             error: error.response?.data?.error || 'Failed to submit quiz',
             score: 0,
             passed: false,
             correctAnswers: 0,
             totalQuestions: 0
         };
     }
 },
// Get tutorial quiz - UPDATED to match Main.java routes
getQuiz: async (tutorialId) => {
    try {
        console.log(`🔄 Fetching quiz for tutorial: ${tutorialId}`);
        const response = await axios.get(`${API_BASE}/tutorial/quiz`, {
            params: { tutorialId }
        });
        console.log(`✅ Quiz data received:`, response.data);
        return response.data.data;
    } catch (error) {
        console.error(`❌ Error fetching quiz for ${tutorialId}:`, error);
        console.error('Error details:', error.response?.data);
        return getFallbackQuiz(tutorialId);
    }
},

// Get tutorial exercise - UPDATED to match Main.java routes
getExercise: async (tutorialId) => {
    try {
        const response = await axios.get(`${API_BASE}/tutorial/exercise`, {
            params: { tutorialId }
        });
        return response.data.data;
    } catch (error) {
        console.error(`Error fetching exercise for ${tutorialId}:`, error);
        return null;
    }
},

// Validate exercise - UPDATED to match Main.java routes
validateExercise: async (tutorialId, answer, username) => {
    try {
        const response = await axios.post(`${API_BASE}/tutorial/validate`, {
            tutorialId: tutorialId,
            answer: answer,
            username: username
        });
        return response.data.correct;
    } catch (error) {
        console.error('Error validating exercise:', error);
        return false;
    }
},
    // Get tutorial quiz
//    getQuiz: async (tutorialId) => {
//        try {
//            const response = await axios.get(`${API_BASE}/tutorials/${tutorialId}/quiz`);
//            return response.data.data;
//        } catch (error) {
//            console.error(`Error fetching quiz for ${tutorialId}:`, error);
//            return null;
//        }
//    },

    // Get tutorial exercise
//    getExercise: async (tutorialId) => {
//        try {
//            const response = await axios.get(`${API_BASE}/tutorials/${tutorialId}/exercise`);
//            return response.data.data;
//        } catch (error) {
//            console.error(`Error fetching exercise for ${tutorialId}:`, error);
//            return null;
//        }
//    },
//
//    // Validate exercise
//    validateExercise: async (tutorialId, answer, username) => {
//        try {
//            const response = await axios.post(`${API_BASE}/tutorials/${tutorialId}/validate`, {
//                answer: answer,
//                username: username
//            });
//            return response.data.correct;
//        } catch (error) {
//            console.error('Error validating exercise:', error);
//            return false;
//        }
//    },
//
//    // Submit quiz
//    submitQuiz: async (tutorialId, answers, username) => {
//        try {
//            const response = await axios.post(`${API_BASE}/tutorials/${tutorialId}/quiz`, {
//                answers: answers,
//                username: username
//            });
//            return response.data.data;
//        } catch (error) {
//            console.error('Error submitting quiz:', error);
//            return { success: false, error: 'Failed to submit quiz' };
//        }
//    },

    // Get user progress
    getUserProgress: async (username) => {
        try {
          const response = await axios.get(`${API_BASE}/tutorials/progress`, {
            params: { username }
          });
          return response.data;
        } catch (error) {
            console.error('Error fetching user progress:', error);
            // Return fallback structure that matches what frontend expects
            return {
                data: {},
                statistics: {
                    completedTutorials: 0,
                    currentLevel: 'Beginner',
                    badges: [],
                    certifications: [],
                    totalTimeSpent: 0,
                    averageScore: 0
                }
            };
        }
    },

    // Get tutorial progress
    getTutorialProgress: async (username, tutorialId) => {
        try {
            const response = await axios.get(`${API_BASE}/tutorials/progress/tutorial`, {
            params: { username, tutorialId }
          });
            return response.data.data;
        } catch (error) {
            console.error('Error fetching tutorial progress:', error);
            // Return fallback structure
            return {
                completed: false,
                score: 0,
                quizScore: 0,
                quizPassed: false,
                timeSpent: 0
            };
        }
    },
    markTutorialComplete: async (username, tutorialId) => {
        try {
            console.log('🔍 Marking tutorial complete - Frontend Debug:');
            console.log('Username:', username);
            console.log('Tutorial ID:', tutorialId);
            console.log('API Base URL:', API_BASE);

            const requestBody = {
                username: username,
                tutorialId: tutorialId
            };

            console.log('Request body:', requestBody);

            const response = await axios.post(`${API_BASE}/tutorials/progress/complete`,
                requestBody,
                {
                    headers: {
                        'Content-Type': 'application/json',
                        'Accept': 'application/json'
                    },
                    timeout: 10000
                }
            );

        console.log('✅ Mark complete response:', response.data);
            return response.data;
        } catch (error) {
            console.error('❌ Error marking tutorial complete:', error);
            console.error('Error response:', error.response?.data);
            console.error('Error status:', error.response?.status);
            console.error('Error headers:', error.response?.headers);

            // Return a consistent structure even on error
            return {
                success: false,
                error: error.response?.data?.error || error.message || 'Failed to mark tutorial complete'
            };
        }
    },

    // Update time spent (fallback - endpoint doesn't exist yet)
    updateTimeSpent: async (username, tutorialId, minutes) => {
        try {
            // This endpoint doesn't exist in current backend, so we'll just return success
            console.log(`Time spent updated: User ${username}, Tutorial ${tutorialId}, ${minutes} minutes`);
            return { success: true };
        } catch (error) {
            console.error('Error updating time spent:', error);
            return { success: false };
        }
    },

    // Get recommended tutorials
    getRecommendedTutorials: async (username) => {
        try {
            const response = await axios.get(`${API_BASE}/tutorials/recommended/${username}`);
            return response.data.data;
        } catch (error) {
            console.error('Error fetching recommended tutorials:', error);
            return [];
        }
    },

    // Get user statistics
    getUserStatistics: async (username) => {
        try {
            const response = await axios.get(`${API_BASE}/tutorials/stats/${username}`);
            return response.data.data;
        } catch (error) {
            console.error('Error fetching user statistics:', error);
            return {
                completedTutorials: 0,
                totalTimeSpent: 0,
                averageScore: 0,
                badges: [],
                certifications: [],
                currentLevel: 'Beginner'
            };
        }
    }
};

// Local fallback data
const getLocalTutorials = () => {
    return [
        {
            id: 'stock-fundamentals',
            title: 'Stock Market Fundamentals',
            description: 'Learn the basic concepts of stock market investing and how markets work',
            level: 'BEGINNER',
            category: 'FOUNDATION',
            estimatedMinutes: 45,
            content: '<h3>Welcome to Stock Market Investing</h3><p>Learn how to build wealth through intelligent investing...</p>',
            keyPoints: [
                'Stocks represent ownership in companies',
                'Markets can be bullish (rising) or bearish (falling)',
                'Diversification reduces risk'
            ],
            exercise: {
                question: 'Calculate the market capitalization: A company has 1 million shares outstanding trading at $50 per share.',
                answer: '50000000',
                hint: 'Market Cap = Shares Outstanding × Price Per Share',
                type: 'CALCULATION'
            },
            hasVideo: true,
            hasQuiz: true,
            completionRate: 85.5
        },
        {
            id: 'chart-reading',
            title: 'Reading Stock Charts & Technical Analysis',
            description: 'Learn how to interpret stock charts and identify trading patterns',
            level: 'BEGINNER',
            category: 'TECHNICAL_ANALYSIS',
            estimatedMinutes: 60,
            hasVideo: true,
            hasQuiz: true
        }
    ];
};