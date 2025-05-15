# 📊 Lectio - Track Your Reading Journey

**Lectio** is a book tracker app with a modern android app designed to help book enthusiasts track their reading progress, manage their library, and receive reading reminders. Built with Jetpack Compose and powered by WorkManager, Lectio offers a clean, intuitive interface and robust functionality for personal book management. Lectio is a personal project developed to explore Android development with Compose, navigation, clean architecture, MVVM architecture, and background tasks. It allows users to add books, view and filter their library, edit entries, and clear their data, all while supporting customizable themes and notifications.

[🎥 Watch the Lectio Demo](https://drive.google.com/file/d/149ebK5TOMMkh2mPCIOIUlUOT6VC-mJCu/view?usp=drive_link)

## ⚠️ Current Limitations
- No backup/restore or sync with external services
  

## 🔧 Future Plans
- Backup and restore (local or cloud-based)
- Add integration with Google Books or Goodreads for metadata

## Features
- **Book Management**: Add, view, edit books, and delete books.
- **Dashboard Stats**: Display key book statistics (total currently reading books, total read pages, and total finished books) on the Dashboard for a quick overview.
- **Bottom Navigation**: Seamless bottom navigation between Dashboard, Library, Add Book, Favorites, and Settings screens.
- **Library Search & Filter**: Search and Filter books in the Library screen by title, author, or other criteria for easy access.
- **Favorites**: Mark books as favorites for quick access.
- **Reading Notes & Rating**: Add personal notes and ratings to finished books.
- **Nested Screens**: Access detailed book views and edit screens with proper back stack management.
- **Custom Settings**: Toggle notifications, switch between light, dark, and system themes, and clear all data with confirmation.
- **Reading Reminders**: Scheduled reminders via WorkManager (configurable).
- **User Feedback**: Inline snackbars for actions like adding books or clearing data.
- **Responsive Design**: Built with Jetpack Compose for a modern, adaptive UI.

## 🧱 Architecture Overview

Lectio follows **Clean Architecture** with an **MVVM** pattern and **Unidirectional Data Flow**, using:

- `ViewModel` + `StateFlow` for state management
- `UseCases` for domain logic
- `Repository` pattern for data abstraction
- `Hilt` for dependency injection

---

## 🛠️ Tech Stack

- **Language**: Kotlin
- **UI**: Jetpack Compose
- **Navigation**: Jetpack Navigation Component
- **Background Tasks**: WorkManager
- **Dependency Injection**: Hilt
- **State Management**: ViewModel + StateFlow
- **Local Storage**: PreferencesManager (for theme/notification settings)
  

## 📸 Screenshot
**Splash Screen**  
![Splash Screen](https://drive.google.com/uc?export=view&id=1kz0ZcQSbgwbjDTdlBp7eyJjC2MV74HSr)

**Dashboard Screen**  
![Dashboard Screen](https://drive.google.com/uc?export=view&id=17drZqJS39PTkSjhR1_6KtA7hn6uQJV-m)

**Library Screen**  
![Library Screen](https://drive.google.com/uc?export=view&id=1kwXp_iWq3BwpPJ9cc7xubBVOMl025LIw)

**Add Book Screen**  
![Add Book Screen](https://drive.google.com/uc?export=view&id=1u5bEKsE4sH660jdA2uq8YxifoX8qU9ZO)

**Favorite Screen**  
![Favorite Screen](https://drive.google.com/uc?export=view&id=1JhtVClYgcqZr_OnwGCADAkOaXNCNv2qI)

**Settings Screen**  
![Settings Screen](https://drive.google.com/uc?export=view&id=12tF4hefy83RS6mVNjVBG_NZst9A4NAGM)

**Detail Book Screen**  
![Detail Book Screen](https://drive.google.com/uc?export=view&id=1H7WyX0_53J9aQQh5-XcyCArhF3uL-Z63)

**Edit Book Screen**  
![Edit Book Screen](https://drive.google.com/uc?export=view&id=1AQqF6Y2SlrKYjdLq2Y-Uy9sc1xWA5Ash)

**Search View**  
![Search View](https://drive.google.com/uc?export=view&id=1XB5VLlvNznVtU2FdTuYpyAP_3qhMyHIt)

**Notification View**  
![Notification View](https://drive.google.com/uc?export=view&id=1NI-I4o8Sytf0rn2Kggf_D4xvfsiIr1oH)
