# Book Management Application

This application allows users to manage a collection of books through a web interface. Users can perform CRUD operations (Create, Read, Update, Delete) and search for books by different criteria (title, author, genre). The backend is powered by Spring Boot, while the frontend is implemented using Thymeleaf.

The system differentiates between **Admin** and **Regular User** roles:

- **Admin**: Can perform CRUD operations (Create, Read, Update, Delete).
- **Regular User**: Can only view and search books.

## Features

- Add new books to the collection (Admin only)
- View a list of all books
- Edit and update book details (Admin only)
- Delete books (Admin only)
- Search books by title, author, or genre
- Pagination and sorting support

## Technologies Used

- **Backend**: Spring Boot (Java)
- **Frontend**: Thymeleaf (HTML templates)
- **Database**: PostgreSQL (using Docker with docker-compose.yml)
- **Build Tool**: Gradle
- **API Communication**: REST (GET, POST, PUT, DELETE)
- **Documentation**: Swagger UI

## Prerequisites

Ensure you have the following installed:

- Java 17+
- Gradle
- Docker (for database and application containers)

## Getting Started

### 1. Clone the Repository
```bash
git clone <repository-url>
cd bookstore
```

### 2. Run with Docker
Ensure Docker is installed and running. The application and database can be started with:

```bash
docker-compose up --build
```

By default, the application will be available at: `http://localhost:8080`

## REST API Endpoints

### 1. Book Operations

- **GET** `/books` - Retrieve all books
- **GET** `/books/{id}` - Retrieve a book by ID
- **POST** `/books` - Create a new book (Admin only)
- **PUT** `/books/{id}` - Update a book (Admin only)
- **DELETE** `/books/{id}` - Delete a book (Admin only)

### 2. Search Books

- **GET** `/books/search`

Parameters:

| Parameter  | Type   | Description                                      |
|------------|--------|--------------------------------------------------|
| `searchBy` | String | Search field (title, author, genre)              |
| `query`    | String | Search term                                      |
| `page`     | Int    | Page number (optional, default: 0)               |
| `size`     | Int    | Items per page (optional, default: 10)           |
| `sortBy`   | String | Sort field (optional, default: title) (optional) |
| `direction`| String | Sort direction (ASC or DESC) (optional)          |

Example:

```bash
GET http://localhost:8080/books/search?searchBy=title&query=Java&page=0&size=10
```

## Testing with Postman

Application was tested with Postman.

### Accessing the Book Management UI

- **Admin Actions**: Create, edit, delete, view, search books.
- **Regular User Actions**: View and search books.

Visit the following URLs:

- View all books: `http://localhost:8080/books-web/view-all`
- Add a book (Admin only): `http://localhost:8080/books-web/create`

### Documentation javadoc: 

Generate documentation in Gradle:

```bash
gradle javadoc
```

Documentation at bookstore/build/docs/javadoc/index.html