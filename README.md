# GrabTutor

An online platform connecting tutors and students for personalized learning experiences.

## Project Overview

GrabTutor is a comprehensive tutoring platform that enables students to post learning requests and allows tutors to bid on teaching opportunities. The system manages courses, lessons, payments, reviews, and provides statistical insights for all stakeholders.

## Technologies

- **Backend Framework**: Spring Boot 3.x
- **Programming Language**: Java
- **Database**: MySQL 8.0
- **Authentication**: JWT (JSON Web Token)
- **File Storage**: Cloudinary
- **AI Integration**: Google Gemini API (gemini-2.5-flash)
- **Email Service**: Spring Mail (SMTP Gmail)
- **Real-time Communication**: WebSocket
- **Caching**: Redis
- **Build Tool**: Maven

## Core Features

### 1. User Management
- User registration and login with JWT authentication
- Role-based access control (Student, Tutor, Admin)
- Profile management
- Tutor verification system

### 2. Post & Bidding System
- Students create posts requesting tutors
- Tutors submit bids with proposed prices
- Students accept tutors based on bids
- Post status tracking (PENDING, ACCEPTED, COMPLETED, CANCELLED)

### 3. Course & Lesson Management
- Create and manage courses
- Organize lessons within courses
- Lesson grading and evaluation
- Course enrollment system
- Course publishing controls

### 4. Payment System
- Account balance management
- Transaction history tracking
- Payment processing for courses
- Virtual wallet functionality (deposit/withdraw)
- Transaction types: DEPOSIT, WITHDRAW, COURSE_ENROLLMENT, GRADING

### 5. Review & Reporting
- Student reviews for tutors after course completion
- Report system for violations
- Rating system affecting tutor reputation
- Report resolution by admin

### 6. Statistics & Analytics
- Revenue analytics
- User growth metrics
- Course performance tracking
- Transaction summaries
- Custom date range filtering

### 7. Additional Features
- Real-time chat between students and tutors
- Notification system
- File/image upload (max 100MB)
- Video upload support
- AI-powered suggestions using Google Gemini
- Subject categorization

## Installation

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+
- Redis (for caching)
- Docker (optional)

### Environment Configuration

Set the following environment variables:

```bash
DB_PASSWORD=your_mysql_password
MAIL_USERNAME=your_email@gmail.com
MAIL_PASSWORD=your_gmail_app_password
CLOUDINARY_API_SECRET=your_cloudinary_secret
GEMINI_API_KEY=your_gemini_api_key
```

Or use default values specified in `application.yaml`:
- DB_PASSWORD: 
- MAIL_USERNAME: 

### Database Setup

1. Install MySQL 8.0+
2. Create database:
```sql
CREATE DATABASE grabtutor_db;
```

3. The application will automatically create tables on first run (Hibernate DDL auto-update enabled)

### Running the Application

**Using Maven:**
```bash
# Clone repository
git clone <repository-url>

# Navigate to project directory
cd grabtutor

# Build project
mvn clean install

# Run application
mvn spring-boot:run
```

**Using Docker:**
```bash
docker-compose up
```

**Using JAR file:**
```bash
mvn clean package
java -jar target/grabtutor-0.0.1-SNAPSHOT.jar
```

The application will be available at: `http://localhost:8080/grabtutor`

## Project Structure

```
grabtutor/
├── src/
│   ├── main/
│   │   ├── java/com/grabtutor/grabtutor/
│   │   │   ├── GrabtutorApplication.java      # Main application entry point
│   │   │   ├── configuration/                  # Configuration classes
│   │   │   │   ├── ApplicationConfiguration.java
│   │   │   │   ├── CloudinaryConfiguration.java
│   │   │   │   ├── CustomJwtDecoder.java
│   │   │   │   ├── EncodingConfiguration.java
│   │   │   │   ├── GeminiConfiguration.java
│   │   │   │   ├── JwtAuthenticationEntryPoint.java
│   │   │   │   ├── RedisConfiguration.java
│   │   │   │   └── SecurityConfiguration.java
│   │   │   ├── controller/                     # REST API endpoints
│   │   │   │   ├── AuthenticationController.java
│   │   │   │   ├── ChatRoomController.java
│   │   │   │   ├── CourseController.java
│   │   │   │   ├── FileUploadController.java
│   │   │   │   ├── GradingController.java
│   │   │   │   ├── LessonController.java
│   │   │   │   ├── NotificationController.java
│   │   │   │   ├── PostController.java
│   │   │   │   ├── ReportController.java
│   │   │   │   ├── ReviewController.java
│   │   │   │   ├── StatisticController.java
│   │   │   │   ├── SubjectController.java
│   │   │   │   ├── TransactionController.java
│   │   │   │   └── UserController.java
│   │   │   ├── dto/                            # Data Transfer Objects
│   │   │   │   ├── request/
│   │   │   │   └── response/
│   │   │   ├── entity/                         # JPA Entities
│   │   │   │   ├── AccountBalance.java
│   │   │   │   ├── BaseEntity.java
│   │   │   │   ├── ChatRoom.java
│   │   │   │   ├── Course.java
│   │   │   │   ├── Lesson.java
│   │   │   │   ├── Post.java
│   │   │   │   ├── Review.java
│   │   │   │   ├── User.java
│   │   │   │   └── ...
│   │   │   ├── enums/                          # Enumerations
│   │   │   ├── exception/                      # Custom exceptions
│   │   │   ├── mapper/                         # Object mappers (MapStruct)
│   │   │   ├── repository/                     # Data access layer
│   │   │   ├── service/                        # Business logic
│   │   │   ├── socket/                         # WebSocket handlers
│   │   │   └── websocket/                      # WebSocket configuration
│   │   └── resources/
│   │       ├── application.yaml                # Application properties
│   │       ├── static/
│   │       └── templates/
│   └── test/
│       └── java/com/grabtutor/grabtutor/
│           └── GrabtutorApplicationTests.java
├── docker-compose.yml
├── pom.xml
├── LICENSE
└── README.md
```

