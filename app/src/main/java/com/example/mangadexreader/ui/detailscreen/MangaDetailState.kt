package com.example.mangadexreader.ui.detailscreen

import com.example.mangadexreader.data.MangaModels

sealed class MangaDetailUiState{
    object Loading:MangaDetailUiState()

    data class Success(val manga: MangaModels.MangaData):MangaDetailUiState()

    data class Error(val message: String):MangaDetailUiState()
}