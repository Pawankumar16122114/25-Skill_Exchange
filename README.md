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

## 📱 App Screenshots & User Interface

### 🎨 Visual Design System

The Skill Exchange app features a modern, user-friendly interface with intuitive navigation and beautiful design.

#### **App Screens Overview**

<div align="center">

| **Home Screen** | **Post Screen** | **Skill Details** |
|:---:|:---:|:---:|
| ![Home Screen](assets/images/home.png) | ![Post Screen](assets/images/post.png) | ![Skill Details](assets/images/skilld.png) |
| Browse & discover skills in your community | Post your skill needs easily | View detailed requirements |

</div>

---

#### **Navigation Architecture**

The app uses a bottom navigation bar with 4 main sections:

```
┌─────────────────────────────────┐
│   Skill Exchange App            │
├─────────────────────────────────┤
│                                 │
│  ┌─ 🏠 Home (Skill Board)      │
│  │  ├─ Search functionality    │
│  │  ├─ Filter by category      │
│  │  ├─ Browse skill listings   │
│  │  └─ View user offers        │
│  │                             │
│  ├─ ➕ Post (Create Needs)     │
│  │  ├─ Select skill category    │
│  │  ├─ Write description        │
│  │  ├─ Add helpful tips         │
│  │  └─ Submit request           │
│  │                              │
│  ├─ 💬 Chat (Messaging)         │
│  │  ├─ View conversations       │
│  │  ├─ Real-time messaging      │
│  │  ├─ User avatars             │
│  │  └─ Message notifications    │
│  │                              │
│  └─ 👤 Profile (User Account)  │
│     ├─ Profile information      │
│     ├─ Skill ratings & reviews  │
│     ├─ Skill points tracker     │
│     └─ Account settings         │
│                                 │
├─────────────────────────────────┤
│  🏠 Home|➕ Post| 💬 Chat | 👤│
└─────────────────────────────────┘
```

---

### 🎯 Key Features by Screen

#### **1. 🏠 Home - Skill Board**
- 🔍 **Full-text Search** - Find skills by name or description
- 🏷️ **Category Filters** - Plumber, Electrician, Carpenter, Mason, Welder, Painter, Mechanic
- 📝 **Detailed Listings** - See complete skill requirements
- 👤 **User Profiles** - View poster info and reputation
- ⏰ **Timestamps** - Know when skills were posted
- 🟢 **Status Badges** - "OPEN" status for active postings
- 💬 **Quick Messaging** - Message users directly from listings

#### **2. ➕ Post - Create Skill Needs**
- 📋 **Skill Dropdown** - Choose from predefined categories
- 📝 **Rich Descriptions** - Explain your skill needs in detail
- 💡 **Helpful Tips** - Guidance for writing effective posts
- 🔵 **One-tap Submit** - Easy posting with success confirmation
- ✅ **Instant Publishing** - Your post goes live immediately

#### **3. 💬 Chat - Real-time Messaging**
- 💬 **Real-time Sync** - Messages appear instantly
- 👤 **User Avatars** - Initials-based profile pictures
- 📨 **Timestamps** - Know exactly when messages were sent
- 🔔 **Notifications** - Get alerted for new messages
- 🎨 **Chat Bubbles** - Distinct sent vs. received message styling
- 📱 **Conversation List** - Manage multiple chats easily

#### **4. 👤 Profile - User Account**
- 👤 **Profile Info** - Name, contact, skills
- ⭐ **Ratings & Reviews** - Community reputation
- 📊 **Skill Points** - Track exchange credits
- 🔧 **Settings** - Notification & privacy preferences
- 🚪 **Sign Out** - Secure logout option

---

### 🎨 Design Assets & Components

The app uses professional vector graphics and Material Design 3:

**Navigation Icons:**
- 🏠 `ic_home.xml` - Home/Board navigation
- ➕ `ic_add_circle.xml` - Create/Post action
- 💬 `ic_chat.xml` - Messaging feature
- 👤 `ic_person.xml` - Profile/User account
- 🔍 `ic_skill_board.xml` - Skill board view
- 🤝 `ic_handshake.xml` - Skill exchange/swap
- ⭐ `ic_star.xml` - Rating & reviews
- ➡️ `ic_send.xml` - Send message button
- 🌱 `ic_empty_leaf.xml` - Empty state indicator

**Visual Components:**
- 📦 **Gradient Backgrounds** - Saffron gradient for splash and branding
- 🎯 **Skill Chips** - Category tag styling
- 💬 **Message Bubbles** - Sent vs. received chat styling
- 🎯 **Input Fields** - Form background styling
- 🎬 **Animations** - Smooth transitions and interactions

