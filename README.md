# ShareMate

**Sharing expenses made easy.**

ShareMate is a comprehensive expense-sharing application built with Spring Boot that allows users to track, split, and settle expenses with friends and groups. The application features AI-powered expense categorization, real-time balance tracking, and support for multiple split types.

## Table of Contents

- [Features](#features)
- [Technology Stack](#technology-stack)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [Configuration](#configuration)
- [API Endpoints](#api-endpoints)
- [Database Schema](#database-schema)
- [Docker Deployment](#docker-deployment)
- [Project Structure](#project-structure)
- [Contributing](#contributing)

## Features

### User Management
- User registration and authentication using JWT
- Email verification system
- User preferences management
- Push notification support (Expo)

### Expense Management
- Add, edit, and delete expenses
- Multiple split types:
  - **Equal**: Split equally among participants
  - **Exact**: Specify exact amounts for each participant
  - **Percentage**: Split based on percentages
  - **Shares**: Split based on share ratios
- AI-powered expense categorization using HuggingFace API
- Expense categories: Food & Dining, Housing & Utilities, Transportation, Entertainment, Shopping, Health & Fitness, Travel, Education, Loans & Debts, Miscellaneous
- Expense settlement tracking

### Group Management
- Create and manage groups
- Add members to groups
- Track group expenses
- View group members

### Friend Management
- Send and accept friend requests
- Search for friends
- Track balances with friends
- Settle expenses with friends

### Balance Tracking
- Real-time balance calculations
- View balances with all friends and groups
- Track who owes whom

### Additional Features
- Excel export functionality
- Email notifications
- RESTful API architecture
- Secure authentication and authorization

## Technology Stack

- **Java**: 21
- **Framework**: Spring Boot 3.4.3
- **Database**: MySQL
- **Security**: Spring Security with JWT (JSON Web Tokens)
- **ORM**: Spring Data JPA / Hibernate
- **Build Tool**: Maven
- **Additional Libraries**:
  - Apache POI (Excel file generation)
  - MapStruct (DTO mapping)
  - Lombok (boilerplate reduction)
  - Expo Push Notifications
  - JJWT (JWT handling)
  - HuggingFace API (AI categorization)

## Prerequisites

Before running the application, ensure you have the following installed:

- **Java 21** or higher
- **Maven 3.9+**
- **MySQL 8.0+** (or compatible database)
- **Docker** (optional, for containerized deployment)

## Getting Started

### 1. Clone the Repository

```bash
git clone <repository-url>
cd sharemate
```

### 2. Database Setup

Create a MySQL database:

```sql
CREATE DATABASE sharemate;
```

### 3. Configure Application Properties

Set the following environment variables or update `src/main/resources/application.properties`:

```properties
# Database Configuration
DB_URL=jdbc:mysql://localhost:3306/sharemate
DB_USERNAME=your_username
DB_PASSWORD=your_password

# Email Configuration (Gmail SMTP)
MAIL_USERNAME=your_email@gmail.com
MAIL_PASSWORD=your_app_password

# HuggingFace API
HUGGINGFACE_API_KEY=your_huggingface_api_key
```

**Note**: For Gmail, you'll need to generate an [App Password](https://support.google.com/accounts/answer/185833) instead of using your regular password.

### 4. Build the Project

```bash
mvn clean install
```

### 5. Run the Application

```bash
mvn spring-boot:run
```

Or using the Maven wrapper:

```bash
./mvnw spring-boot:run  # Linux/Mac
mvnw.cmd spring-boot:run  # Windows
```

The application will start on port **8081** (as configured in `application.properties`).

## Configuration

### Application Properties

Key configuration options in `src/main/resources/application.properties`:

- `server.port`: Application server port (default: 8081)
- `spring.jpa.hibernate.ddl-auto`: Database schema management (update/create-drop)
- `spring.jpa.show-sql`: Enable SQL query logging

### Security Configuration

The application uses JWT-based authentication. JWT tokens are issued upon successful login and must be included in subsequent API requests in the Authorization header:

```
Authorization: Bearer <your_jwt_token>
```

## API Endpoints

### Authentication
- `POST /api/register` - Register a new user
- `POST /api/login` - Login and receive JWT token
- `GET /api/emailexists/{email}` - Check if email exists
- `GET /api/usernameexists/{username}` - Check if username exists

### User Management
- `POST /api/updateUser` - Update user details
- `GET /api/getUserPreferences/{userId}` - Get user preferences
- `POST /api/updateUserPreferences/{userId}` - Update user preferences
- `POST /api/sendVerificationEmail/{userId}` - Send verification email
- `POST /api/verifyEmail/` - Verify email with code
- `POST /api/updateExpoToken` - Update Expo push notification token

### Expense Management
- `GET /api/expenses?userId={userId}` - Get all expenses for a user
- `POST /api/addExpenses` - Add a new expense
- `POST /api/editExpenses` - Edit an existing expense
- `DELETE /api/deleteExpense/{expenseId}` - Delete an expense
- `POST /api/settleExpense/` - Settle an expense

### Group Management
- `GET /api/getGroups/{userId}` - Get all groups for a user
- `POST /api/createGroup` - Create a new group
- `POST /api/addMembersToGroup` - Add members to a group
- `POST /api/updateGroup` - Update group details
- `GET /api/getGroupMembers/{groupId}` - Get group members
- `GET /api/getGroupExpenses/{groupId}` - Get group expenses

### Friend Management
- `GET /api/getFriends/{userId}` - Get all friends
- `GET /api/getFriendRequestList/{userId}` - Get pending friend requests
- `POST /api/addFriend/` - Send a friend request
- `POST /api/updateFriendRequest/` - Accept/reject friend request
- `GET /api/searchFriends/{userId}/{searchQuery}` - Search for friends
- `POST /api/settleFriendExpenses/` - Settle all expenses with a friend

### Balance Management
- `GET /api/getBalance/{userId}` - Get all balances for a user

### Utilities
- `GET /api/keepalive` - Health check and Excel download test

## Database Schema

The application uses the following main entities:

- **User**: User accounts and profiles
- **Expense**: Expense records
- **ExpenseSplit**: Individual splits within an expense
- **Group**: Expense groups
- **GroupMember**: Members of groups
- **Friend**: Friend relationships
- **Balance**: Balance calculations between users
- **Payment**: Payment/settlement records
- **EmailVerification**: Email verification tokens
- **Preference**: User preferences
- **Notification**: Notification records
- **ExponentPushToken**: Push notification tokens

## Docker Deployment

A Dockerfile is included for containerized deployment:

```bash
# Build the Docker image
docker build -t sharemate:latest .

# Run the container
docker run -p 8081:8080 \
  -e DB_URL=jdbc:mysql://host.docker.internal:3306/sharemate \
  -e DB_USERNAME=your_username \
  -e DB_PASSWORD=your_password \
  -e MAIL_USERNAME=your_email@gmail.com \
  -e MAIL_PASSWORD=your_app_password \
  -e HUGGINGFACE_API_KEY=your_api_key \
  sharemate:latest
```

**Note**: The Dockerfile exposes port 8080, but the application runs on 8081 by default. Adjust the port mapping or configuration as needed.

## Project Structure

```
sharemate/
├── src/
│   ├── main/
│   │   ├── java/dev/pawan/sharemate/
│   │   │   ├── config/          # Security and configuration classes
│   │   │   ├── controller/      # REST API controllers
│   │   │   ├── enums/           # Enumeration types
│   │   │   ├── exception/       # Exception handlers
│   │   │   ├── mapper/          # MapStruct mappers
│   │   │   ├── model/           # Entity classes
│   │   │   ├── repository/      # JPA repositories
│   │   │   ├── request/         # Request DTOs
│   │   │   ├── response/        # Response DTOs
│   │   │   ├── service/         # Business logic services
│   │   │   └── util/            # Utility classes
│   │   └── resources/
│   │       ├── application.properties
│   │       └── templates/       # Email templates
│   └── test/                    # Test classes
├── pom.xml                      # Maven dependencies
├── DockerFile                   # Docker configuration
└── README.md                    # This file
```

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License (or specify your license).

## Support

For support, email [your-email@example.com] or open an issue in the repository.

---

**Note**: Remember to never commit sensitive information like API keys, passwords, or database credentials to version control. Always use environment variables or secure configuration management.

