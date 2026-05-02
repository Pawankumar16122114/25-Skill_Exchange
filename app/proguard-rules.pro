# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.

# Firebase
-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.** { *; }

# Data Models (Firestore)
-keep class com.skillexchange.app.model.** { *; }
-keepclassmembers class com.skillexchange.app.model.** { *; }

# Lottie
-dontwarn com.airbnb.lottie.**
-keep class com.airbnb.lottie.** { *; }

# Kotlin Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembers class kotlinx.coroutines.** { volatile <fields>; }

# Navigation
-keepnames class androidx.navigation.fragment.NavHostFragment

# Coil
-dontwarn coil.**

# ViewBinding
-keep class **.databinding.** { *; }
