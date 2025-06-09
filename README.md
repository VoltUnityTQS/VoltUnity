# VoltUnity - Unified EV Charging Platform

## a) Project Abstract  
**VoltUnity** is a medium-scale microservice platform that unifies discovery, booking, charging and payment of electric-vehicle (EV) stations across multiple operators. Drivers can search and reserve charging slots in real time, unlock chargers from their phone, view consumption and CO₂-savings dashboards, and choose between pay-per-use or subscription plans. Operators get tools to register and manage stations, monitor usage trends and export reports. A public API and sandbox enable third-party integrations.

---

## b) Project Team
| Student Name            | Nº MEC    | Role(s)                        |
|-------------------------|---------------|--------------------------------|
| Diogo Gaitas            | 73259         | Product Owner |
| Giovanni Santos         | 115637        | QA Engineer |
| Rafael Semedo           | 115665        | Team Coordinator |

--- 

## c) Project Bookmarks  

- **Project Backlog:**  
  https://ua-team-mdrm30xm.atlassian.net/jira/software/projects/CRM/boards/1?atlOrigin=eyJpIjoiYjFkNzA5ZTlhNzVkNDFmYjlkZjVhNWZlNGUxY2FlNWUiLCJwIjoiaiJ9
- **GitHub Repository:**  
  https://github.com/VoltUnityTQS/VoltUnity
- **API Documentation (Swagger UI):**  
  http://localhost:8080/swagger-ui/index.html
- **Static Analysis Dashboard (SonarCloud):**  
  https://sonarcloud.io/project/overview?id=VoltUnityTQS_VoltUnity
- **Product specification report**  
  - [Product Specification Report](./docs/TQS_Product%20specification%20report.pdf)
- **Quality Assurance manual**
  - [Quality Assurance manual](./docs/TQS_Quality%20Assurance%20manual.pdf)
- **Server URL:**
  http://deti-tqs-16

---

## Run Container

```sh
docker compose up -d --build
```

## Close container

```sh
docker compose down -v
```