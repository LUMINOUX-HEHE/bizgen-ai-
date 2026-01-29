# BizGen AI

A template-driven content generation platform for SMB owners. Generate Privacy Policies, Terms of Service, marketing content, and more—powered by AI.

# BizGen AI – User Guide

BizGen AI helps you quickly generate professional content such as Privacy Policies, Terms of Service, marketing copy, and more, tailored for your business. This guide explains how to use the product as an end user.

## Getting Started
1. **Open BizGen AI in your browser.**
2. **Landing Page:**
   - Read about the product’s features and benefits.
   - Click **Get Started** to begin.

## Dashboard
- View a summary of your recent content generations and categories.
- Click **Create New Content** to start generating content.

## Selecting a Category & Template
1. **Choose a Category** (e.g., Legal, Marketing).
2. **Browse Templates** for the selected category (e.g., Privacy Policy, Social Media Post).
3. Click on a template to proceed.

## Filling the Form
- Each template provides a dynamic form.
- Enter your business details and answer the questions.
- Required fields are marked. Tooltips and help text are available for guidance.

## Generating Content
1. Click **Generate** after filling the form.
2. The AI will process your request (may take up to 2 minutes for long documents).
3. Once ready, you’ll see multiple content variations.

## Reviewing & Using Output
- Review the generated content.
- Use the **Copy** button to copy any variation.
- For legal documents, review carefully before publishing.

## History
- Access your previous generations from the **History** page.
- View, copy, or delete past results.

## Compliance & Guardrails
- BizGen AI includes built-in checks for legal and content safety.
- Warnings or disclaimers are shown if content may need review.

## Support
- For help, feedback, or issues, contact the support team (see website footer or contact section).

---

**BizGen AI is a productivity tool. Always review AI-generated legal or sensitive content with a professional before use.**

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
