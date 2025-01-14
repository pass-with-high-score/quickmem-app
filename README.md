# QuickMem

**QuickMem** is an Android application designed to enhance learning through flashcards. It helps
users efficiently memorize and review various subjects by offering a flashcard-based learning
approach, similar to popular apps like Quizlet and Anki. The app is built using modern Android
technologies, including Kotlin, Jetpack Compose, Dagger Hilt, and more, ensuring a smooth and
engaging user experience.

## Participate in Testing

#### Step 1: Join the Google Group

[Google Group - QuickMem](https://groups.google.com/g/quickmem)

#### Step 2: Join the Closed Testing

[Google Play Closed Testing - QuickMem](https://play.google.com/apps/testing/com.pwhs.quickmem)

## Features

- **User Authentication & Onboarding**: Secure sign-up and sign-in process with user account
  management.
- **Flashcard & Study Set Management**: Create, edit, and manage flashcards and study sets for
  different subjects.
- **Progress Tracking**: Track study progress and flashcard performance over time.
- **Customizable Flashcard Modes**: Choose from various study modes to reinforce learning.
- **Daily Reminders**: Get reminders to review flashcards and stay on track with learning goals.
- **Network Operations**: Sync data and flashcards with the server using Ktor and Retrofit.
- **Image Support**: Add images to flashcards for better context and memorization using Coil.
- **Logging & Debugging**: Built-in logging for debugging and performance monitoring with Timber.
- **Data Storage**: Local data storage using DataStore to persist user data and flashcards.
- **Daily Notifications**: Schedule daily notifications for reminders and study sessions.
- **AI-Powered Features**: Create smart flashcards and study sets using AI algorithms for better
  learning.
- **Multi-Language Support**: Support for multiple languages and localization for a global audience.
- **Multi-Method Study**: Use different study methods like flip, multiple-choice, and
  fill-in-the-blank for flashcards.

## Installation

### Prerequisites

- Android Studio Ladybug Feature Drop | 2024.2.2 Nightly 2024-09-16
- JDK 17
- Android SDK 29 or higher

### Steps

1. Clone the repository:
   ```sh
   git clone https://github.com/pass-with-high-score/quickmem-app.git
   cd quick_mem
   ```

2. Open the project in Android Studio.

3. Add the following lines to your `local.properties` file:
   ```properties
   BASE_URL="https://your-server-url.com"
   EMAIL_VERIFICATION_URL="https://your-server-url.com/verify-email"
   BANNER_ADS_ID="your-banner-ads-id"
   INTERSTITIAL_ADS_ID="your-interstitial-ads-id"
   REWARD_ADS_ID="your-reward-ads-id"
   REWARDED_INTERSTITIAL_ADS_ID="your-rewarded-interstitial-ads-id"
   ONESIGNAL_APP_ID="your-onesignal-app-id"(optional)
   REVENUECAT_API_KEY="your-revenuecat-api-key"
   RELEASE_STORE_FILE="your-release-store-file"
   RELEASE_KEY_ALIAS="your-release-key-alias"
   RELEASE_STORE_PASSWORD="your-release-store-password"
   RELEASE_KEY_PASSWORD="your-release-key-password"
   GOOOGLE_OAUTH_CLIENT_ID_DEBUG="your-google-oauth-client-id-debug"
   GOOOGLE_OAUTH_CLIENT_ID_PRODUCT="your-google-oauth-client-id-product"
   ```

4. Build the project:
   ```sh
   ./gradlew build
   ```

5. Run the app on an emulator or a physical device.

## Usage

1. Launch the app on your device.
2. Follow the onboarding process to set up your profile.
3. Create and manage your flashcards and study sets.
4. Review flashcards, track progress, and get daily reminders for efficient studying.

## Project Structure

- `app/src/main/java/com/pwhs/quickmem/`: Contains the main application code and features related to
  flashcard management, user authentication, and study modes.
- `app/src/main/res/`: Contains resource files like strings, images, and layouts.
- `app/build.gradle.kts`: Build configuration for the app module.
- `.github/workflows/`: Contains GitHub Actions workflows for CI/CD.

## Backend

The app utilizes a backend for syncing data, flashcards, and user information. For security reasons,
sensitive information such as VPS details and Google services are not publicly disclosed. Please
contact me via email for further information.

## Contributing

Contributions are what make the open-source community such an amazing place to learn, inspire, and
create. Any contributions you make are **greatly appreciated**.

## Acknowledgements

- [Jetpack Compose](https://developer.android.com/jetpack/compose) – Modern UI toolkit for Android.
- [Dagger Hilt](https://dagger.dev/hilt/) – Dependency injection library for Android.
- [Ktor](https://ktor.io/) – Asynchronous HTTP client for Kotlin.
- [Retrofit](https://square.github.io/retrofit/) – Type-safe HTTP client for Android.
- [Coil](https://coil-kt.github.io/coil/) – Image loading library for Android.
- [Timber](https://github.com/JakeWharton/timber) – A logger for Android.

