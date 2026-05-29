# Horus Wear ⌚

Horus Wear es una aplicación para Wear OS diseñada para proporcionar acceso rápido a información médica crítica y contactos de emergencia directamente desde el reloj. Forma parte del ecosistema Horus para la gestión de emergencias médicas.

## 🚀 Características

- **Perfil Médico en el Reloj**: Visualiza nombre, tipo de sangre, edad y estado de donante de órganos.
- **Información Crítica**: Acceso rápido a alergias (con niveles de severidad), condiciones crónicas y medicamentos.
- **Botón de Pánico**: Acceso directo para realizar llamadas de emergencia al contacto principal.
- **Sincronización en la Nube**: Los datos se mantienen actualizados mediante una API REST.
- **Modo Offline**: Almacenamiento en caché del perfil para acceso sin conexión a internet.
- **Material 3 para Wear OS**: Interfaz moderna, optimizada para pantallas redondas con animaciones de escalado y morphing.

## 🛠️ Requisitos del Entorno

Para compilar y ejecutar este proyecto, necesitarás:

- **Android Studio**: Ladybug (2024.2.1) o superior recomendado.
- **JDK**: Versión 17 o superior.
- **Android SDK**: API Level 34 (Upside Down Cake).
- **Dispositivo**: 
  - Reloj físico con Wear OS 3.0 (API 30) o superior.
  - O un Emulador de Wear OS (se recomienda un perfil redondo).

## ⚙️ Configuración e Instalación

1. **Clonar el repositorio**:
   ```bash
   git clone <url-del-repositorio>
   ```

2. **Configuración de la API**:
   Abre el archivo `app/src/main/java/com/horus/wear/presentation/util/Constants.kt` (o donde se defina `BASE_URL`) y actualiza la URL del servidor si es necesario:
   ```kotlin
   const val BASE_URL = "https://tu-servidor-api.com"
   ```

3. **Sincronizar Gradle**:
   Abre el proyecto en Android Studio y espera a que la sincronización de Gradle finalice correctamente.

4. **Ejecución**:
   - Selecciona el módulo `app`.
   - Selecciona tu dispositivo Wear OS o emulador.
   - Haz clic en **Run**.

## 📲 Cómo usar en un nuevo dispositivo

1. **Vinculación**:
   Al abrir la app por primera vez en un reloj no vinculado, aparecerá un **código de 6 dígitos**.
2. **Autorización**:
   Ingresa este código en la aplicación móvil de Horus o en el panel web para autorizar el dispositivo.
3. **Sincronización**:
   Una vez verificado, el reloj descargará automáticamente tu perfil médico y lo mantendrá sincronizado cada vez que abras la aplicación.

## 📚 Tecnologías Utilizadas

- **Jetpack Compose para Wear OS**: Material 3.
- **OkHttp3**: Para peticiones de red seguras.
- **Corrutinas de Kotlin**: Gestión de hilos asíncronos.
- **SharedPreferences**: Almacenamiento local del perfil y tokens de sesión.

---
© 2024 Horus Team - Tu salud, siempre a mano.
