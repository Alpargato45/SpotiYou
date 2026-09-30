
# SpotiYou

An Android application that connects to the Spotify Web API to turn Spotify listening data into a personalized mobile experience.

SpotiYou is an Android application developed as a final project for a Higher Vocational Training program in Multiplatform Application Development (DAM).

The project was built to explore real-world Android development, REST API consumption, OAuth authentication, local persistence, JSON processing, asynchronous operations, custom UI components, and integration with an external platform.

Rather than simply displaying Spotify data, SpotiYou combines account management, listening statistics, search, artist/album/track exploration, playback information and Spotify actions into a single mobile application.

✨ Features
🔐 Spotify Authentication
OAuth 2.0 authorization flow with Spotify.
Custom deep-link callback using spotiyou://callback.
Access token and refresh token handling.
Automatic token refresh for previously connected accounts.
Local account selection when the application starts.
👤 Account Management
Multiple Spotify accounts can be stored locally.
Account selection screen at application startup.
Spotify profile information and profile images.
Logout functionality for the currently selected account.
🏠 Home Dashboard

The home screen provides a quick overview of the user's Spotify activity:

```
Currently playing track.
Current playback device.
Recently played tracks.
Spotify profile information.
Premium account indicator.
📊 Listening Statistics
```

Explore personal Spotify listening data across different time ranges:

```
Top artists — last 4 weeks.
Top artists — last 6 months.
Top artists — all time.
Top tracks — last 4 weeks.
Top tracks — last 6 months.
Top tracks — all time.
🔎 Music Search
Search for tracks using the Spotify Web API.
Display results using custom list adapters.
Open detailed information for individual tracks.
🎤 Artist Information
Artist profile information.
Artist image.
Top tracks.
Albums.
Follow/unfollow functionality.
Follow status verification.
💿 Album Information
Album artwork.
Album metadata.
Track listing.
Navigation from album tracks to individual track details.
🎵 Track Information
Track artwork.
Track name.
Artists.
Popularity.
Release date.
Duration.
Short audio preview when available.
Open the track directly in Spotify.
Add a track to the current Spotify playback queue.
🎨 Custom Android UI
Fragment-based navigation.
Bottom navigation.
Navigation drawer.
Custom ListView and GridView adapters.
Custom layouts for artists, tracks and accounts.
Picasso-based remote image loading.
Custom application fonts and visual resources.
📸 Screenshots

```

The screenshots below are intentionally focused on the features that best demonstrate the application from both a user and technical perspective.

<h2>Screenshot 1 — Account Selection</h2>

<img width="1080" height="2400" alt="Screenshot_2024-06-16-14-06-54-350_com example spotiyou" src="https://github.com/user-attachments/assets/bb0faae5-66e1-4ae1-9d01-57311fd9806b" />


<h2>Screenshot 2 — Home Dashboard</h2>

<img width="1220" height="2712" alt="Screenshot_2026-10-01-00-54-23-410_com example spotiyou" src="https://github.com/user-attachments/assets/473fc747-420b-4de8-90d2-7f95d3cae3c5" />





<h2>Screenshot 3 — Listening Statistics</h2>

<img width="1220" height="2712" alt="Screenshot_2026-10-01-01-04-08-371_com example spotiyou" src="https://github.com/user-attachments/assets/dbc9bfb4-b85c-4ac4-82a7-2f0462a233ca" />




<h2>Screenshot 4 — Music Search</h2>

<img width="1220" height="2712" alt="Screenshot_2026-10-01-01-04-29-472_com example spotiyou" src="https://github.com/user-attachments/assets/4cfbb965-8209-448f-b059-ae1d432883b0" />





<h2>Screenshot 5 — Artist Details</h2>

<img width="1220" height="2712" alt="Screenshot_2026-10-01-01-02-39-917_com example spotiyou" src="https://github.com/user-attachments/assets/b1140bca-eea6-4964-887a-2c92475a28cd" />
<img width="1220" height="2712" alt="Screenshot_2026-10-01-01-02-35-329_com example spotiyou" src="https://github.com/user-attachments/assets/418c4062-2ee6-4a8d-a14c-886a3d4d3823" />





<h2>Screenshot 6 — Album Details</h2>

Show an album with its metadata and track listing.




<h2>Screenshot 7 — Track Details</h2>

<img width="1220" height="2712" alt="Screenshot_2026-10-01-01-03-06-759_com example spotiyou" src="https://github.com/user-attachments/assets/8c475a23-4594-46fe-b66d-5c6a3af83826" />




