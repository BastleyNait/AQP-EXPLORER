# 🏛️ AQP Explorer - Aplicación Móvil de Turismo

Una aplicación móvil nativa para Android desarrollada en **Kotlin** con **Jetpack Compose** que permite explorar los principales destinos turísticos de Arequipa, Perú.

## 👥 Integrantes del Trabajo Final

- **Chirinos Negron Sebastian Arley**
- **Cuadros Amanqui Joe Jhonny**
- **Marron Carcausto Daniel Enrique**
- **Marron Lope Misael Josias**
- **Viza Cuti Rodrigo Viza**

## 📱 Descripción del Proyecto

AQP Explorer es una aplicación de turismo que facilita la exploración y descubrimiento de lugares emblemáticos de Arequipa. La aplicación permite a los usuarios navegar por diferentes destinos, marcar favoritos, buscar lugares específicos y obtener información detallada sobre cada atractivo turístico.

## 🚀 Instalación y Configuración

### Prerrequisitos
- Android Studio Arctic Fox o superior
- JDK 11 o superior
- Android SDK API 24 o superior
- Dispositivo Android o emulador

### Pasos de Instalación

1. **Clonar el repositorio**
```bash
git clone https://github.com/usuario/aqp-explorer.git
cd aqp-explorer
```

2. **Abrir en Android Studio**
- Abrir Android Studio
- Seleccionar "Open an existing project"
- Navegar hasta la carpeta del proyecto

3. **Sincronizar dependencias**
```bash
./gradlew build
```

4. **Ejecutar la aplicación**
- Conectar dispositivo Android o iniciar emulador
- Hacer clic en "Run" o usar `Shift + F10`

### Dependencias Principales
```kotlin
// build.gradle.kts (Module: app)
dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation("androidx.compose.ui:ui:1.5.4")
    implementation("androidx.compose.material3:material3:1.1.2")
    implementation("androidx.navigation:navigation-compose:2.7.5")
    implementation("io.coil-kt:coil-compose:2.5.0")
}
```

---

## 📚 PRIMERA UNIDAD - Fundamentos de la Plataforma

### 🎯 Tema 01: Introducción al Desarrollo de Aplicaciones Móviles

#### Tecnologías Utilizadas
- **Lenguaje**: Kotlin
- **Framework UI**: Jetpack Compose
- **Plataforma**: Android (API 24+)
- **IDE**: Android Studio
- **Build System**: Gradle con Kotlin DSL

#### Características de la Aplicación Móvil
```kotlin
// MainActivity.kt - Punto de entrada de la aplicación
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AQPExplorerTheme {
                AQPExplorerApp()
            }
        }
    }
}
```

**Ventajas del desarrollo móvil nativo:**
- Rendimiento optimizado
- Acceso completo a APIs del sistema
- Mejor experiencia de usuario
- Integración con servicios del dispositivo

### 🏗️ Tema 02: Arquitectura Básica de una Aplicación Móvil

#### Arquitectura Implementada: MVC Simplificado + Compose

```
📁 Estructura del Proyecto:
├── 📱 MainActivity.kt          # Controlador principal
├── 🎨 screens/                 # Vistas (UI)
│   ├── HomeScreen.kt
│   ├── SearchScreen.kt
│   ├── FavoritesScreen.kt
│   ├── SettingsScreen.kt
│   └── PlaceDetailScreen.kt
├── 📊 data/                    # Modelo de datos
│   ├── TouristPlace.kt
│   └── AppSettings.kt
├── 🧭 navigation/              # Control de navegación
│   └── Navigation.kt
└── 🎨 ui/theme/               # Temas y estilos
    ├── Color.kt
    ├── Theme.kt
    └── Type.kt
```

#### Componentes de la Arquitectura

**1. Modelo (Data Layer)**
```kotlin
data class TouristPlace(
    val id: Int,
    val name: String,
    val description: String,
    val shortDescription: String,
    val price: Double,
    val currency: String = "S/",
    val categories: List<String>,
    val imageUrl: String,
    val rating: Float,
    val reviewCount: Int,
    val isFavorite: Boolean = false,
    val location: String = "Arequipa"
)
```

**2. Vista (UI Layer)**
```kotlin
@Composable
fun HomeScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A))
    ) {
        SearchHeader()
        ImageCarousel()
        TimeFilters()
        RecommendationsSection(navController)
    }
}
```

**3. Controlador (Navigation & Logic)**
```kotlin
@Composable
fun MainNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") { HomeScreen(navController) }
        composable("search") { SearchScreen(navController) }
        composable("favorites") { FavoritesScreen(navController) }
        // ...
    }
}
```

