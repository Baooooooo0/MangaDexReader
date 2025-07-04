package com.example.mangadexreader

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.mangadexreader.data.MangaModels
import com.example.mangadexreader.ui.mainscreen.MangaListUiState
import com.example.mangadexreader.ui.mainscreen.MangaListViewModel
import com.example.mangadexreader.ui.theme.MangadexReaderTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MangadexReaderTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Lấy instance của ViewModel
                    val mangaViewModel: MangaListViewModel = viewModel()
                    // Lắng nghe và chuyển đổi StateFlow thành State mà Compose có thể sử dụng
                    val uiState by mangaViewModel.uiState.collectAsStateWithLifecycle()

                    // Gọi Composable chính của màn hình, truyền vào trạng thái hiện tại
                    MangaListScreen(uiState = uiState)
                }
            }
        }
    }
}

/**
 * Composable chính, quyết định hiển thị gì dựa trên trạng thái của UI
 */
@Composable
fun MangaListScreen(uiState: MangaListUiState) {
    // Sử dụng 'when' để xử lý từng trạng thái một cách tường minh
    when (uiState) {
        is MangaListUiState.Loading -> LoadingScreen()
        is MangaListUiState.Success -> MangaList(mangaDataList = uiState.mangaList)
        is MangaListUiState.Error -> ErrorScreen(message = uiState.message)
    }
}

/**
 * Màn hình hiển thị khi đang tải dữ liệu
 */
@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

/**
 * Màn hình hiển thị khi có lỗi xảy ra
 */
@Composable
fun ErrorScreen(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )
    }
}

/**
 * Màn hình hiển thị danh sách truyện khi tải thành công
 */
@Composable
fun MangaList(mangaDataList: List<MangaModels.MangaData>) {
    // LazyColumn chỉ render những item đang hiển thị trên màn hình, rất hiệu quả cho danh sách dài
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp) // Khoảng cách giữa các item
    ) {
        items(mangaDataList) { manga ->
            MangaListItem(manga = manga)
        }
    }
}

/**
 * Composable cho một item truyện trong danh sách
 */
@Composable
fun MangaListItem(manga: MangaModels.MangaData) {
    val coverFileName = manga.coverFileName
    val imageUrl: String? =
        if (
            coverFileName != null
        ){
            "https://uploads.mangadex.org/covers/${manga.id}/${coverFileName}.256.jpg"
        } else{
            null
        }

    Log.d("MangaListItemDebug", "Title: ${manga.attributes.title["en"]}, Image URL: $imageUrl")

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                imageUrl,
                "Manga cover",
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .weight(0.5f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Lấy tiêu đề tiếng Anh, nếu không có thì lấy tiêu đề đầu tiên tìm thấy
            val title = manga.attributes.title["en"] ?: manga.attributes.title.values.firstOrNull() ?: "No Title"
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier
                    .weight(1.5f)
            )
        }
    }
}