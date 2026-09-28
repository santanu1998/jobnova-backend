# 📋 Postman Collection Testing Checklist

## 🎯 Pre-Testing Requirements

- [ ] All 5 microservices running locally
- [ ] PostgreSQL databases created for all services
- [ ] Postman collection imported
- [ ] Globalco_Environment_DEV.postman_environment.json imported
- [ ] Environment selected in Postman (top right dropdown)

---

## ✅ User-Service (Port 5001) Testing

### Authentication Endpoints
- [ ] **Signup - Example 1 (Candidate)**
  - Status: 200/201
  - Response includes: id, firstName, lastName, email, role, token
  - Save candidate_user_id and candidate_token

- [ ] **Signup - Example 2 (Employer)**
  - Status: 200/201
  - Response includes: id, firstName, lastName, email, role, token
  - Save employer_user_id and employer_token

- [ ] **Signup - Example 3 (Admin)**
  - Status: 200/201
  - Response includes admin role

- [ ] **Login - Example 1 (Candidate)**
  - Status: 200
  - Returns valid JWT token
  - Token matches signup token

- [ ] **Login - Example 2 (Employer)**
  - Status: 200
  - Returns valid JWT token

- [ ] **Login - Example 3 (Admin)**
  - Status: 200
  - Returns valid JWT token

### User Profile Management
- [ ] **Get Profile - Example 1**
  - Status: 200
  - Headers: X-User-Email: john.doe@example.com
  - Returns correct user data

- [ ] **Get Profile - Example 2**
  - Status: 200
  - Headers: X-User-Email: jane.smith@company.com
  - Returns correct user data

- [ ] **Update Profile - Example 1**
  - Status: 200
  - Headers: X-User-Email required
  - Returns updated user data

- [ ] **Update Profile - Example 2**
  - Status: 200
  - Changes reflected in subsequent GET requests

- [ ] **Get User By ID - Example 1 (ID: 1)**
  - Status: 200
  - Returns user with ID 1

- [ ] **Get User By ID - Example 2 (ID: 2)**
  - Status: 200
  - Returns user with ID 2

- [ ] **Get All Users**
  - Status: 200
  - Returns array of users
  - Contains at least 3 users from signup

- [ ] **Suspend User - Example 1**
  - Status: 200
  - User status changes to SUSPENDED

- [ ] **Suspend User - Example 2**
  - Status: 200
  - Suspended user cannot login

- [ ] **Activate User - Example 1**
  - Status: 200
  - User status changes to ACTIVE

- [ ] **Activate User - Example 2**
  - Status: 200
  - Activated user can login again

- [ ] **Delete User - Example 1**
  - Status: 200
  - User no longer appears in Get All Users

- [ ] **Delete User - Example 2**
  - Status: 200
  - Deleted user cannot login

---

## ✅ Company-Service (Port 5002) Testing

### Company CRUD Operations
- [ ] **Create Company - Example 1 (Tech)**
  - Status: 201
  - Headers: X-User-Id required
  - Response includes: id, name, status (INACTIVE)
  - Save company_id

- [ ] **Create Company - Example 2 (Finance)**
  - Status: 201
  - Different industry type

- [ ] **Create Company - Example 3 (Healthcare)**
  - Status: 201
  - Startup company type

- [ ] **Get Company By ID - Example 1**
  - Status: 200
  - Returns company with ID 1
  - All fields populated correctly

- [ ] **Get Company By ID - Example 2**
  - Status: 200
  - Returns different company (ID 2)

- [ ] **Get My Company**
  - Status: 200
  - Headers: X-User-Id required
  - Returns company owned by user

- [ ] **Get All Companies - No Filter**
  - Status: 200
  - Returns array of all companies
  - Includes companies from all creates

- [ ] **Get All Companies - Filter by Type**
  - Status: 200
  - Query: companyType=PRIVATE
  - Only returns PRIVATE companies

- [ ] **Get All Companies - Filter by Industry**
  - Status: 200
  - Query: industryType=TECHNOLOGY
  - Only returns TECHNOLOGY companies

