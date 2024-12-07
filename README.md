[Watch my demo](https://github.com/ANIKINKIRILL/TestTask/blob/main/TapYouDemoApplication.MOV)

# An application for displaying points on a graph

This is an Android application written in Kotlin, which requests a certain number of coordinates of points (x, y) from the server, and then displays the received response in the form of a table and graph.

## Functionality

- The user enters the number of dots on the main screen and presses the "GO" button.
- The application sends a request to the server API (GET /api/test/points) with a parameter for the number of requested points (count).
- The server returns an array of points in JSON format.
- If the response from the server is received successfully, the application goes to a new screen and displays a table with the received coordinates of the points.
- A graph with points connected by straight lines is displayed below the table. The points on the graph follow the x coordinate in ascending order.
- All input errors are handled in the first screen
- User can download screen image to device local storage and see it from gallery


## Technologies and libraries

- Programming language: Kotlin
- Architecture: MVI, ViewModel
- Views, basic Layouts and View Binding
- Networking: Retrofit, Coroutines
- Graph display: MPAndroidChart
- DI: Koin
