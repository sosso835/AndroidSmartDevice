package fr.isen.rachdaoui.androidsmartdevice

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import fr.isen.rachdaoui.androidsmartdevice.composable.ScanBLEScreen
import fr.isen.rachdaoui.androidsmartdevice.ui.theme.AndroidSmartDeviceTheme

class ScanActivity : ComponentActivity() {

    private lateinit var bluetoothAdapter: BluetoothAdapter
    private var bluetoothScanner: BluetoothLeScanner? = null
    private lateinit var handler: Handler
    private val detectedDevices = mutableStateListOf<BluetoothDevice>()
    private val isScanning = mutableStateOf(false)

    private fun getAllPermissionsForBLE(): Array<String> {
        var allPermissions = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.BLUETOOTH_ADMIN
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            allPermissions += arrayOf(
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_ADMIN
            )
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            allPermissions += Manifest.permission.ACCESS_BACKGROUND_LOCATION
        }
        return allPermissions
    }

    private val permissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        permissions.forEach { permission, isGranted ->
            if (!isGranted) {
                showToast("La permission $permission est requise pour le scan BLE.")
            }
        }
        startBLEScanIfPossible()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter
        bluetoothScanner = bluetoothAdapter.bluetoothLeScanner

        setContent {
            AndroidSmartDeviceTheme {
                ScanBLEScreen(
                    isScanning = isScanning.value,
                    detectedDevices = detectedDevices,
                    onScanButtonClick = { toggleScan() },
                    onDeviceClick = { deviceName, deviceAddress ->
                        navigateToDeviceConnection(deviceName, deviceAddress)
                    }
                )
            }
        }

        checkAndRequestPermissions()
    }

    private fun toggleScan() {
        if (isScanning.value) {
            stopBLEScan()
        } else {
            startBLEScanIfPossible()
        }
    }

    private fun startBLEScanIfPossible() {
        if (!hasPermissions()) {
            showToast("Les permissions requises ne sont pas accordées.")
            return
        }

        if (!bluetoothAdapter.isEnabled) {
            showToast("Veuillez activer Bluetooth pour scanner")
            return
        }

        try {
            handler = Handler(mainLooper)
            isScanning.value = true
            detectedDevices.clear()
            bluetoothScanner?.startScan(scanCallback)

            // Timeout to stop scanning after SCAN_PERIOD
            handler.postDelayed({
                stopBLEScan()
            }, SCAN_PERIOD)
        } catch (e: SecurityException) {
            showToast("Erreur de sécurité: permission manquante pour démarrer le scan.")
        }
    }

    private fun stopBLEScan() {
        try {
            bluetoothScanner?.stopScan(scanCallback)
            handler.removeCallbacksAndMessages(null)
            isScanning.value = false
        } catch (e: SecurityException) {
            showToast("Erreur de sécurité: permission manquante pour arrêter le scan.")
        }
    }

    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            try {
                if (hasPermissions()) {

                    if (!detectedDevices.any { it.address == result.device.address }) {
                        detectedDevices.add(result.device)
                    }
                } else {
                    showToast("Permission manquante pour accéder au nom de l'appareil.")
                }
            } catch (e: SecurityException) {
                showToast("Erreur: impossible d'accéder au nom de l'appareil.")
            }
        }
    }

    private fun navigateToDeviceConnection(deviceName: String, deviceAddress: String) {
        val intent = Intent(this, DeviceConnectionActivity::class.java).apply {
            putExtra("deviceName", deviceName)
            putExtra("deviceAddress", deviceAddress)
        }
        startActivity(intent)
    }

    private fun checkAndRequestPermissions() {
        val missingPermissions = getAllPermissionsForBLE().filter {
            checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED
        }
        if (missingPermissions.isNotEmpty()) {
            permissionsLauncher.launch(missingPermissions.toTypedArray())
        } else {3

            startBLEScanIfPossible()
        }
    }

    private fun hasPermissions(): Boolean {
        return getAllPermissionsForBLE().all {
            checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    companion object {
        private const val SCAN_PERIOD = 10000000L // 10 seconds
    }
}
