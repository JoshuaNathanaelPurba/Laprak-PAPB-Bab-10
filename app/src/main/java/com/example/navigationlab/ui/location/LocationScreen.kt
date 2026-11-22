package com.example.navigationlab.ui.location

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.navigationlab.data.location.DeviceLocation
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun LocationScreen(viewModel: LocationViewModel, onBack: () -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    fun hasAnyPermission(): Boolean =
        ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

    fun hasFinePermission(): Boolean =
        ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

    var permissionGranted by remember { mutableStateOf(hasAnyPermission()) }
    var preciseGranted by remember { mutableStateOf(hasFinePermission()) }
    var permissionDenied by remember { mutableStateOf(false) }

    // Periksa kembali izin ketika pengguna kembali dari Settings.
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                permissionGranted = hasAnyPermission()
                preciseGranted = hasFinePermission()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        val coarse = result[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        val fine = result[Manifest.permission.ACCESS_FINE_LOCATION] == true
        permissionGranted = coarse || fine || hasAnyPermission()
        preciseGranted = fine || hasFinePermission()
        permissionDenied = !permissionGranted
        if (permissionGranted) viewModel.refreshLocation()
    }

    fun requestPermission() {
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("Lokasi Perangkat", style = MaterialTheme.typography.headlineMedium)
        Text("Ambil satu lokasi terkini perangkat tanpa menggunakan peta eksternal.")

        if (!permissionGranted) {
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        if (permissionDenied) "Izin lokasi ditolak. Fitur lokasi belum dapat digunakan."
                        else "Aplikasi memerlukan izin lokasi untuk mengambil posisi perangkat."
                    )
                    Button(onClick = { permissionDenied = false; requestPermission() }) {
                        Text("Minta Izin Lagi")
                    }
                }
            }
        } else {
            Text("Jenis akses: ${if (preciseGranted) "Precise" else "Approximate"}")
            Button(onClick = { viewModel.refreshLocation() }, modifier = Modifier.fillMaxWidth()) {
                Text("Perbarui Lokasi")
            }
        }

        when (val state = uiState) {
            LocationUiState.Idle -> Text("Lokasi belum diminta")
            LocationUiState.Loading -> {
                CircularProgressIndicator()
                Text("Sedang mengambil lokasi...")
            }
            is LocationUiState.Success -> LocationDetails(state.location)
            is LocationUiState.Error -> {
                Text(state.message, color = MaterialTheme.colorScheme.error)
                if (permissionGranted) {
                    Button(onClick = { viewModel.refreshLocation() }) { Text("Coba Lagi") }
                }
            }
        }
        Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("Kembali") }
    }
}

@Composable
private fun LocationDetails(location: DeviceLocation) {
    val dateText = remember(location.updatedAtMillis) {
        SimpleDateFormat("dd MMMM yyyy, HH:mm:ss", Locale("id", "ID"))
            .format(Date(location.updatedAtMillis))
    }
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Lokasi berhasil diperoleh", style = MaterialTheme.typography.titleMedium)
            Text("Latitude: ${location.latitude}")
            Text("Longitude: ${location.longitude}")
            Text("Accuracy: ${location.accuracy?.let { "%.1f".format(Locale.US, it) + " m" } ?: "Tidak tersedia"}")
            Text("Terakhir diperbarui: $dateText")
        }
    }
}
