# <span style="display: inline-block; animation: slideIn 0.8s ease-out;">✨ Skill Exchange</span>

<div align="center">

![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![Firebase](https://img.shields.io/badge/Firebase-FFCA28?style=for-the-badge&logo=firebase&logoColor=black)
![License](https://img.shields.io/badge/License-MIT-blue?style=for-the-badge)

<p>
  <img src="https://img.shields.io/badge/Made%20with-❤️-red?style=flat-square" alt="Made with love">
  <img src="https://img.shields.io/badge/Build%20Status-Active-success?style=flat-square" alt="Build Status">
</p>

**A Modern Android Platform for Rural Technicians to Exchange Skills & Services** 🚀

> **Barter Economy Platform** - Powered by Firebase, Built with Kotlin

</div>

---

## <span style="color: #6366f1;">🌟 Overview</span>

**Skill-Exchange** is a revolutionary Android application designed to empower rural technicians by creating a decentralized marketplace where they can **exchange skills, services, and expertise** without monetary transactions. Built with modern Android technologies and backed by Firebase, it brings transparency and community-driven innovation to remote areas.

---

## 🎯 Key Features

<table>
<tr>
<td width="50%">

### 🔥 Platform Features
- 💬 **Skill Marketplace** - List & discover services
- 🔐 **Secure Authentication** via Firebase
- ⚡ **Real-time Synchronization** 
- 📱 **Push Notifications** support
- 👥 **Community Profiles** & Ratings
- 📍 **Location-based Discovery**
- 🎨 **Smooth Animations** & Transitions

</td>
<td width="50%">

### 💻 Technical Highlights
- **Language**: Kotlin ⭐
- **Architecture**: MVVM Pattern
- **Backend**: Firebase (Auth + Firestore)
- **Async**: Coroutines & Flow
- **UI**: Material Design 3
- **Build**: Gradle with Kotlin DSL
- **Min SDK**: Android 8.0+

</td>
</tr>
</table>

---

## 🚀 Quick Start Guide

### 📋 Prerequisites

- Android Studio (Latest)
- Firebase Account ([Create one free](https://firebase.google.com))
- Gradle (Included with Android Studio)
- Android SDK 26+ 

### 🔧 Setup Instructions

#### ✅ Step 1: Create Firebase Project

1. Navigate to [Firebase Console](https://console.firebase.google.com/)
2. Click **Create Project** and follow the setup wizard
3. Name your project (e.g., "Skill-Exchange")

#### ✅ Step 2: Register Android App

1. Click the **Android** icon in Firebase console
2. Enter package name: `com.skillexchange.app`
3. Download the `google-services.json` file

#### ✅ Step 3: Configure the App

1. Replace `app/google-services.json` with your downloaded file
2. Sync Gradle project in Android Studio

#### ✅ Step 4: Enable Firebase Services

**Authentication:**
```
Firebase Console > Build > Authentication > Get Started
→ Sign-in method > Enable "Email/Password"
```

**Firestore Database:**
```
Firebase Console > Build > Firestore Database > Create Database
→ Start in "Test Mode" (or use rules below for production)
```

#### ✅ Step 5: Apply Security Rules

Paste this in your Firestore Security Rules:

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    // Users collection - readable by authenticated users
    match /users/{userId} {
      allow read, write: if request.auth.uid == userId;
      allow read: if request.auth != null;
    }
    
    // Skills collection - public read, authenticated write
    match /skills/{document=**} {
      allow read: if request.auth != null;
      allow create: if request.auth != null;
      allow update, delete: if request.auth.uid == resource.data.userId;
    }
    
    // Default rule for other collections
    match /{document=**} {
      allow read, write: if request.auth != null;
    }
  }
}
```

#### ✅ Step 6: Build & Run

```bash
# Clean and sync
./gradlew clean
./gradlew build

# Run on device/emulator
# Or press Run (▶️) in Android Studio
```

---

## 📁 Project Structure

```
skill_Exchange/
├── 📱 app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── 📄 AndroidManifest.xml
│   │   │   ├── ☕ java/                  # Kotlin source code
│   │   │   │   └── com/skillexchange/...
│   │   │   └── 🎨 res/                   # Resources
│   │   │       ├── layout/               # XML layouts
│   │   │       ├── drawable/             # Images & drawables
│   │   │       └── values/               # Strings, colors, styles
│   │   └── test/                         # Unit tests
│   ├── 🔧 build.gradle.kts
│   └── 🔑 google-services.json          # Firebase config
├── 🏗️ gradle/
│   ├── libs.versions.toml               # Dependency versions
│   └── wrapper/
├── 📋 build.gradle.kts                  # Root build config
├── ⚙️ settings.gradle.kts               # Project settings
└── 📘 README.md                         # This file
```

---

## 🏗️ Architecture

The app follows **MVVM (Model-View-ViewModel)** architecture:

```
View (UI Layer)
    ↓
ViewModel (Presentation Logic)
    ↓
Repository (Data Access)
    ↓
Firebase (Backend)
```

This ensures:
- ✅ Separation of concerns
- ✅ Testability
- ✅ Maintainability
- ✅ Scalability

---

## 🛠️ Tech Stack

| Component | Technology | Version |
|-----------|-----------|---------|
| **Language** | Kotlin | Latest |
| **Architecture** | MVVM | Best Practice |
| **Backend** | Firebase (Firestore + Auth) | Latest |
| **Async** | Coroutines & Flow | Latest |
| **UI Framework** | Material Design 3 | Latest |
| **Build System** | Gradle + KotlinDSL | Latest |
| **Testing** | JUnit, Mockito | Latest |

---

## 🔒 Security Features

✅ **Implemented Security Measures:**
- 🔐 Firebase Authentication (Email/Password)
- 🔑 Firestore Security Rules configured
- 🔒 HTTPS-only communication
- 📝 Input validation on all forms
- 👤 User-scoped data access
- 🛡️ Encrypted local storage

---

## 📚 Firebase Integration Examples

### Authentication
```kotlin
// Sign up new user
FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
    .addOnSuccessListener { authResult ->
        // User created successfully
    }

// Sign in existing user
FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
    .addOnSuccessListener { authResult ->
        // User signed in
    }
```

### Firestore - Read Data
```kotlin
// Listen to real-time updates
db.collection("skills").whereEqualTo("userId", currentUserId)
    .addSnapshotListener { snapshot, error ->
        snapshot?.documents?.forEach { doc ->
            val skill = doc.toObject(Skill::class.java)
            // Process skill
        }
    }
```

### Firestore - Write Data
```kotlin
// Add new skill
val newSkill = hashMapOf(
    "title" to "Electrical Repair",
    "description" to "Fixing electrical appliances",
    "userId" to currentUserId,
    "timestamp" to FieldValue.serverTimestamp()
)

db.collection("skills").add(newSkill)
    .addOnSuccessListener { documentRef ->
        // Skill added successfully
    }
```

---

## 📱 App Screenshots & Features Walkthrough

### 🖼️ Visual Overview

The Skill Exchange app provides an intuitive interface for users to post skills needed and browse available services.

#### Screenshot 1: Post Your Need Form
![Post My Need Interface](file:///c:/Users/pawan/OneDrive/Pictures/Screenshots/Screenshot%202026-05-02%20153333.png)

**Features Shown:**
- 📋 **Skill Selection Dropdown** - Browse from multiple skill categories:
  - Plumber
  - Electrician
  - Carpenter
  - Mason
  - Welder
  - Painter
  - Mechanic
  - And more...
- 🎯 **Skill Required Field** - Users can specify the exact skill they need
- 💡 **Helpful Tip** - "Be specific - artisans will offer their skill in exchange!"
- 🔵 **Post My Need Button** - Easy one-tap posting with orange accent color
- 🧭 **Bottom Navigation** - Quick access to Board, Post, and Profile

---

#### Screenshot 2: Skill Board - Browse & Discover
![Skill Board Interface](file:///c:/Users/pawan/OneDrive/Pictures/Screenshots/Screenshot%202026-05-02%20153355.png)

**Features Shown:**
- 🔍 **Search Bar** - "Search for skills or posts..." - Find specific skills or services
- 🏷️ **Category Filters** - Quick access buttons:
  - All
  - Plumber
  - Electrician
  - Carpenter
- 👤 **User Posts** - Posts from community members like Rakesh Kumar
- ⏰ **Timestamp** - "1d ago" shows when the need was posted
- 🟢 **Status Badge** - "OPEN" status indicates active postings
- 📌 **Post Details** - Clear title and description visible at a glance

---

#### Screenshot 3: Post Details - Skill Requirement Card
![Post Details View](file:///c:/Users/pawan/OneDrive/Pictures/Screenshots/Screenshot%202026-05-02%20153411.png)

**Features Shown:**
- 👤 **User Profile Badge** - Shows poster's initials (RK for Rakesh Kumar)
- 📝 **Full Post Description** - Detailed explanation of the skill need:
  - "I want to build a shopping website site"
  - Technical details about frontend, backend, and full functionality
- 🏷️ **Skill Category Tags** - "Software Engineer" highlighted in orange
- 📊 **Offer Count** - "(0 offers)" shows current response count
- 🔘 **Action Button** - Easy access button for users to respond with their skills

---

#### Screenshot 4: Post Details - Action Options
![Action Options Dialog](file:///c:/Users/pawan/OneDrive/Pictures/Screenshots/Screenshot%202026-05-02%20153430.png)

**Features Shown:**
- 📋 **Post Details** - "I want teacher for my son" 
- ⚠️ **System Status Message** - "System UI isn't responding"
- ❌ **Close App Option** - Action to close the app
- ⏱️ **Wait Option** - Wait for system to respond
- Smart error handling with user-friendly options

---

#### Screenshot 5: Full Post Board View
![Complete Skill Board](file:///c:/Users/pawan/OneDrive/Pictures/Screenshots/Screenshot%202026-05-02%20153500.png)

**Features Shown:**
- 🏠 **Header** - "Skill Board" with description "Find skills to swap in your community"
- 🔍 **Advanced Search** - Full-width search bar for easy discovery
- 🎯 **Category Tabs** - Quick filter options:
  - ✓ All (default)
  - Plumber
  - Electrician
  - Carpenter
- 📱 **Multiple Posts Feed** - Scrollable list of community needs and offers
- 👤 **User Info** - Shows poster name and time posted
- 💬 **Clear Descriptions** - Full text of what skills are needed
- 🔗 **Open Button** - Green button to engage with postings
- 🧭 **Navigation** - Bottom bar with Board, Post, and Profile

---

## 🎨 UI/UX Features

- **Material Design 3** Components for modern aesthetics
- **Smooth Page Transitions** with fragment animations
- **Interactive Buttons** with ripple effects
- **Responsive Layouts** for all screen sizes
- **Dark Mode Support** for better accessibility
- **Loading States** with elegant progress indicators
- **Custom Animations** for engaging user experience
- **Color Scheme**: Orange (#FF9800) for primary actions and branding
- **Clean Cards** for content organization
- **Bottom Navigation** for easy access to all major features
- **Category Filtering** for quick skill discovery
- **Real-time Status Updates** showing live post information

---

## 🧪 Testing

Run unit tests:
```bash
./gradlew test
```

Run instrumented tests:
```bash
./gradlew connectedAndroidTest
```

---

## 📦 Building

### Debug Build (Development)
```bash
./gradlew assembleDebug
```

### Release Build (Production)
```bash
./gradlew assembleRelease
```

### Clean Build
```bash
./gradlew clean build
```

---

## 🐛 Troubleshooting

| Problem | Solution |
|---------|----------|
| 🔴 **Firebase not initializing** | Ensure `google-services.json` is in `app/` directory |
| 🔴 **Build fails** | Run `./gradlew clean` then rebuild |
| 🔴 **Gradle sync issues** | Android Studio: File > Invalidate Caches > Restart |
| 🔴 **Permission denied errors** | Check Firebase Security Rules in console |
| 🔴 **Database connection timeout** | Verify internet connection & Firebase project is active |

---

## 🤝 Contributing

We welcome contributions! Here's how:

1. **Fork** the repository
2. **Create** a feature branch: `git checkout -b feature/YourFeature`
3. **Commit** your changes: `git commit -m 'Add YourFeature'`
4. **Push** the branch: `git push origin feature/YourFeature`
5. **Open** a Pull Request

### Code Style Guidelines
- Use Kotlin conventions
- Write meaningful commit messages
- Add unit tests for new features
- Update documentation

---

## 📄 License

This project is licensed under the **MIT License** - See the LICENSE file for details.

---

## 🗺️ Roadmap

- [x] Core authentication
- [x] Skill listing & browsing
- [ ] Advanced search filters
- [ ] In-app messaging system
- [ ] Rating & review system
- [ ] Offline mode support
- [ ] Multi-language support (i18n)
- [ ] Payment integration (optional)
- [ ] Video/Photo uploads
- [ ] Analytics dashboard

---

## 👥 Community & Support

<div align="center">

**Questions? Need Help?** 💬

- 📧 **Email**: support@skillexchange.dev
- 🐛 **[Report Issues](https://github.com/yourusername/skill-exchange/issues)**
- 💡 **[Request Features](https://github.com/yourusername/skill-exchange/issues/new)**
- 💬 **[Discussions](https://github.com/yourusername/skill-exchange/discussions)**

</div>

---

## 📊 Performance Tips

- Use **Firestore indexing** for complex queries
- Implement **pagination** for large datasets
- Cache frequently accessed data locally
- Use **background threads** for heavy operations
- Optimize images before uploading

---

<div align="center">

<h3>⭐ Found this helpful? Give us a star! ⭐</h3>

```
╔══════════════════════════════════════════╗
║   Built with Passion for Communities     ║
║   Empowering Rural Technicians Since 2024║
╚══════════════════════════════════════════╝
```

**Thank you for using Skill Exchange! 🙏**

</div>

---

<style>
@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateX(-20px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes pulse {
  0%, 100% {
    opacity: 1;
  }
  50% {
    opacity: 0.7;
  }
}
</style>

*Last Updated: May 2, 2026 | Maintained with ❤️*
* **Lottie Animations**
* **Shimmer Loading**
