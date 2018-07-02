# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/embed/Android/Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-dontobfuscate

# Proguard configuration for Jackson 2.x (fasterxml package instead of codehaus package)
-keep class com.fasterxml.jackson.databind.ObjectMapper {
    public <methods>;
    protected <methods>;
}
-keep class com.fasterxml.jackson.databind.ObjectWriter {
    public ** writeValueAsString(**);
}
-keepnames class com.fasterxml.jackson.** { *; }
-dontwarn com.fasterxml.jackson.databind.**


-keepattributes Exceptions, InnerClasses, Signature, Deprecated, SourceFile, LineNumberTable, *Annotation*, EnclosingMethod

-keep class com.google.android.gms.ads.identifier.** { *; }

# joda time
-keep class org.joda.time.** { *; }
-dontwarn org.joda.time.**

# jackson
-keepnames class com.fasterxml.jackson.** { *; }
-dontwarn com.fasterxml.jackson.databind.**
-keep class org.codehaus.**



# gson
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.examples.android.model.** { *; }

#Retrofit
-dontwarn okio.**
-dontwarn retrofit2.Platform$Java8

-dontwarn okhttp3.**

# Pubnub
-dontwarn com.pubnub.**
-keep class com.pubnub.** { *; }


-dontwarn org.slf4j.**

# EventBus 3.0
-keepclassmembers class ** {
    public void onEvent*(**);
}

# EventBus 3.0 annotation
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

#Wave drawable
-keep class com.truckr.utility.WaveDrawable {*;}

#naver 아이디 로그인 프로가드
-keep public class com.nhn.android.naverlogin.** {
       public protected *;
}

