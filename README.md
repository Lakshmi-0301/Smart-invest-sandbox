# Smart Invest - Stock Market Learning Platform

A full-stack educational platform for learning stock market fundamentals with real LSTM-based price forecasting.

##  Overview

Smart Invest is a sandbox application that combines educational content, interactive quizzes, and AI-powered stock price forecasting. Users can learn about stock market concepts, test their knowledge, and see real machine learning predictions in action.

**Key Features:**
-  Interactive tutorials on stock market basics
-  LSTM neural network for stock price forecasting
-  Interactive quizzes with scoring
-  User authentication & profile management
-  Real-time stock dashboard
-  Support for 20+ stocks across multiple sectors
-  Market status indicator (IST timezone)

##  Architecture

### Backend (Java)
- **Framework**: Lightweight HTTP Server (`com.sun.net.httpserver`)
- **ML Framework**: DeepLearning4J (DL4J) with ND4J
- **Build Tool**: Maven
- **Port**: 8080

**Key Components:**
- `Main.java` - HTTP server bootstrap & endpoint registration
- `AuthController` - User registration & login
- `TutorialController` - Tutorial content delivery
- `ForecastController` - LSTM forecasting API
- `LSTMForecaster` - Neural network model training & prediction

### Frontend (React)
- **Framework**: React 18+ with Vite
- **Build Tool**: Vite
- **Port**: 5173
- **Styling**: CSS3 (responsive design)

**Key Components:**
- `LoginPage` - User authentication UI
- `TutorialPage` - Educational content display
- `QuizPage` - Interactive assessments
- `Forecast.jsx` - Stock forecasting dashboard
- `Dashboard` - User portfolio overview

##  Quick Start

### Prerequisites
- Java 8+ with Maven
- Node.js 16+ with npm/yarn
- Git

### Backend Setup

1. Clone the repository:
```bash
git clone <repo-url>
cd Smart-invest
```

2. Build with Maven:
```bash
mvn clean install
```

3. Run the server:
```bash
mvn clean compile exec:java -Dexec.mainClass="Main"
# OR run Main.java from your IDE
```

The backend will start on `http://localhost:8080`

**Console output:**
```
✓ Server started on http://localhost:8080
✓ Authentication endpoints: /api/auth/*
✓ Tutorial endpoints: /api/tutorials
✓ Forecast (LSTM) endpoint: /api/forecast
```

### Frontend Setup

1. Navigate to frontend directory:
```bash
cd src/frontend
```

2. Install dependencies:
```bash
npm install
```

3. Start development server:
```bash
npm run dev
```

The frontend will start on `http://localhost:5173`

### Full Stack Running

Open two terminals:

**Terminal 1 (Backend):**
```bash
mvn clean compile exec:java -Dexec.mainClass="Main"
```

**Terminal 2 (Frontend):**
```bash
cd src/frontend
npm run dev
```

Then open `http://localhost:5173` in your browser.

## 📡 API Endpoints

### Authentication
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - User login

### Tutorials
- `GET /api/tutorials` - List all tutorials
- `GET /api/tutorials/{id}` - Get tutorial details
- `POST /api/tutorials/{id}/progress` - Update progress

### Forecasting (LSTM)
- `GET /api/forecast?symbol=AAPL&days=7` - Get stock forecast
  - **Parameters:**
    - `symbol` (required) - Stock ticker (AAPL, MSFT, GOOGL, TSLA, etc.)
    - `days` (optional) - Forecast horizon (7 or 30 days, default: 7)
  - **Response:**
    ```json
    {
      "symbol": "AAPL",
      "historical": [150.2, 151.5, ...],
      "forecast": [152.3, 153.1, ...],
      "confidence": 85.6,
      "direction": "bullish",
      "modelType": "LSTM Neural Network",
      "trainingData": "10K+ historical prices"
    }
    ```

##  LSTM Model Details

### Architecture
- **Input Layer**: 30 days of historical prices
- **LSTM Layer 1**: 64 units with ReLU activation
- **LSTM Layer 2**: 32 units with ReLU activation
- **Output Layer**: 7-day forecast with MSE loss
- **Optimizer**: Adam (learning rate: 0.001)
- **Training Epochs**: 50

### Training Pipeline
1. Collects 100+ days of historical price data
2. Normalizes prices to [0, 1] range
3. Creates sliding windows (30-day input, 7-day output)
4. Trains bidirectional LSTM network
5. Validates on unseen test data
6. Denormalizes predictions to original price scale

