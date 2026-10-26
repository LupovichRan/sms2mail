package io.github.lupovichran.sms2mail

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings as AndroidSettings
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import java.util.Locale

class SettingsActivity : Activity() {
    private lateinit var enabled: Switch
    private lateinit var host: EditText
    private lateinit var port: EditText
    private lateinit var user: EditText
    private lateinit var password: EditText
    private lateinit var destination: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Keep the app password out of screenshots and the recent-apps preview.
        window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
        setContentView(R.layout.activity_settings)
        enabled = findViewById(R.id.enabled)
        host = findViewById(R.id.host)
        port = findViewById(R.id.port)
        user = findViewById(R.id.user)
        password = findViewById(R.id.password)
        destination = findViewById(R.id.destination)

        val c = Settings.load(this)
        enabled.isChecked = c.enabled
        host.setText(c.host)
        port.setText(String.format(Locale.ROOT, "%d", c.port))
        user.setText(c.user)
        password.setText(c.password)
        destination.setText(c.destination)

        findViewById<Button>(R.id.save).setOnClickListener {
            val c = save()
            toast(if (c.isComplete) "Saved" else "Saved, but some fields are invalid")
        }
        findViewById<Button>(R.id.test).setOnClickListener { sendTest() }
        findViewById<Button>(R.id.battery).setOnClickListener {
            startActivity(Intent(AndroidSettings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS))
            toast("Choose \"All apps\" > SMS2Mail > Don't optimise")
        }
        enabled.setOnCheckedChangeListener { _, on -> if (on) requestPermissionsIfNeeded() }
        requestPermissionsIfNeeded()
    }

    private fun requestPermissionsIfNeeded() {
        val wanted = mutableListOf(Manifest.permission.RECEIVE_SMS)
        if (Build.VERSION.SDK_INT >= 33) wanted += Manifest.permission.POST_NOTIFICATIONS
        val missing = wanted.filter { checkSelfPermission(it) != android.content.pm.PackageManager.PERMISSION_GRANTED }
        if (missing.isNotEmpty()) requestPermissions(missing.toTypedArray(), 1)
    }

    private fun current() = SmtpConfig(
        enabled = enabled.isChecked,
        host = host.text.toString(),
        port = port.text.toString().toIntOrNull() ?: Settings.DEFAULT_PORT,
        user = user.text.toString(),
        password = password.text.toString(),
        destination = destination.text.toString(),
    )

    private fun save(): SmtpConfig = current().also { Settings.save(this, it) }

    private fun sendTest() {
        val c = save()
        if (!c.isComplete) { toast("Check host, port, user, password and destination email"); return }
        toast("Sending…")
        Thread {
            val result = try {
                Mailer.send(
                    c,
                    MessageFormatter.subject("SMS2Mail test"),
                    MessageFormatter.body(listOf("This is a test email from SMS2Mail."), System.currentTimeMillis()),
                )
                "Test email sent"
            } catch (e: Exception) {
                "Failed: ${e.javaClass.simpleName}: ${e.message?.take(200)}"
            }
            runOnUiThread { toast(result) }
        }.start()
    }

    private fun toast(s: String) = Toast.makeText(this, s, Toast.LENGTH_LONG).show()

    override fun onPause() {
        super.onPause()
        save()
    }
}
