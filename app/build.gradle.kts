plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "io.github.lupovichran.sms2mail"
    compileSdk = 36

    defaultConfig {
        applicationId = "io.github.lupovichran.sms2mail"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0.0"
    }

    // Release signing only when CI provides the upload key via environment variables
    // (see docs/play/SIGNING.md). Locally, bundleRelease/assembleRelease produce unsigned output.
    val uploadKeystore = System.getenv("SMS2MAIL_UPLOAD_KEYSTORE")
    if (uploadKeystore != null) {
        signingConfigs {
            create("upload") {
                storeFile = file(uploadKeystore)
                storeType = "PKCS12"
                storePassword = System.getenv("SMS2MAIL_UPLOAD_KEY_PASSWORD")
                keyAlias = "upload"
                keyPassword = System.getenv("SMS2MAIL_UPLOAD_KEY_PASSWORD")
            }
        }
    }

    buildTypes {
        release {
            if (uploadKeystore != null) signingConfig = signingConfigs.getByName("upload")
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
    lint {
        abortOnError = true
        checkReleaseBuilds = true
        warningsAsErrors = false
    }
    packaging {
        resources {
            excludes += setOf("META-INF/LICENSE.md", "META-INF/NOTICE.md", "META-INF/*.SF", "META-INF/mailcap.default", "META-INF/mimetypes.default")
        }
    }
}

dependencies {
    implementation("androidx.work:work-runtime-ktx:2.12.0")
    implementation("androidx.security:security-crypto:1.1.0")
    implementation("com.sun.mail:android-mail:1.6.8")
    implementation("com.sun.mail:android-activation:1.6.8")
    testImplementation("junit:junit:4.13.2")
}
