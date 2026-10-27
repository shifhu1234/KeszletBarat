-dontobfuscate
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod

-keep class hu.keszletbarat.app.model.** { *; }
-keep class hu.keszletbarat.app.data.** { *; }
-keep class hu.keszletbarat.app.widget.** { *; }
-keepnames class com.google.firebase.firestore.** { *; }
-dontwarn com.google.appengine.**
-dontwarn io.grpc.**
-dontwarn org.codehaus.mojo.animal_sniffer.**
