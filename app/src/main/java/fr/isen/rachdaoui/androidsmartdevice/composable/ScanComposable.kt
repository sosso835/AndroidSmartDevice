
package fr.isen.rachdaoui.androidsmartdevice.composable
import android.bluetooth.BluetoothManager
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.rachdaoui.androidsmartdevice.R
import fr.isen.rachdaoui.androidsmartdevice.showToast

@Composable
fun ScanBLEScreen() {
    var isScanning by remember { mutableStateOf(false) }
    val detectedDevices = remember { mutableStateListOf<String>() }
    var bluetoothEnabled by remember { mutableStateOf(true) }
    var bluetoothAvailable by remember { mutableStateOf(true) }

    // Récupérer le contexte de l'application pour afficher des Toasts
    val context = LocalContext.current

    // Vérification Bluetooth
    val bluetoothAdapter = (context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter

    LaunchedEffect(Unit) {
        // Vérifier si Bluetooth est disponible
        bluetoothAvailable = bluetoothAdapter != null

        // Vérifier si Bluetooth est activé
        bluetoothEnabled = bluetoothAdapter?.isEnabled == true

        if (!bluetoothAvailable) {
            showToast(context, "Bluetooth non disponible")
        } else if (!bluetoothEnabled) {
            showToast(context, "Veuillez activer Bluetooth pour scanner")
        }
        else if (bluetoothAvailable){
            showToast(context, "bluetooth activé")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Scan BLE",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = if (isScanning) painterResource(id = R.drawable.stop)
            else painterResource(id = R.drawable.play2),
            contentDescription = "Scan Button",
            modifier = Modifier
                .size(80.dp)
                .padding(16.dp)
                .clickable {
                    // Si Bluetooth est activé, commencer le scan
                    if (bluetoothEnabled) {
                        isScanning = !isScanning
                        if (isScanning) {
                            detectedDevices.clear()
                            detectedDevices.addAll(listOf("Appareil 1", "Appareil 2", "Appareil 3")) // Ici, simulateur d'appareils BLE
                        } else {
                            detectedDevices.clear()
                        }
                    }
                }
        )

        Text(
            text = when {
                !bluetoothAvailable -> "Bluetooth non disponible"
                !bluetoothEnabled -> "Bluetooth désactivé"
                isScanning -> "Scanning..."
                else -> "Scan arrêté"
            },
            fontSize = 16.sp,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Affichage de la liste des appareils détectés
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(detectedDevices) { device ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    // Nom de l'appareil
                    Text(
                        text = device,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text(
                        text = "Salut",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Light,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Séparateur
                    Divider(
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f),
                        thickness = 1.dp,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
}