- [ ] **Update Company - Example 1**
  - Status: 200
  - Headers: X-User-Id required
  - Employee count updated
  - Changes reflected in subsequent GETs

- [ ] **Update Company - Example 2**
  - Status: 200
  - Description updated
  - Website URL changed

- [ ] **Verify Company - Example 1**
  - Status: 200
  - Company status changes to VERIFIED
  - verified flag = true

- [ ] **Verify Company - Example 2**
  - Status: 200
  - Multiple companies can be verified

- [ ] **Deactivate Company - Example 1**
  - Status: 200
  - Company status changes to INACTIVE
  - Cannot post jobs

- [ ] **Deactivate Company - Example 2**
  - Status: 200
  - Deactivated company visible but inactive

- [ ] **Delete Company - Example 1**
  - Status: 200
  - Company no longer appears in Get All
  - Headers: X-User-Id required

- [ ] **Delete Company - Example 2**
  - Status: 200
  - Related data handled correctly

---

## ✅ Job-Service (Port 5003) Testing

### Job Categories
- [ ] **Create Category - Example 1**
  - Status: 201
  - Response includes: id, name, description
  - Save category_id

- [ ] **Create Category - Example 2**
  - Status: 201
  - Different category created

- [ ] **Create Category - Example 3**
  - Status: 201
  - Third category for future tests

- [ ] **Get All Categories**
  - Status: 200
  - Returns array with all categories
  - Count >= 3

- [ ] **Get Category By ID - Example 1**
  - Status: 200
  - Returns correct category

- [ ] **Get Category By ID - Example 2**
  - Status: 200
  - Different category returned

- [ ] **Update Category - Example 1**
  - Status: 200
  - Description updated

- [ ] **Update Category - Example 2**
  - Status: 200
  - Name updated
  - Changes reflected in GET

- [ ] **Delete Category - Example 1**
  - Status: 200
  - Category removed from list

- [ ] **Delete Category - Example 2**
  - Status: 200
  - Correct category deleted

### Job Skills
- [ ] **Create Skill - Example 1 (Java)**
  - Status: 201
  - Response includes: id, skillName
  - Save skill_id

- [ ] **Create Skill - Example 2 (Python)**
  - Status: 201
  - Different skill

- [ ] **Create Skill - Example 3 (React)**
  - Status: 201
  - Frontend skill

- [ ] **Get All Skills**
  - Status: 200
  - Contains all created skills

- [ ] **Get Skill By ID - Example 1**
  - Status: 200
  - Returns correct skill

- [ ] **Get Skill By ID - Example 2**
  - Status: 200
  - Different skill returned

- [ ] **Update Skill - Example 1**
  - Status: 200
  - Description updated

- [ ] **Update Skill - Example 2**
  - Status: 200
  - Changes reflected

- [ ] **Delete Skill - Example 1**
  - Status: 200
  - Skill removed

- [ ] **Delete Skill - Example 2**
  - Status: 200

### Job Tags
- [ ] **Create Tag - Example 1 (Remote)**
  - Status: 201
  - Save tag_id

- [ ] **Create Tag - Example 2 (Urgent)**
  - Status: 201

- [ ] **Create Tag - Example 3 (Startup)**
  - Status: 201

- [ ] **Get All Tags**
  - Status: 200
  - Contains all tags

- [ ] **Get Tag By ID - Example 1**
  - Status: 200

- [ ] **Get Tag By ID - Example 2**
  - Status: 200

- [ ] **Update Tag - Example 1**
  - Status: 200

- [ ] **Update Tag - Example 2**
  - Status: 200

- [ ] **Delete Tag - Example 1**
  - Status: 200

- [ ] **Delete Tag - Example 2**
  - Status: 200

### Job Management
- [ ] **Create Job - Example 1 (Senior Developer)**
  - Status: 201
  - Headers: X-User-Id required
  - Response includes: id, title, status (DRAFT)
  - Save job_id
  - Update companyId to created company ID

