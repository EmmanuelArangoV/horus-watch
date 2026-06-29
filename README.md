# HorusWear ⌚

HorusWear is the official Wear OS companion application for the Horus ecosystem. It provides users with immediate, wrist-accessible medical and emergency information, ensuring that critical data is always available at a glance when it matters most.

## 🚀 Features

- **Medical ID Tile (Protolayout)**: A highly optimized, easily accessible Wear OS Tile that displays critical medical information (Blood Type, Age, Organ Donor status) directly from the watch face carousel.
- **Smart Push Notifications (FCM)**: Receives targeted emergency alerts and notifications from the Horus backend. The app is smart enough to filter incoming payloads to ensure that alerts only trigger if they match the currently authenticated user on the watch.
- **Quick Action Interface**: Designed specifically for the constraints of smartwatch displays, providing high-contrast typography, adaptive rounded icons, and a streamlined UI to launch the main Horus profile.
- **Standalone Capability**: Capable of operating and syncing data efficiently, keeping the user's emergency profile up-to-date even on the go.

## 🛠️ Technology Stack

- **Kotlin** & **Jetpack Compose for Wear OS**: Modern, declarative UI development optimized for round and square smartwatches.
- **Protolayout & Glance**: For building native, performant, and battery-friendly Wear OS Tiles.
- **Firebase Cloud Messaging (FCM)**: For real-time, user-specific push notification delivery.
- **Wear OS SDK**: Targeting Android API 30+ up to API 36, ensuring compatibility with the latest Wear OS 3, 4, and 5 devices.

## 📦 Installation & Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/EmmanuelArangoV/horus-watch.git
   ```
2. **Open in Android Studio**
   Open the cloned directory using the latest version of Android Studio (Koala or newer recommended).
3. **Connect a Wear OS Device or Emulator**
   Ensure you have a Wear OS emulator configured (API 30+) or a physical smartwatch connected via Wireless Debugging.
4. **Build & Run**
   Click the "Run" button in Android Studio. The app will be installed on the watch.

## 🎨 UI/UX Guidelines

- **Typography**: Uses clean, readable system sans-serif fonts optimized for small screens (`sans-serif-medium`, `sans-serif-condensed`).
- **Icons**: Fully supports Android Adaptive Icons. Ensure any changes to the app icon are made in the `res/mipmap-anydpi-v26` folder using the `horus_logo` background layer to guarantee a perfect circular mask on all launchers.
- **Tiles**: When updating the `HorusEmergencyTileService`, remember that Wear OS caches Tiles aggressively. Always bump the `versionCode` in `build.gradle.kts` and re-add the Tile to the carousel to test UI changes.

## 🤝 Contributing

Contributions are welcome! Please ensure that any new features are tested on both round and square Wear OS form factors, and that battery consumption is kept to an absolute minimum (especially within Tile Services and FCM message handlers).

---
*Built for the Horus Ecosystem.*
