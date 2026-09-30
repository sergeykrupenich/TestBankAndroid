# Test Bank App

A modern, secure Android banking application built with Kotlin and Jetpack Compose. The application demonstrates multi-currency account management, authentication, and deposit flows following Clean Architecture principles.

---

## Tech Stack & Libraries

- **Language:** [Kotlin](https://kotlinlang.org/)
- **UI Framework:** [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material 3)
- **Architecture:** Clean Architecture + MVVM
- **Dependency Injection:** [Koin](https://insert-koin.io/)
- **Networking:** [Retrofit](https://square.github.io/retrofit/) + [kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization)
- **Security:** Android KeyStore + Encrypted DataStore for JWT/session management
- **Async & Reactive Operations:** Kotlin Coroutines & StateFlow

---

## Architecture Decisions

The project follows **Clean Architecture** guidelines, separation of concerns, and unidirectional data flow (UDF):

```text
com.example.testbankapp
├── core/                   # Shared utilities and network handling
├── data/                   # Data Layer
│   ├── api/                # Retrofit Interfaces & Interceptors (AuthInterceptor, AccountApi, AuthApi)
│   ├── dto/                # Data Transfer Objects & Custom Serializers (BigDecimalSerializer)
│   ├── local/              # Token & KeyStore Security Management (SecurityCryptoManager, TokenManager)
│   └── repository/         # Repository Implementations (AccountRepositoryImpl, AuthRepositoryImpl)
├── domain/                 # Domain Layer (Pure Business Logic)
│   ├── model/              # Core Domain Entities (Account, etc.)
│   └── usecase/            # Application Use Cases (GetAccountsUseCase, DepositFundsUseCase, etc.)
├── di/                     # Koin Dependency Injection Modules (AppModule)
└── presentation/           # Presentation Layer (UI & ViewModels)
    ├── accounts/           # Multi-currency Account Management UI
    ├── auth/               # Login & Registration Flow UI
    └── deposit/            # Deposit BottomSheet Component
    
```

## Key Architectural Highlights:
Security First: Session JWTs are securely handled via SecurityCryptoManager and TokenManager, taking advantage of the Android KeyStore for encryption. AuthInterceptor automatically attaches tokens to authenticated endpoints.

Flexible JSON Parsing: Implemented a custom BigDecimalSerializer using `kotlinx.serialization` to safely handle mixed JSON number/string responses without floating-point precision issues.

Unidirectional Data Flow: ViewModels expose read-only StateFlow states consumed by Compose screens, ensuring state stability during re-compositions.

##  Development Trade-offs & Notes
To keep the test assignment focused on core functional requirements and code execution speed, the following simplifications were intentionally made:

Hardcoded UI Strings (No R.string Resources): Text strings and labels are hardcoded inside Composable functions rather than being extracted into strings.xml. In a production app, string resources and localization frameworks would be used.

No Separate Data Mappers: DTOs are mapped directly or reused across domain/data boundaries to avoid boilerplate. In a production environment, dedicated Mappers (AccountDto.toDomain()) would be implemented to keep layers completely decoupled.

Simplified Navigation: Instead of introducing Jetpack Navigation Compose graph overhead, state-driven conditional rendering / straightforward callbacks were used to simplify navigation management between screens.

##  Build & Run Instructions
### Prerequisites
- Android Studio: Ladybug (2024.2.1) or newer
- JDK: Version 17
- Min SDK: 24+
- Target SDK: 34+

Steps to Run
### Clone the Repository:

```Bash
git clone [https://github.com/your-username/TestBankApp.git](https://github.com/your-username/TestBankApp.git)
cd TestBankApp
```

Open in Android Studio: Open the cloned folder and let Gradle sync dependencies.

Run the App:

Select an Android Emulator or physical device (Android 7.0 / API 24 or higher).

Press Run 'app' (Shift + F10 / Control + R).

### Architecture schema
```text
+-----------------------------------------------------------------------------------+
|                                PRESENTATION LAYER                                 |
|                                                                                   |
|   +-----------------------+   +----------------------+   +--------------------+   |
|   |      auth/            |   |      accounts/       |   |      deposit/      |   |
|   | LoginScreen           |   | AccountListScreen    |   | DepositBottomSheet |   |
|   | AuthViewModel         |   | AccountsViewModel    |   |                    |   |
|   +-----------+-----------+   +----------+-----------+   +---------+----------+   |
|               |                          |                         |              |
|               +--------------------------+-------------------------+              |
|                                          | (Observes State / Triggers Events)     |
|                                          v                                        |
|                             +--------------------------+                          |
|                             |  MainActivity /          |                          |
|                             |  MainViewModel           |                          |
|                             +------------+-------------+                          |
+------------------------------------------|----------------------------------------+
|
v Calls Use Cases
+-----------------------------------------------------------------------------------+
|                                   DOMAIN LAYER                                    |
|                                                                                   |
|  +-----------------------------------------------------------------------------+  |
|  |                                  usecase/                                   |  |
|  |  LoginUseCase       | RegisterUseCase        | IsUserLoggedInUseCase        |  |
|  |  LogoutUseCase      | InitSessionUseCase     | GetAccountsUseCase           |  |
|  |  DepositFundsUseCase| CreateAccountUseCase                                  |  |
|  +---------------------------------------+-------------------------------------+  |
|                                          |                                        |
|  +---------------------------------------v-------------------------------------+  |
|  |                           model/ (Domain Models)                            |  |
|  |                       DomainModels | TransactionResult                      |  |
|  +-----------------------------------------------------------------------------+  |
+------------------------------------------^----------------------------------------+
| Implements Interfaces / Uses Models
|
+------------------------------------------+----------------------------------------+
|                                    DATA LAYER                                     |
|                                                                                   |
|  +-----------------------------------------------------------------------------+  |
|  |                                 repository/                                 |  |
|  |                 AuthRepositoryImpl | AccountRepositoryImpl                  |  |
|  +---------------------------------------+-------------------------------------+  |
|                                          |                                        |
|                 +------------------------+------------------------+               |
|                 v                                                 v               |
|  +------------------------------+               +------------------------------+  |
|  |         local/               |               |         api/                 |  |
|  | SecurityCryptoManager        |               | AuthApi | AccountApi         |  |
|  | TokenManager                 |               | AuthInterceptor              |  |
|  +------------------------------+               +--------------+---------------+  |
|                                                                |                  |
|                                                 +--------------v---------------+  |
|                                                 |         dto/                 |  |
|                                                 | DTOs & BigDecimalSerializer  |  |
|                                                 +------------------------------+  |
+-----------------------------------------------------------------------------------+

+-----------------------------------------------------------------------------------+
|                            CROSS-CUTTING / INFRASTRUCTURE                         |
|                                                                                   |
|   +----------------------------+                 +----------------------------+   |
|   | core/ Resource.kt          |                 | di/ AppModule.kt (Koin)    |   |
|   +----------------------------+                 +----------------------------+   |
+-----------------------------------------------------------------------------------+
```
