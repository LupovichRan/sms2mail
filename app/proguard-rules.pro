# JavaMail for Android (com.sun.mail:android-mail) loads providers reflectively.
-keep class javax.mail.** { *; }
-keep class com.sun.mail.** { *; }
-keep class javax.activation.** { *; }
-keep class com.sun.activation.** { *; }
-keep class myjava.awt.datatransfer.** { *; }
-dontwarn javax.mail.**
-dontwarn com.sun.mail.**
-dontwarn javax.activation.**
-dontwarn com.sun.activation.**
-dontwarn java.awt.**
-dontwarn javax.security.sasl.**
-dontwarn javax.naming.**

# Defence in depth: strip any android.util.Log calls from release builds.
-assumenosideeffects class android.util.Log {
    public static int v(...);
    public static int d(...);
    public static int i(...);
    public static int w(...);
    public static int e(...);
    public static int wtf(...);
    public static boolean isLoggable(...);
}

# Compile-time-only annotations referenced by Tink (security-crypto).
-dontwarn com.google.errorprone.annotations.**
-dontwarn javax.annotation.**
