# Keep all public classes and public methods/fields (API surface)
-keep public class com.quad.loadify.** {
    public *;
}
-keep class com.quad.loadify.compose.** { *; }
-keepnames class com.quad.loadify.compose.** { *; }


# Keep annotations
-keepattributes *Annotation*

# Keep Serializable classes (if any)
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# OkHttp3 (basic rules)
-dontwarn okhttp3.**
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }

# AndroidSVG (basic rules)
-dontwarn com.caverock.androidsvg.**
-keep class com.caverock.androidsvg.** { *; }

# Lottie Compose (basic rules)
-dontwarn com.airbnb.lottie.**
-keep class com.airbnb.lottie.** { *; }

# Remove logging (optional, if you want to strip logs)
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
    public static *** w(...);
    public static *** e(...);
}

# If your library uses reflection, add keep rules accordingly
