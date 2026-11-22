package com.example.navigationlab.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.navigationlab.data.local.FavoriteStudentEntity
import com.example.navigationlab.data.preferences.UserPreferencesRepository
import com.example.navigationlab.data.repository.FavoriteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

data class FavoritesUiState(
    val students: List<FavoriteStudentEntity> = emptyList(),
    val sortAscending: Boolean = true
)

class FavoritesViewModel(
    favoriteRepository: FavoriteRepository,
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(favoriteRepository.observeFavorites(), preferencesRepository.sortAscending) { list, ascending ->
                FavoritesUiState(
                    students = if (ascending) list else list.reversed(),
                    sortAscending = ascending
                )
            }.collect { _uiState.value = it }
        }
    }

    fun toggleSortOrder() {
        viewModelScope.launch {
            preferencesRepository.saveSortAscending(!uiState.value.sortAscending)
        }
    }

    companion object {
        fun factory(favoriteRepository: FavoriteRepository, preferencesRepository: UserPreferencesRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(FavoritesViewModel::class.java))
                        return FavoritesViewModel(favoriteRepository, preferencesRepository) as T
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
    }
}
