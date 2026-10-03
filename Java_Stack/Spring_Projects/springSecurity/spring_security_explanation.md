# 🔐 Spring Security + JWT Project — Complete Beginner's Guide

> **One-line summary**: You built a REST API where users **register**, **log in** to get a **JWT token**, and use that token to access **protected endpoints**. Spring Security acts as the gatekeeper, and your custom `JwtFilter` validates the token on every request.

---

## 📁 Project File Overview

| File | Layer | What it does |
|------|-------|--------------|
| `SpringSecurityApplication.java` | Entry Point | Starts the whole app |
| `pom.xml` | Config | Maven dependency list |
| `application.properties` | Config | DB settings, app name |
| `SecurityConfig.java` | Security | Security rules, filter chain setup |
| `JwtFilter.java` | Security | Reads & validates JWT on every request |
| `JWTService.java` | Service | Creates, reads, validates JWT tokens |
| `MyUserDetailsService.java` | Service | Loads user from DB for Spring Security |
| `UserService.java` | Service | Register (BCrypt hash) + Login (auth + JWT) |
| `UserController.java` | Controller | POST /user/register and /user/login |
| `HelloController.java` | Controller | GET / (protected) |
| `StudentController.java` | Controller | GET/POST /students (protected) |
| `Users.java` | Model | Database table blueprint |
| `Student.java` | Model | In-memory data (no DB) |
| `UserPrincipal.java` | Model | Adaptor: wraps Users to speak Spring Security's language |
| `UserRepo.java` | Repository | Database helper for Users table |

---

## 🔄 The Big Picture Flow

```
REGISTER:  POST /user/register → hash password → save to MySQL ✅

LOGIN:     POST /user/login → verify password → return JWT token ✅

USE API:   GET /students
           Header: Authorization: Bearer <token>
           → JwtFilter reads token → validates → sets auth context
           → Spring allows access → returns data ✅

NO TOKEN:  GET /students (no header)
           → JwtFilter finds nothing → not authenticated
           → Spring blocks → 401 Unauthorized ❌
```

---

## 📄 File 1: `pom.xml` — The Shopping List

```xml
<parent>
    <version>3.3.4</version>  <!-- FIXED: was 4.1.1 (doesn't exist!) -->
</parent>
```
📌 **Why fixed**: Spring Boot `4.1.1` does not exist. Maven couldn't download it → build failed. Correct version is `3.3.4`.

```xml
<artifactId>spring-boot-starter-web</artifactId>  <!-- FIXED: was spring-boot-starter-webmvc (doesn't exist!) -->
```
📌 `spring-boot-starter-web` = Everything you need to build REST APIs (Spring MVC, Jackson for JSON, Tomcat server).

```xml
<artifactId>spring-boot-starter-security</artifactId>
```
📌 Adding just this ONE dependency locks down your entire app instantly. All endpoints require authentication automatically!

```xml
<artifactId>spring-boot-starter-data-jpa</artifactId>
<artifactId>mysql-connector-j</artifactId>
```
📌 JPA = talk to database with Java objects (no raw SQL). `mysql-connector-j` = the actual MySQL driver.

```xml
<!-- The JWT library — 3 jars needed -->
<groupId>io.jsonwebtoken</groupId>
<artifactId>jjwt-api</artifactId>     <!-- Your code uses this (interfaces) -->
<artifactId>jjwt-impl</artifactId>    <!-- Actual implementation (runtime only) -->
<artifactId>jjwt-jackson</artifactId> <!-- JSON parsing for JWT (runtime only) -->
```
📌 Three separate jars for JJWT (Java JWT library). `scope=runtime` = needed only at runtime, not during compile.

```xml
<artifactId>spring-boot-starter-test</artifactId>   <!-- FIXED: spring-boot-starter-webmvc-test doesn't exist! -->
<artifactId>spring-security-test</artifactId>        <!-- FIXED: spring-boot-starter-security-test doesn't exist! -->
```
📌 **Why fixed**: The original artifact IDs don't exist in Maven Central. Build would fail with "dependency not found".

