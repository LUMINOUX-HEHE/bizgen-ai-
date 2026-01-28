🔄 COMPLETE FLOW OF BIZGEN AI PROJECT
Let me explain the entire flow from user's first visit to getting their generated content.

📱 USER JOURNEY FLOW
STEP 1: Landing Page (/)
text

User visits website
      ↓
Landing Page displays:
  - Hero section with value proposition
  - Feature highlights
  - "Get Started" CTA button
      ↓
User clicks "Get Started"
      ↓
Navigates to Dashboard (/app)
Files involved:

src/pages/LandingPage.tsx
STEP 2: Dashboard (/app)
text

Dashboard loads
      ↓
Frontend calls:
  - fetchCategories() → GET /api/v1/categories
  - fetchHistory(1, 5) → GET /api/v1/history?page=0&size=5
      ↓
Backend processes:
  1. CategoryController receives request
  2. CategoryService.getAllCategories()
  3. CategoryRepository.findAllByActiveTrue()
  4. Maps entities to CategoryResponse DTOs
      ↓
Dashboard displays:
  - Welcome message
  - Stats cards (Total Generations, This Week)
  - "Create New Content" button
  - Recent history list (last 5 items)
  - Quick access to popular templates
      ↓
User clicks "Create New Content"
      ↓
Navigates to Category Selection (/app/create)
Files involved:

Frontend: src/pages/Dashboard.tsx
Backend: CategoryController.java, CategoryService.java, HistoryController.java
STEP 3: Category Selection (/app/create)
text

Category Selection Page loads
      ↓
Frontend calls:
  - fetchCategories() → GET /api/v1/categories
      ↓
Backend processes:
  1. CategoryController.getAllCategories()
  2. CategoryService loads all active categories
  3. Counts templates per category
  4. Returns CategoryResponse[] with templateCount
      ↓
Page displays grid of category cards:
  
  ┌─────────────────────┐  ┌─────────────────────┐
  │  📣 Marketing       │  │  🛡️ Legal          │
  │  Content            │  │  Documents          │
  │                     │  │                     │
  │  5 templates        │  │  2 templates        │
  │                     │  │  ⚠️ Draft Only     │
  └─────────────────────┘  └─────────────────────┘
      ↓
User clicks a category (e.g., "Marketing Content")
      ↓
Navigates to Template Selection (/app/create/cat-marketing)
Files involved:

Frontend: src/pages/CategorySelection.tsx, src/components/template/CategoryCard.tsx
Backend: CategoryController.java, CategoryService.java
STEP 4: Template Selection (/app/create/:categoryId)
text

Template Selection Page loads
      ↓
Frontend calls:
  - fetchCategoryById(categoryId) → GET /api/v1/categories/{id}
  - fetchTemplates(categoryId) → GET /api/v1/templates?categoryId={id}
      ↓
Backend processes:
  1. TemplateController.getTemplatesByCategoryId()
  2. TemplateService filters templates by category
  3. Loads template metadata from database
  4. Returns TemplateResponse[] array
      ↓
Page displays:
  - Category header with description
  - Legal warning banner (if legal category)
  - Grid of template cards:
  
  ┌────────────────────────┐  ┌────────────────────────┐
  │ 📸 Instagram Post     │  │ 📘 Facebook Post      │
  │                        │  │                        │
  │ Create engaging posts  │  │ Professional posts     │
  │ ⏱ ~2 min   ⭐ Popular │  │ ⏱ ~2 min             │
  │ 🟢 Easy                │  │ 🟢 Easy                │
  └────────────────────────┘  └────────────────────────┘
      ↓
User clicks a template (e.g., "Instagram Post")
      ↓
Navigates to Content Form (/app/create/cat-marketing/tpl-instagram)
Files involved:

Frontend: src/pages/TemplateSelection.tsx, src/components/template/TemplateCard.tsx
Backend: TemplateController.java, TemplateService.java
STEP 5: Content Form (Dynamic) (/app/create/:categoryId/:templateId)
text

Content Form Page loads
      ↓
Frontend calls:
  - fetchTemplateSchema(templateId) → GET /api/v1/templates/{id}/schema
      ↓
