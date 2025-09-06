# Keep model classes
-keep class com.pwhs.quickmem.domain.model.** { *; }
-keep class com.pwhs.quickmem.data.dto.** { *; }

# Keep all enums
-keep class com.pwhs.quickmem.core.data.enums.** { *; }

# Keep repository interfaces
-keep class * implements com.pwhs.quickmem.domain.repository.** { *; }

# Keep classes with reflection
-keep @kotlinx.serialization.Serializable class * {*;}
-keepattributes Signature,RuntimeVisibleAnnotations,AnnotationDefault

# Keep entry points
-keep class com.pwhs.quickmem.MainActivity { *; }
-keep class com.pwhs.quickmem.core.schedule_alarm.** { *; }

# Keep library classes
-keep class com.google.gson.** { *; }
-keep class androidx.credentials.** { *; }
-keep class com.squareup.retrofit2.** { *; }
-keep class com.google.firebase.** { *; }

# Keep all classes annotated with @Serializable
-keep @kotlinx.serialization.Serializable class * { *; }

# Keep the generated serializer classes
-keepclassmembers class * {
    kotlinx.serialization.KSerializer serializer(...);
}

# Keep CredentialManager
-keep class com.google.android.libraries.identity.googleid.** { *; }
# Keep dataStore
-keep class androidx.datastore.** { *; }
-keep class com.pwhs.quickmem.core.datastore.** { *; }