**Color Scheme:**
- Primary: **Saffron Orange** (#FF9800) - Action buttons & branding
- Secondary: **Green** - Success/Positive actions
- Accent: **Material Colors** - Following Material Design 3

---

### 📐 Layout Responsive Design

All screens are optimized for:
- ✅ Mobile phones (320dp - 480dp)
- ✅ Tablets (600dp - 1000dp+)
- ✅ Portrait & Landscape orientations
- ✅ Light & Dark themes
- ✅ Different screen densities (mdpi, hdpi, xhdpi, xxhdpi, xxxhdpi)

---

### 🎬 Animation Features

**Page Transitions:**
- Slide in/out animations
- Fade in/out effects
- Fall-down layout animations

**User Interactions:**
- Button ripple effects (lift on touch)
- Loading state animations
- Smooth scrolling

**Visual Feedback:**
- Toast notifications
- Dialog animations
- Shimmer loading placeholders

---

### 📦 Project Structure & Resources

All visual assets and resources are organized in the project:

```
skill_Exchange/
├── 📁 assets/
│   └── images/
│       ├── home.png              # Home screen screenshot
│       ├── post.png              # Post creation screen
│       ├── skilld.png            # Skill details view
│       └── Screenshot *.png      # Additional screens
│
├── 📱 app/src/main/res/
│   ├── drawable/                 # Vector drawables & icons
│   │   ├── ic_*.xml             # Navigation & UI icons
│   │   ├── bg_*.xml             # Background shapes & gradients
│   │   └── ic_launcher_*.xml    # App launcher icons
│   │
│   ├── mipmap-*/                # Launcher icons (multi-density)
│   │   ├── ic_launcher.webp
│   │   ├── ic_launcher_foreground.webp
│   │   └── ic_launcher_round.webp
│   │
│   ├── layout/                   # Screen layouts
│   │   ├── activity_main.xml
│   │   ├── fragment_home.xml
│   │   ├── fragment_post_need.xml
│   │   ├── fragment_chat.xml
│   │   ├── fragment_profile.xml
│   │   └── item_*.xml           # List item layouts
│   │
│   ├── anim/                     # Animation definitions
│   │   ├── slide_in_*.xml
│   │   ├── fade_*.xml
│   │   └── item_animation_*.xml
│   │
│   ├── values/                   # Colors, strings, dimensions
│   │   ├── colors.xml
│   │   ├── strings.xml
│   │   ├── dimens.xml
│   │   └── themes.xml
│   │
│   └── navigation/               # Navigation graph
│       └── nav_graph.xml
│
└── 📘 README.md                 # Documentation
```

---

### 🖼️ Image Assets

The project includes professionally designed screenshots:

- **home.png** - Home screen showing skill board with search and filters
- **post.png** - Post creation screen for submitting skill needs
- **skilld.png** - Skill details view with user information
- **Screenshot 2026-05-02 153430.png** - System dialogs and interactions

These images are included in the `/assets/images/` folder for easy reference and documentation.

---

### 🔧 Using Project Assets in Development

When building features, developers use the project's built-in resources:

**Example: Using Icons in Navigation**
```xml
<!-- Bottom navigation menu uses drawable icons -->
<item
    android:id="@+id/homeFragment"
    android:icon="@drawable/ic_home"
    android:title="@string/home" />

<item
    android:id="@+id/postFragment"
    android:icon="@drawable/ic_add_circle"
    android:title="@string/post" />

<item
    android:id="@+id/chatFragment"
    android:icon="@drawable/ic_chat"
    android:title="@string/chat" />

<item
    android:id="@+id/profileFragment"
    android:icon="@drawable/ic_person"
    android:title="@string/profile" />
```

**Example: Using Gradient Backgrounds**
```xml
<!-- Saffron gradient for splash and branding -->
<item android:drawable="@drawable/bg_saffron_gradient" />

<!-- Skill chip styling -->
<item android:drawable="@drawable/bg_skill_chip" />
```

**Example: Handling Animations in Code**
```kotlin
// Fragment transitions with animations
val transaction = supportFragmentManager.beginTransaction()
transaction.setCustomAnimations(
    R.anim.slide_in_right,
    R.anim.slide_out_left,
    R.anim.slide_in_left,
    R.anim.slide_out_right
)
transaction.replace(R.id.fragmentContainer, newFragment)
transaction.addToBackStack(null)
transaction.commit()
```

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

*Last Updated: May 2, 2026 | Maintained with ❤️*

