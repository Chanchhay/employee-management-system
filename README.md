# Advanced Employee Management System

A robust, console-based Employee Management System built with Java. This application follows Clean Architecture principles (MVC + Repository + Service layers) and utilizes standard JDBC to interact with a PostgreSQL database. It features role-based access control, interactive console UI using text tables, and robust input validation.

## Features

* **Role-Based Access Control (RBAC):** Distinct dashboards and permissions for `ADMIN` and `STAFF` roles.
* **Secure Authentication:** Login system to verify employee credentials and active status.
* **Pagination:** Efficiently view large directories of employees without overwhelming the console.
* **Formatted Console UI:** Utilizes `text-table-formatter` for clean, readable horizontal and vertical data displays.
* **Robust Data Validation:** Strict Regex checks for emails, phone numbers, and dates before database insertion.
* **Advanced Data Types:** Uses `BigDecimal` for precise financial calculations and `LocalDateTime` for system auditing.

---

## Prerequisites

* **Java Development Kit (JDK):** Version 17 or higher (utilizes `record` types and enhanced `switch` expressions).
* **PostgreSQL:** Version 12 or higher.
* **Build Tool:** Gradle or Maven (to manage dependencies).

## Dependencies

Add the following dependency to your `build.gradle` for the console UI formatting:

```groovy
dependencies {
    // Other dependencies...
    implementation 'org.nocrala.tools.texttablefmt:text-table-formatter:1.2.4'
    
    // Lombok (Required for the Entity models)
    compileOnly 'org.projectlombok:lombok:1.18.30'
    annotationProcessor 'org.projectlombok:lombok:1.18.30'
    
    // PostgreSQL JDBC Driver
    implementation 'org.postgresql:postgresql:42.6.0'
}

```

---

## Database Setup

1. Open your PostgreSQL command line or graphical tool (e.g., pgAdmin, DBeaver).
2. Create a new database named `employee_management_system`.
3. Run the following SQL script to create the necessary table and insert the initial mock data:

```sql
DROP TABLE IF EXISTS employee;

CREATE TABLE employee (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20),
    department VARCHAR(50),
    job_position VARCHAR(50),
    salary NUMERIC(12, 2) NOT NULL,
    hire_date DATE NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    role VARCHAR(20) DEFAULT 'STAFF',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert Initial Mock Data
INSERT INTO employee (first_name, last_name, email, password, phone_number, department, job_position, salary, hire_date, is_active, role) 
VALUES 
('System', 'Admin', 'admin@company.com', 'admin123', '+1-000-0000', 'IT', 'Administrator', 150000.00, '2020-01-01', true, 'ADMIN'),
('Alice', 'Smith', 'alice@company.com', 'staff123', '+1-555-0101', 'Engineering', 'Developer', 125000.00, '2021-03-15', true, 'STAFF'),
('Bob', 'Johnson', 'bob@company.com', 'staff123', '+1-555-0102', 'HR', 'Manager', 85000.00, '2020-11-01', true, 'STAFF'),
('Charlie', 'Davis', 'charlie@company.com', 'staff123', '+1-555-0103', 'Engineering', 'DevOps', 110000.00, '2022-06-10', true, 'STAFF'),
('Diana', 'Prince', 'diana@company.com', 'staff123', '+1-555-0104', 'Sales', 'Executive', 95000.00, '2023-01-20', true, 'STAFF'),
('Evan', 'Wright', 'evan@company.com', 'staff123', '+1-555-0105', 'Marketing', 'Strategist', 78000.00, '2023-08-05', true, 'STAFF'),
('Frank', 'Castle', 'frank@company.com', 'staff123', '+1-555-0106', 'Security', 'Guard', 65000.00, '2023-09-10', true, 'STAFF'),
('Grace', 'Hopper', 'grace@company.com', 'staff123', '+1-555-0107', 'IT', 'Engineer', 145000.00, '2021-02-12', true, 'STAFF');

```

---

## Configuration

Before running the application, ensure your database connection details are correct.

Navigate to `src/main/java/config/DatabaseConfig.java` and update the `URL`, `USER`, and `PASSWORD` constants to match your local PostgreSQL setup:

```java
public class DatabaseConfig {
    private static final String URL = "jdbc:postgresql://localhost:5432/employee_management_system";
    private static final String USER = "postgres";
    private static final String PASSWORD = "your_actual_password_here";
    // ...
}

```

---

## Usage

Compile and run the `App.java` file. Upon launching, you will be greeted with the login screen. You can use the mock data provided above to access the system.

### Default Credentials

**Admin Access:**

* **Email:** `admin@company.com`
* **Password:** `admin123`

**Staff Access:**

* **Email:** `alice@company.com` (or any other staff email from the mock data)
* **Password:** `staff123`

---

## Project Architecture

The project is strictly separated into logical layers to maintain modularity and ease of testing:

* **`config/`**: Contains database connection initialization logic.
* **`controller/`**: Orchestrates data flow between the view and the service layers.
* **`dto/`**: Data Transfer Objects (`records`) used to pass data safely without exposing database entities.
* **`exceptions/`**: Custom runtime exceptions for business logic errors.
* **`mapper/`**: Handles the conversion logic between Entities and DTOs.
* **`model/`**: Contains the core database Entities and Enums.
* **`repository/`**: Handles all direct JDBC SQL executions and database mapping.
* **`service/`**: Interfaces and Implementations containing the core business logic, validations, and pagination math.
* **`view/`**: Manages all standard I/O, console text tables, and regex validations.
