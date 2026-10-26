@file:Suppress("DEPRECATION") // EncryptedSharedPreferences/MasterKey are deprecated upstream but still work.

package io.github.lupovichran.sms2mail

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

data class SmtpConfig(
    val enabled: Boolean,
    val host: String,
    val port: Int,
    val user: String,
    val password: String,
    val destination: String,
) {
    val isComplete: Boolean
        get() = Validation.isValidHost(host) && Validation.isValidPort(port) && Validation.isValidUser(user) &&
            Validation.isValidPassword(password) && Validation.isValidEmail(destination)
}

/** All settings live in EncryptedSharedPreferences (AES-256, key in Android Keystore). */
object Settings {
    const val DEFAULT_HOST = "smtp.gmail.com"
    const val DEFAULT_PORT = 587

    private fun prefs(context: Context): SharedPreferences {
        val key = MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()
        return EncryptedSharedPreferences.create(
            context,
            "sms2mail_settings",
            key,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
        )
    }

    fun load(context: Context): SmtpConfig {
        val p = prefs(context)
        return SmtpConfig(
            enabled = p.getBoolean("enabled", false),
            host = p.getString("host", DEFAULT_HOST) ?: DEFAULT_HOST,
            port = p.getInt("port", DEFAULT_PORT),
            user = p.getString("user", "").orEmpty(),
            password = p.getString("password", "").orEmpty(),
            destination = p.getString("destination", "").orEmpty(),
        )
    }

    fun save(context: Context, c: SmtpConfig) {
        prefs(context).edit()
            .putBoolean("enabled", c.enabled)
            .putString("host", c.host.trim())
            .putInt("port", c.port)
            .putString("user", c.user.trim())
            .putString("password", c.password)
            .putString("destination", c.destination.trim())
            .apply()
    }
}
