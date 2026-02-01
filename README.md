# Weather App - Android

A modern weather forecast application built with Jetpack Compose, following Clean Architecture and MVVM pattern.

## 📱 Features

- **5-Day Weather Forecast**: View detailed weather forecasts for the next 5 days
- **Real-time Location**: Uses device's current location via FusedLocationProviderClient
- **Dynamic Backgrounds**: Background changes based on current weather conditions
- **Material Design 3**: Modern UI with support for light and dark themes
- **Offline Support**: Caches last known location for faster loading
- **Graceful Error Handling**: User-friendly error messages with retry options

## 🏗️ Architecture

This app follows **Clean Architecture** with **MVVM** pattern using **Jetpack Compose** for UI.

### Layer Structure

```
com.tj.weather/
├── domain/              # Core Business Logic (Pure Kotlin)
│   ├── models/          # Domain entities (Location, WeatherForecast, etc.)
│   ├── repositories/    # Repository interfaces
│   └── usecases/        # Business logic use cases
│
├── data/                # Data Layer (Implementation)
│   ├── datasources/     # API services, location services
│   │   └── remote/      # Retrofit API services, FusedLocationProvider
│   ├── repositories/    # Repository implementations
│   ├── mappers/         # API DTO to Domain model mappers
│   └── network/         # Network provider (Retrofit, OkHttp)
│
├── feature/             # Presentation Layer (MVVM)
│   └── forecast/        # Forecast feature module
│       ├── viewmodel/   # ViewModels (ForecastViewModel)
│       ├── ui/          # Compose screens & components
│       └── state/       # UI state models (ForecastUiState)
│
├── di/                  # Dependency Injection (Manual DI / Ready for Hilt)
│
└── ui/                  # Shared UI components & theme (Material 3)
```

### Dependency Rule

**Inner layers NEVER depend on outer layers:**
- **Domain** → No dependencies (pure Kotlin)
- **Data** → Depends on Domain only
- **Presentation** → Depends on Domain only  
- **DI** → Wires everything together

### Data Flow

```
UI (Composable)
    ↓ observes StateFlow
ViewModel
    ↓ executes
Use Cases
    ↓ calls
Repository Interface (Domain)
    ↓ implemented by
Repository Implementation (Data)
    ↓ uses
Data Sources (API, Location Services)
```

## 🚀 Setup Instructions

### Prerequisites

- Android Studio Hedgehog (2023.1.1) or later
- JDK 17
- Android SDK 34
- Minimum SDK 24 (Android 7.0)

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/weather-app-android.git
cd weather-app-android
```

### 2. API Key Configuration

This app uses the [OpenWeatherMap API](https://openweathermap.org/api) for weather data.

#### Get Your API Key

1. Sign up at [OpenWeatherMap](https://home.openweathermap.org/users/sign_up)
2. Navigate to [API Keys](https://home.openweathermap.org/api_keys)
3. Generate a new API key

#### Configure in Project

Create or edit `local.properties` in the project root:

```properties
# local.properties (This file is gitignored)
sdk.dir=/path/to/your/Android/sdk
WEATHER_API_KEY=your_api_key_here
```

**⚠️ Important**: Never commit `local.properties` to version control. It's already in `.gitignore`.

### 3. Build and Run

```bash
# Using Gradle
./gradlew assembleDebug

# Or run directly from Android Studio
# Click Run → Run 'app'
```

## 🧪 Testing Instructions

### Run Unit Tests

```bash
# Run all tests
./gradlew test

# Run specific test class
./gradlew test --tests "GetWeatherForecastUseCaseTest"

# Run tests with coverage
./gradlew testDebugUnitTestCoverage
```

### Run Instrumented Tests

```bash
# Run all instrumented tests
./gradlew connectedAndroidTest

# Run specific instrumented test
./gradlew connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.tj.weather.MainActivityTest
```

### Code Coverage

```bash
# Generate coverage report
./gradlew testDebugUnitTestCoverage

# Report location: build/reports/coverage/test/debug/index.html
```

## 📦 Dependencies

### Core
- Kotlin 1.9.0
- Jetpack Compose (BOM 2024.02.00)
- Material Design 3
- Coroutines 1.7.3

### Architecture
- ViewModel & Lifecycle
- Navigation Compose (Ready for integration)
- Hilt (Ready for integration)

### Network
- Retrofit 2.9.0
- OkHttp 4.12.0
- Gson Converter

### Location
- Google Play Services Location 21.1.0

### Testing
- JUnit 4.13.2
- Mockito
- Coroutines Test
- Hilt Testing (Ready)

## 🎨 UI Components

### Screens
- **MainScreen**: Main container handling permission and location checks
- **ForecastContent**: Displays 5-day forecast list
- **LoadingIndicator**: Loading state UI
- **ErrorView**: Error display with retry button
- **PermissionRationale**: Permission request explanation
- **LocationServicesPrompt**: Location services disabled prompt

### Components
- **ForecastCard**: Individual day forecast card with weather details
- **WeatherBackground**: Dynamic background based on weather type
- **WeatherDetailItem**: Small weather metric display (humidity, wind, etc.)

## 🔐 Permissions

The app requires the following permissions:

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
```

