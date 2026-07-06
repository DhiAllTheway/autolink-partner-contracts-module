# 🚗 Autolink — Enterprise & Desktop Modules

This repository contains my personal contribution to **Autolink**, a university team project developed by a group of five students.  
Autolink is a platform that connects users and companies involved in the recycling and resale of vehicle parts.

> ⚠️ **Note:** This repository does not contain the complete application. It only includes the modules I designed and implemented (**Symfony Web Module + JavaFX Desktop Admin Client**). The full team project is available here:  
> 🔗 **Full project:** [https://github.com/AutolinkTechX](https://github.com/AutolinkTechX)

---

## 🧠 My Contribution

I was responsible for designing and implementing the **Enterprise Module** across two distinct platforms: the **Symfony Web Backend** and a complementary **JavaFX Desktop Management Client**. This architecture handles authentication, enterprise management, corporate partnerships, contracts, and marketplace inventory.

---

## 🌟 Key Features

### 📂 Repository Layout
* **Web Core:** Implemented inside the root project directory using the **Symfony** framework.
* **Desktop Core:** Completely isolated inside the `autolink-desktop/` directory using **JavaFX**.

### 🔐 Authentication & Access Control
* **Multi-role accounts:** Strict separation between User, Enterprise, and Administrator accounts.
* **Symfony Security:** Configured dedicated firewalls and Role-Based Access Control (RBAC).
* **Dynamic Navigation:** Front-end templates change instantly based on user clearance.

### 🏢 Enterprise Profiles
* Fully operational company profile management (including image uploading and profile editing).
* Embedded physical address management sub-forms.
* **Desktop Exclusive:** Interactive visual profile cards to easily view data from the administration panel.

### 🤝 Partnership Management
* **Corporate Workflows:** Allowed companies to send, accept, reject, and keep track of live partnership statuses.
* **Detailed Record Payloads:** Every request captures company details, partnership classification type, official tax identification, and rich text descriptions.
* **Desktop Tools:** Clean JavaFX modal form dialogs for creating and processing corporate applications.

### 📜 B2B Contract Engine
* Implemented a secure enterprise-to-enterprise contract system.
* **Business Logic:** Contracts can strictly only be initialized if a mutual, accepted partnership already exists between the two entities.
* **Desktop Integration:** Real-time administrative contract summary generator embedded within JavaFX layouts.

### 🛒 Marketplace Integration
* Full backend control over enterprise-side inventory tracking for recycled vehicle components.
* Core data entities managed: `Article` and `MaterielRecyclable`.

### 🔌 Hardware & Third-Party APIs
* **Dynamic QR Codes:** Multi-platform data string parsing deployed across both Symfony twig layers and JavaFX canvas view listeners.
* **Automated Notifications:** Dispatched immediate WhatsApp updates via the **Twilio API** during critical pipeline changes.
* **Document Exporters:** Native corporate printing layouts and professional PDF exporting powered by **Apache PDFBox** on the desktop side.

---

## 🛠️ Technology Stack

* **Web & Backend:** PHP, Symfony, Doctrine ORM, Twig, JavaScript
* **Desktop Client:** Java, JavaFX, Apache PDFBox
* **Database & Integrity:** MySQL
* **Third-Party Integrations:** Twilio API, `chillerlan/php-qrcode`

---

## 📈 Engineering Skills Demonstrated

* Core Backend Systems & MVC Architecture
* Cross-Platform Desktop Development (JavaFX UI/UX)
* Advanced Security Schemes & Access Control List (ACL) Policies
* Relational Database Normalization & Object-Relational Mapping (ORM)
* Clean REST-style CRUD Design
* Third-Party API Engineering & Integration

---

## 👥 Team Project Note

This module was developed as part of a five-member university software engineering project curriculum. This repository purposefully showcases **only my individual code contributions**, while the final umbrella application was built collaboratively.
