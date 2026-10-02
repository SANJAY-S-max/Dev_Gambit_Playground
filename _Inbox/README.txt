╔══════════════════════════════════════════════════════════════════════════════╗
║                        _Inbox — THE DROP ZONE                               ║
╚══════════════════════════════════════════════════════════════════════════════╝

  HOW TO USE THIS FOLDER
  ──────────────────────
  1. Drop any new learning folder / exercise into this _Inbox/ folder
  2. Tell the AI assistant: "I added a new folder in _Inbox, categorize it"
  3. The AI will:
       → Analyze the folder contents (file types, frameworks, config files)
       → Move it to the correct stack folder (Java_Stack, MERN_Stack, etc.)
       → Update FOLDER_STRUCTURE.txt with the new entry
       → Update README.md if needed
       → Push everything to GitHub
  4. This folder should be empty after categorization

  CATEGORIZATION RULES (what the AI looks for)
  ─────────────────────────────────────────────
  pom.xml + web.xml + .jsp      → Java_Stack/JSP_Servlet_Projects/
  pom.xml + application.properties  → Java_Stack/Spring_Projects/
  pom.xml + @Entity / persistence.xml → Java_Stack/Hibernate_JPA/
  .java files only              → Java_Stack/Other_Java/
  package.json + React/JSX      → MERN_Stack/Frontend/
  .html / .css / .js only       → MERN_Stack/Frontend/
  package.json + express        → MERN_Stack/Node_Express/
  bin/startup.bat present       → tomcat/
  New stack entirely            → New top-level folder created

══════════════════════════════════════════════════════════════════════════════