### Permission Handling

1. **Check Permission**: App checks if location permission is granted
2. **Check Location Services**: Verifies GPS/Network providers are enabled
3. **Fetch Location**: Uses FusedLocationProviderClient to get current location
4. **Graceful Fallback**: Shows permission rationale if denied, with option to use cached location

## 🌐 API Integration

### OpenWeatherMap 5-Day Forecast API

**Endpoint**: `https://api.openweathermap.org/data/2.5/forecast`

**Example Request**:
```
GET /forecast?lat=25.2048&lon=55.2708&appid={API_KEY}&units=metric&cnt=40
```

**Response Handling**:
- Groups forecasts by day (8 readings per day)
- Calculates daily min/max temperatures
- Determines primary weather condition (uses noon reading)
- Maps weather icons to app weather types

## 🎯 Error Handling

The app provides user-friendly error messages for various scenarios:

| Error Type | Message | Retryable |
|------------|---------|-----------|
| No Internet | "No internet connection. Please check your network and try again." | ✅ |
| Timeout | "Request timed out. Please check your connection and try again." | ✅ |
| API Error (401) | "Invalid API key. Please contact support." | ❌ |
| API Error (404) | "Weather data not found for this location." | ✅ |
| API Error (429) | "Too many requests. Please try again in a few moments." | ✅ |
| Location Services Off | "Please enable location services in your device settings..." | ✅ |
| Permission Denied | "Location Permission Required - We need your location to provide..." | ✅ |

## 📊 State Management

### ForecastViewModel

Exposes `StateFlow<ForecastUiState>` for reactive UI updates.

**States**:
- `Loading`: Initial loading or refresh
- `Success(forecast, weatherType)`: Data loaded successfully
- `Error(message, retryable)`: Error occurred
- `PermissionDenied(hasCachedLocation)`: Location permission not granted
- `LocationServicesDisabled`: GPS/Network disabled
- `LoadLocation`: Fetching device location

### State Flow

```
App Launch
    ↓
Check Permission → Check Location Services
    ↓
┌────────────────────┬───────────────────┬──────────────┐
│ No Permission      │ Permission OK     │ All OK       │
│                    │ Location OFF      │              │
├────────────────────┼───────────────────┼──────────────┤
│ PermissionDenied   │ LocationServices  │ LoadLocation │
│                    │ Disabled          │      ↓       │
│                    │                   │  Fetch Loc   │
│                    │                   │      ↓       │
│                    │                   │ Fetch Weather│
│                    │                   │      ↓       │
│                    │                   │   Success    │
└────────────────────┴───────────────────┴──────────────┘
```

## 🎨 Theming

The app supports both **Light** and **Dark** themes following Material Design 3 guidelines.

**Color Palette**:
- Primary: Weather Blue (#2196F3)
- Secondary: Sunny Orange (#FF9800)
- Error: Alert Red
- Surface variations for cards and backgrounds

**Typography**:
- Display: Large headings (temperature)
- Headline: Screen titles
- Title: Card headers
- Body: Description text
- Label: Small details

## 🌤️ Weather Type Mapping

| OpenWeatherMap Code | App Weather Type | Background |
|---------------------|------------------|------------|
| 01d, 01n | SUNNY | Sunny.png |
| 02d-04d, 02n-04n | CLOUDY | Cloudy.png |
| 09d, 10d, 09n, 10n | RAINY | Rainy.png |
| 11d, 11n | THUNDERSTORM | Rainy.png |
| 13d, 13n | SNOW | Forest.png |

**Reference**: [OpenWeatherMap Weather Conditions](https://openweathermap.org/weather-conditions)

## 📝 Naming Conventions

- **Classes**: PascalCase (e.g., `ForecastViewModel`, `WeatherRepository`)
- **Functions**: camelCase (e.g., `loadWeatherForecast`, `getCurrentLocation`)
- **Variables**: camelCase (e.g., `weatherForecast`, `dailyForecasts`)
- **Constants**: UPPER_SNAKE_CASE (e.g., `BASE_URL`, `TIMEOUT_SECONDS`)

## 🛠️ Build Configuration

### Gradle

- Gradle: 8.6
- Android Gradle Plugin: 8.5.0
- Kotlin: 1.9.0
- Target SDK: 34
- Min SDK: 24
- Compile SDK: 34

### ProGuard

ProGuard rules are configured in `proguard-rules.pro` for release builds.

## 📈 Performance

- **Launch to Forecast**: Target < 3 seconds
- **Network Calls**: All on background threads using Coroutines
- **Compose Recompositions**: Optimized with remember and derivedStateOf
- **Image Loading**: Optimized vector drawables

## ♿ Accessibility

- **Minimum Touch Targets**: 48dp (Material Design guidelines)
- **Content Descriptions**: All images and icons have semantic descriptions
- **Screen Reader Support**: Proper labeling for TalkBack
- **Color Contrast**: AA compliance (4.5:1 for normal text)

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 👨‍💻 Author

**Abdul Rahim**

## 🙏 Acknowledgments

- OpenWeatherMap for weather data API
- Material Design 3 for design guidelines
- Jetpack Compose team for the modern UI toolkit
