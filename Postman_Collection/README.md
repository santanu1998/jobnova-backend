# Globalco AI-Powered Job Board Platform - Postman Collection Guide

## 📋 Overview

This is a comprehensive Postman collection for the **Globalco AI-Powered Job Board Platform** microservices architecture. The collection includes all endpoints across 5 microservices with 2-3 practical examples for each request.

## 📁 Collection Structure

```
Globalco Job Board Platform
├── User-Service (Port 5001)
│   ├── Authentication
│   │   ├── Signup (3 examples)
│   │   └── Login (3 examples)
│   └── User Management
│       ├── Get Profile (2 examples)
│       ├── Update Profile (2 examples)
│       ├── Get User By ID (2 examples)
│       ├── Get All Users
│       ├── Suspend User (2 examples)
│       ├── Activate User (2 examples)
│       └── Delete User (2 examples)
│
├── Company-Service (Port 5002)
│   └── Company Management
│       ├── Create Company (3 examples)
│       ├── Get Company By ID (2 examples)
│       ├── Get My Company
│       ├── Get All Companies (3 filter options)
│       ├── Update Company (2 examples)
│       ├── Verify Company (2 examples)
│       ├── Deactivate Company (2 examples)
│       └── Delete Company (2 examples)
│
├── Job-Service (Port 5003)
│   ├── Job Categories
│   │   ├── Create Category (3 examples)
│   │   ├── Get All Categories
│   │   ├── Get Category By ID (2 examples)
│   │   ├── Update Category (2 examples)
│   │   └── Delete Category (2 examples)
│   ├── Job Skills
│   │   ├── Create Skill (3 examples)
│   │   ├── Get All Skills
│   │   ├── Get Skill By ID (2 examples)
│   │   ├── Update Skill (2 examples)
│   │   └── Delete Skill (2 examples)
│   ├── Job Tags
│   │   ├── Create Tag (3 examples)
│   │   ├── Get All Tags
│   │   ├── Get Tag By ID (2 examples)
│   │   ├── Update Tag (2 examples)
│   │   └── Delete Tag (2 examples)
│   └── Job Management
│       ├── Create Job (3 examples)
│       ├── Get Job By ID (2 examples)
│       ├── Get All Jobs (5 filter options)
│       ├── Get Jobs by Company (2 examples)
│       ├── Update Job (2 examples)
│       ├── Publish Job (2 examples)
│       ├── Close Job (2 examples)
│       └── Delete Job (2 examples)
│
├── Resume-Service (Port 5004)
│   ├── Resume Management
│   │   ├── Create Resume (3 examples)
│   │   ├── Get Resume By ID (2 examples)
│   │   ├── Get All Resumes (2 examples)
│   │   ├── Update Personal Info (2 examples)
│   │   ├── Update Summary (2 examples)
│   │   ├── Set as Default (2 examples)
│   │   └── Delete Resume (2 examples)
│   ├── Education
│   │   ├── Add Education (3 examples)
│   │   ├── Get All Educations (2 examples)
│   │   ├── Update Education (2 examples)
│   │   └── Delete Education (2 examples)
│   ├── Work Experience
│   │   ├── Add Work Experience (3 examples)
│   │   ├── Get All Work Experiences (2 examples)
│   │   ├── Update Work Experience (2 examples)
│   │   └── Delete Work Experience (2 examples)
│   └── Resume Skills
│       ├── Add Resume Skill (3 examples)
│       ├── Get Resume Skills (2 examples)
│       ├── Update Resume Skill (2 examples)
│       └── Delete Resume Skill (2 examples)
│
└── Application-Service (Port 5005)
    ├── Applications
    │   ├── Create Application (3 examples)
    │   ├── Get Application By ID (2 examples)
    │   ├── Get All Applications (2 examples)
    │   ├── Get Applications for Job (2 examples)
    │   ├── Get Applications for Company
    │   ├── Update Application Status (3 examples)
    │   ├── Withdraw Application (2 examples)
    │   ├── Toggle Star (Favorite) (2 examples)
    │   └── Delete Application (2 examples)
    └── Application Notes
        ├── Add Note (3 examples)
        ├── Get All Notes (2 examples)
        └── Delete Note (2 examples)
```

## 🚀 Getting Started

### Prerequisites
- Postman installed (Desktop or Web version)
- All 5 microservices running locally
- PostgreSQL database configured for each service

### Service Ports Configuration

| Service | Port | Database |
|---------|------|----------|
| User-Service | 5001 | job_portal_user |
| Company-Service | 5002 | job_portal_company |
| Job-Service | 5003 | job_portal_job |
| Resume-Service | 5004 | job_portal_resume |
| Application-Service | 5005 | job_portal_application |

