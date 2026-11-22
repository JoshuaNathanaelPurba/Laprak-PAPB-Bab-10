package com.example.navigationlab.data.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import kotlinx.coroutines.tasks.await

class DefaultLocationRepository(
    private val context: Context,
    private val client: FusedLocationProviderClient
) : LocationRepository {
    override suspend fun getCurrentLocation(): Result<DeviceLocation> = runCatching {
        val fineGranted = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val coarseGranted = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        check(fineGranted || coarseGranted) { "Izin lokasi belum diberikan. Silakan minta izin terlebih dahulu." }

        val priority = if (fineGranted) {
            Priority.PRIORITY_HIGH_ACCURACY
        } else {
            Priority.PRIORITY_BALANCED_POWER_ACCURACY
        }
        val location: Location = client.getCurrentLocation(priority, null).await()
            ?: error("Lokasi belum tersedia. Pastikan layanan lokasi/GPS aktif, lalu coba lagi.")

        DeviceLocation(
            latitude = location.latitude,
            longitude = location.longitude,
            accuracy = location.accuracy,
            updatedAtMillis = System.currentTimeMillis()
        )
    }
}
