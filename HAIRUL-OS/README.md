# HAIRUL-OS — Phase 1

Native Android (Kotlin, Jetpack Compose, Material 3, MVVM, Gradle Kotlin DSL) foundation with the J.A.R.V.I.S. interface.

> **Status: source complete, not yet compiled.** The environment that generated this project had no Android SDK, Gradle or network access, so no build was run and no APK exists. Treat compile errors on first sync as expected work, not as surprises. See "Verification status" below.

## Modules

| Module | Package | Purpose |
|---|---|---|
| `:app` | `com.hairulos.jarvis` | JARVIS Mobile: shell, bottom navigation, Navigation Compose |
| `:launcher` | `com.hairulos.launcher` | JARVIS Launcher: separate app with HOME intent filter |
| `:core:design` | `com.hairulos.core.design` | Theme, colours, all reusable components, shared window theme and icon |
| `:core:common` | `com.hairulos.core.common` | `AiState`, clock helpers, `ActivityEntry` |
| `:core:device` | `com.hairulos.core.device` | Battery and network monitoring (`DeviceStatusMonitor`) |
| `:feature:dashboard` | `com.hairulos.feature.dashboard` | Home, Mission Control, System screens + `DashboardViewModel` |
| `:feature:assistant` | `com.hairulos.feature.assistant` | Assistant screen + demo `AssistantViewModel` |
| `:feature:settings` | `com.hairulos.feature.settings` | Settings screen + `SettingsViewModel` |

## Design system (`core:design`)
`JarvisBackground`, `JarvisTopBar`, `JarvisAICore`, `JarvisStatusPanel`, `JarvisGlassCard`, `JarvisBottomNavigation`, `JarvisCommandInput`, `JarvisWaveform`, `JarvisLoadingIndicator`, plus `JarvisStateSelector`, `JarvisInfoRow`, `JarvisMicIcon`. Previews are in each feature file and `DesignPreviews.kt`.

## Versions (all stable, from one catalog: `gradle/libs.versions.toml`)
Gradle 8.7, AGP 8.5.2, Kotlin 2.0.20 (Compose compiler Gradle plugin), Compose BOM 2024.09.00, Navigation Compose 2.8.0, Lifecycle 2.8.5, Activity Compose 1.9.2, Coroutines 1.8.1. compileSdk/targetSdk 34, minSdk 26, JDK 17.

## Build instructions
1. Install Android Studio (Koala or newer) with Android SDK Platform 34.
2. Open the `HAIRUL-OS` folder. Studio reads `gradle/wrapper/gradle-wrapper.properties` and downloads Gradle 8.7.
3. Command line: the wrapper scripts (`gradlew`, `gradle-wrapper.jar`) are binaries that could not be generated here. Run once with a local Gradle 8.7+: `gradle wrapper --gradle-version 8.7`. Create `local.properties` with `sdk.dir=/path/to/Android/Sdk` if Studio has not.
4. Build both apps: `./gradlew :app:assembleDebug :launcher:assembleDebug`
   APKs appear in `app/build/outputs/apk/debug/` and `launcher/build/outputs/apk/debug/`.
5. Run either configuration from Studio on a device or emulator (API 26+).

## Using the launcher
The launcher is a normal app (appears in the app list) and also declares the HOME category. It never requests default status. To use it: install it, then Android Settings → Apps → Default apps → Home app → JARVIS Launcher (path varies by manufacturer). Pressing Home with several launchers installed also shows Android's chooser. Switch back the same way.

## What works in Phase 1
- Five real screens: Home, Assistant, Mission Control (static roadmap), System (live device data), Settings.
- AI core on one Canvas: three rotating rings, radar sweep, pulsing centre, particles, state colours for IDLE / LISTENING / THINKING / SPEAKING / ERROR.
- State changes via demo buttons, the mic button (toggles LISTENING) and sending a command (scripted THINKING → SPEAKING → IDLE).
- Live clock, battery, network; weather tile is labelled unavailable.
- Settings toggles (animations, particles, radar) affect the core immediately; they are not persisted.
- Launcher: clock, tappable AI core (cycles states), page dots, favorites dock (placeholders), app drawer placeholder, swipe architecture (`LauncherGesture`, `launcherGestures`, `LauncherViewModel.onGesture`): up opens the drawer, down closes it, left/right change page.

## Not implemented (by design)
Speech recognition, any AI model, weather provider, real installed-app list and favorites in the launcher, settings persistence.

## Verification status
Done without a toolchain: file/package-path consistency check, brace balance check, XML and TOML parse check, manual review of imports and APIs.
Not done: Gradle sync, compilation, Compose previews, device or emulator run, APK generation.
