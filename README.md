# Weather App - Android

A modern weather forecast application built with Jetpack Compose, following Clean Architecture and MVVM pattern.

##  Features

- **5-Day Weather Forecast**: View detailed weather forecasts for the next 5 days
- **Real-time Location**: Uses device's current location via FusedLocationProviderClient
- **Dynamic Backgrounds**: Background changes based on current weather conditions
- **Material Design 3**: Modern UI with support for light and dark themes
- **Offline Support**: Caches last known location for faster loading
- **Graceful Error Handling**: User-friendly error messages with retry options

## Architecture

This app follows **Clean Architecture** with **MVVM** pattern using **Jetpack Compose** for UI.

![Search_1_recent_search](https://miro.medium.com/v2/resize:fit:1400/format:webp/1*g6bqauGqu1u9Q1kZoNBvDQ.png)
Above image used from [this](https://medium.com/@ami0275/mvvm-clean-architecture-pattern-in-android-with-use-cases-eff7edc2ef76) article


### Dependency Rule

**Inner layers NEVER depend on outer layers:**
- **Domain** → No dependencies (pure Kotlin)
- **Data** → Depends on Domain only
- **Presentation** → Depends on Domain only
- **DI** → Wires everything together

### Data Flow

```
UI (Composable)
    | 
    | observes StateFlow
    ↓ 
ViewModel
    | 
    | executes
    ↓
Use Cases
    | 
    | calls
    ↓ 
Repository Interface (Domain)
    | 
    | implemented by
    ↓ 
Repository Implementation (Data)
    | 
    | uses
    ↓ 
Data Sources (API, Location Services)
```


## UI Components

### 5 days Forecast Screen
| Light Theme | Dark Theme                        |
|--------------------------------------|-----------------------------------|
| ![light.png](screenshots/light.png)  | ![dark.png](screenshots/dark.png) |

### Permission Screen
| Cached Location                     | Without Cached Location           |
|-------------------------------------|-----------------------------------|
| ![permissions-with-caching.png](screenshots/permissions-with-caching.png) | ![permissions.png](screenshots/permissions.png) |

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

## Permissions

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

## API Integration

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
| No Internet | "No internet connection. Please check your network and try again." | Yes       |
| Timeout | "Request timed out. Please check your connection and try again." | Yes       |
| API Error (401) | "Invalid API key. Please contact support." | No        |
| API Error (404) | "Weather data not found for this location." | Yes       |
| API Error (429) | "Too many requests. Please try again in a few moments." | Yes       |
| Location Services Off | "Please enable location services in your device settings..." | Yes       |
| Permission Denied | "Location Permission Required - We need your location to provide..." | Yes       |

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
│                    │                   │Fetch Location│
│                    │                   │      ↓       │
│                    │                   │ Fetch Weather│
│                    │                   │      ↓       │
│                    │                   │   Success    │
└────────────────────┴───────────────────┴──────────────┘
```

## Theming

The app supports both **Light** and **Dark** themes following Material Design 3 guidelines.

![forecast_card.png](screenshots/forecast_card.png)

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

## Weather Type Mapping

| OpenWeatherMap Code | App Weather Type | Background |
|---------------------|------------------|------------|
| 01d, 01n | SUNNY | Sunny.png |
| 02d-04d, 02n-04n | CLOUDY | Cloudy.png |
| 09d, 10d, 09n, 10n | RAINY | Rainy.png |
| 11d, 11n | THUNDERSTORM | Rainy.png |
| 13d, 13n | SNOW | Forest.png |

**Reference**: [OpenWeatherMap Weather Conditions](https://openweathermap.org/weather-conditions)

## Build Configuration

- Gradle: 8.6
- Android Gradle Plugin: 8.5.0
- Kotlin: 1.9.0
- Target SDK: 34
- Min SDK: 24
- Compile SDK: 34

## Code Quality & Analysis
- SonarQube Gradle Plugin 4.4.1.3373

### SonarQube Cloud Integration

This project uses **SonarQube Cloud** for continuous code quality inspection and static code analysis. SonarQube helps identify bugs, code smells, security vulnerabilities, and technical debt.

- **Automated Code Scanning**: Analyzes code on every push/PR
- **Security Vulnerability Detection**: Identifies potential security issues
- **Code Coverage Tracking**: Monitors test coverage across the codebase
- **Code Smell Detection**: Highlights maintainability issues
- **Duplicated Code Analysis**: Detects code duplication
- **Technical Debt Measurement**: Quantifies maintenance costs

## Testing Technologies

### Core Testing Libraries
- **JUnit 4.13.2**: Test framework
- **Mockito Core 5.7.0**: Mocking framework
- **Mockito Kotlin 5.2.1**: Kotlin extensions for Mockito
- **Coroutines Test 1.7.3**: Testing coroutines
- **Truth 1.4.0**: Assertion library (reserved for future use)

### Testing Patterns Used
- **AAA Pattern**: Arrange, Act, Assert
- **Given-When-Then**: BDD-style test naming
- **Test Doubles**: Mocks for dependencies
- **Test Dispatcher**: StandardTestDispatcher for coroutine testing


## Dependencies

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

### Libraries Used

- [OpenWeatherMap](https://openweathermap.org/forecast5?collection=current_forecast) for weather data API
- [Material Design 3](https://developer.android.com/develop/ui/compose/designsystems/material3) for design guidelines
- Jetpack Compose for the modern UI toolkit
- [Icons](https://fonts.google.com/icons)