### 🔄 Tema 03: Manejo de Estado y Eventos

#### Estado Local con Compose
```kotlin
@Composable
fun SearchScreen(navController: NavHostController) {
    // Estado local para la búsqueda
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Todos") }
    
    // Estado derivado para filtrar lugares
    val filteredPlaces = remember(searchQuery, selectedCategory) {
        SampleData.touristPlaces.filter { place ->
            place.name.contains(searchQuery, ignoreCase = true) &&
            (selectedCategory == "Todos" || place.categories.contains(selectedCategory))
        }
    }
}
```

#### Manejo de Eventos de Usuario
```kotlin
@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange, // Evento de cambio de texto
        placeholder = { Text("Buscar destinos...") },
        trailingIcon = {
            IconButton(onClick = onSearch) { // Evento de clic
                Icon(Icons.Default.Search, contentDescription = "Buscar")
            }
        }
    )
}
```

#### Estado de Favoritos
```kotlin
@Composable
fun FavoritesList(navController: NavHostController) {
    var selectedFilter by remember { mutableStateOf("Por Proximidad") }
    
    // Simulación de estado de favoritos
    val favoritePlaces = SampleData.touristPlaces
    
    LazyColumn {
        items(favoritePlaces) { place ->
            FavoriteCard(
                place = place,
                onClick = { navController.navigate("place_detail/${place.id}") }
            )
        }
    }
}
```

### 🧭 Tema 04: Navegación y Comunicación entre Pantallas

#### Sistema de Navegación con Navigation Compose
```kotlin
@Composable
fun MainNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        // Pantalla principal
        composable("home") { 
            HomeScreen(navController) 
        }
        
        // Pantalla de búsqueda
        composable("search") { 
            SearchScreen(navController) 
        }
        
        // Pantalla de favoritos
        composable("favorites") { 
            FavoritesScreen(navController) 
        }
        
        // Pantalla de configuración
        composable("settings") { 
            SettingsScreen(navController) 
        }
        
        // Pantalla de detalle con parámetros
        composable(
            "place_detail/{placeId}",
            arguments = listOf(navArgument("placeId") { type = NavType.IntType })
        ) { backStackEntry ->
            val placeId = backStackEntry.arguments?.getInt("placeId") ?: 0
            PlaceDetailScreen(navController, placeId)
        }
    }
}
```

#### Comunicación entre Pantallas
```kotlin
// Navegación con parámetros
@Composable
fun PlaceCard(place: TouristPlace, onClick: () -> Unit) {
    Card(
        modifier = Modifier.clickable { 
            // Comunicación: pasar ID del lugar a la pantalla de detalle
            onClick() 
        }
    ) {
        // Contenido de la tarjeta
    }
}

// En la pantalla padre
PlaceCard(
    place = place,
    onClick = { 
        navController.navigate("place_detail/${place.id}") 
    }
)
```

#### Bottom Navigation
```kotlin
@Composable
fun AQPExplorerApp() {
    val navController = rememberNavController()
    
    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFF2A2A2A)
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    label = { Text("Inicio") },
                    selected = currentRoute == "home",
                    onClick = { navController.navigate("home") }
                )
                // Más elementos de navegación...
            }
        }
    ) { paddingValues ->
        MainNavigation(navController)
    }
}
```

### 📝 Tema 05: Evaluación 1

**Funcionalidades Implementadas:**
- ✅ Navegación entre 5 pantallas principales
- ✅ Manejo de estado local con Compose
- ✅ Comunicación entre pantallas con parámetros
- ✅ Arquitectura MVC básica
- ✅ Interfaz de usuario responsive

---

## 🎨 SEGUNDA UNIDAD - Interfaz de Usuario

### 📱 Tema 06: Adaptación de Interfaces en Aplicaciones Móviles

#### Responsive Design con Compose
```kotlin
@Composable
fun ResponsiveLayout() {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    
    if (screenWidth > 600.dp) {
        // Layout para tablets
        Row {
            NavigationRail()
            MainContent()
        }
    } else {
        // Layout para teléfonos
        Column {
            MainContent()
            BottomNavigation()
        }
    }
}
```

#### Adaptación de Densidad
```kotlin
@Composable
fun AdaptiveCard() {
    val density = LocalDensity.current
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(with(density) { 120.dp }) // Adaptación de densidad
    ) {
        // Contenido adaptativo
    }
}
```

### 📐 Tema 07: Layouts y Componentes Básicos

