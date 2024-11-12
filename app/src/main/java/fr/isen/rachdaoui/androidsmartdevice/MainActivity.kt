package fr.isen.rachdaoui.androidsmartdevice

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
            modifier = Modifier.size(100.dp)
        )

        Text(
            text = "Application de Scan BLE",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp)
        )


        Text(
            text = "Cette application permet de scanner les appareils Bluetooth Low Energy aux alentours.",
            fontSize = 16.sp,
            modifier = Modifier.padding(vertical = 8.dp)
        )


        Button(
            onClick = {
                context.startActivity(Intent(context, ScanActivity::class.java))
            },
            modifier = Modifier.fillMaxWidth().padding(top =10.dp)

        ) {
            Text(text = "Démarrer le scan")
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
