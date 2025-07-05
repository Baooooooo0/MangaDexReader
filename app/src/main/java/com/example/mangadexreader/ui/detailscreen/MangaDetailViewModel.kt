package com.example.mangadexreader.ui.detailscreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mangadexreader.repository.MangaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MangaDetailViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {
    private val repository = MangaRepository()

    private val _uiState = MutableStateFlow<MangaDetailUiState>(MangaDetailUiState.Loading)

    val uiState: StateFlow<MangaDetailUiState> = _uiState.asStateFlow()

    init {
        val mangaId = savedStateHandle.get<String>("mangaId")

        if (mangaId != null){
            fetchMangaDetails(mangaId)
        } else{
            _uiState.value = MangaDetailUiState.Error("Lỗi: Không tìm thấy ID của truyện.")
        }

    }

    private fun fetchMangaDetails(mangaId: String){
        viewModelScope.launch {
            try {
                val response = repository.getMangaDetail(mangaId)
                _uiState.value = MangaDetailUiState.Success(response.data)
            } catch (e: Exception){
                _uiState.value = MangaDetailUiState.Error("Có lỗi xảy ra")
            }
        }
    }

}