```🛠️ Tech Stack
Technology	Purpose
Java	Main application language
Android SDK	Mobile application platform
AndroidX	Android application components
Material Components	UI components and navigation
Spotify Web API	Music, user, playback and statistics data
OAuth 2.0	Spotify authentication and authorization
SQLite	Local account and token persistence
OkHttp	HTTP requests to Spotify
Gson	JSON parsing
Picasso	Remote image loading
Gradle Kotlin DSL	Build configuration
JUnit	Unit testing
Espresso	Android UI/instrumentation testing
Project Configuration
Compile SDK: 34
Target SDK: 34
Minimum SDK: 24
Android Gradle Plugin: 8.1.1
Gradle: 8.9
Java source compatibility: Java 8
```
🏗️ Architecture

SpotiYou follows a practical Android architecture built around Activities, Fragments, dedicated Spotify API classes, custom adapters and a local SQLite database.

The project separates Spotify operations by domain, including:

```
Albums
Artists
Tracks
Search
Player
Users
Follow management
```

This keeps API-specific functionality separated from the main UI components and makes the codebase easier to navigate and extend.

High-Level Architecture
flowchart TD
    UI[Android UI<br/>Activities + Fragments] --> API[Spotify API Integration]
    UI --> DB[Local SQLite Database]

    API --> AUTH[Spotify OAuth 2.0]
    API --> SPOTIFY[Spotify Web API]
    API --> JSON[JSON Parsing<br/>Gson]

    JSON --> UI
    DB --> UI
```text
Application Flow

Application Startup
        │
        ▼
Account Selection
        │
        ├── New Account
        │       │
        │       ▼
        │   Spotify OAuth
        │       │
        │       ▼
        │   Authorization Code
        │       │
        │       ▼
        │   Access + Refresh Token
        │       │
        │       ▼
        │   SQLite Persistence
        │
        └── Existing Account
                │
                ▼
          Refresh Access Token
                │
                ▼
        Main Application
                │
       ┌────────┼────────┐
       ▼        ▼        ▼
     Home     Music    Search
       │        │        │
       ▼        ▼        ▼
   Playback   Stats    Spotify
    Data      + Data   Search
       │        │        │
       └────────┼────────┘
                ▼
        Detail Screens
     Artist / Album / Track
```

The original authentication flow diagram is also included in the project:

app/src/main/java/com/example/spotiyou/esquemaConexionSpotify.png
🌐 Spotify Web API Integration

The application communicates with Spotify through dedicated Java classes responsible for individual API operations.

```
User Data
Current user profile.
Top artists.
Top tracks.
Recently played tracks.
Player
Currently playing track.
Available playback devices.
Add tracks to the playback queue.
Search & Catalogue
Track search.
Track details.
Artist details.
Artist albums.
Album details and tracks.
Artist top tracks.
Follow System
Check whether an artist is followed.
Follow an artist.
Unfollow an artist.
```

The application therefore demonstrates integration with several independent areas of a real-world third-party REST API rather than relying on a single endpoint.

🔐 Authentication Flow

SpotiYou implements a Spotify authorization-code flow using a custom application URI scheme.

The configured redirect URI is:

spotiyou://callback

The authentication process works as follows:

The user selects an account slot.
For a new account, Spotify authorization is opened.
Spotify redirects the user back to spotiyou://callback.
SpotiYou extracts the authorization code.
The authorization code is exchanged for an access token and refresh token.
Tokens are stored locally for the selected account.
The access token is used for authenticated API requests.
When an existing account is selected, the stored refresh token is used to obtain a new access token.

The application requests permissions related to:

Private profile information.
Email information.
Recently played tracks.
Currently playing information.
Playback state.
Top artists and tracks.
Playback queue modification.
Artist follow status and modifications.

This flow was one of the main technical challenges of the project because it required coordinating Android deep links, browser/WebView navigation, authorization codes, token exchange and local persistence.

🗄️ Local Data Storage

The application uses SQLite through Android's SQLiteOpenHelper API.

The local database stores account-related information including:

Internal account identifier.
Spotify display name.
Profile image.
Account connection state.
Access token.
Refresh token.

This allows SpotiYou to remember connected accounts between sessions and refresh authentication without requiring the user to authorize Spotify every time.

The database is encapsulated in the BBDD class, while account information is represented through dedicated data-model classes and displayed through custom adapters.


```
📂 Project Structure
app/src/main/java/com/example/spotiyou/
│
├── APISpotify/
│   ├── Albums/          # Album and album-track API operations
│   ├── Artists/         # Artist-related API operations
│   ├── Player/          # Playback and queue operations
│   ├── Search/          # Spotify search operations
│   ├── Tracks/          # Track API operations
│   ├── Users/           # Profile, top music and follow operations
│   └── Personalizados/  # Custom adapters and data models
│
├── BBDD/                # SQLite database and local user models
│
├── Fragments/           # Main application screens
│   └── MusicaFragment/  # Music statistics and navigation
│
├── MasInformacion/      # Artist, album and track detail screens
│
├── TOKEN/              # Authentication state helpers
│
├── ActivityElegirCuenta.java
├── ActivityPrincipal.java
├── ConexionSpotifyApi.java
└── PantallaCargaInicio.java

```

