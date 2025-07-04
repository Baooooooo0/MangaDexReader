package com.example.mangadexreader.ui.mainscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mangadexreader.repository.MangaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MangaListViewModel : ViewModel() {

    // Khởi tạo repository
    private val repository = MangaRepository()

    // Dòng state riêng tư, chỉ ViewModel có thể thay đổi
    private val _uiState = MutableStateFlow<MangaListUiState>(MangaListUiState.Loading)

    // Dòng state công khai, chỉ cho phép đọc, để UI quan sát
    val uiState: StateFlow<MangaListUiState> = _uiState.asStateFlow()

    // Khối init sẽ được gọi khi ViewModel được tạo lần đầu tiên
    init {
        fetchMangaList()
    }

    /**
     * Hàm để lấy dữ liệu truyện tranh.
     */
    fun fetchMangaList() {
        // Cập nhật trạng thái là Loading trước khi gọi API
        _uiState.value = MangaListUiState.Loading

        // Khởi chạy một coroutine trong scope của ViewModel
        viewModelScope.launch {
            try {
                // Gọi repository để lấy dữ liệu
                val response = repository.getMangaList()
                // Nếu thành công, cập nhật trạng thái là Success cùng với dữ liệu
                _uiState.value = MangaListUiState.Success(response.data)
            } catch (e: Exception) {
                // Nếu có lỗi (ví dụ: mất mạng), cập nhật trạng thái là Error
                _uiState.value = MangaListUiState.Error(e.message ?: "Đã có lỗi không xác định xảy ra")
            }
        }
    }
}