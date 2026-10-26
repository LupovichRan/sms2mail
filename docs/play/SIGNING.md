# Signing the App Bundle for Google Play (phone only, no computer)

Google Play only accepts an App Bundle signed with an *upload key*. The actual app is then
signed by Google with the *app signing key*, which Google creates and keeps (Play App Signing).
No key is ever created on a shared computer or committed to `main`.

You do everything below in a phone browser on github.com.

## One-time setup

1. **Create the secret.** Generate a random password of 32 or more characters in your password manager and save it there.
   Then go to repo **Settings → Secrets and variables → Actions → New repository secret** and add it:
   - Name: `SMS2MAIL_UPLOAD_KEY_PASSWORD`
   - Value: the password
2. **Enable the two workflows.** Go to **Add file → Create new file** on `main`. Create
   `.github/workflows/upload-key.yml` and paste the contents of [`upload-key.yml`](upload-key.yml).
   Do the same for `.github/workflows/build-signed-aab.yml` with the contents of [`build-signed-aab.yml`](build-signed-aab.yml).
   Commit both, or merge them via a PR if `main` is protected.
3. **Create the key.** Go to **Actions → "Create upload key (run once)" → Run workflow**.
   The workflow creates the branch `upload-key`, containing `upload-key.p12` (password-protected) and `upload-cert.pem`.
   Your backup is that branch plus the password in your password manager. If either is lost, Play
   Console can reset the upload key (**Test and release → App integrity → App signing → Request upload key reset**). The app itself is not affected.

## Every release

1. Bump `versionCode` in `app/build.gradle.kts`. Every upload needs a higher number.
2. Go to **Actions → "Build signed AAB" → Run workflow**.
3. When the run finishes, download the artifact `sms2mail-release-aab` from the run page and unzip it.
   The bundle inside is `app-release.aab`.
4. In Play Console, go to **Test and release → Testing → Internal testing → Create new release** and upload `app-release.aab`.
   On the first upload, accept **Play App Signing** with a Google-generated app signing key.

## Why this route

- A "phone-only" key would need a terminal app with a JDK (e.g. Termux plus OpenJDK) in the personal profile, followed by copy-pasting base64 into a secret. That is more steps and more fragile.
- With this route the private key exists only on GitHub's runner, and is stored only as a password-protected PKCS12 file whose password lives in GitHub Secrets.
- The trade-off: the encrypted `.p12` file is readable in the public repository. Its safety rests on the password being long and random, which is why the workflow enforces at least 24 characters (use 32+). It is only an upload key, and Google can reset it.
