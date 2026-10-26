# SMS2Mail privacy policy

Effective date: 26 October 2026. Developer: Ran Lupovich ([github.com/LupovichRan](https://github.com/LupovichRan)).
App: SMS2Mail (`io.github.lupovichran.sms2mail`), open source at <https://github.com/LupovichRan/sms2mail>.

## What the app does with your data

SMS2Mail forwards text messages (SMS) you receive to an email address you choose.

| Data | Why | Where it goes |
| --- | --- | --- |
| Incoming SMS: sender number and message text, plus the time it was received | To forward it by email (the app's only function) | Sent over an encrypted connection (STARTTLS, TLS 1.2+) to the **SMTP server you configure** (e.g. Gmail), which delivers it to the **destination address you configure**. |
| Your SMTP server, port, username, app password and destination address | To send the email | Stored only on your device, encrypted with a key from the Android Keystore. Sent only to your own SMTP server to log in. |

The app only reads SMS while forwarding is switched on, after you have accepted the in-app disclosure and granted the SMS permission.

## What the app does not do

- The developer receives **none** of your data. There is no developer server, account, analytics, advertising, crash reporting or tracking SDK.
- Messages are not stored by the app after they are sent. A message waits in the device's local work queue only until it is sent, or until it is given up on after repeated failures.
- The app does not sell or share data with anyone. The only recipients are the email provider and address that you choose. Their own privacy policies apply to the emails once they arrive.
- The app is excluded from Android cloud backup and device-to-device transfer.

## Your choices and deletion

- Turn forwarding off in the app at any time.
- Clearing the app's storage or uninstalling it deletes all settings and queued messages from the device.
- Forwarded emails live in your own mailbox. Delete them there.
- Because the developer holds no data about you, there is nothing for the developer to delete. Questions are welcome via [GitHub issues](https://github.com/LupovichRan/sms2mail/issues). Report security issues privately as described in [SECURITY.md](../SECURITY.md).

## Children

The app is not directed at children.

## Changes

Any change to this policy will be published in this file, and its history is visible in the repository.

---

## מדיניות פרטיות (עברית, בקצרה)

SMS2Mail מעבירה כל SMS נכנס (מספר השולח, תוכן ההודעה ושעת הקבלה) למייל שאתה בוחר. השליחה עוברת דרך שרת ה-SMTP שאתה מגדיר (למשל Gmail), בחיבור מוצפן (TLS 1.2 ומעלה).

- פרטי ה-SMTP והסיסמה נשמרים רק במכשיר, מוצפנים באמצעות Android Keystore.
- למפתח לא מגיע שום מידע: אין שרת, אין אנליטיקה, אין פרסומות ואין מעקב.
- הודעות לא נשמרות לאחר שליחתן.
- אפשר לכבות את ההעברה בכל רגע. מחיקת נתוני האפליקציה או הסרתה מוחקת את כל המידע מהמכשיר.