- [ ] **Create Job - Example 2 (Data Scientist)**
  - Status: 201
  - Different job with different category

- [ ] **Create Job - Example 3 (Project Manager)**
  - Status: 201
  - Different company

- [ ] **Get Job By ID - Example 1**
  - Status: 200
  - Returns correct job

- [ ] **Get Job By ID - Example 2**
  - Status: 200
  - Different job

- [ ] **Get All Jobs - No Filter**
  - Status: 200
  - Contains all jobs
  - Count >= 3

- [ ] **Get All Jobs - Filter by Keyword**
  - Status: 200
  - Query: keyword=Java
  - Returns relevant jobs

- [ ] **Get All Jobs - Filter by Salary Range**
  - Status: 200
  - Query: minSalary=80000&maxSalary=150000
  - Correct salary filtering

- [ ] **Get All Jobs - Filter by Job Type**
  - Status: 200
  - Query: jobType=FULL_TIME
  - Only FULL_TIME jobs

- [ ] **Get All Jobs - Filter by Work Mode**
  - Status: 200
  - Query: workMode=REMOTE
  - Only REMOTE jobs

- [ ] **Get Jobs by Company - Example 1**
  - Status: 200
  - Returns jobs for company 1
  - Correct company association

- [ ] **Get Jobs by Company - Example 2**
  - Status: 200
  - Returns jobs for company 2

- [ ] **Update Job - Example 1**
  - Status: 200
  - Headers: X-User-Id required
  - Title updated
  - Status remains DRAFT

- [ ] **Update Job - Example 2**
  - Status: 200
  - Salary range updated

- [ ] **Publish Job - Example 1**
  - Status: 200
  - Job status changes to PUBLISHED
  - Job becomes visible in searches

- [ ] **Publish Job - Example 2**
  - Status: 200
  - Multiple jobs can be published

- [ ] **Close Job - Example 1**
  - Status: 200
  - Job status changes to CLOSED
  - No longer accepting applications

- [ ] **Close Job - Example 2**
  - Status: 200

- [ ] **Delete Job - Example 1**
  - Status: 200
  - Job removed from list
  - Headers: X-User-Id required

- [ ] **Delete Job - Example 2**
  - Status: 200

---

## ✅ Resume-Service (Port 5004) Testing

### Resume Management
- [ ] **Create Resume - Example 1**
  - Status: 201
  - Headers: X-User-Id required
  - Response includes: id, resumeName, summary
  - Save resume_id

- [ ] **Create Resume - Example 2**
  - Status: 201
  - Different resume

- [ ] **Create Resume - Example 3**
  - Status: 201
  - Third resume for testing

- [ ] **Get Resume By ID - Example 1**
  - Status: 200
  - Headers: X-User-Id required
  - Returns correct resume

- [ ] **Get Resume By ID - Example 2**
  - Status: 200
  - Different resume

- [ ] **Get All Resumes - Example 1**
  - Status: 200
  - Headers: X-User-Id required
  - Returns all user resumes

- [ ] **Get All Resumes - Example 2**
  - Status: 200
  - Different user sees their resumes only

- [ ] **Update Personal Info - Example 1**
  - Status: 200
  - Headers: X-User-Id required
  - All fields updated

- [ ] **Update Personal Info - Example 2**
  - Status: 200
  - Different information

- [ ] **Update Summary - Example 1**
  - Status: 200
  - Summary text changed

- [ ] **Update Summary - Example 2**
  - Status: 200
  - New summary reflected

- [ ] **Set as Default - Example 1**
  - Status: 200
  - isDefault flag = true
  - Others set to false

- [ ] **Set as Default - Example 2**
  - Status: 200
  - Different resume becomes default

- [ ] **Delete Resume - Example 1**
  - Status: 200
  - Resume removed
  - Headers: X-User-Id required

- [ ] **Delete Resume - Example 2**
  - Status: 200

