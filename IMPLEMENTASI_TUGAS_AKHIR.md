# Tugas Akhir Praktikum Modul 09 – FavoritesScreen

## Implementasi
1. Route `favorites` ditambahkan pada `NavigationLabApp.kt`.
2. `FavoritesViewModel` mengamati `FavoriteRepository.observeFavorites()` dan preferensi DataStore melalui `combine`.
3. `FavoritesScreen` menampilkan nama dan email, serta membuka DetailScreen berdasarkan ID saat item dipilih.
4. Penghapusan favorit dari DetailScreen dilakukan melalui Room. Karena FavoritesScreen mengamati Flow Room, daftar diperbarui otomatis.
5. Preferensi tambahan `sortAscending` disimpan di DataStore. Tombol urutan A-Z/Z-A pada FavoritesScreen mengubah dan menyimpan nilainya.

## Pengujian manual
- Simpan dua atau lebih mahasiswa sebagai favorit.
- Buka menu Favorit dari Home dan pastikan nama serta email tampil.
- Pilih salah satu item, hapus favorit dari Detail, lalu kembali. Item harus langsung hilang.
- Ubah urutan menjadi Z-A, tutup aplikasi, lalu buka kembali. Urutan harus tetap Z-A.
- Ubah query pencarian, tunggu minimal 500 ms, tutup aplikasi, lalu buka kembali. Query terakhir harus dipulihkan.
- Periksa `navigation_lab.db` melalui App Inspection > Database Inspector.

## Catatan kompatibilitas
Source awal yang diberikan belum memuat Gradle Wrapper dan struktur project-nya bernama Modul 06, walaupun file ZIP disebut sebagai project Modul 08. Implementasi ini mempertahankan package `com.example.navigationlab`, versi Kotlin 2.0.21, dan AGP 8.7.3 dari source. Plugin KSP menggunakan versi pasangan Kotlin `2.0.21-1.0.28`.
