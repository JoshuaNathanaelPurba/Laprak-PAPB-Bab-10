package com.example.navigationlab.ui.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.navigationlab.data.location.LocationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LocationViewModel(
    private val repository: LocationRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<LocationUiState>(LocationUiState.Idle)
    val uiState: StateFlow<LocationUiState> = _uiState.asStateFlow()

    fun refreshLocation() {
        viewModelScope.launch {
            _uiState.value = LocationUiState.Loading
            _uiState.value = repository.getCurrentLocation().fold(
                onSuccess = { LocationUiState.Success(it) },
                onFailure = {
                    LocationUiState.Error(it.message ?: "Gagal mengambil lokasi. Silakan coba lagi.")
                }
            )
        }
    }

    companion object {
        fun factory(repository: LocationRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(LocationViewModel::class.java)) {
                        return LocationViewModel(repository) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                }
            }
    }
}
