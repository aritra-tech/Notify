
![Niotify](https://github.com/aritra-tech/Notify/assets/80090908/cce1cf41-a9e3-4379-88fb-99702ba23a9f)


## Notify 📱

📝 _**Notify**_ is a simple note application which is build with _Modern Android development_ tools.    
This project showcases _Good implementation_ of Android with proper architecture design.              
_Made with ♥ for all Android Developers_   

## Features  👓

_Notify_ focuses on the following key things:

- [x] Single Activity Design
- [x] Offline Capability - Notes ✈️
- [x] Clean and Simple Material UI 🎨
- [X] Dark mode 🌗
- [X] Back/Restore feature 👀
- [x] Jetpack Compose UI
- [ ] Tests

## Built with 🛠

- [Kotlin](https://kotlinlang.org/) - First class and official programming language for Android development.
- [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) - For asynchronous calls and tasks to utilize threads.
- [Jetpack Compose UI Toolkit](https://developer.android.com/jetpack/compose) - Modern UI development toolkit.
- [Android Architecture Components](https://developer.android.com/topic/libraries/architecture) - Collection of libraries that help you design robust, testable, and maintainable apps.
  - [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) - Data objects that notify views when the underlying database changes.
  - [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - Stores UI-related data that isn't destroyed on UI changes.
  - [Room](https://developer.android.com/topic/libraries/architecture/room) - Room is an Android library which is an ORM that wraps Android's native SQLite database.
  - [StateFlow and SharedFlow](https://developer.android.com/kotlin/flow/stateflow-and-sharedflow#:~:text=StateFlow%20is%20a%20state%2Dholder,property%20of%20the%20MutableStateFlow%20class.) - StateFlow and SharedFlow are Flow APIs that enable flows to optimally emit state updates and emit values to multiple consumers.
- [Dependency Injection](https://developer.android.com/training/dependency-injection) -
    - [Hilt-Dagger](https://dagger.dev/hilt/) - A standard way to incorporate Dagger dependency injection into an Android application.
    - [Hilt-ViewModel](https://developer.android.com/training/dependency-injection/hilt-jetpack) - DI for injecting ```ViewModel```. 
- [Material Components for Android](https://github.com/material-components/material-components-android) - Modular and customizable Material Design UI components for Android.

## Architecture 👷‍♂️

![mvvm](https://github.com/aritra-tech/Notify/assets/80090908/6adb42b6-c3b9-4134-8213-88d4ac002cf8)

This app uses [MVVM(Model View View-Model)](https://developer.android.com/topic/architecture#recommended-app-arch) architecture.
