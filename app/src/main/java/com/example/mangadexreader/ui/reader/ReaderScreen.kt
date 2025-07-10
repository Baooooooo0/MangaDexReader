package com.example.mangadexreader.ui.reader

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.mangadexreader.ui.detailscreen.ErrorState
import com.example.mangadexreader.ui.detailscreen.LoadingState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderScreen(navController: NavController) {
    val viewModel: ReaderViewModel = viewModel()
    // Dùng 'by' sẽ gọn hơn
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            // Chỉ tạo TopAppBar ở đây
            TopAppBar(
                title = {
                    // Hiển thị tiêu đề dựa trên pagerState (sẽ được tạo trong nhánh Success)
                    // (Phần này bạn có thể tự hoàn thiện như một thử thách nhỏ)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        // Nội dung chính nằm ở đây, với padding
        Box(modifier = Modifier.padding(innerPadding)) {
            when (val currentState = uiState) {
                is ReaderUiState.Loading -> LoadingState()
                is ReaderUiState.Error -> ErrorState(message = currentState.message)
                is ReaderUiState.Success -> {
                    // Tạo pagerState ở đây để TopAppBar và ReaderContent đều thấy
                    val pagerState = rememberPagerState { currentState.pages.size }

                    // Bạn có thể cập nhật lại title của TopAppBar ở đây nếu muốn
                    // Ví dụ: TopAppBar(title = { Text("Trang ${pagerState.currentPage + 1}") })
                    // (Việc này đòi hỏi tái cấu trúc phức tạp hơn một chút)

                    // Trước mắt, hãy tập trung vào việc hiển thị Pager
                    ReaderContent(pages = currentState.pages, pagerState = pagerState)
                }
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class) // Bạn sẽ cần thêm annotation này cho Pager
@Composable
fun ReaderContent(pages: List<String>, pagerState: PagerState) {
    HorizontalPager(state = pagerState) { pageIndex ->
        AsyncImage(
            model = pages[pageIndex], // Sửa thành 'model'
            contentDescription = "Manga Page ${pageIndex + 1}", // Thêm mô tả
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit
        )
    }
}