# Âme Noire - Clothing Shopping Application <br>(Android)

> [!Note]
> This app requiers a valid network connection to pull data once for offline-mode. <br>
> Minimal [SDK version](https://apilevels.com) required to run the app: **24**


## Overview

Âme Noire is a Kotlin-based Android application designed to deliver an immersive and convenient clothing shopping experience. Our app is dedicated to offering a diverse range of fashion items, allowing users to explore and purchase the latest trends effortlessly.

This application relies on the [Fakestore API](https://fakestoreapi.com) to curate a dynamic catalog of virtual clothing and accessories.

## Index

[Screenshots](README.md#Screenshots)

[Installation](README.md#Installation)

[Features](README.md#Features)

[Android Technology Implementation](README.md#Android_Technology_Implementation)

[Libraries](README.md#Libraries)

[Contributing](README.md#Contributing)

[Contact/Authors](README.md#Contact/Authors)



## Screenshots

<p float="left">
  <img src="/Âme Noire Images/homescreen.png" width="150" />
  <img src="/Âme Noire Images/searchscreen.png" width="150" />
  <img src="/Âme Noire Images/favoritescreen.png" width="150" />
  <img src="/Âme Noire Images/cartscreen.png" width="150" />
  <img src="/Âme Noire Images/profilescreen.png" width="150" />
</p>



## Installation

1. Clone the Âme Noire app using **Git**

```git
  git clone https://github.com/Yanni17/Ame_Noire.git [your-destination-path]

```
2. Launch Android Studio and open the project.

3. Build and run the application by deploying it on your physical device or utilizing the Android Emulator provided.



## Features

- ***Product Variety:*** Explore a diverse range of clothing products.
- ***Smooth User Interface:*** Enjoy a sleek and user-friendly interface.
- ***Data Caching:*** Benefit from efficient data caching for improved performance.
- ***Wish and Cartlist Management:*** Easily save your favorite items for later or organize your shopping cart for a smooth checkout process.
- ***Personalized Profiles:*** Create your personalized profile, allowing you to track your order history, manage preferences, and receive tailored recommendations.




## Android Technology Implementation
- **[MVVM Pattern](https://developer.android.com/topic/architecture)**
- **[Fragments](https://developer.android.com/guide/fragments)**
- **[ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)**
- **[LiveData](https://developer.android.com/topic/libraries/architecture/livedata)**
- **[Navigation components](https://developer.android.com/guide/navigation/get-started)**
- **[Kapt](https://kotlinlang.org/docs/kapt.html)**
- **[RecyclerView](https://developer.android.com/reference/androidx/recyclerview/widget/RecyclerView)**



## Libraries
This app utilizes various third-party libraries and technologies:

- **[Android Architecture Components](https://developer.android.com/topic/architecture)** (ViewModel, LiveData)
- **[Room Database](https://developer.android.com/training/data-storage/room)** for caching
- **[Retrofit2](https://github.com/square/retrofit)** for network requests
- **[Coil](https://github.com/coil-kt/coil)** for Image handling
- **[Material 3](https://m3.material.io)** for UI components

### Backend Integration

Âme Noire leverages Firebase services for seamless backend functionality.

- **[Authentication:](https://firebase.google.com/docs/auth?hl=de)**
  - User registration and login are powered by Firebase Authentication.
  - Secure and flexible authentication methods are implemented for user convenience.

- **[Database:](https://firebase.google.com/docs/firestore?hl=de)**
  - Firebase Firestore is utilized for efficient data storage.
  - The shopping cart information and other user-related data are securely stored and managed in Firestore.

These backend services ensure a reliable and secure foundation for Âme Noire, providing users with a smooth experience while interacting with the app.




## Contributing

[NoctRise](https://github.com/NoctRise)

[SelcukOzkara](https://github.com/SelcukOzkara)




## Contact/Authors

[Yanni17](https://github.com/Yanni17)




