The app might not works because now the firebase remove the cloud storage to store and retreive images , as the images are required to show the other detail of the item . 
However, you can still work around the code 

# EAT-A-WAY -- Android Food Ordering App

A modern food ordering application built with Kotlin and Firebase, providing customers with a seamless food ordering experience on Android devices.

## Features

- User Authentication
  - Email/password signup and login
  - Google Sign-in integration
  - Password reset functionality
  - Remember me option
- Restaurant Discovery
  - Browse restaurants by cuisine
  - Search by dish or restaurant name
  - Filter by rating, price range, and delivery time
  - View restaurant details and menu
- Menu Browsing
  - Categories view
  - Dish details with images
  - Customization options
  - Allergen information
  - Vegetarian/Vegan filters
- Order Management
  - Real-time order tracking
  - Order history
  - Reorder functionality
  - Delivery status updates
- Cart Features
  - Add/remove items
  - Quantity adjustment
  - Special instructions
  - Estimated delivery time

- User Profile
  - Multiple delivery addresses
  - Favorite restaurants
  - Order preferences
  - Payment methods management

## Tech Stack

### Android
- Kotlin
- Firebase SDK
- Material Design 3
- XML
  - Navigation
  - View Binding
  - Coroutines
  - Flow
  - LiveData

### Backend Services (Firebase)
- Authentication
  - Email/Password
  - Google Sign-in
- Cloud Firestore
  - User profiles
  - Orders
  - Restaurant data
- Realtime Database
  - Order tracking
  - Live status updates
- Cloud Storage
  - Food images
  - User avatars
- Cloud Functions
  - Order processing
  - Notifications



## Getting Started

### Prerequisites
- Android Studio Arctic Fox or later
- Kotlin 1.6+
- Android SDK 21+
- Firebase account

### Installation

1. Clone the repository:
```bash
git clone https://github.com/by15190/Food_app.git
cd tasty-bites
```

2. Open the project in Android Studio

3. Create a Firebase project:
   - Go to Firebase Console
   - Create a new project
   - Add an Android app to the project
   - Download `google-services.json`
   - Place it in the app/ directory

4. Build and run the project in Android Studio

## Key Features Implementation



## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

# ⚠️ Important Notice: Firebase Cloud Storage Issue

This app was originally designed to store and retrieve images using Firebase Cloud Storage. However, due to Firebase's recent policy change, Cloud Storage now requires a paid **Blaze Plan**, which is not available on the free **Spark Plan**.

## 🚀 Workarounds & Next Steps

### 🔄 Alternative Storage Solutions
- The app can be modified to use **Cloudinary, Imgur, Supabase, or local storage** for images.
- Future updates may integrate a free cloud storage service.

### 🛠 How to Run the App Without Firebase Storage
- If you want to test the app, you can manually replace Firebase image URLs with **local assets** or a **free hosting service**.

### 🧐 Code Review
- The **image retrieval logic** is already implemented.
- If Firebase Cloud Storage is enabled (**Blaze Plan**), it will work as expected.


## Acknowledgments

- [Firebase](https://firebase.google.com) for backend services
- [Material Design](https://m3.material.io) for UI components
- [Kotlin](https://kotlinlang.org) programming language
