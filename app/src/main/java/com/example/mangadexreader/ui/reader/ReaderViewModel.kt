package com.example.mangadexreader.ui.reader

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mangadexreader.repository.MangaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ReaderViewModel(private val savedStateHandle: SavedStateHandle): ViewModel(){
    private val repository = MangaRepository()

    private val _uiState = MutableStateFlow<ReaderUiState>(ReaderUiState.Loading)
    val uiState: StateFlow<ReaderUiState> = _uiState.asStateFlow()

    init {
        val chapterId = savedStateHandle.get<String>("chapterId")
        if (chapterId != null){
            fetchReaderPages(chapterId)
        }
    }

    private fun fetchReaderPages(chapterId: String){
        viewModelScope.launch {
            try {
                val response = repository.getReaderPages(chapterId)
                val fullImageUrls = response.chapter.data.map { fileName ->
                    "${response.baseUrl}/data/${response.chapter.hash}/${fileName}"
                }
                _uiState.value = ReaderUiState.Success(fullImageUrls)
            } catch (e: Exception){
                _uiState.value = ReaderUiState.Error("Có lỗi xảy ra")
            }
        }
    }
}