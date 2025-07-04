package com.example.mangadexreader.ui.mainscreen

import com.example.mangadexreader.data.MangaModels

/**
 * Một sealed class định nghĩa tất cả các trạng thái có thể có của màn hình danh sách truyện.
 */
sealed class MangaListUiState {
    // Trạng thái đang tải, chưa có dữ liệu
    object Loading : MangaListUiState()

    // Trạng thái thành công, chứa danh sách truyện
    data class Success(val mangaList: List<MangaModels.MangaData>) : MangaListUiState()

    // Trạng thái lỗi, chứa thông báo lỗi
    data class Error(val message: String) : MangaListUiState()
}