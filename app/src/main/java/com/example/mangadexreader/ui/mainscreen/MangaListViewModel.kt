package com.example.mangadexreader.ui.mainscreen

import androidx.compose.foundation.lazy.LazyListState
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

    private val _searchQuery = MutableStateFlow<String>("")

    public val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Khối init sẽ được gọi khi ViewModel được tạo lần đầu tiên
    init {
        fetchMangaList("")
    }

    companion object {
        private const val PAGE_SIZE = 20
    }
    private var currentOffset: Int = 0
    private var canLoadMore: Boolean = true

    /**
     * Hàm để lấy dữ liệu truyện tranh.
     */
    fun fetchMangaList(query: String) {
        // Cập nhật trạng thái là Loading trước khi gọi API
        _uiState.value = MangaListUiState.Loading
        currentOffset = 0
        canLoadMore = true

        // Khởi chạy một coroutine trong scope của ViewModel
        viewModelScope.launch {
            try {
                // Gọi repository để lấy dữ liệu
                val response = repository.getMangaList(title = query, limit = PAGE_SIZE , offset =currentOffset )
                // Nếu thành công, cập nhật trạng thái là Success cùng với dữ liệu
                _uiState.value = MangaListUiState.Success(response.data)
                currentOffset += PAGE_SIZE
                canLoadMore = response.data.isNotEmpty()
            } catch (e: Exception) {
                // Nếu có lỗi (ví dụ: mất mạng), cập nhật trạng thái là Error
                _uiState.value = MangaListUiState.Error(e.message ?: "Đã có lỗi không xác định xảy ra")
            }
        }
    }

    fun onSearchQueryChanged(newQuery: String){
        _searchQuery.value = newQuery
    }

    fun  loadMoreItems(){
        if(!canLoadMore){
            return
        }
        viewModelScope.launch {
            try {
                val response = repository.getMangaList(searchQuery.value, limit = PAGE_SIZE, offset = currentOffset)

                //Lấy danh sách cũ
                val oldList = (_uiState.value as? MangaListUiState.Success)?.mangaList ?: emptyList()
                //Tạo danh sách mới
                val combinedList = oldList + response.data
                //Cập nhật UI
                _uiState.value = MangaListUiState.Success(combinedList)

                //Cập nhật trạng thái cho lần tải tiếp theo
                currentOffset += PAGE_SIZE
                canLoadMore = response.data.isNotEmpty()
            } catch (e: Exception){
                _uiState.value = MangaListUiState.Error(e.message ?: "Đã có lỗi không xác định xảy ra")
            }
        }
    }
}