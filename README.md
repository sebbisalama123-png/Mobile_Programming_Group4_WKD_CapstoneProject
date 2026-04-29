# Ndejje University SACCO Mobile Application

This is the Capstone Project for the Mobile Programming (BCS 2201 / BIT 2205) course. The project serves as a digital solution for members of the Ndejje University community to manage their savings, loans, and accounts.

## Team Members & Roles

| Name | Role | Student ID |
| :--- | :--- | :--- |
| Sebbi Salama | **Lead Developer** | 24/2/306/W/036 |
| Amanya Godfrey | **UI/UX Specialist** | 24/2/314/W/674 |
| Sebbi Salama | **Git and Quality Manager** | 24/2/306/W/036 |
| Kitatta Emmanuel | **Testing and QA Engineer** | 24/2/306/W/082 |
| Ssemayengo Joseph | **Documentation and Research Lead** | 24/2/314/W/214 |

## Technical Architecture

The application is built entirely in Kotlin and adheres to the **MVVM (Model-View-ViewModel)** architectural pattern. 
- **UI Framework:** Jetpack Compose (Material 3 styling)
- **Data Persistence:** Room Database
- **Asynchronous Operations:** Kotlin Coroutines & Flow
- **Navigation:** Compose Navigation

## Testing and Quality Assurance Summary

**Authored by: Testing and Quality Assurance Engineer**

To ensure the application meets professional coding standards and functions correctly under various scenarios, both unit tests and integration tests were designed and executed covering the core features of the application.

### What Was Tested

1. **State Management & Authentication (Unit Test)**:
   - **Target**: `AuthViewModel.kt`
   - **Description**: Tested the logic flow and state emission (`AuthUiState`) during the user login process using `kotlinx-coroutines-test`, `Turbine`, and `MockK`. We verified that providing empty credentials correctly emits an `Error` state, and that providing valid credentials communicates with the repository and emits a `Loading` state followed by a `Success` state.

2. **Database Integrity & Data Persistence (Integration Test)**:
   - **Target**: `SaccoRepository.kt` (using `Room.inMemoryDatabaseBuilder`)
   - **Description**: Tested the core member registration function (`registerUser`). We verified that executing a registration successfully persists a new `UserEntity`, correctly seeds a `MemberAccountEntity` with opening balances, and generates the initial `TransactionEntity` with accurate timestamps and descriptions. We also tested edge cases such as attempting to register a duplicate phone number.

### Outcomes

- All local unit tests for the `AuthViewModel` pass successfully, confirming that the UI state correctly mirrors the application logic without regressions.
- The `SaccoRepository` instrumented tests pass successfully, guaranteeing that Room database transactions strictly maintain referential integrity and seed required records upon user creation. No data leakage or unhandled SQLite exceptions occur during the tested core functions.


# SaccoMobileApplication

An Android mobile application for managing a Savings and Credit Cooperative Organisation (SACCO). Members can register, log in, apply for loans, top up savings, and view their transaction history — all from their phone.

## Features

### Member
- *Register / Login* — phone number and password authentication with local hashing
- *Forgot Password* — reset password by verifying ID number and phone number
- *Dashboard* — savings balance, share capital, and quick-action shortcuts
- *Loan Application* — apply for a loan product; status tracked as Pending / Approved / Rejected
- *My Loans* — view all personal loan requests and their current status
- *Top Up Savings* — record a savings deposit
- *Transaction Ledger* — mini-statement of all transactions
- *Profile* — view account details
- *Change Password*
- *Settings*

### Admin
- *Admin Panel* — manage members, review and approve/reject loan requests

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose + Material 3 |
| Navigation | Navigation Compose |
| Architecture | MVVM (ViewModel + UiState) |
| Local Database | Room (SQLite) |
| Async | Kotlin Coroutines |
| Build | Gradle with KSP |

## Project Structure


app/src/main/java/com/ndejje/saccomobileapplication/
├── model/           # Data layer (Room, API, Repositories)
├── view/            # UI layer (Jetpack Compose screens)
├── viewmodel/       # Logic layer (State management)
├── AppNavigation.kt # Navigation graph and routes
├── MainActivity.kt  # Entry point activity
└── SaccoApplication.kt # Application class for DI/Initialization


## Getting Started

### Prerequisites
- Android Studio Hedgehog or later
- Android SDK 35 (compile), minimum SDK 24 (Android 7.0)
- JDK 11

### Build & Run
1. Clone the repository.
2. Open the project in Android Studio.
3. Let Gradle sync and resolve dependencies.
4. Run on an emulator or physical device (API 24+).

No external backend or API keys are required — all data is stored locally via Room.

## Database Schema

| Table | Key Columns |
|---|---|
| users | userId, fullName, phoneNumber, idNumber, passwordHash |
| member_accounts | userId, accountNumber, savingsBalance, shareCapital |
| loan_requests | requestId, userId, loanProduct, amount, status, createdAt |
| transactions | (see TransactionEntity) |

## Author

*Group 04-WKD* — Year 2, Semester 2 Mobile Programming Project
