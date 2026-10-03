# 🛒 E-Commerce REST API Project — Complete Beginner's Guide

> **One-line summary**: You built a REST API where users can **add products with images**, **get**, **search**, **update**, and **delete** products. Products (including their image data) are stored in a MySQL database using Spring Boot + JPA + Lombok.

---

## 📁 Project File Overview

| File | Layer | What it does |
|------|-------|--------------|
| `EcomProjApplication.java` | Entry Point | Starts the whole app |
| `pom.xml` | Config | Maven dependency list |
| `application.properties` | Config | Database connection settings |
| `Product.java` | Model | Database table blueprint (with image fields) |
| `ProductRepo.java` | Repository | Database helper + custom search query |
| `ProductService.java` | Service | Business logic for all CRUD + image handling |
| `ProductController.java` | Controller | All REST API endpoints |

---

## 🗺️ API Endpoints Reference

| Method | URL | Description |
|--------|-----|-------------|
| `GET` | `/api/` | Hello / health check |
| `GET` | `/api/products` | Get all products |
| `GET` | `/api/product/{id}` | Get one product by ID |
| `GET` | `/api/product/{id}/image` | Get product image |
| `GET` | `/api/products/search?keyword=...` | Search products |
| `POST` | `/api/product` | Add new product + image |
| `PUT` | `/api/product/{id}` | Update product + image |
| `DELETE` | `/api/product/{id}` | Delete product |

---

## 📄 File 1: `pom.xml` — The Dependency List

```xml
<parent>
    <version>3.3.4</version>  <!-- FIXED: was 4.1.1 (doesn't exist!) -->
</parent>
```
📌 **Why fixed**: Spring Boot `4.1.1` doesn't exist on Maven Central. Build failed completely.

```xml
<artifactId>spring-boot-starter-web</artifactId>
```
📌 Provides Spring MVC (controllers), Jackson (JSON ↔ Java conversion), and embedded Tomcat web server.

```xml
<artifactId>spring-boot-starter-data-jpa</artifactId>
```
📌 JPA + Hibernate. Write Java instead of SQL.
- `repo.save(product)` → generates `INSERT INTO product ...`
- `repo.findById(1)` → generates `SELECT * FROM product WHERE id = 1`

```xml
<artifactId>mysql-connector-j</artifactId>
<scope>runtime</scope>
```
📌 MySQL JDBC driver. Allows Java to talk to MySQL. `scope=runtime` = not needed at compile time, only when running.

```xml
<artifactId>lombok</artifactId>
<optional>true</optional>
```
📌 Code generator. Annotations like `@Data`, `@AllArgsConstructor` auto-generate getters/setters/constructors.
📌 `optional=true` = not bundled in final JAR (not needed at runtime, only during compilation).

```xml
<!-- spring-boot-maven-plugin FIXED: added Lombok exclusion -->
<configuration>
    <excludes>
        <exclude>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </exclude>
    </excludes>
</configuration>
```
📌 **Fixed**: Excludes Lombok from the packaged JAR. Lombok generates code at compile time — the generated code is already there. Lombok itself is not needed at runtime. Removes unnecessary bloat.

---

## 📄 File 2: `application.properties` — Settings

```properties
spring.application.name=ecom-proj
```
📌 Your app's name. Appears in logs.

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/Product
spring.datasource.username=root
spring.datasource.password=Thilagasanjay
```
📌 **Database connection**:
- `localhost` = on your own computer
- `3306` = MySQL's default port
- `Product` = the database name

> ⚠️ Run `CREATE DATABASE Product;` in MySQL first!
> ⚠️ Never put passwords in code for production — use environment variables.

```properties
spring.jpa.hibernate.ddl-auto=update
```
📌 Auto-creates/updates the `product` table based on `Product.java`. You don't write any `CREATE TABLE` SQL.

```properties
spring.jpa.show-sql=true
```
📌 Prints all SQL queries to console. Great for learning what JPA does behind the scenes.

---

## 📄 File 3: `Product.java` — The Database Table

```java
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
```
📌 `@Entity` = "Map this class to a MySQL table called `product`."
📌 `@Data` (Lombok) = **Auto-generates** all getters, setters, equals, hashCode, toString. Without Lombok you'd write ~80 lines of code!
📌 `@AllArgsConstructor` (Lombok) = Auto-generates constructor with all fields.
📌 `@NoArgsConstructor` (Lombok) = Auto-generates empty constructor. **JPA requires this** to create objects when reading from DB.

```java
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // FIXED: was missing!
    private int id;
```
📌 `@Id` = Primary key. Each product has a unique id.
📌 **FIXED** `@GeneratedValue` = MySQL auto-assigns the next id (1, 2, 3...). Without this, you must provide an id manually every time — error-prone!

```java
    private String name;
    private String description;
    private BigDecimal price;   // Use BigDecimal for money — never float/double!
    private String category;
    private String brand;
