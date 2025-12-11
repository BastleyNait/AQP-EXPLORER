import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.material3.Icon
import com.example.aqpexplorer.R // Make sure to import your R class
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person // Guía
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ServiceIconsRow(services: Map<String, Boolean>) {
    Text(text = "Servicios disponibles", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)

    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp) // Espacio entre iconos
    ) {
        // Verificamos cada servicio manualmente para asignar su icono

        if (services["baño"] == true) {
            ServiceBadge(icon = ImageVector.vectorResource(id = R.drawable.ic_bathroom), text = "SS.HH.")
        }

        if (services["restaurante"] == true) {
            ServiceBadge(icon = ImageVector.vectorResource(id = R.drawable.restaurant_7720549), text = "Comida")
        }

        if (services["wifi"] == true) {
            ServiceBadge(icon = ImageVector.vectorResource(id = R.drawable.wifi), text = "Wi-Fi")
        }

        if (services["guia"] == true) {
            ServiceBadge(icon = Icons.Default.Person, text = "Guía")
        }
    }
}

// Un componente pequeño para el icono + texto
@Composable
fun ServiceBadge(icon: ImageVector, text: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = Color(0xFF4CAF50), // Verde o el color de tu tema
            modifier = Modifier.size(24.dp)
        )
        Text(text = text, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
    }
}