Backend processes:
  1. TemplateController.getTemplateSchema(id)
  2. TemplateService loads template from database
  3. BlueprintService loads blueprint JSON file
     - Reads from: resources/blueprints/marketing/instagram-post.json
  4. BlueprintParser parses JSON into Blueprint object
  5. SchemaGenerator converts Blueprint → TemplateSchemaResponse
  6. Returns form schema with sections and fields
      ↓
Frontend receives schema:
  {
    "templateId": "tpl-instagram",
    "sections": [
      {
        "id": "product-info",
        "title": "Product Information",
        "fields": [
          {
            "name": "productName",
            "label": "Product/Service Name",
            "type": "text",
            "required": true,
            "validation": { "minLength": 2, "maxLength": 100 }
          },
          ...
        ]
      }
    ]
  }
      ↓
DynamicForm component:
  1. Parses schema
  2. Builds Zod validation schema dynamically
  3. Renders FormSection components
  4. Each FormSection renders FormField components
      ↓
Page displays dynamic form:
  
  ┌─────────────────────────────────────────────┐
  │ Instagram Product Launch Post               │
  │ ⏱ Estimated time: 2-3 minutes              │
  ├─────────────────────────────────────────────┤
  │ 📦 Product Information                      │
  │                                             │
  │ Product/Service Name *                      │
  │ [_________________________]                 │
  │ The main product you're promoting           │
  │                                             │
  │ Key Benefits *                              │
  │ [_________________________]                 │
  │ [_________________________]                 │
  │ What makes your product special?            │
  ├─────────────────────────────────────────────┤
  │ 👥 Target Audience                          │
  │                                             │
  │ Who is this for? *                          │
  │ [_________________________]                 │
  │                                             │
  │ Desired Tone *                              │
  │ [▼ Casual & Friendly    ]                   │
  ├─────────────────────────────────────────────┤
  │ [Cancel]              [Generate Content →]  │
  └─────────────────────────────────────────────┘
      ↓
User fills in all required fields:
  - productName: "Summer Skincare Bundle"
  - keyBenefits: "Hydrating, organic, cruelty-free"
  - targetAudience: "Young professionals"
  - tone: "casual"
      ↓
Form validates on blur (real-time)
      ↓
Auto-saves draft to localStorage every 30s
      ↓
User clicks "Generate Content"
      ↓
Frontend calls:
  - generateContent(payload) → POST /api/v1/generate
      ↓
Request body:
  {
    "templateId": "tpl-instagram",
    "inputs": {
      "productName": "Summer Skincare Bundle",
      "keyBenefits": "Hydrating, organic, cruelty-free",
      "targetAudience": "Young professionals",
      "tone": "casual"
    }
  }
Files involved:

Frontend: src/pages/ContentForm.tsx, src/components/form/DynamicForm.tsx, src/components/form/FormField.tsx
Backend: TemplateController.java, BlueprintService.java, BlueprintParser.java, SchemaGenerator.java
STEP 6: Backend Content Generation Flow (POST /api/v1/generate)
text

GenerationController receives request
      ↓
GenerationService.generateContent(request)
      ↓
┌─────────────────────────────────────────────────────┐
│ STEP 1: Validate Template                           │
│ - TemplateService.getActiveTemplate(templateId)     │
│ - Checks if template exists and is active           │
│ - Throws ResourceNotFoundException if not found     │
└─────────────────────────────────────────────────────┘
      ↓
┌─────────────────────────────────────────────────────┐
│ STEP 2: Load Blueprint                               │
│ - BlueprintService.loadBlueprint(path)              │
│ - Reads: resources/blueprints/marketing/           │
│          instagram-post.json                         │
│ - BlueprintParser parses JSON                        │
│ - Returns Blueprint object with:                     │
│   • metaPrompt (system instructions)                 │
│   • sections (form schema)                           │
│   • validationRules                                  │
│   • domainKnowledgeRefs                              │
│   • outputFormat                                     │
│   • requiredDisclaimers                              │
└─────────────────────────────────────────────────────┘
      ↓
