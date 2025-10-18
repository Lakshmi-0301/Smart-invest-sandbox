// import React, { useState } from 'react';
// import Login from './components/Login';
// import Register from './components/Register';
// import Home from './components/Home';
// import TutorialPage from './components/TutorialPage'; // NEW: Import TutorialPage
// import './App.css';
//
// function App() {
//     const [currentView, setCurrentView] = useState('login');
//     const [currentUser, setCurrentUser] = useState(null);
//
//     const renderView = () => {
//         switch (currentView) {
//             case 'login':
//                 return <Login
//                     onSwitchToRegister={() => setCurrentView('register')}
//                     onLogin={setCurrentUser}
//                 />;
//             case 'register':
//                 return <Register
//                     onSwitchToLogin={() => setCurrentView('login')}
//                     onRegister={setCurrentUser}
//                 />;
//             case 'home':
//                 return <Home
//                     user={currentUser}
//                     onLogout={() => {
//                         setCurrentUser(null);
//                         setCurrentView('login');
//                     }}
//                     onNavigateToTutorials={() => setCurrentView('tutorials')}
//                 />;
//             case 'tutorials':
//                 return <TutorialPage
//                     onBackToHome={() => setCurrentView('home')}
//                     user={currentUser}
//                 />;
//             default:
//                 return <Login
//                     onSwitchToRegister={() => setCurrentView('register')}
//                     onLogin={setCurrentUser}
//                 />;
//         }
//     };
//
//     React.useEffect(() => {
//         if (currentUser) {
//             setCurrentView('home');
//         }
//     }, [currentUser]);
//
//     return (
//         <div className="app">
//             {renderView()}
//         </div>
//     );
// }
//
// export default App;

import React, { useState } from 'react';
import Login from './components/Login';
import Register from './components/Register';
import Home from './components/Home';
import TutorialPage from './components/TutorialPage';
import './App.css';

function App() {
    const [currentView, setCurrentView] = useState('login');
    const [currentUser, setCurrentUser] = useState(null);

    const renderView = () => {
        switch (currentView) {
            case 'login':
                return <Login onSwitchToRegister={() => setCurrentView('register')} onLogin={setCurrentUser} />;
            case 'register':
                return <Register onSwitchToLogin={() => setCurrentView('login')} onRegister={setCurrentUser} />;
            case 'home':
                return <Home user={currentUser} onLogout={() => { setCurrentUser(null); setCurrentView('login'); }} onNavigateToTutorials={() => setCurrentView('tutorials')} />;
            case 'tutorials':
                return <TutorialPage onBackToHome={() => setCurrentView('home')} user={currentUser} />;
            default:
                return <Login onSwitchToRegister={() => setCurrentView('register')} onLogin={setCurrentUser} />;
        }
    };

    React.useEffect(() => {
        if (currentUser) {
            setCurrentView('home');
        }
    }, [currentUser]);

    return (
        <div className="app">
            {renderView()}
        </div>
    );
}

export default App;