## API Documentation

Base URL: `http://localhost:8080/grabtutor`

### Authentication
- `POST /auth/login` - User login
- `POST /auth/register` - User registration
- `POST /auth/logout` - User logout
- `POST /auth/refresh` - Refresh access token

### Users
- `GET /users` - Get all users (paginated)
- `GET /users/{id}` - Get user by ID
- `GET /users/myInfo` - Get current user info
- `PUT /users/{id}` - Update user profile
- `DELETE /users/{id}` - Delete user

### Posts
- `GET /posts` - Get all posts (paginated, sortable)
- `GET /posts/{id}` - Get post by ID
- `POST /posts` - Create new post
- `PUT /posts/{id}` - Update post
- `PUT /posts/acceptTutor/{tutorBidId}` - Accept tutor bid
- `DELETE /posts/{id}` - Delete post

### Courses
- `GET /courses` - Get all courses (paginated, sortable)
- `GET /courses/{id}` - Get course by ID
- `GET /courses/myEnrolledCourses` - Get user's enrolled courses
- `POST /courses` - Create new course
- `POST /courses/enroll` - Enroll in course
- `PUT /courses/{id}` - Update course
- `DELETE /courses/{id}` - Delete course

### Lessons
- `GET /lessons` - Get all lessons
- `GET /lessons/{id}` - Get lesson by ID
- `GET /lessons/course/{courseId}` - Get lessons by course
- `POST /lessons` - Create new lesson
- `PUT /lessons/{id}` - Update lesson
- `DELETE /lessons/{id}` - Delete lesson

### Grading
- `POST /grading` - Submit grade for lesson
- `GET /grading/course/{courseId}` - Get grades for course
- `GET /grading/lesson/{lessonId}` - Get grades for lesson

### Transactions
- `GET /transactions` - Get transaction history (paginated)
- `POST /transactions/deposit` - Deposit money
- `POST /transactions/withdraw` - Withdraw money
- `GET /transactions/balance` - Get account balance

### Reviews
- `GET /reviews` - Get all reviews (paginated)
- `POST /reviews` - Create review
- `GET /reviews/tutor/{tutorId}` - Get tutor reviews
- `PUT /reviews/{id}` - Update review
- `DELETE /reviews/{id}` - Delete review

### Reports
- `POST /reports` - Submit report
- `GET /reports` - Get all reports (Admin only)
- `GET /reports/sent` - Get sent reports
- `GET /reports/received` - Get received reports
- `PUT /reports/resolve/{id}` - Resolve report (Admin)

### Statistics
- `GET /statistics/revenue` - Revenue statistics (with date range)
- `GET /statistics/users` - User statistics
- `GET /statistics/courses` - Course statistics
- `GET /statistics/transactions` - Transaction statistics

### Subjects
- `GET /subjects` - Get all subjects
- `POST /subjects` - Create subject (Admin)
- `PUT /subjects/{id}` - Update subject
- `DELETE /subjects/{id}` - Delete subject

### Chat
- `GET /chatrooms` - Get all chat rooms
- `GET /chatrooms/{id}` - Get chat room by ID
- `GET /chatrooms/{id}/messages` - Get messages in chat room
- `POST /chatrooms` - Create chat room

### Notifications
- `GET /notifications` - Get user notifications
- `GET /notifications/{id}` - Get notification by ID
- `PUT /notifications/{id}/read` - Mark notification as read
- `DELETE /notifications/{id}` - Delete notification

### File Upload
- `POST /upload` - Upload file/image/video (max 100MB)

## Configuration

### Application Settings (application.yaml)

**Server Configuration:**
- Port: 8080
- Context Path: /grabtutor