```
📌 `BigDecimal` for price = **exact decimal arithmetic**. `float 0.1 + 0.2 = 0.30000000000000004` (wrong!). BigDecimal never has rounding errors — essential for currency!

```java
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date releaseDate;   // FIXED: was "releaseData" (typo!)
```
📌 **FIXED typo**: `releaseData` → `releaseDate`. The typo gave wrong JSON keys and confusing method names (`setReleaseData` doesn't make sense!).
📌 `@JsonFormat` = Show date as `"2024-01-15"` in JSON instead of a Unix timestamp number.

```java
    private boolean available;
    private int quantity;

    private String imageName;   // e.g., "laptop.jpg"
    private String imageType;   // e.g., "image/jpeg"

    @Lob
    private byte[] imageData;   // FIXED: was "imageDate" (typo!)
```
📌 **FIXED typo**: `imageDate` → `imageData`. The typo caused compile errors (`setImageDate()` doesn't exist!).
📌 `@Lob` = "Large Object". Tells MySQL to use a `LONGBLOB` column for storing binary data (can hold up to 4GB).
📌 `byte[]` = raw binary data = your image file stored directly in the database row!

---

## 📄 File 4: `ProductRepo.java` — Database Helper

```java
@Repository
public interface ProductRepo extends JpaRepository<Product, Integer> {

    @Query("SELECT p FROM Product p WHERE " +
           "LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.brand) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.category) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Product> searchProducts(@Param("keyword") String keyword);
}
```
📌 `@Repository` = "This is a database layer class." Spring generates the implementation.
📌 `extends JpaRepository<Product, Integer>` = Get **all these free**:
| Method | SQL generated |
|--------|--------------|
| `save(product)` | `INSERT` or `UPDATE` |
| `findById(1)` | `SELECT WHERE id = 1` |
| `findAll()` | `SELECT *` |
| `deleteById(1)` | `DELETE WHERE id = 1` |

📌 **`@Query`** = Custom JPQL (JPA Query Language) — like SQL but using Java class names.

Breaking down the search query:
```
LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
```
- `LOWER()` = convert to lowercase (case-insensitive search)
- `LIKE` = pattern matching
- `%keyword%` = "contains keyword anywhere"
- `:keyword` = the parameter you pass in
- `OR` across 4 fields = search in name, description, brand, AND category

📌 `@Param("keyword")` = Maps `:keyword` in the query to the `keyword` method parameter.

---

## 📄 File 5: `ProductService.java` — Business Logic

```java
@Service
public class ProductService {

