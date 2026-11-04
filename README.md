# FITTRACK Android

FITTRACK is an offline-first Android fitness tracker. The interface is bundled in the app as HTML, CSS, and JavaScript and stores fitness records in the Android WebView's local storage. Normal tracking does not use a backend or require internet access.

## Features

- Manually started walking and running sessions with GPS distance, active duration, pace, and optional step-detector counts.
- Pause and resume, an ongoing location foreground-service notification, GPS accuracy filtering, and a Stop action in the notification.
- A weekly exercise plan with editable set/repetition targets, actual values for each set, completion status, and exercise history.
- A 60-day challenge, day-by-day calendar, completion streaks, weekly and monthly summaries, and activity history.
- Local weight and waist measurements with historical charts.
- JSON backup export and validated, merge-only backup import.

Location is requested only for a manually started session. The step detector is used only while that session is active and only when supported and permitted by the device. Fitness data remains on the device; clearing app data or uninstalling the app can remove it, so export backups regularly.

## Build locally

Install Android Studio with Android SDK Platform 35 and a Java 17 JDK. From the project root, use Gradle 8.9:

```shell
gradle --no-daemon --stacktrace assembleDebug
```

The installable debug APK is generated at `app/build/outputs/apk/debug/app-debug.apk`. The project does not currently include a Gradle wrapper.

## Build with Codemagic

The `fittrack-android` workflow in `codemagic.yaml` uses Java 17, downloads Gradle 8.9, runs `assembleDebug`, and collects `app/build/outputs/apk/debug/*.apk`. The configured `mac_mini_m2` instance avoids the Linux build tier that previously caused a billing-plan restriction. Confirm the selected instance is available to the Codemagic account before starting a build.

## Install and test

Install the debug APK on an Android device. Grant precise location when prompted to start a walk or run; grant activity recognition to enable step counts on Android 10 and later. Test an active session with the screen locked, pause and resume, stop from the app or notification, and verify the saved session in Activity history. GPS and step counts depend on device hardware and location conditions.
