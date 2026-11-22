# NavigationLab — Modul 10: Location

Project ini merupakan kelanjutan dari NavigationLab Modul 09. Fitur REST API/pencarian, DataStore, Room, dan Favorites tetap dipertahankan.

## Fitur Modul 10
- Mengambil satu lokasi terkini menggunakan `FusedLocationProviderClient.getCurrentLocation()`.
- Meminta `ACCESS_COARSE_LOCATION` dan `ACCESS_FINE_LOCATION` melalui permission launcher Compose.
- Menampilkan jenis akses `Precise` atau `Approximate`.
- Menampilkan latitude, longitude, accuracy (meter), dan waktu terakhir lokasi berhasil diperbarui.
- Tombol `Perbarui Lokasi` mengambil lokasi kembali tanpa meminta izin ulang selama izin masih tersedia.
- Menampilkan pesan ketika izin ditolak atau lokasi tidak tersedia, serta menyediakan `Minta Izin Lagi` dan `Coba Lagi`.
- Memeriksa ulang status permission saat pengguna kembali ke aplikasi.

## Struktur kode tambahan
- `data/location/DeviceLocation.kt`
- `data/location/LocationRepository.kt`
- `data/location/DefaultLocationRepository.kt`
- `ui/location/LocationUiState.kt`
- `ui/location/LocationViewModel.kt`
- `ui/location/LocationScreen.kt`

## Dependency dan permission
Dependency yang ditambahkan pada `app/build.gradle.kts`:
- `com.google.android.gms:play-services-location:21.4.0`
- `org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.11.0`

Manifest mendeklarasikan `ACCESS_COARSE_LOCATION` dan `ACCESS_FINE_LOCATION`. `ACCESS_BACKGROUND_LOCATION` tidak digunakan karena fitur hanya mengambil lokasi satu kali ketika screen digunakan.

## Menjalankan
1. Ekstrak ZIP, lalu buka folder `NavigationLab_Modul09` melalui Android Studio.
2. Lakukan **Sync Project with Gradle Files**.
3. Jalankan aplikasi pada perangkat/emulator yang mendukung Google Play services dan aktifkan Location.
4. Dari Home, tekan **Buka Lokasi**.
5. Berikan izin lokasi. Android dapat memberikan akses approximate saja; aplikasi tetap dapat digunakan.
6. Tekan **Perbarui Lokasi** untuk mengambil posisi terbaru.

Catatan: Build belum dapat diverifikasi di lingkungan penyusunan ZIP karena Android Gradle build environment/Gradle executable tidak tersedia. Lakukan Gradle Sync dan Build Project di Android Studio untuk verifikasi akhir.
