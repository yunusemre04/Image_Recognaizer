# Image Recognizer

![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-0095D5?&style=for-the-badge&logo=kotlin&logoColor=white)
![TensorFlow Lite](https://img.shields.io/badge/TensorFlow_Lite-FF6F00?style=for-the-badge&logo=tensorflow&logoColor=white)

An advanced, Jetpack Compose-based Android application that performs both real-time camera object recognition and gallery image classification using a TensorFlow Lite model. 

## Features

- 📸 **Live Camera Analysis**: Real-time object recognition utilizing CameraX and TensorFlow Lite.
- 🖼️ **Gallery Analysis**: Pick photos from your local device storage and classify them.
- 🕒 **Prediction History**: Keeps track of your past predictions using Jetpack DataStore (with the ability to delete history).
- 🌍 **Bilingual Support**: Fully localized in English (Default) and Turkish (TR). Users can seamlessly switch languages on the fly using the built-in top-right button on the home screen.

## ⚠️ Important Setup Instructions (Model Download)

To comply with GitHub's 100MB file size limit, the heavy machine learning model is **not** included directly in this repository. 

In order to build and run this application successfully, you must manually download the model and place it in your local project directory.

**Step 1:** Download the Inception-ResNet-v2 TensorFlow Lite model from Kaggle:
👉 **[Download Model Here](https://www.kaggle.com/models/tensorflow/inception-resnet-v2)**

**Step 2:** Ensure the downloaded model file is named exactly `model.tflite`.

**Step 3:** Place the `model.tflite` file into the following project directory:
```text
app/src/main/assets/model.tflite
```
*(If the `assets` folder does not exist, create it).*

## Installation & Running

1. Clone this repository:
   ```bash
   git clone https://github.com/yunusemre04/Image_Recognaizer.git
   ```
2. Follow the model download instructions above.
3. Open the project in **Android Studio**.
4. Allow Gradle to sync and download all necessary dependencies (Jetpack Compose, TensorFlow Lite, CameraX, etc.).
5. Build and Run on an Android Emulator or a real device (Minimum SDK 28).

## Tech Stack

*   **UI Toolkit**: [Jetpack Compose](https://developer.android.com/jetpack/compose)
*   **Machine Learning**: [TensorFlow Lite Support / Task Vision](https://www.tensorflow.org/lite) 
*   **Camera Integration**: [CameraX](https://developer.android.com/training/camerax)
*   **Permissions**: Accompanist Permissions
*   **Local Storage**: Jetpack DataStore (Preferences) & JSON via Gson
*   **Localization**: AndroidX AppCompat API (Per-App Language Preferences)

The Project made in May 2025
