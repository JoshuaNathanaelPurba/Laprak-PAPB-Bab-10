package com.example.navigationlab.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.navigationlab.ui.favorites.FavoritesUiState

@Composable
fun FavoritesScreen(
    uiState: FavoritesUiState,
    onStudentClick: (Int) -> Unit,
    onToggleSort: () -> Unit,
    onBack: () -> Unit
) {
    Column(Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Mahasiswa Favorit", style = MaterialTheme.typography.headlineMedium)
        Button(onClick = onToggleSort, modifier = Modifier.fillMaxWidth()) {
            Text(if (uiState.sortAscending) "Urutan: A-Z (ubah ke Z-A)" else "Urutan: Z-A (ubah ke A-Z)")
        }
        if (uiState.students.isEmpty()) {
            Text("Belum ada mahasiswa favorit.")
        } else {
            LazyColumn(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(uiState.students, key = { it.id }) { student ->
                    Card(Modifier.fillMaxWidth().clickable { onStudentClick(student.id) }) {
                        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(student.firstName, style = MaterialTheme.typography.titleMedium)
                            Text(student.email, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
        Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("Kembali") }
    }
}
