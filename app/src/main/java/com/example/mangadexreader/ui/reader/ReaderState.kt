package com.example.mangadexreader.ui.reader

import android.os.Message

sealed class ReaderUiState {

    object Loading : ReaderUiState()

    data class Success(val pages: List<String>) : ReaderUiState()

    data class Error(val message: String): ReaderUiState()
}