The project contains approximately 50 Java source files, covering UI, API integration, authentication, persistence, adapters and data models.

⚙️ Getting Started
Requirements
Android Studio.
Android SDK 34.
A JDK compatible with the Android Gradle Plugin used by the project.
A Spotify account.
A Spotify Developer application.
1. Clone the Repository
git clone https://github.com/YOUR_USERNAME/SpotiYou.git
cd SpotiYou
2. Create a Spotify Developer Application

Create a Spotify Developer application and configure the following redirect URI:

spotiyou://callback

You will need:

Spotify Client ID
Spotify Client Secret
3. Configure Credentials Securely

The original academic version of SpotiYou contains the Spotify credentials directly inside ConexionSpotifyApi.java.

This was acceptable for the original local academic environment, but credentials must not be committed to a public repository.

Before publishing the project:

Revoke/rotate any credentials that have previously been exposed.
Remove the hard-coded credentials from the source code.
Store them in a local or environment-specific configuration.
Keep the secret outside version control.

A production-oriented implementation could use a configuration approach such as:

private static final String CLIENT_ID =
        BuildConfig.SPOTIFY_CLIENT_ID;

private static final String CLIENT_SECRET =
        BuildConfig.SPOTIFY_CLIENT_SECRET;

rather than hard-coded values.

4. Open the Project

Open the project in Android Studio and allow Gradle to synchronize the project and download the required dependencies.

5. Run the Application

Run the app configuration on an Android device or emulator running:

Android 7.0 / API 24 or newer
🧪 Testing

The project includes the standard Android testing infrastructure:

JUnit for local unit tests.
AndroidX Test.
Espresso for instrumentation/UI testing.

The current testing layer is intentionally limited because the original project focused primarily on implementing the application's functionality.

A natural next step would be expanding automated coverage around:

Authentication.
Token refresh.
JSON parsing.
SQLite operations.
Spotify API error handling.
Search behaviour.
Main navigation flows.
🚀 Future Improvements

The current project provides a functional foundation, but several improvements would make it more production-ready.

🔐 Security
Move Spotify credentials outside the source code.
Protect locally stored authentication data.
Improve token storage and lifecycle management.
🧩 Architecture
Introduce a dedicated repository/service layer.
Separate API, database and presentation responsibilities more strictly.
Introduce dependency injection.
Improve lifecycle-aware state management.
⚡ Modern Android Development
Replace deprecated AsyncTask usage with modern asynchronous approaches.
Improve lifecycle handling.
Modernize the Java/Android build configuration.
Update dependencies where appropriate.
🧪 Testing
Increase unit test coverage.
Add integration tests for API communication.
Add UI tests for the main application flows.
🌐 Networking
Improve handling of connection failures.
Handle expired sessions and API errors more gracefully.
Avoid performing network operations directly from UI components.
Introduce a more robust networking abstraction.
🎨 UI/UX
Improve responsiveness across screen sizes.
Refine loading and error states.
Add animations and transitions where appropriate.
Improve accessibility.
💡 What This Project Demonstrates

From a software-development perspective, SpotiYou demonstrates practical experience with:

Java and Object-Oriented Programming
Android application development
REST API integration
OAuth 2.0 authentication
HTTP communication
JSON parsing
SQLite and relational data persistence
Access and refresh token management
Fragment-based navigation
Custom Android adapters
Dynamic UI components
Remote image loading
Android intents and deep links
Asynchronous operations
Third-party API integration
Feature/domain-based code organization

More importantly, the project demonstrates the ability to take an external service, understand its API and authentication requirements, and integrate it into a complete mobile application with persistent local state and a multi-screen user experience.

📌 Project Status

Academic Project — Functional Prototype / Portfolio Project

SpotiYou was developed as a final project to demonstrate the complete development of an Android application integrating a real-world third-party API.

The current implementation is functional, while some areas — particularly security, asynchronous networking, automated testing and architectural modernization — could be improved before considering the application production-ready.

The project is therefore presented both as a finished academic application and as a foundation for further development.

📄 License

This project was developed for educational and portfolio purposes.

Spotify is a trademark of Spotify AB. SpotiYou is an independent project and is not affiliated with or endorsed by Spotify.

👤 Author

[Jorge del Cid Moreno]

Final Project — Multiplatform Application Development (DAM)
