# ExpenseKar 💰



A simple Android expense tracker built from scratch using **Kotlin, Jetpack Compose, ViewModel, StateFlow, and Room**.



The purpose of this project is not just to build an expense tracker, but to learn how a real Android application works from the UI all the way down to persistent local storage.



---



## 📱 Current Features



- Add income and expense transactions

- Select transaction category

- Select payment method

- Select transaction date

- Add notes

- Display saved transactions

- Persist transactions locally using Room

- Transactions remain available after closing and reopening the app



---



## 🛠️ Technologies Used



| Technology | Purpose |

|---|---|

| Kotlin | Programming language |

| Jetpack Compose | User interface |

| Material 3 | UI components |

| ViewModel | Holds application/screen state |

| StateFlow | Exposes changing data to the UI |

| Room | Local database |

| KSP | Room code generation |

| Gradle | Build system and dependency management |

| Android Studio | Development environment |



---



# 🏗️ Project Architecture



ExpenseKar currently follows a simple layered structure:
```text
User
  ↓
Jetpack Compose UI
  ↓
ViewModel
  ↓
Repository

  ↓
DAO

  ↓
Room Database

  ↓

SQLite
