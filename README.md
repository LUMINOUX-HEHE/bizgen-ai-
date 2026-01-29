# BizGen AI

A template-driven content generation platform for SMB owners. Generate Privacy Policies, Terms of Service, marketing content, and more—powered by AI.

## Features
- Modern React + Vite frontend with Tailwind CSS
- Spring Boot backend with modular architecture
- Template-based content generation (legal, marketing, etc.)
- AI integration (pluggable, currently mock for dev)
- User-friendly dashboard, form builder, and output carousel
- Content guardrails for compliance and safety
- OpenAPI/Swagger documentation

## Tech Stack
- **Frontend:** React 19, Vite, TypeScript, Tailwind CSS, Axios, React Router, Zod, Framer Motion
- **Backend:** Java 17+, Spring Boot 3, Spring Data JPA, H2 DB, Lombok, MapStruct, JUnit 5

## Project Structure
```
BizGen AI project/
├── backend/           # Spring Boot backend
│   ├── src/main/java/com/bizgenai/
│   ├── src/main/resources/
│   ├── pom.xml
│   └── ...
├── src/              # React frontend
│   ├── components/
│   ├── pages/
│   ├── api/
│   ├── hooks/
│   ├── utils/
│   ├── App.tsx
│   ├── main.tsx
│   └── ...
├── index.html        # Frontend entry
├── package.json      # Frontend dependencies
├── vite.config.ts    # Vite config
└── README.md         # Project overview
```

## Getting Started

### Prerequisites
- Node.js 18+
- Java 17+
- Maven 3.8+

### Backend Setup
```bash
cd backend
./mvnw spring-boot:run
# or
./mvnw clean package
java -jar target/bizgen-ai-1.0.0.jar
```

### Frontend Setup
```bash
npm install
npm run dev
```
Visit: http://localhost:5173

### API Docs
- Swagger UI: http://localhost:8080/swagger-ui.html

## Usage Flow
1. User lands on homepage, clicks "Get Started"
2. Dashboard loads categories and history
3. User selects a template, fills dynamic form
4. Submits to generate content (AI-powered)
5. Output variations shown, can copy/save
6. History and compliance guardrails included

## Customization
- Add new templates: backend/resources/blueprints/
- Add domain knowledge: backend/resources/domain-knowledge/
- Integrate real AI: implement AiService interface in backend

## License
MIT
