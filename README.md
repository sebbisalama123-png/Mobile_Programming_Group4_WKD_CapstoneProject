# Unity Sacco: Mobile Programming Capstone

### **Project Identity**
**App Name:** Unity Sacco  
> ### ** [▶️ Watch: Unity Sacco Mobile App Demonstration](https://youtu.be/V-113RGYXfI?si=zeoyxHPODluUq4uU)**  
> **Featured Showcase:** *A role-based technical walkthrough (Problem Pitch, UI/UX, Architecture, QA and Happy Path).*

---

## Team Roster
| Name | Student ID | Clearly Defined Role |
| :--- | :--- | :--- |
| **Sebbi Salama** | 24/2/306/W/036 | Lead Developer & Git/Quality Manager |
| **Kitatta Emmanuel** | 24/2/306/W/082 | Testing and QA Engineer |
| **Amanya Godfrey** | 24/2/314/W/674 | UI/UX Specialist |
| **Ssemayengo Joseph** | 24/2/314/W/214 | Documentation and Research Lead |

---

## Feature Set
*   **Secure Authentication:** Member registration and login with local password hashing.
*   **Digital Dashboard:** Real-time view of savings balance, share capital, and quick actions.
*   **Loan Management:** End-to-end application process with status tracking (Pending/Approved/Rejected).
*   **Transaction Ledger:** Automated mini-statements of all savings and loan activities.
*   **Admin Control Panel:** Specialized view for managing members and approving/rejecting loan requests.
*   **Account Security:** Password reset via ID verification and internal profile management.

---

## Technical Stack
*   **Language:** Kotlin
*   **UI Framework:** Jetpack Compose (Material 3)
*   **Architecture:** MVVM (Model-View-ViewModel)
*   **Asynchronous Logic:** Kotlin Coroutines & Flow
*   **Local Persistence:** Room Database (SQLite)
*   **Navigation:** Jetpack Navigation Component (Compose)
*   **Dependency Processing:** KSP (Kotlin Symbol Processing)

---

## QA Summary
The application underwent rigorous testing to ensure data integrity and state reliability. Below is the summary of core test cases executed:

### **Test Cases vs. Results**
| Test Case ID | Feature Tested | Description | Result |
| :--- | :--- | :--- | :--- |
| TC-01 | **Authentication** | Validate login with empty/invalid credentials | **PASS** |
| TC-02 | **State Management** | Verify `AuthUiState` transitions (Loading -> Success) | **PASS** |
| TC-03 | **Registration** | Ensure `UserEntity` persists with atomic name values | **PASS** |
| TC-04 | **Data Integrity** | Verify automatic seeding of `MemberAccount` on signup | **PASS** |
| TC-05 | **Loan Logic** | Prevent duplicate loan submissions while one is "Pending" | **PASS** |
| TC-06 | **Room Persistence** | Verify data survival after application process kill | **PASS** |

**Technical Audit:** Unit tests were implemented using `Turbine` and `MockK` for ViewModels, while `Room.inMemoryDatabaseBuilder` was utilized for repository integration tests to prevent data leakage.

---

## Build & Run
1. Clone the repository.
2. Open the project in Android Studio.
3. Let Gradle sync and resolve dependencies.
4. Run on an emulator or physical device (API 24+).

---
*© 2026 Ndejje University - Faculty of Science and IT (Group 04-WKD)*
