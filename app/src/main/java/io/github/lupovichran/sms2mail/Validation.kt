package io.github.lupovichran.sms2mail

/** Strict checks for user-entered SMTP settings (no whitespace or control chars, sane formats). */
object Validation {
    private val HOST = Regex("^(?=.{1,253}$)([A-Za-z0-9]([A-Za-z0-9-]{0,61}[A-Za-z0-9])?)(\\.[A-Za-z0-9]([A-Za-z0-9-]{0,61}[A-Za-z0-9])?)*$")
    private val EMAIL = Regex("^[A-Za-z0-9.!#$%&'*+/=?^_`{|}~-]{1,64}@[A-Za-z0-9]([A-Za-z0-9-]{0,61}[A-Za-z0-9])?(\\.[A-Za-z0-9]([A-Za-z0-9-]{0,61}[A-Za-z0-9])?)+$")
    private val USER = Regex("^[^\\s\\p{Cntrl}]{1,254}$")

    fun isValidHost(host: String) = HOST.matches(host)
    fun isValidPort(port: Int) = port in 1..65535
    fun isValidEmail(email: String) = email.length <= 254 && EMAIL.matches(email)
    fun isValidUser(user: String) = USER.matches(user)
    fun isValidPassword(password: String) = password.isNotEmpty() && password.none { it == '\r' || it == '\n' }
}
