Jakarta Mail for Android
========================

Jakarta Mail for Android is now available!

Android does not provide a Java Compatible runtime and so can't run the
standard Jakarta Mail distribution.  Instead, a special version of
Jakarta Mail is available for Android.  This special version of Jakarta
Mail depends on a special version of Jakarta Activation.

This version is available from the java.net maven repository.
You can try out this version by adding the following to your
build.gradle file for your Android application:

    android {
        packagingOptions {
            pickFirst 'META-INF/LICENSE.txt' // picks the Jakarta Mail license file
        }
    }
    
    dependencies {
        // use whatever the current version is...
        compile 'com.sun.mail:android-mail:1.6.4'
        compile 'com.sun.mail:android-activation:1.6.4'
    }


One of the standard Java features not supported on Android is SASL.  That means
none of the "mail._protocol_.sasl.*" properties will have any effect.  One of
the main uses of SASL was to enable OAuth2 support.  The latest version
of Jakarta Mail includes built-in OAuth2 support that doesn't require SASL.
See the [OAuth2](OAuth2) page for more details.

Jakarta Mail for Android requires at least Android API level 19,
which corresponds to
[Android KitKat](https://en.wikipedia.org/wiki/Android_version_history#Android_4.4_KitKat_.28API_19.29),
currently the oldest supported version of Android.

If you discover problems, please report them to
[javamail_ww@oracle.com](mailto:javamail_ww@oracle.com).