**Database:**
- URL: jdbc:mysql://localhost:3306/grabtutor_db
- Username: root
- Password: Configured via `DB_PASSWORD` environment variable (default: 123456)

**JWT Configuration:**
- Token Validity: 3600 seconds (1 hour)
- Refresh Token Validity: 36000 seconds (10 hours)
- Signing Key: Configured in application.yaml

**File Upload:**
- Max File Size: 100MB
- Max Request Size: 100MB

**Email Configuration:**
- SMTP Host: smtp.gmail.com
- Port: 587
- TLS: Enabled

**Cloudinary:**
- Cloud Name: dk3pmg0tr
- API Key: 746891315239322
- API Secret: Configured via environment variable

**Gemini AI:**
- Model: gemini-2.5-flash
- API Key: Configured via environment variable

## Security

- **JWT-based Authentication**: Secure token-based authentication
- **Role-based Access Control**: Different permissions for Student, Tutor, and Admin
- **Password Encryption**: BCrypt password encoding
- **Token Refresh**: Automatic token refresh mechanism
- **Protected Endpoints**: Most endpoints require authentication
- **CORS Configuration**: Cross-origin resource sharing enabled

## Database Schema

### Main Entities:

**User Management:**
- `users` - User accounts and profiles
- `account_balance` - User wallet information

**Tutoring System:**
- `posts` - Tutor request posts
- `tutor_bids` - Bidding information

**Learning Management:**
- `subjects` - Subject categories
- `courses` - Course details
- `lessons` - Lesson content
- `grading` - Lesson grades

**Financial:**
- `user_transactions` - Payment and transaction records

**Communication:**
- `chat_rooms` - Chat room information
- `messages` - Chat messages
- `notifications` - User notifications

**Feedback:**
- `reviews` - Course and tutor reviews
- `reports` - Violation reports

## Key Features Explained

### Account Balance System
Each user has an `AccountBalance` entity that tracks their virtual wallet balance. This balance is used for:
- Enrolling in courses
- Paying for grading services
- Receiving payments as a tutor

### Transaction System
All financial activities are recorded in `UserTransaction` entity with types:
- **DEPOSIT**: Adding money to wallet
- **WITHDRAW**: Removing money from wallet
- **COURSE_ENROLLMENT**: Payment for course enrollment
- **GRADING**: Payment for lesson grading

### Post & Bidding Flow
1. Student creates a post requesting a tutor
2. Tutors submit bids with their proposed price
3. Student reviews bids and accepts one tutor
4. Post status changes to ACCEPTED
5. After completion, status changes to COMPLETED

### Course Enrollment
1. User browses available courses
2. User enrolls in course (payment deducted from balance)
3. Transaction is recorded
4. User gains access to all lessons in the course

## Error Handling

The application uses custom exception handling with:
- `AppException` for application-specific errors
- `ErrorCode` enum for standardized error codes
- Global exception handler for consistent error responses
- Detailed error messages for debugging

## API Response Format

**Success Response:**
```json
{
  "success": true,
  "message": "Operation successful",
  "data": { ... },
  "timestamp": "2025-11-26T10:30:00"
}
```

**Error Response:**
```json
{
  "success": false,
  "message": "Error description",
  "code": 99999,
  "timestamp": "2025-11-26T10:30:00"
}
```

**Paginated Response:**
```json
{
  "pageNo": 0,
  "pageSize": 10,
  "totalPages": 5,
  "items": [ ... ]
}
```

## Development Guidelines

### Code Style
- Follow Java naming conventions
- Use Lombok for reducing boilerplate code
- Use MapStruct for object mapping
- Keep controllers thin, put business logic in services

### Best Practices
- Use DTOs for API requests/responses
- Implement pagination for list endpoints
- Add proper validation annotations
- Handle exceptions appropriately
- Write meaningful commit messages

### Testing
Run tests using:
```bash
mvn test
```

## Troubleshooting

### Common Issues

**Database Connection Error:**
- Verify MySQL is running
- Check database credentials
- Ensure database `grabtutor_db` exists

**JWT Token Invalid:**
- Check if token has expired
- Verify JWT signing key configuration
- Use refresh token endpoint to get new token

**File Upload Fails:**
- Check file size (max 100MB)
- Verify Cloudinary credentials
- Check internet connection

**Email Not Sending:**
- Verify SMTP credentials
- Enable "Less secure app access" or use App Password for Gmail
- Check firewall settings

## License

See `LICENSE` file for details.

## Contact

- GitHub: [@leminhkhai345](https://github.com/leminhkhai345)
- Email: khoatrancraft3@gmail.com

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

### How to Contribute
1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## Acknowledgments

- Spring Boot Framework
- Cloudinary for file storage
- Google Gemini for AI capabilities
- MySQL Database
- Redis for caching
- All contributors and supporters

---

**Built with ❤️ by the GrabTutor Team**

