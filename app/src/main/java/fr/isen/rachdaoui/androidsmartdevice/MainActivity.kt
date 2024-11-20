package fr.isen.rachdaoui.androidsmartdevice

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
//import androidx.compose.material3.icons.filled.Bluetooth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.rachdaoui.androidsmartdevice.ui.theme.AndroidSmartDeviceTheme
import androidx.compose.ui.tooling.preview.Preview


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidSmartDeviceTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Icône de l'application
        Image(
            painter = painterResource(id = R.drawable.ic_ble_icon),
            contentDescription = "Application Icon",
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(16.dp)) // Icône arrondie
                .background(MaterialTheme.colorScheme.primary) // Fond coloré pour l'icône
                .padding(16.dp)
                .shadow(8.dp, shape = RoundedCornerShape(16.dp)) // Ombre autour de l'icône
        )

        Spacer(modifier = Modifier.height(24.dp)) // Espacement après l'icône

        // Titre de l'application avec une police stylisée
        Text(
            text = "Application de Scan BLE",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(top = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp)) // Espacement après le titre

        // Description avec un texte explicatif
        Text(
            text = "Cette application permet de scanner les appareils Bluetooth Low Energy aux alentours.",
            fontSize = 16.sp,
            color = Color.Gray,
            modifier = Modifier.padding(vertical = 8.dp),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp)) // Espacement avant le bouton

        // Bouton "Démarrer le scan" avec animation d'appui
        Button(
            onClick = {
                context.startActivity(Intent(context, ScanActivity::class.java))
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .clip(RoundedCornerShape(50.dp)) // Bouton arrondi
                .shadow(8.dp, shape = RoundedCornerShape(50.dp)), // Ombre autour du bouton
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            )
        ) {
            // Icône avec le texte dans le bouton
            Icon(
                imageVector = Icons.Filled.CheckCircle,
                contentDescription = "Bluetooth",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp)) // Espacement entre l'icône et le texte
            Text(
                text = "Démarrer le scan",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    AndroidSmartDeviceTheme {
        MainScreen()
    }
}
