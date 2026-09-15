# 📄 PROJECT STATEMENT: CAMPUS LOST & FOUND MANAGEMENT SYSTEM

**Course:** Java Programming & Object-Oriented Software Engineering  
**Evaluation Program:** VITyarthi - Build Your Own Project (Flipped Course Evaluation)  
**Academic Institution:** Vellore Institute of Technology (VIT)  
**Project Title:** Campus Lost & Found Management System  

---

## 🎯 1. Problem Statement
Across higher-education residential campuses with tens of thousands of active students and staff members, hundreds of personal items—including student identification cards, laptops, scientific calculators, vehicle keys, and wallets—are misplaced or found daily across lecture halls, computer laboratories, libraries, cafeterias, and sports arenas. 

The existing manual process relies on informal campus WhatsApp or Telegram groups, physical notice boards, and scattered security desk logbooks. This ad-hoc approach suffers from acute limitations:
1. **Low Recovery Rate:** Unclaimed items lie forgotten at various security gates without a central searchable registry.
2. **Identity Fraud & False Claims:** Without verified security questions or proof of ownership, unauthorized individuals can falsely claim high-value electronics.
3. **No Automated Inventory Lifecycle:** Unclaimed belongings clutter campus storage rooms indefinitely because security staff have no automated background mechanism to archive expired listings.
4. **Lack of Transparent Audit Trails:** Students have no way of knowing whether an item turned in at a distant building was logged into university records.

The **Campus Lost & Found Management System** resolves these pain points by offering a unified, student-focused terminal application backed by MySQL/JDBC persistence, concurrent background expiration daemons, and transparent ownership verification workflows.

---

## 🌐 2. Scope of the Project
The system automates the end-to-end recovery lifecycle across the entire university campus:
- **Student & Faculty Self-Service:** Allows students to report lost belongings with last-seen locations, descriptions, and optional recovery rewards, or search the existing inventory using keyword queries.
- **Security & Staff Handover Desk:** Empowers campus security guards and laboratory technicians to log found items into a centralized database, record secure holding locations, and set verification challenge questions.
- **Ownership Verification & Claim Engine:** Mediates the claim process by matching claimant proof against registered item details and transitioning statuses from `UNCLAIMED` to `PENDING_VERIFICATION` and `CLAIMED`.
- **Background Multithreaded Archiving:** Automatically runs a background daemon thread (`AutoArchiveWorkerThread`) to scan and archive items unclaimed for over 30 days without halting user interactions.
- **Persistent Storage & File I/O Reporting:** Ensures full data durability via MySQL JDBC CRUD operations and exports formatted monthly recovery summaries using Java Character Streams (`PrintWriter`/`FileWriter`).

---

## 👥 3. Target Users
1. **University Students:** Report lost items, search the active database, track the status of misplaced items, and submit claims for their belongings.
2. **Campus Security Officers & Block Wardens:** Register items found in common areas, record the physical storage locker, evaluate ownership claims, and officially release items to verified students.
3. **Campus Administrators & Department Heads:** Audit recovery rates, review unresolved losses, and generate monthly campus recovery reports for institutional record-keeping.

---

## 🌟 4. High-Level Features
- 📦 **Dual Item Registration (CRUD):** Separate, specialized data capture for lost items (with reward and roll number) and found items (with holding location and verification questions).
- 🔍 **Real-Time Keyword Search & Undo Stack:** Instant in-memory search using `ArrayList` paired with `java.util.Stack` to track recent search history and provide undo capability.
- 🛡️ **Two-Tier Ownership Verification:** Prevents unauthorized claims by mandating secret answer verification before marking belongings as claimed.
- 🧵 **Asynchronous Background Archiving:** Concurrently scans inventory on a dedicated thread to archive obsolete listings without slowing down terminal operations.
- 💾 **Robust MySQL Database Integration:** Full JDBC implementation featuring `PreparedStatement`, `Statement`, and `ResultSet` with graceful fallback to in-memory persistence.
- 🖨️ **File I/O Summary Exporter:** Exports comprehensive, formatted campus recovery audit summaries using Character-oriented and Byte-oriented streams.