┌─────────────────────────────────────────────────────┐
│ STEP 3: Validate User Inputs                         │
│ - ValidationService.validateInputs(inputs, schema)  │
│ - Checks required fields                             │
│ - Validates against blueprint rules                  │
│ - Throws ValidationException if invalid              │
└─────────────────────────────────────────────────────┘
      ↓
┌─────────────────────────────────────────────────────┐
│ STEP 4: Load Domain Knowledge                        │
│ - DomainKnowledgeService.loadReferences(refs)       │
│ - Loads: marketing-best-practices.json               │
│         social-media-guidelines.json                 │
│ - Returns domain knowledge snippets                  │
└─────────────────────────────────────────────────────┘
      ↓
┌─────────────────────────────────────────────────────┐
│ STEP 5: Assemble Prompt                              │
│ - PromptAssemblyService.assemble()                  │
│                                                       │
│ Builds SYSTEM PROMPT:                                │
│ --------------------------------------------------   │
│ You are a professional social media copywriter       │
│ specializing in Instagram content for small          │
│ businesses.                                           │
│                                                       │
│ Role: Create engaging, conversion-focused            │
│ Instagram posts that resonate with the target        │
│ audience while maintaining brand voice.              │
│                                                       │
│ Constraints: Keep posts under 2200 characters.       │
│ Include relevant emoji usage. Structure with hook,   │
│ value proposition, and call-to-action.               │
│                                                       │
│ Style: Use short, punchy sentences. Create urgency   │
│ without being pushy. Be authentic and relatable.     │
│                                                       │
│ Reference Knowledge (marketing-best-practices):      │
│ - Optimal length: 125-150 characters                 │
│ - Hashtag strategy: Use 5-10 relevant hashtags       │
│ - Emoji usage: Strategic placement increases         │
│   engagement by 48%                                   │
│ --------------------------------------------------   │
│                                                       │
│ Builds USER PROMPT:                                  │
│ --------------------------------------------------   │
│ Generate content with the following details:         │
│                                                       │
│ - Product/Service Name: Summer Skincare Bundle       │
│ - Key Benefits: Hydrating, organic, cruelty-free    │
│ - Who is this for?: Young professionals              │
│ - Desired Tone: casual                               │
│                                                       │
│ Output Requirements:                                  │
│ - Generate exactly 3 variations                      │
│ - Each variation should be clearly separated         │
│ - Follow the structure: hook-body-cta                │
│ --------------------------------------------------   │
│                                                       │
│ Returns AssembledPrompt object                       │
└─────────────────────────────────────────────────────┘
      ↓
┌─────────────────────────────────────────────────────┐
│ STEP 6: Call AI Service                              │
│ - aiService.generate(aiRequest)                      │
│                                                       │
│ Since we're in DEV mode:                             │
│ - MockAiService is used (not real AI)                │
│ - Simulates 1.5 second delay                         │
│ - Generates 3 mock variations:                       │
│                                                       │
│   Variation 1:                                        │
│   ✨ Get ready for summer! ☀️                        │
│   Our Summer Skincare Bundle is here to keep         │
│   your skin hydrated and glowing. 100% organic,      │
│   cruelty-free, and made for busy professionals      │
│   like you. Link in bio! 🌿                          │
│                                                       │
│   Variation 2:                                        │
│   🌸 Summer skin sorted! Our new bundle is          │
│   everything you need: hydrating, organic, and       │
│   totally cruelty-free. Perfect for young pros       │
│   on the go. Tap to shop! ✨                         │
│                                                       │
│   Variation 3:                                        │
│   Say hello to your new skincare obsession! 💙       │
│   Our Summer Bundle brings the glow with organic,    │
│   cruelty-free goodness. Made for real people        │
│   with real lives. Get yours today! 🛒              │
│                                                       │
│ Returns AiResponse with variations                   │
└─────────────────────────────────────────────────────┘
      ↓
