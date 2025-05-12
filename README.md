# 📊 Lectio - Track Your Reading Journey

**Lectio** is a book tracker app with a modern android app designed to help book enthusiasts track their reading progress, manage their library, and receive reading reminders.Built with Jetpack Compose and powered by WorkManager, Lectio offers a clean, intuitive interface and robust functionality for personal book management. Lectio is a personal project developed to explore Android development with Compose, navigation, clean architecture, MVVM architecture, and background tasks. It allows users to add books, view and filter their library, edit entries, and clear their data, all while supporting customizable themes and notifications.

[🎥 Demo Aplikasi Lectio https://drive.google.com/file/d/149ebK5TOMMkh2mPCIOIUlUOT6VC-mJCu/view?usp=drive_link]

## ⚠️ Current Limitations
- No backup/restore or sync with external services
  

## 🔧 Future Plans
- Backup and restore (local or cloud-based)
- Add integration with Google Books or Goodreads for metadata

## Features
- **Book Management**: Add, view, and edit books
- **Navigation**: Seamless bottom navigation between Dashboard, Library, Add Book, Favorites, and Settings screens.
- Dashboard Stats: Display key book statistics (e.g., total books, total read pages) on the Dashboard for a quick overview.
- **Library Filter**: Filter books in the Library screen by title, author, or other criteria for easy access.
- **Nested Screens**: Access detailed book views and edit screens with proper back stack management.
- **Custom Settings**: Toggle notifications, switch between light, dark, and system themes, and clear all data with confirmation.
- **Reading Reminders**: Schedule periodic reminders using WorkManager to encourage reading (pending full implementation).
- **User Feedback**: Inline snackbars for actions like adding books or clearing data.
- **Responsive Design**: Built with Jetpack Compose for a modern, adaptive UI.

## 🧱 Architecture Overview

- **MVVM** pattern with `ViewModel` and `StateFlow`
- **Clean Architecture** separating UI, domain, and data layers
- **Unidirectional data flow**
- **Dependency Injection** with Hilt

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
Splash screen https://drive.google.com/file/d/1kz0ZcQSbgwbjDTdlBp7eyJjC2MV74HSr/view?usp=drive_link
Dashboard screen https://drive.google.com/file/d/17drZqJS39PTkSjhR1_6KtA7hn6uQJV-m/view?usp=drive_link
Library screen https://drive.google.com/file/d/1kwXp_iWq3BwpPJ9cc7xubBVOMl025LIw/view?usp=drive_link
Add book screen https://drive.google.com/file/d/1u5bEKsE4sH660jdA2uq8YxifoX8qU9ZO/view?usp=drive_link
Favorite screen https://drive.google.com/file/d/1JhtVClYgcqZr_OnwGCADAkOaXNCNv2qI/view?usp=drive_link
Settings screen https://drive.google.com/file/d/12tF4hefy83RS6mVNjVBG_NZst9A4NAGM/view?usp=drive_link
Detail Book Screen https://drive.google.com/file/d/1H7WyX0_53J9aQQh5-XcyCArhF3uL-Z63/view?usp=drive_link
Edit Book Screen https://drive.google.com/file/d/1AQqF6Y2SlrKYjdLq2Y-Uy9sc1xWA5Ash/view?usp=drive_link
search view https://drive.google.com/file/d/1XB5VLlvNznVtU2FdTuYpyAP_3qhMyHIt/view?usp=drive_link
notification view https://drive.google.com/file/d/1NI-I4o8Sytf0rn2Kggf_D4xvfsiIr1oH/view?usp=drive_link