    @Autowired
    private ProductRepo repo;
```
📌 `@Service` = "Business logic layer." Spring creates and manages this.
📌 `@Autowired` = "Spring, inject ProductRepo here. I don't create it."

```java
    public Product getProductById(int id) {
        return repo.findById(id).orElse(null);  // FIXED: was .orElse(new Product())!
    }
```
📌 **FIXED**: Was `.orElse(new Product())` — when a product wasn't found, it returned an empty `Product{id=0, name=null}` instead of `null`. The controller's `if (product != null)` check never triggered! Always returned HTTP 200 with empty data.
📌 Now returns `null` if not found → controller correctly returns HTTP 404.
📌 `Optional.orElse(null)` = "Return the value if present, otherwise return null."

```java
    public Product addProduct(Product product, MultipartFile imageFile) throws IOException {
        product.setImageName(imageFile.getOriginalFilename());
        product.setImageType(imageFile.getContentType());
        product.setImageData(imageFile.getBytes());   // FIXED: was setImageDate()!
        return repo.save(product);
    }
```
📌 `MultipartFile` = an uploaded file from an HTTP multipart request.
📌 `getOriginalFilename()` = `"laptop.jpg"`
📌 `getContentType()` = `"image/jpeg"`
📌 `getBytes()` = the raw binary bytes of the image file
📌 **FIXED**: Was `setImageDate()` — typo carried from field name bug. Method doesn't exist → compile error!
📌 `throws IOException` = `getBytes()` can fail (file read error) → must declare this possibility.

```java
    public Product updateProduct(int id, Product product, MultipartFile imageFile) throws IOException {
        product.setImageData(imageFile.getBytes());  // FIXED: was setImageDate()
        product.setImageName(imageFile.getOriginalFilename());
        product.setImageType(imageFile.getContentType());
        return repo.save(product);
    }
```
📌 `repo.save(product)` → If `product.id` already exists in DB → does `UPDATE`. If not → does `INSERT`.

---

## 📄 File 6: `ProductController.java` — REST Endpoints

```java
@RestController
@CrossOrigin
@RequestMapping("/api")
public class ProductController {
```
📌 `@RestController` = Handles HTTP requests and returns JSON/text (not HTML pages).
📌 `@CrossOrigin` = Allow requests from any frontend URL (React, Angular, etc.). Without this, browser blocks cross-origin requests.
📌 `@RequestMapping("/api")` = All URLs in this class start with `/api`.

### GET All Products

```java
@GetMapping("/products")
public ResponseEntity<List<Product>> getAllProduct() {
    return new ResponseEntity<>(service.getAllProducts(), HttpStatus.OK);
}
```
📌 `ResponseEntity` = Wraps response body + HTTP status code.
📌 `HttpStatus.OK` = **200 OK** — everything went fine.

### GET Product by ID

```java
@GetMapping("/product/{id}")
public ResponseEntity<Product> getProduct(@PathVariable int id) {
    Product product = service.getProductById(id);
    if (product != null) {
        return new ResponseEntity<>(product, HttpStatus.OK);
    } else {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
```
📌 `{id}` = URL variable. `GET /api/product/5` → `id = 5`.
📌 `@PathVariable` = Extract the `{id}` value from the URL path.
📌 After fix: `getProductById()` returns `null` when not found → correctly returns **404 Not Found**.

### POST Add Product (with Image)

```java
@PostMapping("/product")
public ResponseEntity<?> addProduct(@RequestPart Product product,
                                    @RequestPart MultipartFile imageFile) {
    try {
        Product product1 = service.addProduct(product, imageFile);
        return new ResponseEntity<>(product1, HttpStatus.CREATED);
    } catch (Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
```
📌 `@RequestPart` = Read a **part** of a multipart/form-data request.
📌 Multipart = HTTP request with both JSON data AND file in the same request.
📌 Spring auto-converts the JSON part → `Product` object.
📌 Spring gives the file part as `MultipartFile`.
📌 `HttpStatus.CREATED` = **201 Created** — a new resource was created.
📌 `try/catch` = If anything fails, return **500 Internal Server Error** with the error message.

### GET Product Image

```java
@GetMapping("/product/{productId}/image")
public ResponseEntity<byte[]> getImageByProductId(@PathVariable int productId) {
    Product product = service.getProductById(productId);
    if (product == null) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);  // FIXED: null check added!
    }
    byte[] imageFile = product.getImageData();  // FIXED: was getImageDate()
    return ResponseEntity.ok()
            .contentType(MediaType.valueOf(product.getImageType()))
            .body(imageFile);
}
```
📌 **FIXED null check**: Before the fix, if product wasn't found, `product.getImageData()` would throw a `NullPointerException` → HTTP 500 crash!
📌 `MediaType.valueOf("image/jpeg")` = Sets `Content-Type: image/jpeg` header → browser knows to render it as an image.
📌 Returns raw `byte[]` — the binary image data stored in the database.

### PUT Update Product

```java
@PutMapping("/product/{id}")
public ResponseEntity<String> updateProduct(@PathVariable int id,
                                             @RequestPart Product product,
                                             @RequestPart MultipartFile imageFile) throws IOException {
```
📌 `@PutMapping` = HTTP PUT = "Replace/update an existing resource."
📌 `throws IOException` = Declared because `imageFile.getBytes()` in service can throw IOException.

### DELETE Product

```java
@DeleteMapping("/product/{id}")
public ResponseEntity<String> deleteProduct(@PathVariable int id) {
    Product product = service.getProductById(id);
    if (product != null) {
        service.deleteProduct(id);
        return new ResponseEntity<>("Deleted", HttpStatus.OK);
    } else {
        return new ResponseEntity<>("Product not Found", HttpStatus.NOT_FOUND);  // FIXED!
    }
}
```
📌 **FIXED**: Was `HttpStatus.BAD_REQUEST` (400) for "not found". That's WRONG!
- `400 Bad Request` = the CLIENT sent something invalid.
- `404 Not Found` = the RESOURCE doesn't exist.
Using the right HTTP status codes makes your API professional and easy to use.

### GET Search Products

```java
@GetMapping("/products/search")   // FIXED: was "/products" (duplicate!)
public ResponseEntity<List<Product>> searchProduct(@RequestParam String keyword) {
```
📌 **FIXED duplicate mapping**: Both `getAllProduct()` and `searchProduct()` were mapped to `@GetMapping("/products")`. Spring throws `AmbiguousHandlerMappingException` → **app can't start**!
📌 Solution: Search endpoint moved to `/products/search`.

📌 **FIXED `@RequestParam`**: Without this annotation, Spring has no idea where to find the `keyword` value. The URL `?keyword=laptop` would be ignored.
- `@RequestParam` = "Get this value from the query string (`?keyword=...`)."
- Usage: `GET /api/products/search?keyword=laptop` → `keyword = "laptop"`.

---

## 🐛 Complete Bug Fix Summary

| # | File | Bug | Fix |
|---|------|-----|-----|
| 1 | `pom.xml` | Spring Boot `4.1.1` doesn't exist | Changed to `3.3.4` |
| 2 | `pom.xml` | No Lombok exclusion in plugin | Added `<excludes>` in plugin config |
| 3 | `Product.java` | Typo: `releaseData` | Renamed to `releaseDate` |
| 4 | `Product.java` | Typo: `imageDate` | Renamed to `imageData` |
| 5 | `Product.java` | Missing `@GeneratedValue` on `@Id` | Added `@GeneratedValue(strategy = IDENTITY)` |
| 6 | `ProductService.java` | `.orElse(new Product())` → never null | Changed to `.orElse(null)` |
| 7 | `ProductService.java` | `setImageDate()` called (typo) | Changed to `setImageData()` |
| 8 | `ProductController.java` | Duplicate `@GetMapping("/products")` | Search moved to `/products/search` |
| 9 | `ProductController.java` | `keyword` param has no annotation | Added `@RequestParam` |
| 10 | `ProductController.java` | No null check before `getImageData()` → NPE | Added null check, return 404 |
| 11 | `ProductController.java` | `BAD_REQUEST` (400) for not found | Changed to `NOT_FOUND` (404) |
| 12 | `ProductController.java` | `getImageDate()` called (typo) | Changed to `getImageData()` |

---

## 📦 How Image Storage Works

```
ADD PRODUCT request (multipart):
│
├── Part 1: product (JSON)
│   {"name": "Laptop", "price": 75000, ...}
│   → Spring parses → Product Java object
│
└── Part 2: imageFile (Binary)
    [raw bytes of laptop.jpg]
    → Spring gives as MultipartFile

ProductService.addProduct():
    product.setImageName("laptop.jpg")     → stores filename
    product.setImageType("image/jpeg")     → stores MIME type
    product.setImageData([...bytes...])    → stores raw image bytes

MySQL product table row:
    id | name   | price  | image_name  | image_type  | image_data
    1  | Laptop | 75000  | laptop.jpg  | image/jpeg  | [binary...]


GET IMAGE request:
    GET /api/product/1/image
    → Load product from DB
    → Read image_data column (binary bytes)
    → Send bytes with Content-Type: image/jpeg header
    → Browser renders the image!
```

---

## 🧱 Architecture Overview

```
ProductController  (Web Layer — handles HTTP)
       ↓
ProductService    (Business Layer — logic, image handling)
       ↓
ProductRepo       (Data Layer — database operations)
       ↓
MySQL Database    (product table)
```

> ✅ **Always follow this layered pattern!** Controllers should NEVER directly call the repository. The Service layer is where you add validation, business rules, and data transformation.

---

## 🧪 Testing with Postman

### 1. Start MySQL
```sql
CREATE DATABASE Product;
```

### 2. Start the app
Console: `Started EcomProjApplication on port 8080`

### 3. Add a product
```
Method: POST
URL: http://localhost:8080/api/product
Body: form-data
  product (Text): {"name":"Gaming Laptop","price":75000.00,"brand":"Dell","category":"Electronics","quantity":5,"available":true,"description":"Fast laptop","releaseDate":"2024-01-15"}
  imageFile (File): [select any .jpg file]
```

### 4. Get all products
```
GET http://localhost:8080/api/products
```

### 5. Get product image
```
GET http://localhost:8080/api/product/1/image
(Paste URL in browser to see the image directly!)
```

### 6. Search products
```
GET http://localhost:8080/api/products/search?keyword=laptop
```

### 7. Delete product
```
DELETE http://localhost:8080/api/product/1
```

---

## 🔑 Key Concepts Cheat Sheet

| Concept | Simple Explanation |
|---------|-------------------|
| `@Entity` | Maps Java class to MySQL table |
| `@Id + @GeneratedValue` | Primary key that MySQL auto-increments |
| `@Data` (Lombok) | Auto-generates all getters/setters/toString |
| `JpaRepository` | Free database operations (save, find, delete, etc.) |
| `@Query` | Custom JPQL query for complex searches |
| `MultipartFile` | Represents an uploaded file in HTTP request |
| `byte[] imageData + @Lob` | Store image as binary in MySQL LONGBLOB |
| `ResponseEntity` | Wraps response with both body + HTTP status code |
| `@PathVariable` | Reads value from URL path: `/product/{id}` |
| `@RequestParam` | Reads value from query string: `?keyword=...` |
| `@RequestPart` | Reads part of multipart/form-data (JSON + file) |
| `BigDecimal` | Exact decimal math for prices (no rounding errors!) |
| `Optional.orElse(null)` | Return value if present, null if not found |