┌─────────────────────────────────────────────────────┐
│ STEP 7: Apply Guardrails                             │
│ - GuardrailService.apply(variations, blueprint)     │
│                                                       │
│ Runs each variation through rules:                   │
│                                                       │
│ 1. PlaceholderDetectionRule                          │
│    - Searches for [PLACEHOLDER_NAME] patterns        │
│    - Highlights: [COMPANY_NAME], [EMAIL], etc.       │
│    - Adds warnings for each placeholder found        │
│                                                       │
│ 2. LegalComplianceRule (if legal content)            │
│    - Checks for required sections                    │
│    - Validates legal language                        │
│    - Adds review warnings                            │
│                                                       │
│ 3. DisclaimerRule (if legal content)                 │
│    - Inserts header: "⚠️ DRAFT DOCUMENT"            │
│    - Inserts footer disclaimer                       │
│                                                       │
│ 4. ContentSafetyRule                                 │
│    - Checks for inappropriate content                │
│    - Validates compliance                            │
│                                                       │
│ Adds required disclaimers from blueprint:            │
│ - For legal: DRAFT_ONLY, LEGAL_REVIEW_REQUIRED      │
│                                                       │
│ Returns GuardrailResult:                             │
│ - processedVariations (modified content)             │
│ - disclaimers (array of disclaimer texts)            │
│ - warnings (array of warning objects)                │
└─────────────────────────────────────────────────────┘
      ↓
┌─────────────────────────────────────────────────────┐
│ STEP 8: Persist to Database                          │
│ - Creates Generation entity                          │
│ - Creates GenerationVariation entities (3)           │
│ - Stores:                                            │
│   • inputData (JSON of user inputs)                  │
│   • assembledPrompt (full prompt sent to AI)         │
│   • variations (generated content)                   │
│   • disclaimers (JSON array)                         │
│   • warnings (JSON array)                            │
│   • status (COMPLETED)                               │
│   • generationTimeMs (1500)                          │
│ - GenerationRepository.save(generation)              │
└─────────────────────────────────────────────────────┘
      ↓
┌─────────────────────────────────────────────────────┐
│ STEP 9: Map to Response DTO                          │
│ - GenerationMapper.toResponse(generation)            │
│ - Converts entities to DTOs                          │
│ - Returns GenerationResponse                         │
└─────────────────────────────────────────────────────┘
      ↓
Response sent to frontend:
  {
    "generationId": "abc-123-xyz",
    "templateId": "tpl-instagram",
    "templateName": "Instagram Product Launch Post",
    "category": "Marketing Content",
    "variations": [
      {
        "id": "var-1",
        "variationNumber": 1,
        "content": "✨ Get ready for summer!...",
        "placeholders": []
      },
      {
        "id": "var-2",
        "variationNumber": 2,
        "content": "🌸 Summer skin sorted!...",
        "placeholders": []
      },
      {
        "id": "var-3",
        "variationNumber": 3,
        "content": "Say hello to your new...",
        "placeholders": []
      }
    ],
    "disclaimers": [],
    "warnings": [],
    "generationTimeMs": 1500,
    "createdAt": "2024-01-15T10:30:00Z"
  }
      ↓
Frontend navigates to: /app/output/abc-123-xyz
Files involved:

Backend:
GenerationController.java
GenerationService.java
TemplateService.java
BlueprintService.java
ValidationService.java
PromptAssemblyService.java
MockAiService.java
GuardrailService.java
PlaceholderDetectionRule.java
LegalComplianceRule.java
DisclaimerRule.java
ContentSafetyRule.java
GenerationRepository.java
GenerationMapper.java
STEP 7: Generated Output Display (/app/output/:generationId)
text

Generated Output Page loads
      ↓
Frontend receives response from POST /generate
      ↓
