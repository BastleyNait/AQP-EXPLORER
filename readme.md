# Proyecto AQP-EXPLORER

**Curso:** IDNP - 2025B
**Universidad Nacional de San Agustín**

### Integrantes:
- Chirinos Negron, Sebastián Arley
- Cuadros Amanqui, Joe Jhonny
- Marrón Carcausto, Daniel Enrique
- Marrón Lope, Misael Josias
- Viza Cuti, Rodrigo Estefano

-------------------------------------

## 1. Resumen Ejecutivo del Trabajo

**AQP-EXPLORER** es una aplicación nativa de Android desarrollada en **Kotlin** diseñada para fomentar el turismo en la región de Arequipa. La aplicación permite a los usuarios explorar destinos turísticos, realizar búsquedas filtradas, gestionar una lista de favoritos y realizar reservas de tours o visitas.

El proyecto destaca por su arquitectura robusta y moderna, implementando **MVVM (Model-View-ViewModel)** y **Clean Architecture** para asegurar la escalabilidad y mantenibilidad. Una característica técnica central es su enfoque **"Offline-First"**, logrado mediante la sincronización de datos entre una base de datos local (**Room**) y una base de datos en la nube (**Firebase Firestore**). Esto garantiza que la aplicación sea funcional incluso sin conexión a internet.

Además, la interfaz de usuario ha sido construida enteramente con **Jetpack Compose** y **Material Design 3**, ofreciendo una experiencia visual moderna, reactiva y adaptable al tema del sistema (Dark Mode).

-------------------------------------

## 2. Descripción de las Interfaces Implementadas

La aplicación consta de un flujo de navegación principal gestionado por un `BottomNavigationBar` y pantallas secundarias de detalle y configuración. Las interfaces desarrolladas son:

1.  **Pantalla de Inicio (Home Screen):** La vista principal que recibe al usuario.
2.  **Pantalla de Búsqueda (Search Screen):** Interfaz dedicada a la exploración activa.
3.  **Pantalla de Favoritos (Favorites Screen):** Espacio personal del usuario para guardar intereses.
4.  **Pantalla de Reservas (Reservations Screen):** Gestor de itinerarios y compras.
5.  **Pantalla de Detalle del Lugar (Place Detail Screen):** Vista informativa profunda de un destino específico.
6.  **Pantalla de Configuración (Settings Screen):** Panel de control de la aplicación.

-------------------------------------

## 3. Propósito de cada Interfaz

A continuación se detalla la funcionalidad técnica y el objetivo de usuario de cada pantalla, basándose en la estructura de paquetes `presentation/screen`:

### 3.1. Home Screen (`screen/home`)
* **Propósito:** Funcionar como el "escaparate" de la aplicación.
* **Funcionalidad:** Muestra un carrusel de imágenes destacadas y listas de recomendaciones horizontales. Implementa filtros rápidos por categoría (Histórico, Aventura, etc.) que actualizan la vista en tiempo real mediante `HomeViewModel`. Se conecta al repositorio para sincronizar los datos más recientes al iniciar.

### 3.2. Search Screen (`screen/search`)
* **Propósito:** Permitir al usuario encontrar destinos específicos rápidamente.
* **Funcionalidad:** Combina una barra de búsqueda de texto con filtros de categoría ("Chips"). Utiliza un flujo reactivo (`combine` en Kotlin Flows) para filtrar la base de datos local instantáneamente mientras el usuario escribe, sin necesidad de hacer peticiones de red constantes.

### 3.3. Favorites Screen (`screen/favorites`)
* **Propósito:** Permitir al usuario guardar lugares de interés para consultarlos más tarde.
* **Funcionalidad:** Muestra una lista de lugares marcados con "Me gusta". La persistencia se maneja en la base de datos Room, por lo que los favoritos no se pierden al cerrar la aplicación. Permite eliminar ítems directamente desde la lista.

### 3.4. Reservations Screen (`screen/reservations`)
* **Propósito:** Gestionar el ciclo de vida de las reservas del usuario.
* **Funcionalidad:** Divide la información en dos secciones: "Próximos Viajes" e "Historial". Permite visualizar el estado de la reserva (Confirmada/Cancelada) y ofrece la opción de cancelar reservas futuras, actualizando tanto la nube como la base de datos local.

### 3.5. Place Detail Screen (`screen/detail`)
* **Propósito:** Proveer toda la información necesaria para la toma de decisiones y conversión (reserva).
* **Funcionalidad:** Muestra imágenes de alta calidad, descripción, precio, calificación y servicios. Incluye integración con **Google Maps** (mediante `TransportCard`) para ver cómo llegar y un diálogo modal para seleccionar fecha y cantidad de personas para realizar una reserva.

### 3.6. Settings Screen (`screen/settings`)
* **Propósito:** Personalizar la experiencia del usuario y ofrecer información legal/créditos.
* **Funcionalidad:** Controles (Switches) para activar/desactivar notificaciones y alertas de viaje. Incluye secciones informativas sobre la aplicación.

-------------------------------------

## 4. Instrucciones para Ejecutar el Proyecto

Para compilar y ejecutar **AQP-EXPLORER** en un entorno local, siga los siguientes pasos:

### Requisitos Previos
* **Android Studio:** Versión Koala, Ladybug o superior recomendada.
* **JDK:** Versión 17 o superior.
* **Dispositivo/Emulador:** Android 8.0 (Oreo) API 26 como mínimo recomendado.

### Pasos de Instalación

1.  **Clonar el Repositorio:**
    Descargue el código fuente o clone el repositorio en su máquina local.

2.  **Configuración de Firebase (Crítico):**
    * El proyecto utiliza Firebase Firestore. Asegúrese de que el archivo `google-services.json` esté ubicado en la carpeta `app/`.
    * Si no tiene este archivo, debe crear un proyecto en la consola de Firebase, registrar el paquete `com.example.aqpexplorer` y descargar el archivo de configuración.

3.  **Sincronización de Gradle:**
    * Abra el proyecto en Android Studio.
    * Espere a que Gradle descargue todas las dependencias (Hilt, Room, Retrofit/Firebase, Coil, Compose, etc.).
    * Si aparece un error de sincronización, verifique su conexión a internet y la versión de JDK en *Settings > Build, Execution, Deployment > Build Tools > Gradle*.

4.  **Ejecución:**
    * Seleccione un emulador o conecte su dispositivo físico.
    * Presione el botón **Run (Shift + F10)**.
    * *Nota:* La primera vez que se ejecute, la aplicación intentará sincronizar datos de Firebase. Asegúrese de tener conexión a internet para la carga inicial de datos (luego funcionará offline).

5.  **Verificación de Workers (Opcional):**
    * La aplicación utiliza `WorkManager` para notificaciones en segundo plano. Puede verificar los logs en el **Logcat** filtrando por la etiqueta `SYNC_TEST` o `ReservationWorker`.
