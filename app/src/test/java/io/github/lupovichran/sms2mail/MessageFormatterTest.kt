package io.github.lupovichran.sms2mail

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.ZoneId

class MessageFormatterTest {
    private val utc = ZoneId.of("UTC")
    private val jerusalem = ZoneId.of("Asia/Jerusalem")

    @Test fun subjectUsesSender() =
        assertEquals("SMS from +972501234567", MessageFormatter.subject("+972501234567"))

    @Test fun subjectTrimsAndStripsNewlines() =
        assertEquals("SMS from Bank X", MessageFormatter.subject("  Bank\r\nX "))

    @Test fun subjectHandlesMissingSender() {
        assertEquals("SMS from unknown", MessageFormatter.subject(null))
        assertEquals("SMS from unknown", MessageFormatter.subject("   "))
    }

    @Test fun bodySinglePartWithTime() =
        assertEquals("Hello\n\nReceived: 1970-01-01 00:00:00 UTC", MessageFormatter.body(listOf("Hello"), 0L, utc))

    @Test fun bodyJoinsMultipartWithoutSeparator() =
        assertEquals(
            "Part one, part two.\n\nReceived: 1970-01-01 00:00:00 UTC",
            MessageFormatter.body(listOf("Part one, ", "part two."), 0L, utc),
        )

    @Test fun bodyIgnoresNullPartsAndKeepsHebrew() =
        assertEquals(
            "קוד: 123456\n\nReceived: 2026-10-26 08:10:00 IST",
            MessageFormatter.body(listOf("קוד: ", null, "123456"), 1_792_995_000_000L, jerusalem),
        )
}