---

## 📄 File 2: `application.properties` — Settings File

```properties
spring.application.name=springSecurity
```
📌 Just a name for your app. Shows up in logs.

```properties
spring.security.user.name=sanjay
spring.security.user.password=1234
```
📌 **These lines do NOTHING now**. They set the default in-memory user, but since you use database users + JWT, Spring ignores them. Safe to delete but doesn't cause errors.

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/my_database
```
📌 Where is your database? `localhost` = your own computer. `3306` = MySQL's port. `my_database` = the schema name.
⚠️ Make sure to run `CREATE DATABASE my_database;` in MySQL first!

```properties
spring.jpa.hibernate.ddl-auto=update
```
📌 `update` = Auto-create the `users` table if it doesn't exist. Update it if structure changed. Do nothing if same. You never have to write `CREATE TABLE`!

```properties
spring.jpa.show-sql=true
```
📌 Print every SQL query to the console. Super helpful when learning — you can see exactly what JPA is doing.

---

## 📄 File 3: `Users.java` — Database Table Blueprint

```java
@Entity
public class Users {
    @Id
    private int id;
    private String username;
    private String password;
```
📌 `@Entity` = "Create a table called `users` in MySQL for this class."
📌 `@Id` = This is the **Primary Key** — uniquely identifies each row (like an Aadhaar number).
📌 Each field = one column in the database table.

```java
    public Users() { }
```
📌 **Empty constructor is MANDATORY for JPA!** JPA creates an empty object first, then fills it with `set...()` methods. Remove this → crash!

```java
    // getters and setters for id, username, password
    // toString for debugging
```
📌 `private` fields need getters/setters so outside code can read/write them. Spring, Jackson (JSON), and JPA all need these.

---

## 📄 File 4: `Student.java` — In-Memory Data (No Database)

```java
public class Student {
    private int id;
    private String name;
    private int mark;
```
📌 Notice: **No `@Entity`**, **No `@Id`**! This class is NOT stored in a database.
📌 Student data lives in RAM only. Restart the app → data is gone.
📌 Used to practice REST endpoints — not meant to persist data.

---

## 📄 File 5: `UserRepo.java` — Database Helper Interface

```java
@Repository
public interface UserRepo extends JpaRepository<Users, Integer> {
    Users findByUsername(String username);
}
```
📌 `@Repository` = "This is a database layer class. Spring, manage it."
📌 `interface` = No actual code! Spring Data JPA **generates the implementation** at startup.
📌 `extends JpaRepository<Users, Integer>` = Get **20+ free database methods**:
- `save(user)` → INSERT or UPDATE
- `findById(1)` → SELECT WHERE id = 1
- `findAll()` → SELECT *
- `deleteById(1)` → DELETE
- No SQL needed!

```java
Users findByUsername(String username);
```
📌 **Magic method** — Just by naming it right, Spring generates:
```sql
SELECT * FROM users WHERE username = ?
```
Spring reads: `find` = SELECT, `By` = WHERE, `Username` = column name.

---

## 📄 File 6: `UserPrincipal.java` — The Adaptor/Bridge

> 🤔 **Why does this exist?** Spring Security only understands `UserDetails` interface. Your `Users` class doesn't implement it. `UserPrincipal` WRAPS your `Users` object and speaks Spring Security's language.

```java
public class UserPrincipal implements UserDetails {
    private Users user;

