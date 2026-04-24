# Gear 360 Android Controller 📸

![Android SDK](https://img.shields.io/badge/Android-SDK%2029%2B-green)
![Java](https://img.shields.io/badge/Language-Java-orange)
![License](https://img.shields.io/badge/License-MIT-blue)

A professional-grade, open-source Android application for the **Samsung Gear 360**. This app replaces the legacy Samsung software, offering a modern UI and direct integration with the camera's hardware using the **Open Spherical Camera (OSC) API**.

---

## 🌟 Key Features

### 📡 Smart Connectivity
- **Auto-Connect:** Seamlessly links to the Gear 360 Wi-Fi hotspot using the `WifiNetworkSpecifier` API.
- **Persistence:** Securely remembers your 8-digit camera password using encrypted SharedPreferences.

### 🎥 Live Viewfinder
- **Real-time Streaming:** Low-latency MJPEG video feed streamed directly from the camera.
- **Lifecycle Aware:** Automatically pauses/resumes the stream to save phone battery and CPU.

### 🖼️ Interactive 360° Gallery
- **VR Viewer:** Explore your photos in 3D using the Google VR SDK (Panorama View).
- **Remote Fetching:** Dynamically loads media list and thumbnails from the camera's SD card.
- **Save & Delete:** Wireless download to phone's local storage or hardware-level deletion.

### 🔘 Remote Control
- **Wireless Shutter:** Trigger photos remotely with instant beep and UI feedback.
- **Hardware State:** Real-time monitoring of camera battery levels and firmware info.

---

## 🛠️ Tech Stack & Architecture

- **Architecture:** Clean Architecture with `ui`, `network`, `model`, and `utils` package separation.
- **Networking:** [Retrofit 2.9](https://square.github.io/retrofit/) for RESTful OSC commands.
- **Image Loading:** [Glide](https://github.com/bumptech.glide/glide) for smooth thumbnail caching.
- **VR Rendering:** [Google VR SDK](https://developers.google.com/vr) for spherical projection.

---

## 🚀 Getting Started
1. **Clone the Project**
   ```bash
   git clone [https://github.com/mvinduwara/gear-360.git](https://github.com/mvinduwara/gear-360.git)

   
2.Open in Android Studio
Ensure you are using Android Studio Ladybug or newer.
Sync Gradle (The project uses the Aliyun mirror to fetch the archived VR SDK).

3.Connect & Run
Turn on your Gear 360.
Hold the Menu button to enter Wi-Fi mode.

📂 Project Structure
com.example.gear360
├── model       # Data models for OSC JSON responses
├── network     # Retrofit Client, MJPEG Streamer, & Wifi Manager
├── ui          # Activities (Dashboard, Gallery, Fullscreen)
└── utils       # Shared Constants & Preference Helpers

