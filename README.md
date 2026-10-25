# Komikta Android Native — Migration Base

Native Android application scaffold using Kotlin, Jetpack Compose, Material 3, Navigation Compose, Retrofit, Coroutines and Coil.

## Open
Open this folder in Android Studio (recent stable), allow Gradle sync, then run the `app` configuration.

## Backend
The existing Komikta Node/Express backend remains separate and should be deployed unchanged. Configure the production API base URL in the Android networking module before connecting real data.

## Current state
This is the first native migration foundation: app module, native navigation shell, theme baseline, and four primary destinations. The original web project contains full catalog/detail/chapter/reader/offline behaviors that still need to be ported screen-by-screen and verified against the live backend. This scaffold does not claim feature parity yet.
