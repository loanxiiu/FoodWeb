# Food_Web2

## Overview
Food_Web2 is a full-stack web application for online food ordering and delivery, built with Java, Spring Boot, Thymeleaf, and MySQL. The system supports both customer and admin roles, enabling users to browse food items, manage shopping carts, place orders, and make payments, while admins can manage products, brands, customers, orders, deliveries, and view financial/statistical reports.

## Features

### For Customers
- **User Registration & Login:** Secure sign-up and authentication.
- **Browse Foods:** View food items by category/brand, with images and descriptions.
- **Shopping Cart:** Add, update, or remove items from the cart.
- **Order Placement:** Place orders, view order history, and order details.
- **Payment:** Integrated payment and order confirmation.
- **Account Management:** Update personal information and view order status.

### For Admins
- **Dashboard:** View sales and revenue statistics with interactive charts.
- **Product Management:** CRUD operations for food items and brands, including image uploads.
- **Order Management:** View, update, and manage all customer orders.
- **Customer Management:** View and manage customer accounts.
- **Delivery Management:** Assign and track deliveries, manage delivery personnel.
- **Financial Reports:** Generate and view sales, customer, and product statistics.

## Tech Stack
- **Backend:** Java 17, Spring Boot, Spring Data JPA
- **Frontend:** Thymeleaf, HTML, CSS, JavaScript
- **Database:** MySQL
- **Build Tool:** Maven
- **Other:** Lombok, MapStruct

## Getting Started

### Prerequisites
- Java 17+
- Maven
- MySQL

### Setup Instructions
1. **Clone the repository:**
   ```bash
   git clone https://github.com/yourusername/Food_Web2.git
   cd Food_Web2
   ```
2. **Configure the database:**
   - Create a MySQL database named `food_web`.
   - Update `src/main/resources/application.properties` with your MySQL username and password if needed.
   - (Optional) Use `database.sql` to seed initial data.

3. **Build and run the application:**
   ```bash
   ./mvnw spring-boot:run
   ```
   The app will be available at [http://localhost:8080/food_web](http://localhost:8080/food_web)

## Usage
- **Customer:** Register or log in, browse foods, add to cart, place orders, and manage your account.
- **Admin:** Log in with an admin account to access the admin dashboard for managing products, orders, customers, deliveries, and viewing reports.

## Folder Structure
- `src/main/java/com/example/food_web2/` - Java source code (controllers, services, entities, repositories, DTOs, mappers)
- `src/main/resources/templates/` - Thymeleaf HTML templates for user and admin interfaces
- `src/main/resources/static/` - Static assets (CSS, JS, images)
- `database.sql` - Sample data for initial setup
- `pom.xml` - Maven dependencies and build configuration

## Screenshots
_Add screenshots of the main pages here (home, product list, cart, admin dashboard, etc.)_

## License
This project is for educational purposes.