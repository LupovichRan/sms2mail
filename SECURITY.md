# Security Policy

## Reporting a vulnerability

Please report vulnerabilities privately via GitHub's
[private vulnerability reporting](https://github.com/LupovichRan/sms2mail/security/advisories/new)
("Security" tab > "Report a vulnerability"). Do not open a public issue.
Include steps to reproduce and the affected version. You should get a reply within 7 days.

Only the latest release is supported.

## Threat model (short)

**Protects:** SMS text, sender numbers and the SMTP app password.

- **Network attacker:** SMTP requires STARTTLS (`starttls.required`), TLS 1.2+, certificate validation against the system trust store and hostname verification. Cleartext traffic is disabled. The app talks to nobody except the SMTP server you configure.
- **Other apps on the device:** The SMS receiver only accepts broadcasts from the system (`BROADCAST_SMS`). No other component is exported except the launcher activity. Settings are in `EncryptedSharedPreferences`, with the key in the Android Keystore.
- **Malicious SMS content:** The sender and text are untrusted. Control characters and CR/LF are stripped from the subject and the sender is length-capped, which prevents mail-header injection. The body is sent as plain text.
- **Backup / device transfer / screen capture:** All app data is excluded from cloud backup and device-to-device transfer. The settings screen uses `FLAG_SECURE`. Error notifications are hidden on the lock screen.
- **Release builds:** Not debuggable, R8-shrunk, and `android.util.Log` calls are stripped.

**Out of scope:** A rooted or compromised device. Someone who holds your unlocked phone. Your mailbox or SMTP provider, which receive every SMS by design. Android's own 3-hour OTP delay (see README).

**Known residual risk:** Until an SMS is sent, its text sits in WorkManager's private app database. WorkManager prunes it after the job finishes, about a day later. That database is not encrypted, but it is app-private and excluded from backups.
