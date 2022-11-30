Jakarta Mail for Android
========================

Jakarta Mail for Android is now available!

As of Jakarta Mail 2.0.0, standard Jakarta Mail distribution can run on Android.

This version is available from the maven central repository.
You can try out this version by adding the following to your
build.gradle file for your Android application:

    android {
        packagingOptions {
            pickFirst 'META-INF/LICENSE.md' // picks the Jakarta Mail license file
            pickFirst 'META-INF/NOTICE.md' // picks the Jakarta Mail notice file
        }
    }
    
    dependencies {
        // use whatever the current 2.x version is...
        compile 'com.sun.mail:jakarta.mail:2.0.0'
        compile 'com.sun.activation:jakarta.activation:2.0.0'
    }

Previous versions of Jakarta Mail, ie 1.6.5, do not run on Android `as is`, therefore,
a special version of Jakarta Mail is available.  This special version of Jakarta
Mail depends on a special version of Jakarta Activation.

    android {
        packagingOptions {
            pickFirst 'META-INF/LICENSE.md' // picks the Jakarta Mail license file
            pickFirst 'META-INF/NOTICE.md' // picks the Jakarta Mail notice file
        }
    }
    
    dependencies {
        // use whatever the current 1.x version is...
        compile 'com.sun.mail:android-mail:1.6.5'
        compile 'com.sun.mail:android-activation:1.6.5'
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
[mail-dev@eclipse.org](https://accounts.eclipse.org/mailing-list/mail-dev).