#### Layouts Principales Utilizados

**1. Column Layout (Vertical)**
```kotlin
@Composable
fun HomeScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A))
    ) {
        SearchHeader()           // Componente 1
        Spacer(modifier = Modifier.height(16.dp))
        ImageCarousel()          // Componente 2
        TimeFilters()           // Componente 3
        RecommendationsSection(navController) // Componente 4
    }
}
```

**2. Row Layout (Horizontal)**
```kotlin
@Composable
fun FavoritesHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Favoritos", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        IconButton(onClick = { }) {
            Icon(Icons.Default.Settings, contentDescription = "Filtros")
        }
    }
}
```

**3. Box Layout (Superposición)**
```kotlin
@Composable
fun ImageWithOverlay() {
    Box(modifier = Modifier.fillMaxSize()) {
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
        
        // Overlay con gradiente
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f))
                    )
                )
        )
        
        // Texto superpuesto
        Text(
            text = "Cañón del Colca",
            modifier = Modifier.align(Alignment.BottomStart),
            color = Color.White
        )
    }
}
```

#### Componentes Básicos Implementados

**Material 3 Components:**
```kotlin
// Cards
Card(
    colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2A2A)),
    shape = RoundedCornerShape(12.dp)
) { /* Contenido */ }

// Buttons
Button(
    onClick = { },
    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A90E2))
) {
    Text("Explorar")
}

// Text Fields
OutlinedTextField(
    value = searchQuery,
    onValueChange = { searchQuery = it },
    placeholder = { Text("Buscar destinos...") }
)

// Filter Chips
FilterChip(
    onClick = { onFilterSelected(filter) },
    label = { Text(filter) },
    selected = selectedFilter == filter
)
```

### 🎨 Tema 08: Gráficos y Personalización con Canvas

#### Personalización de Temas
```kotlin
// Color.kt
val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

// Colores personalizados para la app
val DarkBackground = Color(0xFF1A1A1A)
val CardBackground = Color(0xFF2A2A2A)
val AccentBlue = Color(0xFF4A90E2)
```

#### Shapes y Estilos Personalizados
```kotlin
// Formas personalizadas
val CustomShapes = Shapes(
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp)
)

// Gradientes personalizados
@Composable
fun GradientBackground() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1A1A1A),
                        Color(0xFF2A2A2A)
                    )
                )
            )
    )
}
```

#### Iconos y Elementos Gráficos
```kotlin
// Uso de Material Icons
Icon(Icons.Default.Home, contentDescription = "Inicio")
Icon(Icons.Default.Search, contentDescription = "Buscar")
Icon(Icons.Default.Favorite, contentDescription = "Favoritos")
Icon(Icons.Default.Settings, contentDescription = "Configuración")

// Iconos personalizados con tint
Icon(
    Icons.Default.Star,
    contentDescription = "Rating",
    tint = Color.Yellow,
    modifier = Modifier.size(16.dp)
)
```

### 📋 Tema 09: Listas y Visualización de Datos

#### LazyColumn para Listas Verticales
```kotlin
@Composable
fun RecommendationsList(places: List<TouristPlace>, navController: NavHostController) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(places) { place ->
            PlaceCard(
                place = place,
                onClick = { navController.navigate("place_detail/${place.id}") }
            )
        }
        
        // Espacio adicional para bottom navigation
        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}
```

#### LazyRow para Listas Horizontales
```kotlin
@Composable
fun CategoryFilters(categories: List<String>, selectedCategory: String, onCategorySelected: (String) -> Unit) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(categories) { category ->
            FilterChip(
                onClick = { onCategorySelected(category) },
                label = { Text(category) },
                selected = selectedCategory == category,
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = Color(0xFF2A2A2A),
                    selectedContainerColor = Color(0xFF4A4A4A)
                )
            )
        }
    }
}
```

#### Visualización de Datos Complejos
```kotlin
@Composable
fun PlaceCard(place: TouristPlace, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2A2A))
    ) {
        Column {
            // Imagen del lugar
            AsyncImage(
                model = place.imageUrl,
                contentDescription = place.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentScale = ContentScale.Crop
            )
            
            // Información del lugar
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = place.name,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                
                Text(
                    text = place.shortDescription,
                    color = Color.Gray,
                    fontSize = 12.sp,
                    maxLines = 2
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${place.currency} ${place.price}",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Row {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = Color.Yellow,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = place.rating.toString(),
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}
```

