# 1. Project Name and Objective

## Project Name
**Markme Attendance System**

## Project Purpose and Objectives
The primary purpose of the Markme Attendance System is to streamline the process of attendance recording and management within academic institutions. This system aims to:


- **Enhance Accessibility for Users**: Provide an easy-to-use interface for both instructors and students to interact with their attendance data.
- **Support Real-Time Updates and Notifications**: Keep students informed about their attendance status and any related updates through immediate notifications.

## Problem Solution and Value Proposition
The Markme Attendance System addresses several key issues prevalent in traditional academic attendance management:


- **Error Minimization**: Digital tracking reduces the likelihood of human error in attendance recording, ensuring more accurate attendance records.
- **Improved Engagement and Communication**: With features like real-time notifications, the system fosters better communication between instructors and students, potentially improving course attendance rates and student engagement.

## Target Audience
The primary users of the Markme Attendance System are:

- **Educational Institutions**: Universities, colleges, and schools looking to modernize and streamline their attendance systems.
- **Professors and Academic Staff**: Individuals who require an efficient method to manage attendance for multiple classes and sections.
- **Students**: Beneficiaries of a more transparent and accessible way to keep track of their attendance and related notifications.

This system is designed to offer a comprehensive solution that not only simplifies attendance management but also enhances the educational experience through technological innovation.
# 2. Setting Up the Project Locally

This section provides a comprehensive guide to setting up the Markme Attendance System on a local development machine. Follow these steps carefully to ensure a successful setup.

## Software Requirements
Before initiating the setup, ensure the following software requirements are met:

- **Java**: JDK 17 or newer (for backend services)
- **Spring Boot**: Version 2.3.1.RELEASE or newer
- **Maven**: To manage project dependencies and builds
- **MySQL**: For the database
- **Git**: For version control
- **Integrated Development Environment (IDE)**: IntelliJ IDEA or Eclipse (with Spring Boot plugin)

