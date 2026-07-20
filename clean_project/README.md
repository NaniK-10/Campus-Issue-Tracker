# Campus Issue and Tracking System

A comprehensive web application designed to streamline the reporting, tracking, and resolution of campus-related issues. This system connects students with administration to ensure efficient handling of maintenance, academic, and facility concerns.

## 🚀 Features

### 👤 User Roles & Authentication
- **Secure Signup**: Email verification via OTP (One-Time Password) to ensure valid users.
- **Roles**:
  - **Student**: Can report issues, track status, and provide feedback.
  - **Staff/Admin**: Can view all issues, update status, and manage the dashboard.
- **Authentication**: JWT (JSON Web Token) based auth stored securely in HttpOnly cookies.

### 📝 Issue Reporting
- **Categorized Reporting**: Specialized forms for different issue types:
  - 🎓 **Academic**: Course units, scheduling, etc.
  - 🏢 **Hostel**: Block and room maintenance.
  - 💳 **Payments**: Transaction disputes and inquiries.
  - 🛠️ **Other**: General maintenance or other concerns.
- **Evidence**: Support for image attachments to provide context.
- **Geolocation**: Automatically captures or allows manual entry of issue location (Latitude/Longitude).
- **Anonymous Reporting**: Option for students to report issues without revealing their identity.
- **Duplicate Detection**: Smart detection to warn users if a similar issue has already been reported.

### 📊 Management & Tracking
- **Dashboard**: Centralized view for staff to monitor incoming issues with pagination.
- **Status Workflow**: Track issues through various stages (Pending, In Progress, Resolved, Rejected).
- **Auto-Escalation**: System automatically tracks escalation levels for unresolved issues over time.
- **Feedback Loop**: Students can rate and review the handling of their issues after resolution.

### 🌍 Internationalization
- **Multi-language Support**: Configured to support English, Sinhala, and Tamil.

## 🛠️ Technology Stack

- **Backend**: Java, Spring Boot 3.3.3
- **Security**: Spring Security, JWT
- **Database**: MySQL (Production), H2 (Testing)
- **Frontend**: Thymeleaf, Bootstrap, JavaScript
- **Build Tool**: Maven
- **Other**: Java Mail Sender (for OTPs), Lombok

## 📋 Prerequisites

- **Java JDK**: Version 17 or higher.
- **Maven**: For dependency management and building.
- **MySQL**: Local or remote instance.

## ⚙️ Installation & Setup

1.  **Clone the Repository**
    ```bash
    git clone https://github.com/your-username/Campus-Issue-and-Tracking-System.git
    cd Campus-Issue-and-Tracking-System
    ```

2.  **Configure Database**
    - Create a MySQL database named `campus_issue_db`.
    - Open `src/main/resources/application.properties` and update your database credentials:
      ```properties
      spring.datasource.url=jdbc:mysql://localhost:3306/campus_issue_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
      spring.datasource.username=YOUR_DB_USERNAME
      spring.datasource.password=YOUR_DB_PASSWORD
      ```

3.  **Configure Email (Optional for OTP)**
    - To enable OTP emails, update the mail settings in `application.properties`:
      ```properties
      spring.mail.username=YOUR_EMAIL@gmail.com
      spring.mail.password=YOUR_APP_PASSWORD
      ```

4.  **Run the Application**
    ```bash
    mvn spring-boot:run
    ```

5.  **Access the Application**
    - Open your browser and navigate to: `http://localhost:8080`

## 📂 Project Structure

```
src/main/java/com/campus/issue_tracker/
├── controller/       # REST and View controllers (Endpoints)
├── dto/              # Data Transfer Objects
├── entity/           # JPA Entities (Database Models)
├── repository/       # Data Access Layer
├── service/          # Business Logic
├── security/         # Auth configuration & JWT utilities
└── config/           # App configurations
```

## 🤝 Contributing

1.  Fork the project.
2.  Create your feature branch (`git checkout -b feature/AmazingFeature`).
3.  Commit your changes (`git commit -m 'Add some AmazingFeature'`).
4.  Push to the branch (`git push origin feature/AmazingFeature`).
5.  Open a Pull Request.

## 📄 License

Distributed under the MIT License.
