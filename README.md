NY State Emergency Relief Fund Management System

A full-stack web application for managing disaster relief claims for New York State citizens featuring JWT authentication role-based access control and comprehensive WCAG 2.1 AA accessibility compliance.

=Features

-User Registration & Authentication JWT-based secure login
-Claim Submission Citizens can submit disaster relief claims
-Admin Dashboard Review approve reject claims with statistics
-Accessibility Compliant Full WCAG 2.1 AA implementation
-Responsive Design Mobile-first approach works on all devices

=Technology Stack

-Backend
Java 17
Spring Boot 3.2.1
Spring Security with JWT (HS512)
PostgreSQL 18
Maven

-Frontend
Angular 17
TypeScript
HTML5 CSS3
Reactive Forms

=Quick Start

-Backend Setup

bash
#Create database
createdb relief_db

#Configure database src/main/resources/application.yml
spring:
    datasource: 
    url: jdbc:postgresql://localhost:5432/relief_db
    username: system
    password: password

#Run backend
mvn spring-boot run

#Backend runs on http localhost 8080

-Frontend Setup

bash
cd frontend

#Install dependencies
npm install

#Run frontend
npm start

#Frontend runs on http localhost 4200

-Default Admin Account

Username: admin
Password: Admin@2025

=API Endpoints

-Authentication
POST api/auth/register - Register new user
POST api/auth/login Login and get JWT token

Claims (User)
POST /api/claims - Submit new claim
GET /api/claims/my-claims - Get user's claims
PUT /api/claims/{id} - Update claim (PENDING only)
DELETE /api/claims/{id} - Delete claim (PENDING only)

Claims (Admin)
GET /api/claims/all - Get all claims
GET /api/claims/pending - Get pending claims
GET /api/claims/statistics - Get statistics
POST /api/claims/{id}/approve - Approve claim
POST /api/claims/{id}/reject - Reject claim

-Accessibility Features

WCAG 2.1 Level AA compliant
ARIA labels and roles
Full keyboard navigation
Screen reader compatible NVDA JAWS
High contrast mode
Focus indicators
Skip to content links
Minimum 44x44px touch targets


Security

BCrypt password hashing
JWT token authentication (HS512)
Role-based access control (USER ADMIN)
CORS configured for localhost development
Input validation on frontend and backend

