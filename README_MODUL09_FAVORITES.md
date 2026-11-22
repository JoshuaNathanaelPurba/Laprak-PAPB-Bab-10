# Implementasi Modul Praktikum 09 – DataStore, Room, dan Favorites

Project ini dikembangkan dari source ZIP yang diberikan (struktur awal bernama NavigationLab_Modul09). Fitur REST API, pencarian, retry, StateFlow, dan Navigation Compose yang tersedia dipertahankan.

## Fitur tambahan
- DataStore menyimpan query pencarian terakhir (`last_search_query`) dengan debounce 500 ms.
- DataStore menyimpan preferensi urutan favorit (`sort_ascending`) dan nilainya dapat diubah melalui tombol urutan pada FavoritesScreen.
- Room menyimpan data favorit pada `navigation_lab.db`, tabel `favorite_students`.
- DetailScreen menyediakan tombol Simpan ke Favorit / Hapus dari Favorit.
- FavoritesScreen menampilkan nama dan email, membuka DetailScreen saat item dipilih, dan menerima pembaruan Flow Room secara otomatis.
- Route `favorites` ditambahkan ke Navigation Compose dan dapat dibuka dari Home.

## Catatan versi KSP
Project memakai Kotlin 2.0.21 dan AGP 8.7.3. Karena itu plugin KSP dikunci ke pasangan `2.0.21-1.0.28` yang sesuai dengan Kotlin project ini, bukan menyalin versi KSP dari stack Kotlin lain. Jika versi Kotlin diubah, sesuaikan pasangan KSP.

## Cara menjalankan
1. Buka folder `NavigationLab_Modul09` melalui Android Studio.
2. Pastikan JDK 17 terpilih.
3. Sync Gradle dan tunggu dependency selesai diunduh.
4. Jalankan aplikasi pada emulator/perangkat dengan koneksi internet untuk REST API.
5. Dari Home pilih Favorit untuk melihat data tersimpan.
6. Buka Detail mahasiswa dan simpan/hapus favorit.
7. Ubah urutan A-Z/Z-A pada FavoritesScreen, tutup lalu buka kembali aplikasi untuk menguji persistensi preferensi.

## Struktur paket tambahan
- `data/preferences`: DataStore dan repository preferensi.
- `data/local`: Room Entity, DAO, Database, dan singleton provider.
- `data/repository/FavoriteRepository.kt`: akses data favorit.
- `ui/favorites`: ViewModel dan state Favorites.
- `ui/screen/FavoritesScreen.kt`: tampilan daftar favorit.