### Import Steps

1. **Open Postman** → Click **Import** button
2. **Select** `Globalco_Job_Board_Platform.postman_collection.json`
3. **Click Import** and the collection will be available in your workspace
4. Start testing the endpoints!

## 🔐 Authentication Headers

Some endpoints require authentication headers:

- **X-User-Id**: User ID (numeric) - Required for protected endpoints
- **X-User-Email**: User email (string) - Required for profile endpoints

Example headers:
```
X-User-Id: 1
X-User-Email: john.doe@example.com
```

## 📝 Request Examples by Service

### User-Service (5001) - Authentication Flow

**Signup Example 1 (Candidate)**
```json
POST /auth/signup
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "password": "SecurePass123!",
  "role": "CANDIDATE",
  "phoneNumber": "+1-555-0101"
}
```

**Login Example 1**
```json
POST /auth/login
{
  "email": "john.doe@example.com",
  "password": "SecurePass123!"
}
```

### Company-Service (5002) - Company Management

**Create Company Example 1**
```json
POST /api/companies/create
Headers: X-User-Id: 1

{
  "name": "Tech Innovations Ltd",
  "description": "Leading software development company",
  "website": "https://techinnovations.com",
  "companyType": "PRIVATE",
  "industryType": "TECHNOLOGY",
  "employeeCount": 500,
  "location": "San Francisco, CA",
  "phoneNumber": "+1-555-0123",
  "email": "contact@techinnovations.com"
}
```

**Filter Companies by Industry**
```
GET /api/companies/all?industryType=TECHNOLOGY
```

### Job-Service (5003) - Job Management

**Create Job Example 1**
```json
POST /api/jobs/create
Headers: X-User-Id: 1

{
  "title": "Senior Java Developer",
  "description": "We are looking for experienced Java developers",
  "companyId": 1,
  "categoryId": 1,
  "jobType": "FULL_TIME",
  "workMode": "REMOTE",
  "location": "San Francisco, CA",
  "experienceLevel": "SENIOR",
  "minSalary": "100000",
  "maxSalary": "150000",
  "currencyCode": "USD",
  "noOfOpenings": 3,
  "skills": [1],
  "tags": [1]
}
```

**Search Jobs with Multiple Filters**
```
GET /api/jobs/all?keyword=Java&jobType=FULL_TIME&workMode=REMOTE&minSalary=80000&maxSalary=150000
```

### Resume-Service (5004) - Resume Management

**Create Resume Example 1**
```json
POST /api/resumes/create
Headers: X-User-Id: 1

{
  "resumeName": "John Doe - Software Developer Resume",
  "summary": "Experienced software developer with 5+ years in Java and Spring Boot"
}
```

**Add Education Example 1**
```json
POST /api/resumes/education/1/add
Headers: X-User-Id: 1

{
  "institutionName": "State University",
  "degree": "Bachelor of Science",
  "fieldOfStudy": "Computer Science",
  "startDate": "2015-09",
  "endDate": "2019-05",
  "grade": "3.8",
  "description": "Graduated with honors"
}
```

**Add Work Experience Example 1**
```json
POST /api/work-experiences/1/add
Headers: X-User-Id: 1

{
  "jobTitle": "Senior Java Developer",
  "companyName": "Tech Solutions Inc",
  "location": "San Francisco, CA",
  "startDate": "2019-06",
  "endDate": "2023-12",
  "description": "Led development of microservices architecture",
  "isCurrentlyWorking": false
}
```

**Add Resume Skill Example 1**
```json
POST /api/resume-skills/1/add
Headers: X-User-Id: 1

{
  "skillName": "Java",
  "proficiencyLevel": "EXPERT",
  "yearsOfExperience": 5
}
```

### Application-Service (5005) - Application Management

**Create Application Example 1**
```json
POST /api/applications/create
Headers: X-User-Id: 1

{
  "jobId": 1,
  "resumeId": 1,
  "coverLetter": "I am interested in the Senior Java Developer position. With 5+ years of experience in Java and Spring Boot, I am confident I can contribute significantly to your team."
}
```

**Update Application Status Example 1 (Accept)**
```json
PATCH /api/applications/1/status
Headers: X-User-Id: 1

{
  "status": "ACCEPTED"
}
```

Possible Status Values: `PENDING`, `UNDER_REVIEW`, `ACCEPTED`, `REJECTED`, `WITHDRAWN`

