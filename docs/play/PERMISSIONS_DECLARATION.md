# SMS and Call Log permissions declaration (RECEIVE_SMS)

Play Console path: **Policy → App content → Sensitive app permissions → SMS and Call Log permissions**. The form appears after you upload a bundle that requests RECEIVE_SMS.

## Answers

- **Does your app need SMS/Call Log permissions for its core functionality?** Yes.
- **Permissions requested:** `RECEIVE_SMS` only. The app does not use READ_SMS, SEND_SMS or any Call Log permission.
- **Core functionality / exception:** *Cross-device synchronization or transfer of SMS or calls*, which the policy describes as "Apps that enable the user to sync texts and calls across multiple devices (such as between phone and laptop)".
- **Describe the core functionality** (paste):

  > SMS2Mail's only function is to forward a copy of each SMS the user receives to an email address the user chooses, so the user can read their texts on another device such as a laptop or tablet. The app uses RECEIVE_SMS to get each incoming message (sender, text and time) and sends it by email over TLS through the SMTP account the user configures. Nothing is sent to the developer: there is no backend, no analytics and no third-party SDK. Forwarding is off by default. Before the permission is requested, a prominent in-app disclosure explains exactly what is read and where it is sent, and the user must accept it. The user can switch forwarding off at any time. The app is not a default SMS handler and has no other features. The source code is public: https://github.com/LupovichRan/sms2mail

- **Video demonstration** (required): record a 30–60 s unlisted YouTube video and paste its link. Show, in this order:
  1. Open the app and switch Forward on.
  2. The disclosure dialog appears; tap "Agree and continue".
  3. The Android SMS permission prompt appears; tap Allow.
  4. Enter the SMTP settings and tap "Send test".
  5. Send an SMS to the phone from another phone.
  6. The forwarded email arrives in the inbox, on a laptop or other device if possible.

## Honest verdict

**Approval is uncertain, and the rejection risk is real.**

- A non-default-handler app gets RECEIVE_SMS only through one of the listed exceptions. SMS-to-email forwarding fits "cross-device synchronization or transfer of SMS" most closely, but Google decides case by case.
- The same policy lists "SMS or phone notification enhancement and alerts" and "Remote control" as invalid uses, and reviewers sometimes put forwarders there.
- The declaration and its review apply to **internal testing as well**. A rejected bundle that stays active blocks publishing changes until it is removed.

If it is rejected:
- appeal once, citing the cross-device exception and the open source; or
- install the APK in the **personal** profile instead (see README.md: SMS never reach a work-profile app anyway).

Sources:
- SMS/Call Log policy and exceptions: https://support.google.com/googleplay/android-developer/answer/10208820
- Declaration applies to internal/closed/open tracks: https://support.google.com/googleplay/android-developer/answer/9214102
- User Data policy (prominent disclosure): https://support.google.com/googleplay/android-developer/answer/10144311
