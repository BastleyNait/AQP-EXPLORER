# AQP-EXPLORER
## Resumen Ejecutivo
AQP-EXPLORER es una aplicación Android nativa desarrollada con Jetpack Compose y Kotlin que sirve como guía turística interactiva de Arequipa, Perú. El proyecto implementa una arquitectura MVC simplificada evolucionando hacia MVVM, permitiendo a los usuarios explorar lugares turísticos, buscar destinos, gestionar favoritos y acceder a información detallada de cada atractivo de Arequipa.
*Objetivo*: Proveer una experiencia fluida y educativa para turistas y locales interesados en descubrir Arequipa, con validaciones en tiempo real, navegación intuitiva y gestión de estado persistente ante rotaciones de pantalla.

### Integrantes del trabajo final
- Chirinos Negron Sebastian Arley
- Cuadros Amanqui Joe Jhonny
- Marron Carcausto Daniel Enrique
- Marron Lope Misael Josias
- Viza Cuti Rodrigo Viza

## Descripción de Interfaces Implementadas
1. **HomeScreen**

**Propósito**: Pantalla principal que muestra lugares turísticos destacados de Arequipa
**Comportamiento**:
- Despliega lista dinámica con LazyColumn de lugares turísticos
- Cada elemento muestra imagen, nombre y descripción breve
- Al tocar un lugar, navega a DetailScreen pasando el ID del lugar
- Incluye barra de búsqueda rápida para filtrar destinos
- Se actualiza reactivamente desde datos en memoria

2. **SearchScreen**

**Propósito**: Permite búsqueda avanzada y filtrado de lugares turísticos
**Comportamiento**:
- Campo de texto con validación en tiempo real usando onValueChange
- Filtra resultados instantáneamente según criterios (nombre, categoría, ubicación)
- Muestra sugerencias mientras el usuario escribe
- Lista de resultados actualizable dinámicamente
- Navegación directa a detalles desde resultados

3. **DetailScreen**

**Propósito**: Muestra información completa de un lugar turístico específico
**Comportamiento**:
- Recibe ID del lugar vía argumentos de navegación
- Despliega galería de imágenes, descripción extendida, horarios y ubicación
- Botón de favoritos con cambio de estado visual
- Opción de compartir información del lugar
- Botón de retroceso para volver a la pantalla anterior

4. **LoginScreen**

**Propósito**: Autenticación de usuarios con validación de credenciales
**Comportamiento**:
- Campos de email y contraseña con validación en tiempo real
- Email validado con Patterns.EMAIL_ADDRESS.matcher()
- Contraseña con requisitos de longitud mínima (8 caracteres)
- Mensajes de error en texto rojo vía derivedStateOf
- Botón "Iniciar Sesión" habilitado solo si campos son válidos (enabled = isValid)
- Simulación de autenticación contra lista de usuarios en memoria

5. **FavoritesScreen**

**Propósito**: Gestiona lugares marcados como favoritos por el usuario
**Comportamiento**:
- Lista persistente de favoritos usando MutableList en DataStore
- Permite eliminar favoritos con gesto de deslizar o botón
- Navegación a detalles desde cada favorito
- Estado sincronizado con otras pantallas
- Mensaje informativo si no hay favoritos guardados

6. **Navigation (NavGraph)**

**Propósito**: Centraliza el flujo de navegación de la aplicación
**Comportamiento**:
- Configurado en MainActivity con NavHost y rememberNavController
- Define rutas como composable("home"), composable("detail/{placeId}")
- Maneja paso de argumentos entre pantallas (ej: navController.navigate("detail/$id"))
- Gestiona back stack y transiciones suaves
- Ubicado en ui/navigation/nav_graph.kt

## Instrucciones para Ejecutar o Probar el Aplicativo

### Requisitos Previos
- Android Studio Hedgehog (2023.1.1) o superior
- JDK 17 o superior
- Gradle 8.0+
- Dispositivo físico con Android 8.0+ o emulador con API 26+

### Pasos de Instalación
1. Clonar el repositorio:
``` bash
git clone https://github.com/BastleyNait/AQP-EXPLORER.git
cd AQP-EXPLORER
```

2. Abrir en Android Studio:
- File → Open → Seleccionar carpeta AQP-EXPLORER
- Esperar sincronización de Gradle (puede tardar 2-5 minutos)

3. Configurar emulador (opcional):
- Tools → Device Manager → Create Device
- Recomendado: Pixel 5 con API 33 (Android 13)

4. Ejecutar la aplicación:
- Conectar dispositivo físico o iniciar emulador
- Click en botón Run
- Seleccionar dispositivo de destino
- La app se instalará y abrirá automáticamente
