package com.example.mangadexreader.ui.detailscreen

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mangadexreader.data.ChapterData
import com.example.mangadexreader.repository.MangaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MangaDetailViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {
    private val repository = MangaRepository()

    private val _uiState = MutableStateFlow<MangaDetailUiState>(MangaDetailUiState.Loading)

    val uiState: StateFlow<MangaDetailUiState> = _uiState.asStateFlow()

    private val _chapterListState = MutableStateFlow<List<ChapterData>>(emptyList())

    val chapterListState: StateFlow<List<ChapterData>> = _chapterListState.asStateFlow()

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
                fetchChapterFeed(mangaId)
            } catch (e: Exception){
                _uiState.value = MangaDetailUiState.Error("Có lỗi xảy ra")
            }
        }
    }

    private suspend fun fetchChapterFeed(mangaId: String){
        viewModelScope.launch {
            try {
                val chapterResponse = repository.getChapterFeed(mangaId)
                _chapterListState.value = chapterResponse.data
            } catch (e: Exception){
                Log.d("Chapter", "fetch Chapter Error")
            }
        }
    }
}