    public UserPrincipal(Users user) {
        this.user = user;
    }
```
📌 `implements UserDetails` = "I promise to provide all 7 required methods Spring Security needs."

```java
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
    }
```
📌 **FIXED**: Was `"USER"`. Now correctly `"ROLE_USER"`.
📌 Spring Security **requires the `ROLE_` prefix**. Without it, role-based access checks (`hasRole("USER")`) won't work.
📌 Every user gets the `ROLE_USER` permission. Real apps might give some users `ROLE_ADMIN`.

```java
    @Override public String getPassword() { return user.getPassword(); }
    @Override public String getUsername() { return user.getUsername(); }
```
📌 Spring Security asks "what's the password/username?" — we delegate to our `Users` object.

```java
    @Override public boolean isAccountNonExpired()    { return true; }
    @Override public boolean isAccountNonLocked()     { return true; }
    @Override public boolean isCredentialsNonExpired(){ return true; }
    @Override public boolean isEnabled()              { return true; }
```
📌 All return `true` = account is valid, not locked, not expired, enabled. Simplified for learning. Real apps check actual database flags.

---

## 📄 File 7: `MyUserDetailsService.java` — The User Lookup Service

```java
@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo repo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = repo.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("user not found");
        }
        return new UserPrincipal(user);
    }
}
```
📌 `implements UserDetailsService` = Promise to implement `loadUserByUsername()`.
📌 Spring Security **automatically calls this** when it needs to find a user — you never call it manually.
📌 `repo.findByUsername(username)` → SQL: `SELECT * FROM users WHERE username = ?`
📌 If user not found → throw `UsernameNotFoundException` → Spring Security returns 401.
📌 If found → wrap in `UserPrincipal` and return → Spring Security verifies the password.

---

## 📄 File 8: `JWTService.java` — The JWT Factory ⭐ NEW!

```java
@Service
public class JWTService {

    private String secretKey;

    public JWTService() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
        SecretKey sK = keyGen.generateKey();
        secretKey = Base64.getEncoder().encodeToString(sK.getEncoded());
    }
```
📌 **Generates a random secret key on startup** using HmacSHA256 algorithm.
📌 `Base64.encode` converts binary key bytes to a safe text string.
📌 This key is used to **sign** tokens — only your server knows it, so only it can create valid tokens.
⚠️ Key regenerated on every restart → existing tokens become invalid. In production, use a fixed key from config!

```java
    public String generateToken(String userName) {
        return Jwts.builder()
            .claims().add(new HashMap<>()).subject(userName)
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 10))  // FIXED!
            .and().signWith(getKey())
            .compact();
        // FIXED: removed the unreachable hardcoded return below .compact()!
    }
```
📌 **Builds and returns a signed JWT token** for the given username.

> **Bug fixed** — Expiration was: `.expiration(new Date(System.currentTimeMillis() * 60 * 60 * 10))`
> - `System.currentTimeMillis()` ≈ 1.7 trillion ms. **Multiplying** gives an astronomically wrong number!
> - Fixed to: `+ 1000L * 60 * 60 * 10` = **adds 10 hours** to now. The `L` prevents integer overflow.

> **Bug fixed** — There was a second `return "hardcodedToken"` after `.compact()`. Unreachable code + compile error. Removed!

```java
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);  // FIXED: was return "";
    }
```
📌 **FIXED**: Was hardcoded `return ""` — always returned empty string, breaking all JWT auth!
📌 Now reads the `"sub"` (subject = username) from the JWT payload.

```java
    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
        // FIXED: was return true; (always accepted any token!)
    }
```
📌 **FIXED**: Was hardcoded `return true` — any token (even fake/expired) would pass!
📌 Now checks **two things**:
1. Does the username in token match the expected user? ✓
2. Is the token NOT expired? ✓
Only returns `true` if both pass.

```java
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
            .verifyWith((SecretKey) getKey())
            .build().parseSignedClaims(token).getPayload();
    }
```
📌 Parses the JWT, **verifies the signature** using the secret key.
📌 If signature is tampered → throws `JwtException` → token rejected!
📌 Returns the `Claims` object (all data inside the token).

---

## 📄 File 9: `JwtFilter.java` — The Request Interceptor ⭐ NEW!

```java
@Component    // FIXED: was missing — Spring couldn't manage this class!
public class JwtFilter extends OncePerRequestFilter {

