# Keep model classes
-keep class com.pwhs.quickmem.domain.model.** { *; }
-keep class com.pwhs.quickmem.data.dto.** { *; }

# Keep all enums
-keep class com.pwhs.quickmem.core.data.enums.** { *; }

# Keep repository interfaces
-keep class * implements com.pwhs.quickmem.domain.repository.** { *; }

# Keep classes with reflection
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}
-keepattributes Signature,RuntimeVisibleAnnotations,AnnotationDefault

# Keep entry points
-keep class com.pwhs.quickmem.MainActivity { *; }
-keep class com.pwhs.quickmem.core.schedule_alarm.** { *; }

# Keep library classes
-keep class androidx.credentials.** { *; }
-keep class com.google.gson.** { *; }
-keep class com.squareup.retrofit2.** { *; }
-keep class com.google.firebase.** { *; }

# Keep all classes annotated with @Serializable
-keep @kotlinx.serialization.Serializable class * { *; }

# Keep the generated serializer classes
-keepclassmembers class * {
    kotlinx.serialization.KSerializer serializer(...);
}

-if class androidx.credentials.CredentialManager
-keep class androidx.credentials.playservices.** {
  *;
}