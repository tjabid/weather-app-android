# weather-app-android

This app follows **Clean Architecture** with **MVVM** pattern using **Jetpack Compose** for UI.

## Layer Structure

```
com.tj.weather/
├── domain/              # Core Business Logic (Pure Kotlin)
│   ├── models/          # Domain entities
│   ├── repositories/    # Repository interfaces
│   └── usecases/        # Business logic use cases
│
├── data/                # Data Layer (Implementation)
│   ├── datasources/     # API services, local storage
│   │   └── remote/      # Retrofit API services
│   ├── repositories/    # Repository implementations
│   └── mappers/         # DTO to Domain model mappers
│
├── feature/             # Presentation Layer (MVVM)
│   └── forecast/        # Forecast feature module
│       ├── viewmodel/   # ViewModels
│       ├── ui/          # Compose screens & components
│       └── state/       # UI state models
│
├── di/                  # Dependency Injection (Hilt modules)
│
└── ui/                  # Shared UI components & theme

```

## Dependency Rule
**Inner layers NEVER depend on outer layers:**
- Domain → No dependencies (pure Kotlin)
- Data → Depends on Domain only
- Presentation → Depends on Domain only
- DI → Wires everything together


### State Management
Exposes StateFlow<ForecastUiState> for reactive UI
Private mutable state, public immutable state (best practice)
State updates in viewModelScope for lifecycle awareness

### Functions Implemented
loadWeatherForecast()
Sets Loading state
Fetches location → Fetches forecast → Determines weather type
Maps to Success state with forecast + current weather type
Handles errors (network, API, permission)
Smart error detection (checks for "permission" in error message)
retryLoadWeather()
Simple retry mechanism
Calls loadWeatherForecast() again
onPermissionResult(granted: Boolean)
Handles permission callback from UI
If granted → loads weather
If denied → shows PermissionDenied state
### State Flow Logic
```
Loading → getCurrentLocation()
  ├─ Success → getWeatherForecast()
  │    ├─ Success → determineWeatherType() → Success(forecast, type)
  │    └─ Failure → Error(message, retryable=true)
  └─ Failure → PermissionDenied (if permission error)
           or → Error(message, retryable=true)
```

Weather Condition Icons Reference: https://openweathermap.org/weather-conditions?collection=other