    @Autowired private JWTService jwtService;
    @Autowired ApplicationContext context;
```
📌 **FIXED `@Component`**: Without this, Spring doesn't know about `JwtFilter`. The `@Autowired` fields (jwtService, context) would be `null` → `NullPointerException` on every request!
📌 `extends OncePerRequestFilter` = Run exactly **once per request** (not multiple times).

```java
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;
```
📌 `doFilterInternal` = Called for every incoming HTTP request.
📌 Read the `Authorization` header from the request.

```java
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            username = jwtService.extractUserName(token);
        }
```
📌 JWT tokens are sent as: `Authorization: Bearer eyJhbG...`
📌 `startsWith("Bearer ")` = confirms it's a JWT token.
📌 `substring(7)` = removes `"Bearer "` (7 chars) to get just the token.
📌 `extractUserName` reads who this token belongs to.

```java
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = context.getBean(MyUserDetailsService.class)
                                             .loadUserByUsername(username);

            if (jwtService.validateToken(token, userDetails)) {   // FIXED: was missing ')'!
```
📌 **FIXED syntax error**: Was `if(jwtService.validateToken(token,userDetails){` — missing `)` before `{`. COMPILE ERROR!
📌 Only process if `username` was found AND request isn't already authenticated.
📌 Load full user details from DB to verify the user still exists.
📌 `validateToken` checks both username match AND expiry.

```java
                UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
```
📌 Creates an authentication object with user details + their roles.
📌 **Puts it in `SecurityContextHolder`** — this is how Spring Security "knows" the user is logged in for this request!

```java
        filterChain.doFilter(request, response);
```
📌 **MUST call this** — passes the request to the next filter in the chain.
📌 Without this line: request is stuck, client never gets a response!

---

## 📄 File 10: `SecurityConfig.java` — The Security Rulebook ⭐ UPDATED!

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired private UserDetailsService userDetailsService;
    @Autowired private JwtFilter jwtFilter;  // FIXED: semicolon was missing!
```
📌 **FIXED**: `private JwtFilter jwtFilter` with no `;` at the end → **compile error**!
📌 `@Configuration` = "This class has Spring settings."
📌 `@EnableWebSecurity` = "Activate Spring Security for this web app."

```java
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(AbstractHttpConfigurer::disable)
```
📌 **CSRF disabled** — safe for REST APIs tested with Postman. Enable for browser-facing apps.

```java
            .authorizeHttpRequests(request ->
                request
                    .requestMatchers("/user/register", "/user/login").permitAll()  // FIXED!
                    .anyRequest().authenticated()
            )
```
📌 **FIXED**: Was `.requestMatchers("/user/register","login")`. The string `"login"` was missing `/`!
📌 Also fixed path to match UserController: the login endpoint is at `/user/login`.
📌 `permitAll()` = These two URLs work WITHOUT authentication (no token needed).
📌 `anyRequest().authenticated()` = Everything else needs a valid JWT token.

```java
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
```
📌 **STATELESS** = Server remembers NO sessions. Client must send JWT token with every request. Perfect for REST APIs.

```java
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
```
📌 **KEY LINE** — Adds our JwtFilter to the security filter chain, **before** Spring's default username/password filter.
📌 So: JWT validation happens first → if valid, authentication is set → Spring's filter sees it and skips its own check.

```java
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {   // FIXED: was missing throws Exception!
        return config.getAuthenticationManager();
    }
```
📌 **FIXED**: `getAuthenticationManager()` throws a checked `Exception`. Java requires `throws Exception` in the method signature. Without it → **compile error**!
📌 This `@Bean` makes `AuthenticationManager` injectable. `UserService` uses it to verify login credentials.

```java
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        return provider;
    }
```
📌 `DaoAuthenticationProvider` = verifies credentials against a database.
📌 `new DaoAuthenticationProvider(userDetailsService)` = "Use MyUserDetailsService to find users."
📌 `BCryptPasswordEncoder(12)` = "Passwords are BCrypt-hashed. Use strength 12 to verify."
📌 During login: `BCrypt.matches(typedPassword, storedHash)` is called automatically.

---

## 📄 File 11: `UserService.java` — Business Logic ⭐ UPDATED!

```java
@Service
public class UserService {

    @Autowired private UserRepo repo;
    @Autowired private JWTService jwtService;
    @Autowired private AuthenticationManager authManager;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
```
📌 Three dependencies injected by Spring:
- `repo` → save/find users in database
- `jwtService` → generate JWT tokens after login
- `authManager` → verify username+password during login

```java
    public Users register(Users user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return repo.save(user);
    }
```
📌 `encoder.encode("S@123")` → `"$2a$12$randomhash..."`
📌 BCrypt is **one-way**: you can't reverse the hash to get `"S@123"` back. Only way to verify is hash again and compare.
📌 `repo.save(user)` → `INSERT INTO users ...` in MySQL.

```java
    public String verify(Users user) {
        Authentication authentication = authManager.authenticate(  // FIXED: was AuthenticationManager type!
            new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(user.getUsername());  // FIXED: was generateToken() with no arg!
        } else {
            return "Fail";
        }
    }
```
📌 **FIXED**: `authManager.authenticate()` returns `Authentication`, NOT `AuthenticationManager`. Wrong type = compile error!
📌 **FIXED**: `jwtService.generateToken()` requires a `String userName` argument. Calling it with no arg = compile error!

📌 `authManager.authenticate()` internally:
1. Calls `MyUserDetailsService.loadUserByUsername("sanjay")` → loads from DB
2. Calls `BCrypt.matches(typedPassword, storedHash)` → checks password
3. If wrong → throws `BadCredentialsException` → 401

📌 If successful → generate and return JWT token to the client.

---

## 📄 File 12: `UserController.java` — REST Endpoints

```java
@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class UserController {

    @Autowired private UserService service;

    @PostMapping("/register")  // → POST /user/register
    public Users register(@RequestBody Users user) {
        return service.register(user);
    }

    @PostMapping("/login")  // → POST /user/login
    public String login(@RequestBody Users user) {
        return service.verify(user);
    }
}
```
📌 `@RequestMapping("/user")` = All URLs start with `/user`.
📌 `@CrossOrigin("*")` = Allow requests from any frontend origin (CORS).
📌 `@RequestBody` = Convert JSON body → Java object automatically.
📌 Both endpoints are **public** (no token needed) — configured in `SecurityConfig`.

---

## 📄 File 13: `HelloController.java`

```java
@RestController
public class HelloController {
    @GetMapping("/")
    public String hello(HttpServletRequest req) {
        return "Hola hole" + req.getSession().getId();
    }
}
```
📌 `GET /` → **Protected!** Needs JWT token.
📌 Returns a session ID — shows that even in STATELESS mode, Spring creates a temporary request-scoped session object.

---

## 📄 File 14: `StudentController.java`

```java
@RestController
public class StudentController {
    private List<Student> students = new ArrayList<>(List.of(
        new Student(1, 80, "sanjay"),
        new Student(2, 50, "Abi")
    ));

    @GetMapping("/students")   // Protected
    public List<Student> getStudents() { return students; }

    @GetMapping("/csrf-token") // Protected (returns null — CSRF is off)
    public CsrfToken getCsrfToken(HttpServletRequest req) {
        return (CsrfToken) req.getAttribute("_csrf");
    }

    @PostMapping("/students")  // Protected
    public Student addStudent(@RequestBody Student student) {
        students.add(student);
        return student;
    }
}
```
📌 In-memory list — pre-populated on startup. Not stored in database.
📌 All endpoints are **protected** — require valid JWT token.
📌 `new ArrayList<>(List.of(...))` — `List.of()` creates read-only list. Wrapping in `ArrayList` makes it modifiable (so you can add students).

---

## 🐛 Complete Bug Fix Summary

| # | File | Bug | Fix |
|---|------|-----|-----|
| 1 | `pom.xml` | Spring Boot `4.1.1` doesn't exist | Changed to `3.3.4` |
| 2 | `pom.xml` | `spring-boot-starter-webmvc` doesn't exist | Changed to `spring-boot-starter-web` |
| 3 | `pom.xml` | `spring-boot-starter-security-test` doesn't exist | Changed to `spring-security-test` |
| 4 | `pom.xml` | `spring-boot-starter-webmvc-test` doesn't exist | Changed to `spring-boot-starter-test` |
| 5 | `JwtFilter.java` | Missing `@Component` → Spring can't manage/inject it → NPE | Added `@Component` |
| 6 | `JwtFilter.java` | Syntax error: missing `)` in `if(jwtService.validateToken(...)` | Added `)` |
| 7 | `JwtFilter.java` | Wrong unused import | Removed |
| 8 | `SecurityConfig.java` | Missing `;` after `private JwtFilter jwtFilter` | Added `;` |
| 9 | `SecurityConfig.java` | `"login"` missing `/` → `/user/login` | Fixed to `"/user/login"` |
| 10 | `SecurityConfig.java` | `authenticationManager()` missing `throws Exception` | Added `throws Exception` |
| 11 | `JWTService.java` | Unreachable `return "hardcodedToken"` after `.compact()` | Removed |
| 12 | `JWTService.java` | `currentTimeMillis() * 60 * 60 * 10` — overflow, wrong time | Fixed to `+ 1000L * 60 * 60 * 10` |
| 13 | `JWTService.java` | `extractUserName()` returned `""` (hardcoded empty!) | Implemented real extraction |
| 14 | `JWTService.java` | `validateToken()` returned `true` (always — no real security!) | Implemented real validation |
| 15 | `JWTService.java` | Wrong import `org.apache.catalina.User` | Removed |
| 16 | `UserService.java` | `authenticate()` result type was `AuthenticationManager` (wrong!) | Changed to `Authentication` |
| 17 | `UserService.java` | `generateToken()` called with no argument | Added `user.getUsername()` |
| 18 | `UserPrincipal.java` | Role `"USER"` missing required `"ROLE_"` prefix | Changed to `"ROLE_USER"` |

---

## 🧪 How to Test (Postman Guide)

### Step 1: Start MySQL
```sql
CREATE DATABASE my_database;
```

### Step 2: Start the app
Console should show: `Started SpringSecurityApplication on port 8080`

### Step 3: Register
```
POST http://localhost:8080/user/register
Body (JSON): {"id": 1, "username": "sanjay", "password": "S@123"}
```

### Step 4: Login → Get JWT Token
```
POST http://localhost:8080/user/login
Body (JSON): {"username": "sanjay", "password": "S@123"}
Response: "eyJhbGciOiJIUzI1NiJ9..."  ← COPY THIS!
```

### Step 5: Use Token to Access Protected API
```
GET http://localhost:8080/students
Header → Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

### Step 6: Try Without Token → Should Get 401
```
GET http://localhost:8080/students
(no Authorization header)
→ 401 Unauthorized ← correct behaviour!
```

---

## 🔑 Key Concepts Cheat Sheet

| Concept | Simple Explanation |
|---------|-------------------|
| **JWT Token** | A signed digital pass. Login once, use everywhere for 10 hours. |
| **BCrypt** | One-way password hasher. `"1234"` → `"$2a$12$..."`. Can't reverse. |
| **JwtFilter** | Runs on every request. Reads token from header, sets auth context. |
| **SecurityContextHolder** | Spring's memory for "who is logged in right now". |
| **STATELESS** | Server remembers nothing. Client sends token every time. |
| **AuthenticationManager** | Verifies username+password. Calls UserDetailsService + BCrypt. |
| **DaoAuthenticationProvider** | Database-backed credential verifier. |
| **UserDetails / UserPrincipal** | Spring Security's standard format for user info. |
| **@Bean** | Method that creates an object Spring manages. Others can @Autowired it. |
| **@Autowired** | "Spring, give me the object I need. I don't create it." |
| **@Component** | "Spring, manage this class as a bean." Required for JwtFilter! |
| **`throws Exception`** | Required for methods that can throw checked exceptions. |
