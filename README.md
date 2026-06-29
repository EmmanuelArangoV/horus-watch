# HorusWear ⌚

HorusWear is the official Wear OS companion application for the Horus ecosystem. Designed specifically for smartwatches, it provides users with immediate, wrist-accessible medical and emergency information, ensuring that critical data is always available at a glance when it matters most.

## 🔗 Ecosystem Integration

HorusWear does not operate in isolation; it is a fully integrated node within the larger **Horus** platform:

- **Mobile App Synchronization**: The watch app pairs with `horus-mobile`. Medical profile updates, privacy settings, and authenticated user state are securely synced from the mobile application directly to the watch.
- **Backend & FCM (Firebase Cloud Messaging)**: `horus-watch` registers a unique push token with the `horus-emergency` backend. When an emergency event is triggered (e.g., from the mobile app or another trusted source), the backend dispatches a high-priority FCM payload.
- **Smart Filtering**: To prevent cross-contamination of alerts (e.g., if multiple profiles have logged into the same watch), the watch locally validates the `userId` attached to the incoming FCM payload against the currently authenticated wearer before firing any haptic or visual alarms.

## 🚀 Key Features

- **Medical ID Tile (Protolayout)**: A highly optimized, easily accessible Wear OS Tile that displays critical medical information (Blood Type, Age, Organ Donor status) directly from the watch face carousel.
- **Targeted Push Notifications**: Receives targeted emergency alerts from the Horus backend. 
- **Quick Action Interface**: Designed specifically for the constraints of smartwatch displays, providing high-contrast typography, adaptive rounded icons, and a streamlined UI to launch the main Horus profile.
- **Standalone Capability**: Capable of operating and syncing data efficiently, keeping the user's emergency profile up-to-date even on the go.

## 📂 Project Structure

The project follows a clean architecture pattern tailored for Wear OS, leveraging Jetpack Compose and modern Kotlin standards.

```text
horus-watch/app/src/main/java/com/horus/wear/
└── presentation/
    ├── MainActivity.kt        # Entry point for the Wear OS Compose application
    ├── model/                 # Data classes and domain models (Profile, Settings, etc.)
    ├── network/               # FCM Services (HorusMessagingService), API calls, and payload handlers
    ├── theme/                 # Compose Wear OS Theme definitions (Colors, Typography, Shapes)
    ├── tile/                  # Protolayout implementations (HorusEmergencyTileService)
    ├── ui/                    # Jetpack Compose UI components and screens
    └── util/                  # Helper functions (e.g., age calculation, date formatting)
```

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

- **Typography**: Uses clean, readable system sans-serif fonts optimized for small screens (`sans-serif-medium`, `sans-serif-condensed`). Custom `.ttf` fonts are generally avoided in Tiles to maximize battery and system performance.
- **Icons**: Fully supports Android Adaptive Icons. The app icon relies on `mipmap-anydpi-v26` using the `horus_logo` background layer to guarantee a perfect circular mask on all Wear OS launchers.
- **Tiles (Cache Warning)**: Wear OS caches Tiles aggressively. When modifying `HorusEmergencyTileService`, always bump the `versionCode` in `build.gradle.kts` and re-add the Tile to the carousel on the watch to test UI changes.

---
*Built for the Horus Ecosystem.*