### Education
- [ ] **Add Education - Example 1**
  - Status: 201
  - Headers: X-User-Id required
  - Response includes: id, institutionName, degree
  - Save education_id

- [ ] **Add Education - Example 2**
  - Status: 201
  - Master's degree

- [ ] **Add Education - Example 3**
  - Status: 201
  - Certification

- [ ] **Get All Educations - Example 1**
  - Status: 200
  - Returns all education for resume

- [ ] **Get All Educations - Example 2**
  - Status: 200
  - Different resume

- [ ] **Update Education - Example 1**
  - Status: 200
  - Headers: X-User-Id required
  - Grade updated

- [ ] **Update Education - Example 2**
  - Status: 200
  - Description updated

- [ ] **Delete Education - Example 1**
  - Status: 200
  - Education removed
  - Headers: X-User-Id required

- [ ] **Delete Education - Example 2**
  - Status: 200

### Work Experience
- [ ] **Add Work Experience - Example 1**
  - Status: 201
  - Headers: X-User-Id required
  - Response includes: id, jobTitle, companyName
  - Save work_experience_id

- [ ] **Add Work Experience - Example 2**
  - Status: 201
  - Different company

- [ ] **Add Work Experience - Example 3**
  - Status: 201
  - Currently working

- [ ] **Get All Work Experiences - Example 1**
  - Status: 200
  - Returns all work experiences

- [ ] **Get All Work Experiences - Example 2**
  - Status: 200
  - Different resume

- [ ] **Update Work Experience - Example 1**
  - Status: 200
  - Headers: X-User-Id required
  - Description updated

- [ ] **Update Work Experience - Example 2**
  - Status: 200
  - Company name updated

- [ ] **Delete Work Experience - Example 1**
  - Status: 200
  - Removed from list
  - Headers: X-User-Id required

- [ ] **Delete Work Experience - Example 2**
  - Status: 200

### Resume Skills
- [ ] **Add Resume Skill - Example 1 (Java)**
  - Status: 201
  - Headers: X-User-Id required
  - Response includes: id, skillName, proficiencyLevel
  - Save resume_skill_id

- [ ] **Add Resume Skill - Example 2 (Python)**
  - Status: 201

- [ ] **Add Resume Skill - Example 3 (Spring Boot)**
  - Status: 201
  - Intermediate level

- [ ] **Get Resume Skills - Example 1**
  - Status: 200
  - Returns all skills for resume

- [ ] **Get Resume Skills - Example 2**
  - Status: 200
  - Different resume

- [ ] **Update Resume Skill - Example 1**
  - Status: 200
  - Headers: X-User-Id required
  - Years of experience updated

- [ ] **Update Resume Skill - Example 2**
  - Status: 200
  - Proficiency level changed

- [ ] **Delete Resume Skill - Example 1**
  - Status: 200
  - Removed from skills list
  - Headers: X-User-Id required

- [ ] **Delete Resume Skill - Example 2**
  - Status: 200

---

## ✅ Application-Service (Port 5005) Testing

### Applications
- [ ] **Create Application - Example 1**
  - Status: 201
  - Headers: X-User-Id required
  - Response includes: id, status (PENDING)
  - Update jobId and resumeId with saved values
  - Save application_id

- [ ] **Create Application - Example 2**
  - Status: 201
  - Different job and resume

- [ ] **Create Application - Example 3**
  - Status: 201
  - Different candidate

- [ ] **Get Application By ID - Example 1**
  - Status: 200
  - Returns correct application
  - Includes cover letter

- [ ] **Get Application By ID - Example 2**
  - Status: 200
  - Different application

- [ ] **Get All Applications - Example 1**
  - Status: 200
  - Headers: X-User-Id required
  - Returns candidate's applications only

- [ ] **Get All Applications - Example 2**
  - Status: 200
  - Different candidate sees their apps

- [ ] **Get Applications for Job - Example 1**
  - Status: 200
  - Returns all applications for job 1
  - Count matches applications created

- [ ] **Get Applications for Job - Example 2**
  - Status: 200
  - Different job