Page displays:
  
  ┌─────────────────────────────────────────────────┐
  │ ✓ Content Generated Successfully                │
  │ Template: Instagram Product Launch Post         │
  ├─────────────────────────────────────────────────┤
  │ ⚠️ DISCLAIMER (if legal content)                │
  │ This is a draft document. Consult a legal       │
  │ professional before use.                         │
  ├─────────────────────────────────────────────────┤
  │ ⚡ Warnings (if any)                            │
  │ • Missing: [COMPANY_NAME] - Fill this in        │
  ├─────────────────────────────────────────────────┤
  │ Variation 1 of 3              [Copy] [Download] │
  │ ┌───────────────────────────────────────────┐   │
  │ │ ✨ Get ready for summer! ☀️                │   │
  │ │                                             │   │
  │ │ Our Summer Skincare Bundle is here to keep │   │
  │ │ your skin hydrated and glowing. 100%        │   │
  │ │ organic, cruelty-free, and made for busy    │   │
  │ │ professionals like you.                      │   │
  │ │                                             │   │
  │ │ Link in bio! 🌿                             │   │
  │ │                                             │   │
  │ │ [COMPANY_NAME] ← highlighted as placeholder │   │
  │ └───────────────────────────────────────────┘   │
  │                                                   │
  │ ● ○ ○  (variation dots)                          │
  │ [← Previous]                      [Next →]       │
  ├─────────────────────────────────────────────────┤
  │ [Create Another]        [Save to History]       │
  └─────────────────────────────────────────────────┘
      ↓
User can:
  1. Navigate between 3 variations (carousel)
  2. Click "Copy" to copy variation to clipboard
  3. Review warnings and placeholders
  4. Click "Create Another" → goes back to /app/create
  5. Click "Save to History" → saves to history
Files involved:

Frontend:
src/pages/GeneratedOutput.tsx
src/components/output/DisclaimerBanner.tsx
src/components/output/WarningsList.tsx
src/components/output/VariationCarousel.tsx
src/components/output/CopyButton.tsx
STEP 8: History Management (/app/history)
text

User navigates to History page
      ↓
Frontend calls:
  - fetchHistory(0, 20) → GET /api/v1/history?page=0&size=20
      ↓
Backend processes:
  1. HistoryController.getHistory(page, size)
  2. HistoryService.getPaginatedHistory()
  3. GenerationRepository.findAllByOrderByCreatedAtDesc(pageable)
  4. Maps to HistoryItemResponse DTOs
      ↓
Page displays paginated list:
  
  ┌─────────────────────────────────────────────────┐
  │ Generation History (156 items)                   │
  ├─────────────────────────────────────────────────┤
  │ 📸 Instagram Product Launch Post                │
  │ Marketing Content • 2 hours ago                  │
  │ "✨ Get ready for summer! Our Summer..."        │
  │                           [View] [Delete]       │
  ├─────────────────────────────────────────────────┤
  │ 🛡️ Privacy Policy                               │
  │ Legal Documents • Yesterday                      │
  │ "⚠️ DRAFT DOCUMENT - This Privacy Policy..."   │
  │                           [View] [Delete]       │
  ├─────────────────────────────────────────────────┤
  │ ... more items ...                               │
  ├─────────────────────────────────────────────────┤
  │ [← Previous]  Page 1 of 8  [Next →]            │
  └─────────────────────────────────────────────────┘
      ↓
User clicks [View]
      ↓
Navigates to: /app/output/{generationId}
      ↓
User clicks [Delete]
      ↓
Confirmation modal appears
      ↓
User confirms
      ↓
Frontend calls:
  - deleteHistoryItem(id) → DELETE /api/v1/history/{id}
      ↓
Backend deletes from database (cascade delete variations)
      ↓
History list refreshes
Files involved:

Frontend: src/pages/History.tsx
Backend: HistoryController.java, HistoryService.java, GenerationRepository.java
🔧 TECHNICAL DATA FLOW
Database Schema Flow
text

┌─────────────────┐
│   Categories    │
│                 │
│ - id (UUID)     │
│ - name          │◄──────────┐
│ - displayName   │           │
│ - description   │           │
│ - icon          │           │
│ - active        │           │
└─────────────────┘           │
                               │
                               │ Many-to-One
                               │
┌─────────────────┐           │
│   Templates     │           │
│                 │           │
│ - id (UUID)     │───────────┘
│ - name          │
│ - description   │◄──────────┐
│ - blueprintPath │           │
│ - estimatedTime │           │
│ - popular       │           │
│ - difficulty    │           │
└─────────────────┘           │
                               │
                               │ Many-to-One
                               │
