# Library Management System - REST API

A beginner-friendly **Spring Boot 3** REST API that connects to an existing **SQLite** database (`libms.db`).

## Tech Stack
- Java 17+ (built here with Java 24)
- Spring Boot 3.4 (Web + Data JPA)
- SQLite (via `sqlite-jdbc`) with a custom Hibernate dialect
- Maven Wrapper (no global Maven install needed)

## Project Structure
```
src/main/java/com/library/
├── LibraryApplication.java      # main entry point
├── entity/                      # JPA entities (Book, User, Issued, Fine)
├── repository/                  # Spring Data repositories
├── service/                     # business logic (including fine calculation)
├── controller/                  # REST controllers
├── config/SQLiteDialect.java    # Hibernate 6 dialect for SQLite
├── dto/IssueRequest.java        # request body for issuing a book
└── exception/                   # global exception handler
src/main/resources/application.properties
```

## How to Run
The app expects the SQLite file at `../libms.db` (relative to the project root).
With the project located at `~/library-management-system` and the DB at `~/libms.db`, this already works.

```bash
# build
./mvnw clean package

# run
java -jar target/library-management-system-1.0.0.jar
```

The server starts on **http://localhost:8080**.

## Database Tables
- `books`   (id, name, author, isbn)
- `users`   (id, name, user_type, emp_roll_no)
- `issued`  (id, book_id, user_id, issue_date, return_date, status, fine_amount)
- `fine`    (issued_id, allowed_days, fine_per_day, fine_amount)

## API Endpoints

### Books — `/api/books`
| Method | Path      | Description              |
|--------|-----------|--------------------------|
| GET    | `/api/books`     | List all books           |
| GET    | `/api/books/{id}`| Get a book by id         |
| POST   | `/api/books`     | Add a book (unique ISBN) |
| PUT    | `/api/books/{id}`| Update a book            |
| DELETE | `/api/books/{id}`| Delete a book            |

### Users — `/api/users`
| Method | Path      | Description              |
|--------|-----------|--------------------------|
| GET    | `/api/users`      | List all users           |
| GET    | `/api/users/{id}` | Get a user by id         |
| POST   | `/api/users`      | Add a user (unique emp/roll no) |
| PUT    | `/api/users/{id}` | Update a user            |
| DELETE | `/api/users/{id}` | Delete a user            |

### Issued (issue/return) — `/api/issued`
| Method | Path                       | Description                          |
|--------|----------------------------|--------------------------------------|
| GET    | `/api/issued`              | List all issue records               |
| GET    | `/api/issued/{id}`         | Get an issue record by id            |
| GET    | `/api/issued/user/{userId}`| Get all issues for a user            |
| POST   | `/api/issued/issue`        | Issue a book (creates a fine record) |
| PUT    | `/api/issued/return/{issuedId}` | Return a book (computes fine)    |

### Fines — `/api/fines`
| Method | Path                   | Description                    |
|--------|------------------------|--------------------------------|
| GET    | `/api/fines`              | List all fine records       |
| GET    | `/api/fines/{issuedId}`   | Get fine for an issue record|

## Examples

Issue a book:
```bash
curl -X POST http://localhost:8080/api/issued/issue \
  -H "Content-Type: application/json" \
  -d '{"bookId": 1, "userId": 1, "allowedDays": 10}'
```
`allowedDays` is optional and defaults to 10.

Return a book (fine is computed automatically if the book is held past `allowed_days`):
```bash
curl -X PUT http://localhost:8080/api/issued/return/1
```

## Notes
- `ddl-auto` is set to `none` so Hibernate never alters your existing tables.
- The fine is `(days_held - allowed_days) * fine_per_day` when the book is overdue, otherwise `0`.
- The custom `SQLiteDialect` maps SQLite types and overrides the limit handler (`limit ?`) because SQLite does not support the `FETCH FIRST` clause that Hibernate generates by default.
