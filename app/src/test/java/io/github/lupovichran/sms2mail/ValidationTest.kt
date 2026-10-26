package io.github.lupovichran.sms2mail

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidationTest {
    @Test fun hosts() {
        assertTrue(Validation.isValidHost("smtp.gmail.com"))
        assertTrue(Validation.isValidHost("mail-1.example.co.il"))
        assertFalse(Validation.isValidHost(""))
        assertFalse(Validation.isValidHost("smtp.gmail.com\r\nX"))
        assertFalse(Validation.isValidHost("smtp gmail.com"))
        assertFalse(Validation.isValidHost("-bad.com"))
        assertFalse(Validation.isValidHost("smtp://gmail.com"))
    }

    @Test fun ports() {
        assertTrue(Validation.isValidPort(587))
        assertFalse(Validation.isValidPort(0))
        assertFalse(Validation.isValidPort(65536))
    }

    @Test fun emails() {
        assertTrue(Validation.isValidEmail("ran@example.com"))
        assertTrue(Validation.isValidEmail("first.last+sms@sub.example.co.il"))
        assertFalse(Validation.isValidEmail("ran@example"))
        assertFalse(Validation.isValidEmail("a@b.com, c@d.com"))
        assertFalse(Validation.isValidEmail("ran@example.com\r\nBcc: x@y.com"))
        assertFalse(Validation.isValidEmail("Ran <ran@example.com>"))
    }

    @Test fun usersAndPasswords() {
        assertTrue(Validation.isValidUser("ran@example.com"))
        assertFalse(Validation.isValidUser("ran\n@example.com"))
        assertFalse(Validation.isValidUser(""))
        assertTrue(Validation.isValidPassword("abcd efgh ijkl mnop"))
        assertFalse(Validation.isValidPassword(""))
        assertFalse(Validation.isValidPassword("a\r\nb"))
    }
}