- [ ] **Get Applications for Company**
  - Status: 200
  - Headers: X-User-Id required
  - Returns applications for employer's jobs

- [ ] **Update Status - Accept - Example 1**
  - Status: 200
  - Headers: X-User-Id required
  - Status changes to ACCEPTED
  - Only employer can update

- [ ] **Update Status - Reject - Example 2**
  - Status: 200
  - Status changes to REJECTED

- [ ] **Update Status - Under Review - Example 3**
  - Status: 200
  - Status changes to UNDER_REVIEW

- [ ] **Withdraw Application - Example 1**
  - Status: 200
  - Headers: X-User-Id required
  - Status changes to WITHDRAWN
  - Only candidate can withdraw

- [ ] **Withdraw Application - Example 2**
  - Status: 200
  - Different application

- [ ] **Toggle Star - Example 1**
  - Status: 200
  - Headers: X-User-Id required
  - Starred status toggles
  - Only employer can star

- [ ] **Toggle Star - Example 2**
  - Status: 200
  - Star status toggled

- [ ] **Delete Application - Example 1**
  - Status: 200
  - Application removed
  - Headers: X-User-Id required

- [ ] **Delete Application - Example 2**
  - Status: 200

### Application Notes
- [ ] **Add Note - Example 1**
  - Status: 201
  - Headers: X-User-Id required
  - Response includes: id, noteContent
  - Save note_id

- [ ] **Add Note - Example 2**
  - Status: 201
  - Different note

- [ ] **Add Note - Example 3**
  - Status: 201
  - Third note

- [ ] **Get All Notes - Example 1**
  - Status: 200
  - Headers: X-User-Id required
  - Returns all notes for application
  - Count matches notes added

- [ ] **Get All Notes - Example 2**
  - Status: 200
  - Different application

- [ ] **Delete Note - Example 1**
  - Status: 200
  - Note removed
  - Headers: X-User-Id required

- [ ] **Delete Note - Example 2**
  - Status: 200

---

## 🔄 Integration Tests

### Complete User Workflow
- [ ] User signup
- [ ] User login
- [ ] Update profile
- [ ] Create company (employer)
- [ ] Create jobs (employer)
- [ ] Create resume (candidate)
- [ ] Add education (candidate)
- [ ] Add experience (candidate)
- [ ] Search jobs (candidate)
- [ ] Apply for job (candidate)
- [ ] View applications (employer)
- [ ] Add notes (employer)
- [ ] Update status (employer)

### Data Integrity Checks
- [ ] Company ID in job matches created company
- [ ] Resume ID in application matches created resume
- [ ] Job ID in application matches created job
- [ ] User ID in headers matches user in database
- [ ] All relationships maintained after updates
- [ ] Deleted records not accessible

### Error Handling
- [ ] Missing required headers returns 401
- [ ] Invalid IDs return 404
- [ ] Malformed JSON returns 400
- [ ] Unauthorized user actions return 403
- [ ] Database errors return 500

---

## 📊 Test Results Summary

**Date**: _______________  
**Tester**: _______________

### Test Counts
- **Total Endpoints**: 59
- **Tested**: ___ / 59
- **Passed**: ___ / 59
- **Failed**: ___ / 59
- **Success Rate**: ___%

### Issues Found
```
Issue #1: 
Service: 
Endpoint: 
Status Code: 
Expected: 
Actual: 

Issue #2:
Service:
Endpoint:
Status Code:
Expected:
Actual:
```

### Notes
```
Additional observations, bugs, or improvements:
```

---

## ✨ Final Checklist

- [ ] All 59 endpoints tested
- [ ] All status codes verified
- [ ] All request/response formats correct
- [ ] Headers properly implemented
- [ ] Error handling working
- [ ] Integration tests passed
- [ ] No critical bugs found
- [ ] Documentation complete
- [ ] Results documented above
- [ ] Ready for deployment

---

**Tested By**: _______________  
**Date**: _______________  
**Sign-off**: _______________  