#### Carousel de Imágenes
```kotlin
@Composable
fun ImageCarousel() {
    val images = SampleData.carouselImages
    
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(images) { imageUrl ->
            Card(
                modifier = Modifier
                    .width(300.dp)
                    .height(180.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}
```

### 📝 Tema 10: Evaluación 2

**Componentes de UI Implementados:**
- ✅ Layouts responsivos (Column, Row, Box)
- ✅ Componentes Material 3 (Cards, Buttons, TextFields)
- ✅ Listas eficientes (LazyColumn, LazyRow)
- ✅ Navegación con Bottom Navigation
- ✅ Temas personalizados y colores
- ✅ Manejo de imágenes con Coil
- ✅ Filtros y chips interactivos

---

---

## 📱 Funcionalidades Principales

### 🏠 Pantalla Principal (Home)
- Barra de búsqueda interactiva
- Carrusel de imágenes destacadas
- Filtros por tiempo del día
- Lista de recomendaciones

### 🔍 Búsqueda (Search)
- Búsqueda en tiempo real
- Filtros por categorías
- Resultados dinámicos
- Navegación a detalles

### ❤️ Favoritos (Favorites)
- Lista de lugares favoritos
- Filtros de ordenamiento
- Acciones de compartir y eliminar
- Navegación a detalles

### ⚙️ Configuración (Settings)
- Configuraciones de la aplicación
- Historial de actividades
- Preferencias de usuario
- Información de la app

### 📍 Detalle del Lugar
- Información completa del destino
- Galería de imágenes
- Reseñas y calificaciones
- Opciones de compartir

---

## 🛠️ Tecnologías y Herramientas

| Categoría | Tecnología | Propósito |
|-----------|------------|-----------|
| **Lenguaje** | Kotlin | Desarrollo nativo Android |
| **UI Framework** | Jetpack Compose | Interfaz de usuario declarativa |
| **Navegación** | Navigation Compose | Navegación entre pantallas |
| **Imágenes** | Coil | Carga de imágenes asíncronas |
| **Arquitectura** | MVC + Compose | Separación de responsabilidades |
| **Build System** | Gradle KTS | Gestión de dependencias |
| **Design System** | Material 3 | Componentes de UI modernos |

---

## 📊 Estructura de Datos

### Modelo Principal: TouristPlace
```kotlin
data class TouristPlace(
    val id: Int,                    // Identificador único
    val name: String,               // Nombre del lugar
    val description: String,        // Descripción completa
    val shortDescription: String,   // Descripción breve
    val price: Double,              // Precio de entrada
    val currency: String = "S/",    // Moneda
    val categories: List<String>,   // Categorías del lugar
    val imageUrl: String,           // URL de la imagen
    val rating: Float,              // Calificación (1-5)
    val reviewCount: Int,           // Número de reseñas
    val isFavorite: Boolean = false,// Estado de favorito
    val location: String = "Arequipa" // Ubicación
)
```

### Datos de Ejemplo
- **Cañón del Colca**: Aventura y cultura, rating 4.8/5
- **Volcán Misti**: Trekking y aventura, rating 4.7/5
- **Plaza de Armas**: Historia y arquitectura, rating 4.6/5
- **Monasterio de Santa Catalina**: Cultura religiosa, rating 4.9/5

---

## 🎯 Objetivos de Aprendizaje Alcanzados

### Primera Unidad ✅
- [x] Comprensión de fundamentos móviles
- [x] Implementación de arquitectura básica
- [x] Manejo de estado con Compose
- [x] Sistema de navegación completo
- [x] Comunicación entre pantallas

### Segunda Unidad ✅
- [x] Interfaces adaptativas y responsive
- [x] Layouts complejos con Compose
- [x] Personalización gráfica y temas
- [x] Listas eficientes y visualización de datos
- [x] Componentes Material 3 avanzados

---

## 🔮 Futuras Mejoras

### Arquitectura
- [ ] Migración a MVVM + Repository Pattern
- [ ] Implementación de ViewModels
- [ ] Inyección de dependencias con Hilt
- [ ] Manejo de estado con StateFlow

### Funcionalidades
- [ ] Integración con APIs reales
- [ ] Sistema de autenticación
- [ ] Mapas interactivos
- [ ] Reservas y pagos
- [ ] Notificaciones push

### UI/UX
- [ ] Animaciones avanzadas
- [ ] Modo oscuro/claro
- [ ] Soporte para tablets
- [ ] Accesibilidad mejorada



## 📄 Licencia

Este proyecto es desarrollado con fines educativos y está disponible bajo la licencia MIT.

---

*AQP Explorer - Descubre la belleza de Arequipa* 🏛️✨