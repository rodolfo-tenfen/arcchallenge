# ArcTouch Mobile Development Code Challenge

## Synopsis

This project is my resolution of **ArcTouch's Mobile Code Challenge**. It is an app that shows you a list of upcoming movies provided by The Movie Database's API. Clicking on a movie on the list gives you detailed information on it, also provided by TMDb. Finally the app also allows you to search The Movie Database using the full or partial title of a movie.

## Screenshots
<p align="center">
<img src="https://github.com/rodolfo-tenfen/arcchallenge/raw/dev/screenshots/main_activity.png" width="400"> <img src="https://github.com/rodolfo-tenfen/arcchallenge/raw/dev/screenshots/thor_ragnarok_details.png" width="400"> </p>

## Installation

The app was developed targeting Android's SDK 26 (Oreo), but with minimum SDK 19 (KitKat). To run it simply download this project, import it to Android Studio and run it on an actual Android phone or on an Emulator.

## Libraries Used

* **Retrofit:** Used to handle HTTP requests. It was used to turn The Movie Database's API into a Java interface, so that functionalities could be accessed as regular Java methods. Retrofit also handles all network calls and updates to the user thread
* **Gson:** The Movie Database's API responds using JSON objects, which can be transparently converted to Java objects using Gson
* **Retrofit Gson Converter:** Retrofit can translate JSON objects received as soon as they are received in responses to Java objects using its Gson Converter
* **Picasso:** Used to load images, this library has a built in cache and handles updates to the user thread