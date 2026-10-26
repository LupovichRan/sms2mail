package io.github.lupovichran.sms2mail

import java.util.Date
import java.util.Properties
import javax.mail.Message
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

/** Sends one plain-text email over SMTP with mandatory STARTTLS. */
object Mailer {
    fun send(c: SmtpConfig, subject: String, body: String) {
        val props = Properties().apply {
            put("mail.smtp.host", c.host)
            put("mail.smtp.port", c.port.toString())
            put("mail.smtp.auth", "true")
            put("mail.smtp.starttls.enable", "true")
            put("mail.smtp.starttls.required", "true")
            put("mail.smtp.ssl.checkserveridentity", "true")
            put("mail.smtp.connectiontimeout", "20000")
            put("mail.smtp.timeout", "20000")
            put("mail.smtp.writetimeout", "20000")
        }
        val session = Session.getInstance(props)
        val msg = MimeMessage(session).apply {
            setFrom(InternetAddress(c.user))
            setRecipients(Message.RecipientType.TO, InternetAddress.parse(c.destination))
            setSubject(subject, "UTF-8")
            setText(body, "UTF-8")
            sentDate = Date()
        }
        Transport.send(msg, c.user, c.password)
    }
}
