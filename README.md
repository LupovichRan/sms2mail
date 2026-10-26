# SMS2Mail

A tiny, open-source Android app that forwards every incoming SMS to your email over SMTP.

- Kotlin, Android 8.0+ (minSdk 26)
- One settings screen, no account, no analytics, no ads
- The only network traffic is the SMTP connection you configure (STARTTLS is required)

## How it works

1. An `SMS_RECEIVED` broadcast receiver joins the parts of each (multi-part) message.
2. It enqueues a WorkManager job that waits for a network connection and retries with exponential backoff.
3. The job sends an email with subject `SMS from <sender>` and a body containing the message text and the time it was received.

If sending fails permanently (bad login, or 20 failed attempts) you get a notification.

## Setup (Gmail)

1. Turn on 2-Step Verification on your Google account.
2. Create an App Password at <https://myaccount.google.com/apppasswords> (name it "SMS2Mail"). Copy the 16-character password.
3. Install the APK and open **SMS2Mail**. Allow the SMS and notification permissions.
4. Fill in:
   - **SMTP host**: `smtp.gmail.com` (default)
   - **SMTP port**: `587` (default, STARTTLS)
   - **SMTP user**: your full Gmail address
   - **App password**: the 16-character App Password (not your normal password)
   - **Send to**: the address that should receive the SMS
5. Tap **Send test email** and check your inbox.
6. Turn on **Forward incoming SMS**.
7. Tap **Battery optimisation settings**, choose *All apps* > SMS2Mail > *Don't optimise*, so forwarding is not delayed by Doze.

Other providers work too, as long as they offer SMTP with STARTTLS (usually port 587).

## Privacy

- SMS texts and sender numbers go only to the SMTP server you configure, and from there to your mailbox.
- Settings, including the app password, are stored on the device with `EncryptedSharedPreferences` (key held in the Android Keystore) and are excluded from backups.
- No analytics, crash reporting, trackers, or other network calls. Cleartext traffic is disabled.
- Note that forwarding SMS to email means your one-time codes end up in your mailbox. Protect that mailbox accordingly.

## Android 17: three-hour delay for OTP messages

Starting with Android 17, Android withholds SMS messages that contain one-time passwords (OTP) from most apps for **three hours** after they arrive, to stop OTP hijacking. During the delay the `SMS_RECEIVED` broadcast is not delivered, so SMS2Mail forwards these messages only after three hours.

- Messages in the WebOTP and SMS Retriever formats are delayed for all apps.
- Ordinary OTP messages are delayed for apps that target API 37 or higher. SMS2Mail currently targets API 35, so they are not affected yet.
- Only exempt apps (default SMS app, assistant, connected-device companions, system and carrier apps) get them immediately.

See [Android 17 behaviour changes](https://developer.android.com/about/versions/17/behavior-changes-all#sms-otp-protection).

## Build

Requires JDK 17 and the Android SDK (platform 35).

```sh
./gradlew testDebugUnitTest assembleDebug
# APK: app/build/outputs/apk/debug/app-debug.apk
```

A GitHub Actions workflow is in [`docs/build-apk.yml`](docs/build-apk.yml). Move it to `.github/workflows/` to enable it.

## עברית

**SMS2Mail** מעבירה כל SMS נכנס לאימייל שלך דרך SMTP.

**הגדרה עם Gmail:** הפעילו אימות דו-שלבי בחשבון Google, צרו סיסמת אפליקציה (App Password) בכתובת <https://myaccount.google.com/apppasswords>, ובאפליקציה הזינו: שרת `smtp.gmail.com`, פורט `587`, כתובת ה-Gmail המלאה כמשתמש, את סיסמת האפליקציה בת 16 התווים, ואת כתובת היעד. לחצו "Send test email", הפעילו את המתג, ופטרו את האפליקציה מאופטימיזציית סוללה.

**פרטיות:** ההודעות נשלחות רק לשרת ה-SMTP שהגדרתם. ההגדרות והסיסמה נשמרות מוצפנות במכשיר ואינן נכללות בגיבוי. אין אנליטיקס ואין תעבורת רשת מלבד SMTP.

**Android 17:** החל מ-Android 17 המערכת מעכבת הודעות SMS שמכילות קוד חד-פעמי (OTP) בשלוש שעות עבור רוב האפליקציות, ולכן הודעות כאלה יועברו למייל רק אחרי שלוש שעות.

## License

[MIT](LICENSE) © 2026 Ran Lupovich
