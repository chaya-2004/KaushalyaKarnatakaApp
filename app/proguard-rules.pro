# Add project specific ProGuard rules here.
-keep class com.kaushalya.app.data.entities.** { *; }
-keep class com.kaushalya.app.data.dao.** { *; }
-keepattributes *Annotation*
-keepclassmembers class * {
    @androidx.room.* *;
}
