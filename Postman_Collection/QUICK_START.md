# 🚀 Postman Collection - Quick Start Guide

## ⚡ 5-Minute Setup

### Step 1: Import Collection
1. Open **Postman**
2. Click **File** → **Import**
3. Select `Globalco_Job_Board_Platform.postman_collection.json`
4. Click **Import**

### Step 2: Verify Services Running
```bash
# Check all 5 services are running on their ports:
User-Service:     http://localhost:5001
Company-Service:  http://localhost:5002
Job-Service:      http://localhost:5003
Resume-Service:   http://localhost:5004
Application-Service: http://localhost:5005
```

### Step 3: Start Testing

Pick a folder and start making requests!

---

## 🎯 Common Testing Scenarios

### Scenario 1: Complete User Journey (10-15 minutes)

**Step 1: Authentication** (User-Service)
```
1. Run: User-Service > Authentication > Signup - Example 1 (Candidate)
2. Run: User-Service > Authentication > Login - Example 1
   → Save the JWT token from response
```

**Step 2: Create Company** (Company-Service)
```
1. Run: Company-Service > Create Company - Example 1
   → Save company ID from response
```

**Step 3: Build Job Posting** (Job-Service)
```
1. Run: Job-Service > Job Categories > Create Category - Example 1
2. Run: Job-Service > Job Skills > Create Skill - Example 1
3. Run: Job-Service > Job Tags > Create Tag - Example 1
4. Run: Job-Service > Job Management > Create Job - Example 1
   → Update companyId with your saved company ID
   → Save job ID from response
```

**Step 4: Build Resume** (Resume-Service)
```
1. Run: Resume-Service > Resume Management > Create Resume - Example 1
   → Save resume ID
2. Run: Resume-Service > Education > Add Education - Example 1
3. Run: Resume-Service > Work Experience > Add Work Experience - Example 1
4. Run: Resume-Service > Resume Skills > Add Resume Skill - Example 1
```

**Step 5: Apply for Job** (Application-Service)
```
1. Run: Application-Service > Applications > Create Application - Example 1
   → Update jobId and resumeId with your saved IDs
2. Run: Application-Service > Applications > Get All Applications - Example 1
3. Run: Application-Service > Application Notes > Add Note - Example 1
```

**Result**: You've tested the complete flow! ✅

---

### Scenario 2: Employer Hiring Flow (8-10 minutes)

**As Employer:**
```
1. Signup as Employer (User-Service > Signup - Example 2)
2. Create Company (Company-Service > Create Company)
3. Create Job (Job-Service > Job Management > Create Job)
4. Publish Job (Job-Service > Job Management > Publish Job)
5. View Applications (Application-Service > Get Applications for Company)
6. Add Notes (Application-Service > Add Note)
7. Update Status (Application-Service > Update Application Status)
```

---

### Scenario 3: Search & Filter Jobs (5 minutes)

**Test Search Features:**
```
1. Get All Jobs - No Filter
   → /api/jobs/all
   
2. Filter by Keyword
   → /api/jobs/all?keyword=Java
   
3. Filter by Salary Range
   → /api/jobs/all?minSalary=80000&maxSalary=150000
   
4. Filter by Job Type
   → /api/jobs/all?jobType=FULL_TIME
   
5. Filter by Work Mode
   → /api/jobs/all?workMode=REMOTE
```

---

## 📋 All Endpoints Summary

### User-Service (Port 5001) - 8 Endpoints
- ✅ POST /auth/signup
- ✅ POST /auth/login
- ✅ GET /api/users/profile
- ✅ PUT /api/users/update-profile
- ✅ GET /api/users/{id}
- ✅ GET /api/users/all
- ✅ PATCH /api/users/{id}/suspend
- ✅ PATCH /api/users/{id}/activate
- ✅ DELETE /api/users/{id}

### Company-Service (Port 5002) - 8 Endpoints
- ✅ POST /api/companies/create
- ✅ GET /api/companies/{id}
- ✅ GET /api/companies/my
- ✅ GET /api/companies/all
- ✅ PUT /api/companies/{id}
- ✅ PATCH /api/companies/{id}/verify
- ✅ PATCH /api/companies/{id}/deactivate
- ✅ DELETE /api/companies/{id}

### Job-Service (Port 5003) - 17 Endpoints
**Categories (5):**
- ✅ POST /api/job-categories/create
- ✅ GET /api/job-categories/all
- ✅ GET /api/job-categories/{id}
- ✅ PUT /api/job-categories/update/{id}
- ✅ DELETE /api/job-categories/{id}

**Skills (5):**
- ✅ POST /api/job-skills/create
- ✅ GET /api/job-skills/all
- ✅ GET /api/job-skills/{id}
- ✅ PUT /api/job-skills/update/{id}
- ✅ DELETE /api/job-skills/delete/{id}

**Tags (5):**
- ✅ POST /api/job-tags/create
- ✅ GET /api/job-tags/all
- ✅ GET /api/job-tags/{id}
- ✅ PUT /api/job-tags/update/{id}
- ✅ DELETE /api/job-tags/delete/{id}

**Jobs (9):**
- ✅ POST /api/jobs/create
- ✅ GET /api/jobs/{id}
- ✅ GET /api/jobs/all (with filters)
- ✅ GET /api/jobs/company/{companyId}
- ✅ PUT /api/jobs/update/{id}
- ✅ PATCH /api/jobs/publish/{id}
- ✅ PATCH /api/jobs/close/{id}
- ✅ DELETE /api/jobs/delete/{id}

