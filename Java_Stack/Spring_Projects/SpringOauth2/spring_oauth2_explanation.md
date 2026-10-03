# 🔑 Spring Security OAuth2 Project — Complete Beginner's Guide

> **One-line summary**: You built an app that uses **GitHub for Login**. Instead of creating your own database table for users and passwords, you redirect users to GitHub. GitHub authenticates them, asks for their permission, and sends them back to your app with their profile data.

---

## 📁 Project File Overview

| File | Layer | What it does |
|------|-------|--------------|
| `SpringOauth2Application.java` | Entry Point | Starts the whole app |
| `pom.xml` | Config | Maven dependency list (includes `oauth2-client`) |
| `application.properties` | Config | Your GitHub Client ID & Secret |
| `SecurityConfig.java` | Security | Turns on OAuth2 login for all URLs |
| `HelloController.java` | Controller | Reads the GitHub profile and says Welcome! |

---

## 🔄 The OAuth2 Flow (What happens behind the scenes)

```
1. User visits GET / 
   → Spring Security blocks it: "You are not logged in!"
   
2. Spring redirects user to: /oauth2/authorization/github
   → This triggers the Spring OAuth2 Client.

3. Spring redirects user to github.com
   → User logs in at GitHub and clicks "Authorize".

4. GitHub redirects user BACK to your app: /login/oauth2/code/github?code=XYZ
   → Spring automatically takes this code.

5. Spring calls GitHub API (in background)
   → Swaps the code for an Access Token.

6. Spring calls GitHub API again
   → Uses the Access Token to get User Profile (name, email).

7. Spring logs the user in!
   → Calls your HelloController!
```
> 🎉 **The best part**: Steps 2, 3, 4, 5, 6, and 7 are **COMPLETELY AUTOMATIC**! Spring Boot handles all the complex OAuth2 logic for you.

---

## 📄 File 1: `pom.xml` — The Dependencies

```xml
<parent>
    <version>3.3.4</version>  <!-- FIXED: was 4.1.1 (doesn't exist!) -->
</parent>
```
📌 **Why fixed**: Spring Boot `4.1.1` doesn't exist on Maven Central.

```xml
<artifactId>spring-boot-starter-web</artifactId>
```
📌 Provides Spring MVC and the embedded Tomcat server.

```xml
<artifactId>spring-boot-starter-oauth2-client</artifactId>  <!-- FIXED! -->
```
📌 **FIXED typo**: Was `spring-boot-starter-outh2-client` (missing 'a').
📌 This is the **most important dependency** in the project! It brings in all the automatic OAuth2 redirection and token-fetching logic.

---

## 📄 File 2: `application.properties` — GitHub Keys

```properties
spring.security.oauth2.client.registration.github.client-id=YOUR_CLIENT_ID
spring.security.oauth2.client.registration.github.client-secret=YOUR_CLIENT_SECRET
```
📌 This tells Spring Boot: "I want to enable GitHub Login, and here are my app's keys."
📌 **FIXED**: Changed `gitHub` to `github`. Spring Boot looks for lowercase names (`github`, `google`, `facebook`) to automatically configure the endpoints. If you use `gitHub`, Spring thinks it's a custom provider and asks for more configuration!

> ⚠️ **How to get these keys:**
> Go to GitHub.com → Settings → Developer Settings → OAuth Apps → New OAuth App.
> Set Homepage to `http://localhost:8080`.
> Set Callback URL to `http://localhost:8080/login/oauth2/code/github`.

---

## 📄 File 3: `SecurityConfig.java` — The Rulebook

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                .anyRequest().authenticated()
            )
            .oauth2Login(Customizer.withDefaults());
            
        return http.build();
    }
}
```
📌 `@Configuration` & `@EnableWebSecurity` = "Turn on Spring Security."
📌 `anyRequest().authenticated()` = "Every URL requires the user to be logged in."
📌 **`.oauth2Login(Customizer.withDefaults())`** = "Instead of a username/password form, use OAuth2 for login!" This single line activates all the magic.

---

## 📄 File 4: `HelloController.java` — The Welcome Desk

```java
@RestController
public class HelloController {
    
    @GetMapping("/")
    public String greet(OAuth2AuthenticationToken token) {
```
📌 `@RestController` = "Handle HTTP requests and return plain text/JSON."
📌 `OAuth2AuthenticationToken` = **Spring automatically injects this!** It holds all the profile data returned by GitHub.

```java
        if (token == null) {
            return "Welcome to Telusko, Guest!";
        }
```
📌 **FIXED**: Added a null check. If security is disabled later, this prevents a `NullPointerException` crash.

```java
        String name = token.getPrincipal().getAttribute("name");
        String login = token.getPrincipal().getAttribute("login");
        
        return "Welcome to Telusko, " + (name != null ? name : login) + "!";
    }
}
```
📌 `getAttribute("name")` = The user's real name from their GitHub profile.
📌 `getAttribute("login")` = The user's GitHub username.
📌 `(name != null ? name : login)` = Fallback. If they haven't set a public Real Name, show their Username instead.

---

## 🐛 Complete Bug Fix Summary

| # | File | Bug | Fix |
|---|------|-----|-----|
| 1 | `pom.xml` | Spring Boot `4.1.1` doesn't exist | Changed to `3.3.4` |
| 2 | `pom.xml` | Typo: `outh2-client` (missing 'a') | Fixed to `oauth2-client` |
| 3 | `pom.xml` | `spring-boot-starter-webmvc` doesn't exist | Changed to `spring-boot-starter-web` |
| 4 | `pom.xml` | `spring-boot-starter-security-test` doesn't exist | Changed to `spring-boot-starter-test` |
| 5 | `application.properties` | Capital 'H' in `gitHub` breaks auto-config | Lowercased to `github` |
| 6 | `HelloController.java` | Directly printing `token` object (risk of NPE) | Extracted name/login cleanly with null checks |

---

## 🧪 How to Test (Step-by-Step)

1. Get your `Client ID` and `Client Secret` from GitHub (see instructions above).
2. Paste them into `application.properties`.
3. Start the Spring Boot app.
4. Open your browser in **Incognito Mode** (so you aren't already logged into GitHub).
5. Visit `http://localhost:8080/`.
6. You will be redirected to the GitHub Login Page.
7. Log in with your GitHub account.
8. Click the green "Authorize" button.
9. You will be redirected back to `http://localhost:8080/`.
10. The page will say: `Welcome to Telusko, [Your Name]!` 🎉
