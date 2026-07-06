# Autolink — Enterprise Module

This repository contains **my personal contribution** to **Autolink**, a university team project developed by a group of five students.

Autolink is a platform that connects users and companies involved in the recycling and resale of vehicle parts.

> **Note:** This repository does **not** contain the complete application. It only includes the module I designed and implemented. The full team project is available here:

**Full project:** https://github.com/AutolinkTechX

---

# My Contribution

I was responsible for designing and implementing the **Enterprise Module**, including authentication, enterprise management, partnerships, contracts, and marketplace integration.

## Features

### Authentication

- Multi-role authentication
- User / Enterprise / Administrator accounts
- Symfony Security
- Dedicated firewalls
- Role-Based Access Control (RBAC)
- Dynamic navigation menus based on user roles

---

### Enterprise Profiles

- Company profile management
- Image upload
- Embedded address management
- Profile editing

---

### Partnership Management

Companies can:

- Send partnership requests
- Accept requests
- Reject requests
- View pending partnerships

Each request contains:

- Company information
- Partnership type
- Tax identification
- Description

---

### Contracts

Implemented an enterprise-to-enterprise contract system.

Contracts can only be created when an accepted partnership already exists between both companies.

---

### Marketplace Integration

Implemented enterprise-side marketplace management for recycled vehicle parts.

Entities include:

- Article
- MaterielRecyclable

---

### External Integrations

- QR Code generation
- WhatsApp notifications using Twilio API

---

# Technologies

- Symfony
- PHP
- Doctrine ORM
- MySQL
- Twig
- Twilio API
- chillerlan/php-qrcode
- Javascript

---

# Skills Demonstrated

- Backend Development
- Authentication & Authorization
- Role-Based Access Control
- Database Design
- Doctrine ORM
- Symfony Security
- MVC Architecture
- REST-style CRUD Development
- Third-party API Integration

---

# Team Project

This module was developed as part of a **five-member university software engineering project**.

This repository showcases **only my individual contribution**, while the complete application was developed collaboratively.