### Prediction Process
1. Takes last 30 days of prices
2. Feeds through trained LSTM network
3. Generates 7-day forecast
4. Calculates confidence based on model loss
5. Determines trend direction (bullish/bearish)

## 📁 Project Structure

```
Smart-invest/
├── pom.xml                                  # Maven dependencies
├── README.md                                # This file
├── src/
│   ├── main/java/
│   │   └── Main.java                       # HTTP server bootstrap
│   ├── backend/
│   │   ├── controllers/
│   │   │   ├── AuthController.java         # Auth endpoints
│   │   │   ├── TutorialController.java     # Tutorial endpoints
│   │   │   └── ForecastController.java     # Forecast API
│   │   ├── services/
│   │   │   ├── AuthService.java
│   │   │   ├── TutorialService.java
│   │   │   └── (ForecastService.java)
│   │   ├── models/
│   │   │   ├── User.java
│   │   │   ├── TutorialSection.java
│   │   │   ├── Quiz.java
│   │   │   └── Stock.java
│   │   └── ml/
│   │       └── LSTMForecaster.java         # LSTM implementation
│   └── frontend/
│       ├── src/
│       │   ├── components/
│       │   │   ├── LoginPage.jsx
│       │   │   ├── TutorialPage.jsx
│       │   │   ├── QuizPage.jsx
│       │   │   ├── Forecast.jsx            # Forecasting UI
│       │   │   └── Dashboard.jsx
│       │   ├── services/
│       │   │   └── api.js                  # API client
│       │   ├── App.jsx                     # Root component
│       │   └── main.jsx                    # Entry point
│       ├── package.json
│       ├── vite.config.js
│       └── index.html
```

##  Key Dependencies

### Backend
```xml
<dependency>
    <groupId>org.deeplearning4j</groupId>
    <artifactId>deeplearning4j-core</artifactId>
    <version>1.0.0-M2</version>
</dependency>
<dependency>
    <groupId>org.nd4j</groupId>
    <artifactId>nd4j-native-platform</artifactId>
    <version>1.0.0-M2</version>
</dependency>
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.15.2</version>
</dependency>
```

### Frontend
```json
{
  "react": "^18.2.0",
  "vite": "^5.0.0"
}
```

##  Supported Stocks

**Technology:**
- AAPL (Apple)
- MSFT (Microsoft)
- GOOGL (Google)
- TSLA (Tesla)

**Finance:**
- JPM (JP Morgan)
- BAC (Bank of America)
- GS (Goldman Sachs)

**E-commerce:**
- AMZN (Amazon)
- EBAY (eBay)

**And 11+ more across healthcare, energy, consumer goods sectors**

##  Data Flow

```
User (Frontend)
    ↓
React Component (Forecast.jsx)
    ↓ HTTP GET /api/forecast?symbol=AAPL&days=7
    ↓
ForecastController (Java)
    ↓
LSTMForecaster.forecast()
    ↓ LSTM Prediction (30-day input → 7-day output)
    ↓
Denormalized Forecast + Confidence
    ↓ JSON Response
    ↓
Chart.js Visualization
    ↓
User sees predictions with confidence scores
```

##  Deployment

### Production Build

**Backend:**
```bash
mvn clean package
java -jar target/smart-invest-1.0.jar
```

**Frontend:**
```bash
cd src/frontend
npm run build
# Serve dist/ folder with nginx or similar
```

##  Performance Notes

- **LSTM Training**: ~30-60 seconds on first load
- **Forecast Generation**: <100ms per request
- **Frontend Responsiveness**: Optimized with Vite
- **Memory Usage**: ~500MB with loaded LSTM model

##  Troubleshooting

### Port Already in Use
```bash
# Kill process on port 8080 (macOS/Linux)
lsof -ti:8080 | xargs kill -9

# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F
```

### LSTM Model Not Training
- Ensure DL4J dependencies are installed: `mvn clean install`
- Check Java heap size: `-Xmx4g` for better performance
- Verify ND4J backend is loaded (check console logs)

### Frontend Not Connecting
- Ensure backend is running on port 8080
- Check CORS headers in `Main.java`
- Clear browser cache and restart dev server

### API Errors
- Check browser DevTools Network tab
- Verify request URL matches endpoint
- Ensure JSON parsing matches response format

##  Learning Resources

- [DL4J Documentation](https://deeplearning4j.org/)
- [React Documentation](https://react.dev/)
- [LSTM Explained](https://colah.github.io/posts/2015-08-Understanding-LSTMs/)
- [Stock Market Basics](https://www.investopedia.com/)