## Prerequisites
1. **Install Java Development Kit (JDK)**: Download and install JDK 17 from the [Oracle website](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html).
2. **Install Maven**: Follow the instructions on the [Apache Maven Project website](https://maven.apache.org/install.html).
3. **Setup MySQL Database**:
   - Install MySQL from [MySQL Downloads](https://dev.mysql.com/downloads/mysql/).
   - Create a new database named `markme`.
   - Ensure the database is running and accessible.
4. **Install Git**: Download and install Git from [Git Downloads](https://git-scm.com/downloads).
5. **IDE Setup**: Install either IntelliJ IDEA or Eclipse and configure it to support Spring Boot applications.

## Environment Configuration
Configure the application properties to connect to your local MySQL instance by modifying the `src/main/resources/application.properties` file:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/markme?useSSL=false&serverTimezone=UTC
spring.datasource.username=yourDatabaseUsername
spring.datasource.password=yourDatabasePassword
```

## Step-by-Step Setup Instructions
1. **Clone the Repository**:
   - Open a terminal or command prompt.
   - Navigate to the directory where you want to store the project.
   - Run the following Git command to clone the repository:
     ```
     git clone https://github.com/your-username/markme.git
     ```
   - Change into the project directory:
     ```
     cd markme
     ```

2. **Build the Project**:
   - In the project root directory, execute the following Maven command to build the project and install dependencies:
     ```
     mvn clean install
     ```

3. **Run the Application**:
   - After the build completes, run the application using Maven:
     ```
     mvn spring-boot:run
     ```
   - The system should start and be accessible via `http://localhost:8080`.

4. **Verify Setup**:
   - Open your web browser and navigate to `http://localhost:8080`.
   - You should see the landing page of the Markme Attendance System, indicating that the setup was successful.

Following these steps will set up the Markme Attendance System on your local machine for development and testing purposes. Ensure that each component is correctly configured and operational to avoid runtime issues.

# 3. Changes Required Before Running the Project

Before launching the Markme Attendance System in your local environment, it is essential to make specific configurations to ensure the application functions correctly. Below is a list of necessary changes, including modifications to configuration files, setting environment variables, and updating database credentials.

## Configuration Files
You need to update the following configuration files to match your local environment settings:

### Application Properties
Modify the `application.properties` file located at `src/main/resources/application.properties` with the correct database connection settings and other environmental configurations:

```type:Generated,lang:Properties,path:,lines:0-0
spring.datasource.url=jdbc:mysql://localhost:3306/markme?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=update
```





## Environment Variables
Set the following environment variables specific to your development environment. These can be set in your IDE or directly in your operating system:

- **JAVA_HOME**: Set this variable to the path where your JDK is installed.
- **MAVEN_HOME**: Set this to the path where Maven is installed.

## API Keys and Credentials
If the system integrates with external services (e.g., email services or cloud databases), ensure that the respective API keys or service credentials are updated in the configuration files or as environment variables.

## Database Setup
Ensure the following before running the application:

- **Database Creation**: A MySQL database named `markme` should be created.
- **User Privileges**: Ensure the database user specified in `application.properties` has the necessary privileges for operations like SELECT, INSERT, UPDATE, and DELETE.

## Customization Steps
Customize the project to fit specific needs, such as adjusting the port number or configuring cross-origin resource sharing (CORS) settings:

- **Change Port Number**: Modify the server port in `application.properties` if the default `8080` is already in use:
  ```properties
  server.port=8081
  ```

- **CORS Configuration**: If your frontend runs on a different server, add or adjust CORS mappings in the Spring Boot configuration to allow cross-origin requests.

## Critical Changes
The following changes are critical and must be addressed for the application to run successfully:

- **Database Credentials**: Correct database credentials in `application.properties`.
- **API Keys**: Valid API keys for any integrated external services.

By carefully updating these configurations and settings, you can ensure that the Markme Attendance System runs smoothly in your local development environment.
# 4. API Documentation

This section provides a detailed overview of the APIs available in the Markme system. Each controller's APIs are documented separately, detailing their purpose, HTTP methods, endpoints, and input parameters.

## StudentController APIs

### 1. Mark Attendance
- **Controller Name**: StudentController
- **API Description**: Allows students to mark their attendance for a session.
- **HTTP Method**: POST
- **Endpoint URL**: /api/student/mark
- **Input Parameters**:
  - **Body**: JSON containing `MarkAttendanceRequestDTO` (subjectName, otpCode, studentId)
  - **Authentication**: Required (Bearer token)

### 2. Check Attendance
- **Controller Name**: StudentController
- **API Description**: Checks if the student's attendance is marked for a specific session.
- **HTTP Method**: GET
- **Endpoint URL**: /api/student/check
- **Input Parameters**:
  - **Body**: JSON containing `CheckAttendanceRequestDTO` (subjectName, otpCode)
  - **Authentication**: Required (Bearer token)

### 3. Send Message
- **Controller Name**: StudentController
- **API Description**: Allows students to send messages during an attendance session.
- **HTTP Method**: POST
- **Endpoint URL**: /api/student/message
- **Input Parameters**:
  - **Body**: JSON containing `AttendanceMessageRequestDTO` (sessionId, message)
  - **Authentication**: Required (Bearer token)

### 4. Attendance History
- **Controller Name**: StudentController
- **API Description**: Retrieves the attendance history of the student.
- **HTTP Method**: GET
- **Endpoint URL**: /api/student/history
- **Input Parameters**:
  - **Authentication**: Required (Bearer token)

### 5. Session Summary
- **Controller Name**: StudentController
- **API Description**: Provides a summary of attendance sessions for the student.
- **HTTP Method**: GET
- **Endpoint URL**: /api/student/summary
- **Input Parameters**:
  - **Authentication**: Required (Bearer token)

### 6. View Notifications
- **Controller Name**: StudentController
- **API Description**: Retrieves notifications relevant to the student's section.
- **HTTP Method**: GET
- **Endpoint URL**: /api/student/notifications
- **Input Parameters**:
  - **Authentication**: Required (Bearer token)

## ProfessorController APIs

### 1. Create Attendance Session
- **Controller Name**: ProfessorController
- **API Description**: Allows professors to create a new attendance session.
- **HTTP Method**: POST
- **Endpoint URL**: /api/professor/session
- **Input Parameters**:
  - **Body**: JSON containing `AttendanceSessionRequestDTO` (subjectName, sectionName, date)
  - **Authentication**: Required (Bearer token)

### 2. Get Sessions
- **Controller Name**: ProfessorController
- **API Description**: Retrieves all attendance sessions created by the professor.
- **HTTP Method**: GET
- **Endpoint URL**: /api/professor
- **Input Parameters**:
  - **Authentication**: Required (Bearer token)

### 3. Get Students for Session
- **Controller Name**: ProfessorController
- **API Description**: Retrieves the list of students who attended a specific session.
- **HTTP Method**: GET
- **Endpoint URL**: /api/professor/{sessionId}/students
- **Input Parameters**:
  - **Path**: sessionId (ID of the session)
  - **Authentication**: Required (Bearer token)

### 4. Add Student Manually
- **Controller Name**: ProfessorController
- **API Description**: Allows professors to manually add a student to a session.
- **HTTP Method**: POST
- **Endpoint URL**: /api/professor/add-student
- **Input Parameters**:
  - **Body**: JSON containing `AddStudentRequestDTO` (sessionId, studentId)
  - **Authentication**: Required (Bearer token)

### 5. Update Session
- **Controller Name**: ProfessorController
- **API Description**: Updates details of an existing attendance session.
- **HTTP Method**: PUT
- **Endpoint URL**: /api/professor/update/{otpCode}
- **Input Parameters**:
  - **Path**: otpCode (OTP code of the session)
  - **Body**: JSON containing `UpdateAttendanceSessionRequestDTO` (new details)
  - **Authentication**: Required (Bearer token)

### 6. Delete Session
- **Controller Name**: ProfessorController
- **API Description**: Deletes an existing attendance session.
- **HTTP Method**: DELETE
- **Endpoint URL**: /api/professor/delete/{otpCode}
- **Input Parameters**:
  - **Path**: otpCode (OTP code of the session)
  - **Authentication**: Required (Bearer token)

### 7. Get Messages for Session
- **Controller Name**: ProfessorController
- **API Description**: Retrieves all messages sent during a specific session.
- **HTTP Method**: GET
- **Endpoint URL**: /api/professor/{sessionId}/messages
- **Input Parameters**:
  - **Path**: sessionId (ID of the session)

### 8. Add Notification
- **Controller Name**: ProfessorController
- **API Description**: Allows professors to create a notification for students.
- **HTTP Method**: POST
- **Endpoint URL**: /api/professor/add/notification
- **Input Parameters**:
  - **Body**: JSON containing `NotificationRequestDTO` (message, target, section)
  - **Authentication**: Required (Bearer token)

## AuthController APIs

### 1. User Login
- **Controller Name**: AuthController
- **API Description**: Authenticates a user and returns a JWT token.
- **HTTP Method**: POST
- **Endpoint URL**: /api/auth/login
- **Input Parameters**:
  - **Body**: JSON containing user credentials (username, password)

### 2. User Registration
- **Controller Name**: AuthController
- **API Description**: Registers a new user into the system.
- **HTTP Method**: POST
- **Endpoint URL**: /api/auth/register
- **Input Parameters**:
  - **Body**: JSON containing `UserDto` (username, password, email, role)

### 3. Update Profile
- **Controller Name**: AuthController
- **API Description**: Updates the profile information of a user.
- **HTTP Method**: PUT
- **Endpoint URL**: /api/auth/profile/{id}
- **Input Parameters**:
  - **Path**: id (ID of the user)
  - **Body**: JSON containing `UserProfileUpdateDTO` (new profile details)
  - **Authentication**: Required (Bearer token)

### 4. Get User Details
- **Controller Name**: AuthController
- **API Description**: Retrieves the profile details of the authenticated user.
- **HTTP Method**: GET
- **Endpoint URL**: /api/auth/profile/details
- **Input Parameters**:
  - **Authentication**: Required (Bearer token)

This documentation aims to provide a comprehensive understanding of the APIs available in the Markme system, sorted by controller for easier navigation and clarity.
# 5. Conclusion

## Summary of the Project

The Markme system is a comprehensive attendance management platform specifically designed for educational institutions. It effectively bridges the gap between professors and students by providing a digital attendance interface that is both efficient and user-friendly. Key features of the system include the ability to mark attendance, manage attendance sessions, view attendance history, and send and receive notifications pertinent to courses and sections.

## Future Improvements and Planned Updates

Looking ahead, the Markme system is poised for several exciting updates and enhancements:
- **Integration with Learning Management Systems (LMS)**: We plan to integrate with popular LMS platforms to streamline the workflow for students and professors.
- **Advanced Analytics**: Implementing more detailed analytical tools to help professors monitor attendance trends and patterns.
- **Mobile Application**: Development of a mobile app to provide users with the flexibility to manage attendance on-the-go.
- **Real-Time Communication Features**: Adding real-time chat or discussion boards to enhance communication during sessions.

These updates are aimed at enhancing the functionality of the Markme system and ensuring it remains at the forefront of attendance management technology.

## Encouragement for Contributions and Feedback

We strongly believe in the power of community and collaboration. Users, developers, and educational professionals are encouraged to contribute to the Markme project. Whether it's through code contributions, feedback on features, or suggestions for improvements, your input is invaluable to us.

## Call to Action

Feel free to reach out for support or collaboration on enhancing the Markme system. We are open to partnerships, creative ideas, and any feedback that can help us improve. Together, we can make Markme an even more powerful tool for educational communities.

Thank you for your interest and engagement in making the Markme system a success. We look forward to your contributions and are excited to see how together we can advance this platform to new heights.
# 6. Author Details

## Full Name
- **Name**: Aditya Bondre

## GitHub Username
- **GitHub**: [Aditya Bondre](https://github.com/AdityaBondre/Markme)

## Email Address
- **Email**: abondre299@gmail.com

## LinkedIn Profile
- **LinkedIn**: [Aditya Bondre](https://www.linkedin.com/in/aditya-bondre-78a623299/)

## Invitation for Connection and Collaboration
I am always looking for opportunities to collaborate and connect with like-minded professionals and enthusiasts in the tech community. If you are interested in discussing potential projects, exchanging ideas, or simply connecting for a chat about technology, please feel free to reach out to me via email or LinkedIn. I am excited to explore opportunities that enhance our learning and contribute to the tech community. Let's connect!