┌─────────────────┐           │
│  Generations    │           │
│                 │           │
│ - id (UUID)     │───────────┘
│ - inputData     │
│ - assembledPrompt│
│ - disclaimers   │
│ - warnings      │
│ - status        │◄──────────┐
│ - generationTime│           │
└─────────────────┘           │
                               │
                               │ One-to-Many
                               │
┌─────────────────┐           │
│ Gen Variations  │           │
│                 │           │
│ - id (UUID)     │───────────┘
│ - variationNumber│
│ - content       │
│ - placeholders  │
└─────────────────┘
Blueprint Loading Flow
text

Template selected
      ↓
Template has: blueprintPath = "marketing/instagram-post.json"
      ↓
BlueprintService.loadBlueprint(path)
      ↓
Reads from: src/main/resources/blueprints/marketing/instagram-post.json
      ↓
File contains:
{
  "templateId": "tpl-instagram",
  "version": "1.0.0",
  "metaPrompt": {
    "systemInstruction": "You are a professional...",
    "roleDefinition": "Create engaging...",
    ...
  },
  "sections": [
    {
      "id": "product-info",
      "title": "Product Information",
      "fields": [
        {
          "name": "productName",
          "type": "text",
          "required": true,
          ...
        }
      ]
    }
  ],
  "domainKnowledgeRefs": [
    "marketing-best-practices",
    "social-media-guidelines"
  ],
  "requiredDisclaimers": []
}
      ↓
BlueprintParser parses JSON → Blueprint Java object
      ↓
Blueprint used for:
  1. Schema generation (form fields)
  2. Validation rules
  3. Prompt assembly (meta-prompt)
  4. Domain knowledge loading
  5. Guardrail configuration
Guardrail Processing Flow
text

AI generates 3 variations
      ↓
GuardrailService.apply(variations, blueprint, category)
      ↓
For each variation:
  ┌─────────────────────────────────────┐
  │ Run PlaceholderDetectionRule        │
  │                                     │
  │ Input: "Welcome to [COMPANY_NAME]"  │
  │ Output: Detected placeholder        │
  │ Warning: "Fill in COMPANY_NAME"     │
  └─────────────────────────────────────┘
       ↓
  ┌─────────────────────────────────────┐
  │ Run LegalComplianceRule (if legal)  │
  │                                     │
  │ Checks for: contact, data           │
  │ collection, user rights sections    │
  │ Warns if missing                    │
  └─────────────────────────────────────┘
       ↓
  ┌─────────────────────────────────────┐
  │ Run DisclaimerRule (if legal)       │
  │                                     │
  │ Adds: "⚠️ DRAFT DOCUMENT"          │
  │ Adds footer disclaimer              │
  └─────────────────────────────────────┘
       ↓
  ┌─────────────────────────────────────┐
  │ Run ContentSafetyRule               │
  │                                     │
  │ Validates content safety            │
  └─────────────────────────────────────┘
      ↓
All variations processed
      ↓
Returns:
  - processedVariations (modified content)
  - disclaimers (array)
  - warnings (array)
🎨 UI/UX FLOW
Responsive Design Flow
text

Desktop (>1024px):
  - Sidebar visible
  - Multi-column layouts (3 col for templates)
  - Side-by-side form sections

Tablet (768px - 1024px):
  - Sidebar collapses to hamburger
  - 2-column layouts
  - Stacked form sections

Mobile (<768px):
  - Hamburger menu only
  - Single column layouts
  - Bottom-fixed CTAs
  - Touch-friendly tap targets (44px min)
Loading States Flow
text

Page load initiated
      ↓
Show Skeleton Loader
  - SkeletonCard for dashboard stats
  - SkeletonText for lists
  - Shimmer animation
      ↓
API call in progress
      ↓
Button shows:
  - Spinner icon
  - "Loading..." text
  - Disabled state
      ↓
Data received
      ↓
Replace skeleton with real content
  - Fade-in animation (150ms)
Error Handling Flow
text

API call fails
      ↓
Axios interceptor catches error
      ↓
