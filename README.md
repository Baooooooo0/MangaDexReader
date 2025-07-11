# Mangadex Reader

A simple manga reader application for Android, built with modern technologies. This app utilizes the public Mangadex API to search for, display, and read manga.

## ✨ Features

  * **Search Manga**: Find manga by title.
  * **Manga List**: Displays a list of manga with cover art and titles.
  * **Infinite Scrolling**: Automatically loads more manga as the user scrolls to the end of the list.
  * **Manga Details**: View detailed information about a manga, including its description and a list of chapters.
  * **Manga Reader**: Read manga with a page-turning interface (using a Pager).
  * **State Handling**: Clearly displays loading, error, and success states.

-----

## 🛠️ Tech Stack and Architecture

This project was built with the goal of using modern best practices and technologies for Android development.

### Core Technologies

  * **Language**: [Kotlin](https://kotlinlang.org/)
  * **User Interface**: [Jetpack Compose](https://developer.android.com/jetpack/compose)
  * **Architecture**: MVVM (Model-View-ViewModel)
  * **Asynchronous Programming**: [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)
  * **Dependency Injection**: Built-in with Jetpack Compose (`viewModel()`).
  * **Networking**:
      * [Retrofit](https://square.github.io/retrofit/): For making API calls.
      * [OkHttp Logging Interceptor](https://github.com/square/okhttp/tree/master/okhttp-logging-interceptor): For debugging network requests.
  * **JSON Parsing**: [Kotlinx Serialization](https://github.com/Kotlin/kotlinx.serialization)
  * **Image Loading**: [Coil](https://coil-kt.github.io/coil/compose/)
  * **Navigation**: [Navigation for Compose](https://developer.android.com/jetpack/compose/navigation)

### Project Structure

The project is organized into feature-based packages for easy management and scalability:

  * `data`: Contains data classes (Models), the Retrofit `ApiService`, `ApiClient`, and `MangaRepository`.
  * `repository`: The class responsible for fetching data from the API.
  * `ui`: Contains the user interface components (Composables) and ViewModels.
      * `mainscreen`: The screen for listing and searching manga.
      * `detailscreen`: The screen for manga details and the chapter list.
      * `reader`: The screen for reading manga.
      * `theme`: The application's theme (colors, typography).
  * `navigation`: Defines the routes for navigating within the app.

-----

## 🚀 Setup and Run

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/your-username/MangaDexReader.git
    ```
2.  Open the project in **Android Studio**.
3.  Wait for Gradle to sync the dependencies.
4.  Build and run the application on an emulator or a physical device.
