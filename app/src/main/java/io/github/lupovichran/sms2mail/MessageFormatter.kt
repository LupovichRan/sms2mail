package io.github.lupovichran.sms2mail

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

/** Pure formatting logic, kept free of Android classes so it can be unit-tested. */
object MessageFormatter {
    private val TIME: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z", Locale.US)
    private val CONTROL = Regex("[\\p{Cntrl}\\u2028\\u2029]+")
    const val MAX_SENDER_LENGTH = 64

    /** Sender is attacker-controlled: strip CR/LF and other control chars (header injection) and cap length. */
    fun subject(sender: String?): String {
        val s = sender?.replace(CONTROL, " ")?.trim()?.take(MAX_SENDER_LENGTH)?.trim().orEmpty()
        return "SMS from ${s.ifEmpty { "unknown" }}"
    }

    /** Joins the parts of a multi-part SMS and appends the receive time. */
    fun body(parts: List<String?>, timestampMillis: Long, zone: ZoneId = ZoneId.systemDefault()): String {
        val text = parts.joinToString(separator = "") { it.orEmpty() }
        val time = TIME.format(Instant.ofEpochMilli(timestampMillis).atZone(zone))
        return "$text\n\nReceived: $time"
    }
}