### Resume-Service (Port 5004) - 16 Endpoints
**Resume (7):**
- ✅ POST /api/resumes/create
- ✅ GET /api/resumes/{resumeId}
- ✅ GET /api/resumes/all
- ✅ PUT /api/resumes/{resumeId}/update/personal-info
- ✅ PATCH /api/resumes/{resumeId}/update/summary
- ✅ PATCH /api/resumes/{resumeId}/set-default
- ✅ DELETE /api/resumes/{resumeId}/delete

**Education (4):**
- ✅ POST /api/resumes/education/{resumeId}/add
- ✅ GET /api/resumes/education/{resumeId}/all
- ✅ PUT /api/resumes/education/{resumeId}/update/{educationId}
- ✅ DELETE /api/resumes/education/{resumeId}/delete/{educationId}

**Work Experience (4):**
- ✅ POST /api/work-experiences/{resumeId}/add
- ✅ GET /api/work-experiences/{resumeId}/all
- ✅ PUT /api/work-experiences/{resumeId}/{workExperienceId}/update
- ✅ DELETE /api/work-experiences/{resumeId}/{workExperienceId}/delete

**Skills (4):**
- ✅ POST /api/resume-skills/{resumeId}/add
- ✅ GET /api/resume-skills/{resumeId}
- ✅ PUT /api/resume-skills/{resumeId}/{skillId}/update
- ✅ DELETE /api/resume-skills/{resumeId}/{skillId}/delete

### Application-Service (Port 5005) - 10 Endpoints
**Applications (9):**
- ✅ POST /api/applications/create
- ✅ GET /api/applications/{id}
- ✅ GET /api/applications/all
- ✅ GET /api/applications/job/{jobId}
- ✅ GET /api/applications/company
- ✅ PATCH /api/applications/{id}/status
- ✅ PATCH /api/applications/{id}/withdraw
- ✅ PATCH /api/applications/{id}/star
- ✅ DELETE /api/applications/{id}

**Notes (3):**
- ✅ POST /api/application-notes/{applicationId}/add
- ✅ GET /api/application-notes/{applicationId}/all
- ✅ DELETE /api/application-notes/{applicationId}/delete/{noteId}

**Total Endpoints: 59**

---

## 🔄 Status & Response Codes

| Code | Meaning | Example |
|------|---------|---------|
| 200 | OK | GET, PATCH, PUT, DELETE successful |
| 201 | Created | POST successful |
| 400 | Bad Request | Invalid JSON, missing fields |
| 401 | Unauthorized | Missing X-User-Id header |
| 404 | Not Found | Resource doesn't exist |
| 500 | Server Error | Service crashed |

---

## 💾 Example Response Formats

### Success Response (User)
```json
{
  "id": 1,
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "role": "CANDIDATE",
  "phoneNumber": "+1-555-0101",
  "createdAt": "2024-01-15T10:30:00Z"
}
```

### Success Response (Company)
```json
{
  "id": 1,
  "name": "Tech Innovations Ltd",
  "description": "Leading software development company",
  "companyType": "PRIVATE",
  "industryType": "TECHNOLOGY",
  "employeeCount": 500,
  "status": "ACTIVE",
  "verified": true,
  "createdAt": "2024-01-15T10:30:00Z"
}
```

### Success Response (Job)
```json
{
  "id": 1,
  "title": "Senior Java Developer",
  "description": "We are looking for experienced Java developers",
  "companyId": 1,
  "jobType": "FULL_TIME",
  "workMode": "REMOTE",
  "location": "San Francisco, CA",
  "minSalary": "100000.00",
  "maxSalary": "150000.00",
  "experienceLevel": "SENIOR",
  "status": "PUBLISHED",
  "noOfOpenings": 3,
  "postedDate": "2024-01-15T10:30:00Z"
}
```

---

## ❌ Common Errors & Solutions

| Error | Cause | Solution |
|-------|-------|----------|
| Connection refused | Service not running | Start all 5 services |
| 404 Not Found | Wrong endpoint | Check URL spelling |
| 401 Unauthorized | Missing header | Add X-User-Id header |
| 400 Bad Request | Invalid JSON | Check JSON format |
| 500 Server Error | Database issue | Check PostgreSQL running |

---

## 🎓 Learning Tips

1. **Start Simple**: Begin with GET requests to fetch data
2. **Then Create**: Use POST requests to create new records
3. **Then Update**: Use PATCH/PUT to modify records
4. **Finally Delete**: Use DELETE for cleanup
5. **Test Relationships**: Verify IDs link correctly

---

## 📝 Notes for Developers

- **X-User-Id**: Required for most endpoints - represents current user
- **Company ID**: Must exist before creating jobs
- **Resume ID**: Must exist before applying for jobs
- **Job ID**: Must exist before creating applications
- **Database IDs**: Start from 1 and increment

---

## 🔗 API Flow Chart

```
User Registration
    ↓
Login & Get Token
    ↓
User Profile Setup
    ↓
[Candidate Path]           [Employer Path]
    ↓                            ↓
Resume Creation            Company Creation
    ├─ Education                ├─ Company Details
    ├─ Experience               ├─ Verify Company
    ├─ Skills                   └─ Company Settings
    └─ Languages                      ↓
    ↓                           Job Creation
Search Jobs              ├─ Categories
    ↓                   ├─ Skills
Apply for Job           ├─ Tags
    ↓                   └─ Job Details
Track Applications           ↓
                        Publish Job
                            ↓
                        Manage Applications
                        ├─ View Applications
                        ├─ Add Notes
                        └─ Update Status
```

---

## 🎯 Next Steps

1. ✅ Import the collection
2. ✅ Start with User-Service endpoints
3. ✅ Create test data (users, companies, jobs)
4. ✅ Test complete workflows
5. ✅ Document any API issues
6. ✅ Share feedback with the team

---

**Happy Testing! 🎉**

For more details, refer to **README.md** in the same folder.

