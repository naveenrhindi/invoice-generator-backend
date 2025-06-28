# 🧾 Invoice Generator (Backend)

🔗 **Live Frontend**: [https://invoice-generator-frontend-theta-ten.vercel.app](https://invoice-generator-frontend-theta-ten.vercel.app)  
📦 **Frontend GitHub Repo**: [invoice-generator-client](https://github.com/naveenrhindi/invoice-generator-client)

> The **backend service** of a full-stack invoice generator app that powers secure invoice creation, email dispatch, image uploads, and JWT-based authentication for freelancers and businesses.

---

## 📌 Features

- 📤 Create & save invoices  
- 📧 Send invoices via email  
- 🧾 Preview printable invoices  
- ☁️ Upload and store logos securely  
- 🔐 JWT-based authentication with Clerk  
- 🌐 CORS-enabled backend for frontend communication  
- 💾 Data persistence using MongoDB Atlas  
- 🚀 Deployed using Railway

---

## 🧑‍💻 Tech Stack

| Layer            | Tools & Technologies                            |
| ---------------- | ----------------------------------------------- |
| **Framework**    | Spring Boot (Java 21)                           |
| **Database**     | MongoDB Atlas (Cloud-hosted NoSQL)              |
| **Auth**         | [Clerk.dev](https://clerk.dev) (JWT, Webhooks)  |
| **Email**        | Java Mail + Gmail SMTP                          |
| **Image Upload** | [Cloudinary](https://cloudinary.com)            |
| **Deployment**   | [Railway](https://railway.app)                  |
| **Versioning**   | Git & [GitHub](https://github.com/naveenrhindi) |

---

## 🌍 Live API Base URL

https://invoice-generator-backend-production-cc2f.up.railway.app/api

Use this as the base URL for all frontend API requests.

---

## 🔐 Security & Config

- 🔒 **JWT Auth** via Clerk.dev  
- ✅ **Webhook Verification** for Clerk events  
- 🔑 Secrets managed using Railway Environment Variables  
- 🌐 **CORS Allowed Origin** set via properties (`frontend.url`)

---

## ⚙️ Environment Variables (`.env`)

```env
# MongoDB
MONGODB_URI=your-mongodb-uri

# Mail Sender (Java Mail)
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-password

# Clerk Authentication
CLERK_ISSUER=https://your-clerk-subdomain.accounts.dev
CLERK_JWKS_URL=https://your-clerk-subdomain.accounts.dev/.well-known/jwks.json
CLERK_WEBHOOK_SECRET=your-webhook-secret

# Frontend URL
FRONTEND_URL=https://invoice-generator-frontend-theta-ten.vercel.app
```


## 🧪 Running Locally
## 🖥️ Prerequisites

- Java 21+
- Maven installed
- MongoDB URI (Atlas or Local)
- Active Clerk & Cloudinary accounts


## ⚙️ Steps
```
# 1. Clone the backend repository

git clone https://github.com/naveenrhindi/invoice-generator-backend.git
cd invoice-generator-backend

# 2. Set up environment variables on Railway (or create `.env`)

# 3. Build the application
./mvnw clean install

# 4. Run the backend locally
java -jar target/invoice-generator-backend-0.0.1-SNAPSHOT.jar
```

## 📁 Folder Structure
```
src/
├── config/              # General config (CORS, beans, etc.)
├── controller/          # REST endpoints and request mappings
├── entity/              # MongoDB schema models
├── repository/          # Interfaces for database operations
├── security/            # JWT filters, auth providers, Clerk config
├── service/             # Core business logic and implementations
```


## 🧰 Tooling & Integrations

| Purpose            | Tool Used                                 |
| ------------------ | ----------------------------------------- |
| 🚀 Deployment      | [Railway](https://railway.app)            |
| 🛡️ Authentication | [Clerk.dev](https://clerk.dev)            |
| ☁️ Image Upload    | [Cloudinary](https://cloudinary.com)      |
| 📧 Mail Service    | Gmail + JavaMail                          |
| 🔄 Source Control  | [GitHub](https://github.com/naveenrhindi) |


## 🧠 Why Use This?

- ✅ Secure – JWT Auth, CORS configured, and webhooks verified
- 📈 Scalable – Built with Spring Boot and MongoDB
- ☁️ Cloud-first – Fully deployed and integrated in the cloud
- 🛠️ Modular – Clear service structure and REST design
- 🔗 Seamless Integration – Frontend communicates via REST APIs
- 💼 Resume-Ready – Shows experience with real-world stack and deployment

## 👨‍💼 Author

Created with ❤️ by **Naveen Ravindra Hindi**  
📧 Email: [naveenhindi4@gmail.com](mailto:naveenhindi4@gmail.com)  
🔗 Backend Repo: [invoice-generator-backend](https://github.com/naveenrhindi/invoice-generator-backend)  
🔗 Frontend Repo: [invoice-generator-client](https://github.com/naveenrhindi/invoice-generator-frontend)

