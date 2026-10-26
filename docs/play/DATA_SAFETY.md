# Data safety form answers

Apps that are active **only on internal testing** are exempt from the Data safety form, per [Play Console Help](https://support.google.com/googleplay/android-developer/answer/10787469). Fill it in before moving to closed testing or production. Play counts any data an app transmits off the device as "collected", even when it goes to a third-party server rather than the developer's.

**Data collection and security**
- Does your app collect or share any of the required user data types? **Yes**
- Is all of the user data collected by your app encrypted in transit? **Yes** (SMTP with STARTTLS required, TLS 1.2+; cleartext is disabled)
- Do you provide a way for users to request that their data is deleted? **No**. The developer stores no user data; on-device data is removed by clearing the app's storage or uninstalling it, and this is explained in the privacy policy.

**Data types**

| Data type | Collected | Shared | Processed ephemerally | Required or optional | Purpose |
| --- | --- | --- | --- | --- | --- |
| Messages → **SMS or MMS** (sender number and text) | Yes | No* | No | Required | App functionality |
| Personal info → **Email address** (the SMTP login and destination the user enters) | Yes | No* | No | Required | App functionality |

\* Not "shared": the user starts the transfer, to an email account the user chose, after an in-app prominent disclosure and consent. This matches the sharing exception "User-initiated action or prominent disclosure and user consent".

Everything else: **not collected**. There is no location, contacts, identifiers, analytics or crash reporting.

**Security practices**
- Independent security review: No.
- Families policy: not applicable (the app is not for children).
