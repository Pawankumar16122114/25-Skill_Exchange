package com.skillexchange.app.util

import android.content.Context
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun View.show() { visibility = View.VISIBLE }
fun View.hide() { visibility = View.GONE }
fun View.invisible() { visibility = View.INVISIBLE }

fun View.animateFadeIn(duration: Long = 300) {
    alpha = 0f
    visibility = View.VISIBLE
    animate().alpha(1f).setDuration(duration).start()
}

fun View.animateScale(fromScale: Float = 0.8f, toScale: Float = 1f, duration: Long = 200) {
    scaleX = fromScale
    scaleY = fromScale
    visibility = View.VISIBLE
    animate()
        .scaleX(toScale)
        .scaleY(toScale)
        .setDuration(duration)
        .start()
}

fun View.pulseAnimation() {
    animate()
        .scaleX(1.05f)
        .scaleY(1.05f)
        .setDuration(150)
        .withEndAction {
            animate()
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(150)
                .start()
        }
        .start()
}

fun Fragment.showSnackbar(message: String, action: String? = null, actionCallback: (() -> Unit)? = null) {
    val snackbar = Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT)
    if (action != null && actionCallback != null) {
        snackbar.setAction(action) { actionCallback() }
    }
    snackbar.show()
}

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Date?.formatRelative(): String {
    if (this == null) return ""
    val now = System.currentTimeMillis()
    val diff = now - time
    return when {
        diff < 60_000 -> "Just now"
        diff < 3_600_000 -> "${diff / 60_000}m ago"
        diff < 86_400_000 -> "${diff / 3_600_000}h ago"
        diff < 604_800_000 -> "${diff / 86_400_000}d ago"
        else -> SimpleDateFormat("dd MMM", Locale.getDefault()).format(this)
    }
}

fun Date?.formatTime(): String {
    if (this == null) return ""
    return SimpleDateFormat("hh:mm a", Locale.getDefault()).format(this)
}

fun String.toInitials(): String {
    return split(" ")
        .mapNotNull { it.firstOrNull()?.uppercaseChar() }
        .take(2)
        .joinToString("")
}
