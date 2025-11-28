# Smart Invest Sandbox

Minimal full-stack learning sandbox for stock market tutorials, quizzes, exercises and simple user management.

- Backend (Java, lightweight HTTP server + services)
  - Main entry: [`Main`](src/main/java/Main.java) — [src/main/java/Main.java](src/main/java/Main.java)  
  - Controllers: [`AuthController`](src/backend/controllers/AuthController.java), [`TutorialController`](src/backend/controllers/TutorialController.java)  
    - Files: [src/backend/controllers/AuthController.java](src/backend/controllers/AuthController.java), [src/backend/controllers/TutorialController.java](src/backend/controllers/TutorialController.java)  
  - Services: [`TutorialService`](src/backend/services/TutorialService.java) — [src/backend/services/TutorialService.java](src/backend/services/TutorialService.java)  
  - Models / data: [`TutorialSection`](src/backend/models/TutorialSection.java), [`QuizData`](src/backend/models/data/QuizData.java)  
    - Files: [src/backend/models/TutorialSection.java](src/backend/models/TutorialSection.java), [src/backend/models/data/QuizData.java](src/backend/models/data/QuizData.java)

- Frontend (React + Vite)
  - App bootstrap: [src/frontend/src/main.jsx](src/frontend/src/main.jsx)  
  - API helper: [`authAPI` / `tutorialAPI`] — [src/frontend/src/services/api.js](src/frontend/src/services/api.js)  
  - Key UI: [`TutorialPage`](src/frontend/src/components/TutorialPage.jsx) — [src/frontend/src/components/TutorialPage.jsx](src/frontend/src/components/TutorialPage.jsx)

- Build / tooling files
  - Maven: [pom.xml](pom.xml)
  - Frontend package: [src/frontend/package.json](src/frontend/package.json)
  - Vite config: [src/frontend/vite.config.js](src/frontend/vite.config.js)

Quick start

1. Backend (run locally)
   - Build with Maven or run in your IDE:
     - Option A (IDE): run the `Main` class: [`Main`](src/main/java/Main.java).
     - Option B (CLI): open project root and use your preferred Maven/IDE command to run the app. The backend listens on port 8080 by default.
   - API notes:
     - Main HTTP entrypoints are created in [`Main`](src/main/java/Main.java) and routed to handlers and controllers. See controller implementations: [src/backend/controllers/AuthController.java](src/backend/controllers/AuthController.java) and [src/backend/controllers/TutorialController.java](src/backend/controllers/TutorialController.java).
     - CORS is handled for frontend usage (see `setCorsHeaders` in [`Main`](src/main/java/Main.java)).

2. Frontend
   - Install and run:
     - cd into the frontend folder: `cd src/frontend`
     - Install: `npm install`
     - Dev server: `npm run dev` (Vite default port is typically 5173)
   - The frontend uses the helper APIs defined in [src/frontend/src/services/api.js](src/frontend/src/services/api.js) to talk to the backend at `http://localhost:8080/api`.

API summary (most-used)
- POST /api/register — handled by [`AuthController`](src/backend/controllers/AuthController.java) / [`RegisterHandler`](src/main/java/Main.java)
- POST /api/login — handled by [`AuthController`](src/backend/controllers/AuthController.java) / [`LoginHandler`](src/main/java/Main.java)
- GET /api/tutorials — list tutorials (controller: [`TutorialController`](src/backend/controllers/TutorialController.java))
- GET/POST quiz & exercise endpoints — see [`TutorialService`](src/backend/services/TutorialService.java) and handlers in [`Main`](src/main/java/Main.java)

Project structure (high level)
- src/backend — backend controllers, services, models and DB handlers
  - [src/backend/controllers](src/backend/controllers)  
  - [src/backend/services](src/backend/services)  
  - [src/backend/models](src/backend/models)  
- src/frontend — React app (Vite)
  - [src/frontend/src](src/frontend/src)  
- src/main/java — lightweight HTTP server setup (fallback / alternative server)
  - [src/main/java/Main.java](src/main/java/Main.java)

Testing & data
- The repository contains in-memory/sample content and helper data providers such as [`QuizData`](src/backend/models/data/QuizData.java) used to seed quizzes.

References (quick links)
- [`Main`](src/main/java/Main.java) — [src/main/java/Main.java](src/main/java/Main.java)  
- [`AuthController`](src/backend/controllers/AuthController.java) — [src/backend/controllers/AuthController.java](src/backend/controllers/AuthController.java)  
- [`TutorialController`](src/backend/controllers/TutorialController.java) — [src/backend/controllers/TutorialController.java](src/backend/controllers/TutorialController.java)  
- [`TutorialService`](src/backend/services/TutorialService.java) — [src/backend/services/TutorialService.java](src/backend/services/TutorialService.java)  
- [`TutorialSection`](src/backend/models/TutorialSection.java) — [src/backend/models/TutorialSection.java](src/backend/models/TutorialSection.java)  
- Frontend API: [src/frontend/src/services/api.js](src/frontend/src/services/api.js)  
- Frontend main: [src/frontend/src/main.jsx](src/frontend/src/main.jsx)
