# Gps TrackerExample

Gps TrackerExample is an Android application that allows users to track their current GPS location
in real-time, visualize their tracked route on Google Maps, and store the location history locally
using Room database.

## Features

- Start and stop real-time GPS location tracking service
- Display current and previous locations on Google Maps
- Store location history locally (timestamp, latitude, longitude)
- View location logs in a RecyclerView list
- Draw route polylines on the map from location history
- Foreground service notification of latest location
- Permissions handling for location, notifications, and foreground/background location access

## Screenshots

*Add screenshots here of main screen/map, tracking service notification, RecyclerView history list*

## Requirements

- **Android Studio** Hedgehog (Giraffe+) or later
- **Android SDK** 21+
- **Google Maps API key** (see below)
- **Internet access**

## Setup

1. **Clone the repository**
2. **Open in Android Studio** (File > Open > select project root)
3. **Configure your Google Maps API Key**
    - Obtain an API
      key: [Google Maps Android SDK](https://developers.google.com/maps/documentation/android-sdk/get-api-key)
    - In your `local.properties` file (NOT checked into version control), add:
      ```
      personal_maps_API_key = YOUR_API_KEY_HERE
      ```
    - The key is referenced from AndroidManifest.xml via `${personal_maps_API_key}`.
4. **Build and run the app** on an emulator or real device (ensure location permissions granted)
5. **Location tracking**: Tap "Start Live Tracking" to begin, "End Live Tracking" to stop (history
   clears on end)

## Usage

- Grant all requested permissions (location, notifications)
- The map centers on your latest location, showing a polyline of your tracked route
- The RecyclerView lists the captured coordinates (tap an item for latitude/longitude toast)
- The foreground notification displays your live location

## Module structure

- `:app` — Main Android application module

## Main components

- **MainActivity.kt:** Google Map, location history display, start/stop service, permissions
- **LocationServices.kt**: Foreground service for location updates and notification
- **TrackingItem.kt / Dao / Database:** Room database entities/schema to persist location logs
- **activity_main.xml:** Layout with map, history list, and start/stop buttons

## Dependencies

- Kotlin 2.0.21
- AndroidX Core KTX 1.15.0
- Google Play Services Location 21.3.0
- Google Maps SDK for Android 19.0.0
- Room 2.6.1
- Material Components 1.12.0
- ViewBinding
  See `gradle/libs.versions.toml` for versions

## Building

- Uses Gradle with Kotlin DSL (`build.gradle.kts`)
- Run via Android Studio or with `./gradlew assembleDebug`

## License

This project is licensed under the terms of the [GNU General Public License v3.0](LICENSE).

## Notes

- Location history is reset when you tap "End Live Tracking"
- Do not check your `local.properties` (with API key) into source control
- To extend: add export to CSV, background tracking, or remote sync features