**Add Note to Application Example 1**
```json
POST /api/application-notes/1/add
Headers: X-User-Id: 1

{
  "noteContent": "Candidate has strong technical skills. Schedule second round interview."
}
```

## 🔑 Key Enums and Reference Values

### User Roles
- `CANDIDATE`
- `EMPLOYER`
- `ADMIN`

### Company Type
- `PRIVATE`
- `PUBLIC`
- `STARTUP`

### Industry Type
- `TECHNOLOGY`
- `FINANCE`
- `HEALTHCARE`
- `MANUFACTURING`
- `EDUCATION`
- `RETAIL`

### Job Type
- `FULL_TIME`
- `PART_TIME`
- `CONTRACT`
- `INTERNSHIP`

### Work Mode
- `REMOTE`
- `ONSITE`
- `HYBRID`

### Experience Level
- `FRESHER`
- `JUNIOR`
- `MID_LEVEL`
- `SENIOR`
- `LEAD`

### Job Status
- `DRAFT`
- `PUBLISHED`
- `CLOSED`

### Proficiency Level
- `BEGINNER`
- `INTERMEDIATE`
- `EXPERT`

### Application Status
- `PENDING`
- `UNDER_REVIEW`
- `ACCEPTED`
- `REJECTED`
- `WITHDRAWN`

## 📊 Sample Testing Workflow

### 1. User Registration & Login
1. Register a Candidate using **Signup - Example 1**
2. Register an Employer using **Signup - Example 2**
3. Login with both accounts using **Login** endpoints
4. Note the JWT tokens received in response

### 2. Company Setup (Employer)
1. Create Company using **Create Company - Example 1**
2. Verify Company using **Verify Company - Example 1**
3. Get Company details using **Get Company By ID - Example 1**

### 3. Job Creation (Employer)
1. Create Job Categories using **Create Category** examples
2. Create Job Skills using **Create Skill** examples
3. Create Job Tags using **Create Tag** examples
4. Create Job using **Create Job - Example 1**
5. Publish Job using **Publish Job - Example 1**

### 4. Resume Building (Candidate)
1. Create Resume using **Create Resume - Example 1**
2. Add Education using **Add Education - Example 1**
3. Add Work Experience using **Add Work Experience - Example 1**
4. Add Skills using **Add Resume Skill - Example 1**
5. Set Default Resume using **Set Resume as Default - Example 1**

### 5. Job Application (Candidate)
1. Search for Jobs using **Get All Jobs** with filters
2. Apply for Job using **Create Application - Example 1**
3. View All Applications using **Get All Applications - Example 1**

### 6. Application Management (Employer)
1. View Applications for Company using **Get Applications for Company**
2. Add Notes using **Add Note to Application - Example 1**
3. Update Status using **Update Application Status - Example 1**

## ⚠️ Important Notes

1. **ID References**: Replace ID placeholders (1, 2, 3, etc.) with actual IDs from your database
2. **User Headers**: Always include `X-User-Id` or `X-User-Email` headers where required
3. **Database Setup**: Ensure all PostgreSQL databases are created and running
4. **Request Body**: Validate JSON format before sending requests
5. **Response Codes**:
   - `200 OK` - Successful GET/PATCH/PUT/DELETE
   - `201 CREATED` - Successful POST
   - `400 BAD REQUEST` - Invalid input
   - `401 UNAUTHORIZED` - Missing authentication
   - `404 NOT FOUND` - Resource not found
   - `500 INTERNAL SERVER ERROR` - Server error

## 🔍 Testing Tips

### Use Variables for Dynamic Values
Set Postman variables for base URLs and common headers:
```
{{base_url_user}} = http://localhost:5001
{{base_url_company}} = http://localhost:5002
{{user_id}} = 1
```

### Save Response Values
In Post-test scripts, extract and save values:
```javascript
pm.environment.set("userId", pm.response.json().id);
```

### Validate Responses
Create assertions in Tests tab:
```javascript
pm.test("Status code is 200", function() {
    pm.response.to.have.status(200);
});
```

## 📞 Support

For API issues or questions:
1. Check service logs in `/services/[Service-Name]/target/`
2. Verify database connectivity
3. Ensure all microservices are running on correct ports
4. Check request headers and body format

## 📚 Additional Resources

- **Spring Boot Documentation**: https://spring.io/projects/spring-boot
- **Postman Documentation**: https://learning.postman.com/
- **REST API Best Practices**: https://restfulapi.net/

---

**Last Updated**: July 26, 2026  
**Collection Version**: 1.0.0  
**Platform**: Globalco AI-Powered Job Board Platform

