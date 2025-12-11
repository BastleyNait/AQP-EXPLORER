import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape

import com.example.aqpexplorer.R

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri

// COMPONENTE: Tarjeta de Movilidad
@Composable
fun TransportCard(
    transportInfo: String,
    lat: Double,
    lng: Double,
    placeName: String
) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2A2A)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // 1. Cabecera con Icono
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.direction_bus), // O Place
                    contentDescription = null,
                    tint = Color(0xFF4CAF50), // Verde
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Cómo llegar",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 2. El "Dato Local" (Texto de Firebase)
            Text(
                text = transportInfo.ifEmpty { "Ubicación céntrica, accesible mediante taxi o transporte público." },
                color = Color.LightGray,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 3. Botones de Acción
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Botón A: Abrir Google Maps (Ruta directa)
                Button(
                    onClick = {
                        // Intent universal para abrir mapas en modo navegación
                        val gmmIntentUri = "google.navigation:q=$lat,$lng".toUri()
                        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                        mapIntent.setPackage("com.google.android.apps.maps")

                        // Si no tiene Google Maps, intentamos abrir cualquier navegador
                        if (mapIntent.resolveActivity(context.packageManager) != null) {
                            context.startActivity(mapIntent)
                        } else {
                            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/?api=1&query=$lat,$lng"))
                            context.startActivity(browserIntent)
                        }
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3E8E41)), // Verde mapa
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    Icon(imageVector = ImageVector.vectorResource(R.drawable.map), contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Ver Ruta", fontSize = 13.sp)
                }

                // Botón B: Copiar al Portapapeles (El "Wow" útil)
                OutlinedButton(
                    onClick = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("Ubicación", "$placeName, Arequipa") // Copiamos el nombre para apps de taxi
                        clipboard.setPrimaryClip(clip)
                        Toast.makeText(context, "Ubicación copiada para tu Taxi", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.weight(1f),
                    border = BorderStroke(1.dp, Color.Gray)
                ) {
                    Icon(imageVector = ImageVector.vectorResource(R.drawable.outline_content_copy_24), contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Copiar", color = Color.Gray, fontSize = 13.sp)
                }
            }
        }
    }
}