Error handler determines type:
  
  404 Not Found:
    → Toast: "Template not found"
    → Navigate to /app/create
  
  400 Validation Error:
    → Show inline field errors
    → Highlight invalid fields
  
  500 Server Error:
    → Toast: "Something went wrong. Please try again."
    → Show retry button
  
  Network Error:
    → Banner: "No internet connection"
    → Auto-retry in 3 seconds
🔄 AUTO-SAVE FLOW
text

User typing in form
      ↓
Debounced onChange handler (500ms)
      ↓
After 500ms of no typing:
  - Collect form values
  - Serialize to JSON
  - Save to localStorage:
      key: `draft-${templateId}`
      value: { inputs, timestamp }
      ↓
Next time user visits same template:
  - Check localStorage for draft
  - If found and < 24 hours old:
      → Show "Resume draft?" modal
      → User can restore or discard
  - If user generates content:
      → Clear draft from localStorage
📊 COMPLETE ARCHITECTURE FLOW
text

┌─────────────────────────────────────────────────────────┐
│                    FRONTEND (React)                      │
│                                                           │
│  Pages → Components → API Client (Axios)                 │
│    ↓         ↓              ↓                            │
│  Routing  Validation    HTTP Calls                       │
│  (Router) (Zod)         (REST)                           │
└────────────────────────┬────────────────────────────────┘
                         │
                         │ HTTP/JSON
                         ↓
┌─────────────────────────────────────────────────────────┐
│                BACKEND (Spring Boot)                     │
│                                                           │
│  ┌───────────────────────────────────────────────┐      │
│  │          API Layer (Controllers)               │      │
│  │  - CategoryController                          │      │
│  │  - TemplateController                          │      │
│  │  - GenerationController                        │      │
│  │  - HistoryController                           │      │
│  └────────────────┬──────────────────────────────┘      │
│                   │                                       │
│  ┌────────────────▼──────────────────────────────┐      │
│  │        Service Layer (Business Logic)         │      │
│  │  - CategoryService                             │      │
│  │  - TemplateService                             │      │
│  │  - GenerationService ◄────────────────┐       │      │
│  │  - BlueprintService                    │       │      │
│  │  - PromptAssemblyService               │       │      │
│  │  - GuardrailService                    │       │      │
│  │  - ValidationService                   │       │      │
│  └────────────────┬──────────────────────┬───────┘      │
│                   │                       │               │
│  ┌────────────────▼──────────┐  ┌────────▼────────┐     │
│  │   Domain Layer             │  │  AI Abstraction │     │
│  │  - Entities                │  │  - AiService    │     │
│  │  - Value Objects           │  │  - MockAiService│     │
│  │  - Blueprint               │  │    (Dev Mode)   │     │
│  └────────────────┬───────────┘  └─────────────────┘     │
│                   │                                       │
│  ┌────────────────▼──────────────────────────────┐      │
│  │      Infrastructure Layer                      │      │
│  │  - Repositories (JPA)                          │      │
│  │  - BlueprintParser (JSON → Object)             │      │
│  │  - DomainKnowledgeLoader                       │      │
│  └────────────────┬──────────────────────────────┘      │
│                   │                                       │
│  ┌────────────────▼──────────────────────────────┐      │
│  │          Database (H2)                         │      │
│  │  - categories                                  │      │
│  │  - templates                                   │      │
│  │  - generations                                 │      │
│  │  - generation_variations                       │      │
│  └────────────────────────────────────────────────┘      │
│                                                           │
│  ┌─────────────────────────────────────────────┐        │
│  │      File System (Resources)                 │        │
│  │  - blueprints/*.json                         │        │
│  │  - domain-knowledge/*.json                   │        │
│  └─────────────────────────────────────────────┘        │
└─────────────────────────────────────────────────────────┘
✨ KEY TAKEAWAYS
User never writes prompts - All prompts are assembled from blueprints
Blueprint-driven - Everything is governed by JSON blueprints
Guardrails mandatory - Every output goes through safety checks
Form is dynamic - Rendered from backend schema
AI is abstracted - Can swap MockAiService for real AI without code changes
Legal = Draft only - Always shows disclaimers and warnings
Full history - Every generation is saved and retrievable
This is the complete flow of BizGen AI from landing page to generated content! 🚀