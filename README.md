# Resume Builder System (Project 50)

A full-stack Java web application (Spring Boot + Thymeleaf) implementing all required modules:

- **User Registration** — sign up / login (BCrypt-hashed passwords), session-based auth
- **Profile Management** — name, contact info, address, profile summary
- **Education Details** — add/delete degrees, institutions, scores
- **Experience Management** — add/delete work experience entries
- **Skills & Certifications** — skills with proficiency level, plus certifications
- **Resume Preview** — live HTML preview of the compiled resume
- **PDF Generation** — download a formatted PDF (OpenPDF), in a chosen template style
- **Admin** — manage resume templates (add/toggle/delete), view registered users, monitor usage (users / templates / resumes generated)

## Tech stack
- Java 17, Spring Boot 3.2 (Web, Data JPA, Thymeleaf, Validation)
- H2 in-memory database (zero setup) — MySQL config included but commented out in `application.properties`
- OpenPDF for PDF generation
- Plain HTML/CSS (no frontend framework), session-based auth via a `HandlerInterceptor` (no Spring Security filter chain — only `spring-security-crypto` is used, for password hashing)

## Running it

```bash
mvn spring-boot:run
```

Then open http://localhost:8080

- Register a new account, or log in as the seeded admin:
  - **Admin login:** `admin@resumebuilder.com` / `Admin@123`
- H2 console (to inspect the database) is at http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:resumedb`, user `sa`, blank password

## Switching to MySQL

In `application.properties`, comment out the H2 block and uncomment the MySQL block, then add the MySQL driver dependency to `pom.xml`:

```xml
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <scope>runtime</scope>
</dependency>
```

## Project structure

```
src/main/java/com/resumebuilder/
  entity/       JPA entities (User, Education, Experience, Skill, Certification, Project, Achievement, ResumeTemplate, ResumeGenerationLog)
  repository/   Spring Data JPA repositories
  service/      UserService (auth), PdfGenerationService (OpenPDF resume rendering)
  controller/   One controller per module (Auth, Dashboard/Profile, Education, Experience, Skill, Certification, Project, Achievement, Resume, Admin)
  filter/       Session-based auth/admin interceptors
  config/       WebConfig (registers interceptors)
src/main/resources/
  templates/    Thymeleaf HTML pages (+ templates/admin/ for the admin panel)
  static/css/   Stylesheet
  application.properties
```

## Notes / things to extend for a stronger submission
- Add file upload for a profile photo.
- Add more resume template styles in `PdfGenerationService` (currently "classic" and "modern").
- Add pagination/search on the admin Users page for larger datasets.
- Add server-side validation messages on the module forms (Education, Experience, etc.) similar to the registration form.
