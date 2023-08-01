# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keep class com.databaker.authorize.AuthorizationType {*;}
-keep class com.databaker.authorize.AuthorizationScope {*;}
-keep class com.databaker.authorize.bean.Token {*;}
-keep class com.databaker.authorize.callback.AuthorizationCallback {*;}
-keep class com.databaker.authorize.BakerAuthorizationEngine {*;}
-keep class com.k2fsa.sherpa.ncnn.* {*;}
-keep class com.databaker.offlineasr.ErrorCode {*;}
-keep class com.databaker.offlineasr.BakerASRManager {*;}
-keep class com.databaker.offlineasr.callback.* {*;}