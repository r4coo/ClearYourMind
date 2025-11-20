# Clear Your Mind - Diario Personal

**Clear Your Mind** es una aplicación de diario personal para Android, diseñada para ayudar a los usuarios a registrar su estado de ánimo, reflexiones diarias y organizar su semana de una manera sencilla e intuitiva. Este proyecto fue desarrollado como una aplicación completa y funcional, implementando prácticas modernas de desarrollo en Android.

<!-- ¡Añade aquí una captura de pantalla de la app! -->

---

## ✨ Características Principales

El proyecto cuenta con un flujo de usuario completo, desde la autenticación hasta las funciones principales de la aplicación.

### 1. Autenticación Completa
- **Pantalla de Inicio de Sesión:** Permite a los usuarios ingresar con correo y contraseña, o a través de opciones sociales (Google, Apple).
- **Pantalla de Registro:** Un formulario completo que incluye validación de campos, como la estructura del correo electrónico y la longitud de la contraseña.
- **Modo Invitado:** Permite el acceso directo a la aplicación sin necesidad de una cuenta.

### 2. Diario de Emociones
- **Registro de Estado de Ánimo:** Un `SeekBar` para calificar el ánimo del 1 al 10.
- **Reflexiones:** Un campo de texto para escribir los pensamientos del día.
- **Integración Multimedia:**
    - **Cámara:** Botón para tomar una foto y adjuntarla a la entrada del diario.
    - **Ubicación:** Botón para guardar la geolocalización actual.
- **Historial:** Las entradas se guardan en una base de datos local y se muestran en una lista cronológica en la pantalla principal.

### 3. Rutina Semanal
- **Calendario Editable:** Una vista dedicada para planificar actividades de Lunes a Viernes.
- **Persistencia de Datos:** La rutina se guarda localmente, por lo que la información no se pierde al cerrar la app.
- **Interfaz Dual:** Cambia entre un modo de "edición" (con campos de texto) y un modo de "visualización" (en una tabla ordenada).

### 4. Perfil de Usuario
- **Pantalla de Perfil:** Muestra información simulada del usuario (nombre, foto, correo).
- **Opciones de Configuración:** Incluye botones para "Editar Perfil", "Seguridad" y "Notificaciones" (simulados).
- **Cerrar Sesión (Logout):** Un botón funcional que finaliza la sesión del usuario, borra el historial de navegación y lo devuelve a la pantalla de Login.

---

## 🛠️ Características Técnicas

El proyecto está construido utilizando las mejores prácticas modernas de desarrollo Android:

- **Lenguaje:** **Kotlin** al 100%.
- **Arquitectura:** **MVVM (Model-View-ViewModel)** para separar la lógica de la interfaz de usuario, haciendo el código más limpio y mantenible.
- **Persistencia de Datos:**
    - **Room Database:** Para almacenar de forma robusta y local todas las entradas del diario.
    - **SharedPreferences:** Para guardar datos simples como la rutina semanal.
- **Componentes Modernos de Android Jetpack:**
    - **ViewModel:** Para gestionar los datos de la UI y sobrevivir a cambios de configuración.
    - **Lifecycle & LiveData:** Para crear un flujo de datos reactivo desde la base de datos hasta la interfaz.
    - **View Binding:** Para eliminar `findViewById` y acceder a las vistas de forma segura.
    - **ViewModel:** Para gestionar datos relacionados con la UI de manera consciente del ciclo de vida.
    - **RecyclerView:** Para mostrar eficientemente la lista de entradas del diario.
- **Intents de Hardware:** Uso de la cámara y los servicios de ubicación del dispositivo.
- **ActivityResultContracts:** Para gestionar permisos y resultados de la cámara de forma moderna y segura.
- **Room:** Para la persistencia de datos SQLite local.
- **Diseño de UI:** XML Layouts con Material Design Components.
---

## 🔐 Permisos Requeridos

La aplicación solicita los siguientes permisos al usuario para funcionar al 100%:

- **Camara:** Necesario para tomar fotos y adjuntarlas al diario.
- **ACCESS_FINE_LOCATION y ACCESS_COARSE_LOCATION:** Necesarios para geoetiquetar las entradas del diario.
- **READ/WRITE_EXTERNAL_STORAGE:** (Dependiendo de la versión de Android) Para gestionar el guardado de fotos.

## 📂 Estructura del Proyecto

com.clearyourmind.diario
- ├── data/                 #Capa de Datos
- │   ├── AppDatabase.kt    #Configuración de Room
- │   ├── MoodEntry.kt      #Entidad (Modelo de datos)
- │   └── MoodEntryDao.kt   #Data Access Object
- ├── ui/                   #Capa de Interfaz de Usuario
- │   ├── MainActivity.kt  # Lógica principal del diario
- │   ├── MoodViewModel.kt # Gestión de estado y lógica de negocio
- │   ├── MoodEntryAdapter.kt # Adaptador para el RecyclerView
- │   └── LoginActivity.kt # (Pendiente) Lógica de autenticación
- └── res/                 # Recursos
  - ├── layout/          # XML de UI (activity_main, activity_login)
  - └── anim/            # Animaciones (bounce, scale_up_down)



## 🚀 ¿Cómo Ejecutar el Proyecto?

1.  Clona este repositorio en tu máquina local.
2.  Abre el proyecto con Android Studio.
3.  Deja que Gradle sincronice todas las dependencias (puede tardar unos minutos la primera vez).
4.  Ejecuta la aplicación en un emulador o en un dispositivo físico.

---

