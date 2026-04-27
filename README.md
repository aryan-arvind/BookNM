# BookNM - Campus Venue Booking System

BookNM is a professional Android application designed for managing and booking campus venues such as labs, seminar rooms, and auditoriums. It features a robust **Offline-first architecture** with **Firebase Cloud Synchronization**, ensuring data integrity and accessibility.

## 🚀 Key Features

- **Dual Portal Access**: Dedicated interfaces for Admins and Users.
- **Smart Venue Suggester**: AI-inspired logic to recommend the best venue based on capacity and equipment needs.
- **Real-time Search**: Instant filtering of venues by name or description.
- **Secure Authentication**: Integrated with **Firebase Authentication** for cloud-based identity management.
- **Cloud Sync**: Local bookings are automatically synchronized to **Google Firestore**.
- **Admin Dashboard**: Comprehensive tools for managing venues, approving bookings, and exporting data (CSV/TXT).
- **Professional UI**: Built with Material 3 guidelines and a custom premium Indigo palette.

## 🛠️ Tech Stack

- **Language**: Java
- **UI Framework**: Android XML (Material 3)
- **Database**: SQLite (Local) & Firebase Firestore (Cloud)
- **Authentication**: Firebase Auth
- **Build System**: Gradle (Kotlin DSL)

## 🏗️ Architecture: Offline-First
BookNM implements an **Offline-First** strategy. All data is first stored in a local SQLite database for maximum performance and offline availability. A dedicated `FirebaseHelper` then synchronizes this data with the cloud, allowing for multi-device consistency and data backup. This architecture is a key talking point for technical interviews as it demonstrates a deep understanding of data persistence and synchronization.

## ⚙️ Setup Instructions

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/yourusername/BookNM.git
   ```
2. **Firebase Configuration**:
   - Create a project on the [Firebase Console](https://console.firebase.google.com/).
   - Add an Android App with the package name `com.example.booknm`.
   - Download the `google-services.json` file and place it in the `app/` directory.
   - Enable **Email/Password** Authentication and **Firestore Database**.
3. **Build & Run**:
   - Open the project in Android Studio.
   - Sync Gradle files.
   - Run on an emulator or physical device.

## 👨‍💻 Author
**Your Name**
*College Project - Enhanced for MVP*

## 📄 License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
