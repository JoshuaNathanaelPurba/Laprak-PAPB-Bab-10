# Perbaikan kompatibilitas Kotlin dan Gradle

## Penyebab error sebelumnya

Build sebelumnya gagal pada `:app:kspDebugKotlin` karena Kotlin compiler 2.0.21 tidak dapat membaca metadata Kotlin 2.2/2.3 dari dependency `play-services-location:21.4.0` dan `kotlinx-coroutines:1.11.0`.

## Versi yang diselaraskan

- Android Gradle Plugin: `8.13.2`
- Kotlin Android: `2.3.0`
- Kotlin Compose compiler plugin: `2.3.0`
- KSP: `2.3.4`
- Gradle: `8.13` (gunakan Gradle 8.13 untuk project ini)
- kotlinx-coroutines-android: `1.11.0`
- kotlinx-coroutines-play-services: `1.11.0`
- play-services-location: `21.4.0`

## Pengaturan Gradle di Android Studio

Log build sebelumnya menunjukkan Gradle `9.3.0`. Untuk konfigurasi AGP `8.13.2` ini, gunakan Gradle `8.13`, bukan Gradle `9.3.0`.

1. Buka **File > Settings > Build, Execution, Deployment > Build Tools > Gradle**.
2. Pada **Gradle distribution**, pilih instalasi Gradle `8.13` jika tersedia, atau gunakan konfigurasi wrapper project yang menunjuk ke Gradle `8.13`.
3. Pastikan **Gradle JDK** menggunakan JDK 17.
4. Klik **Apply**, lalu **OK**.
5. Jalankan **Sync Project with Gradle Files**.
6. Pilih **Build > Clean Project**, kemudian **Build > Rebuild Project**.

Jika Android Studio hanya menawarkan Gradle 9.3.0, unduh/disiapkan Gradle 8.13 terlebih dahulu atau buat Gradle Wrapper 8.13 melalui instalasi Gradle lokal, lalu buka ulang project.

## Catatan

Versi dependency lokasi sesuai instruksi praktikum tetap dipertahankan: `play-services-location:21.4.0` dan `kotlinx-coroutines-play-services:1.11.0`. File kode fitur Modul 09 dan Modul 10 tidak diubah dalam perbaikan kompatibilitas ini.

Konfigurasi telah diperbarui pada file Gradle, tetapi build Android tidak dapat dieksekusi di lingkungan pembuatan ZIP ini. Verifikasi akhir tetap perlu dilakukan melalui Android Studio setelah Gradle 8.13 dan JDK 17 dipilih.
