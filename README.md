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
