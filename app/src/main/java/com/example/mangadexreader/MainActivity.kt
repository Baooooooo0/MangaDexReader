package com.example.mangadexreader

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import com.example.mangadexreader.data.MangaModels
import com.example.mangadexreader.navigation.ScreenRoutes
import com.example.mangadexreader.ui.detailscreen.MangaDetailScreen
import com.example.mangadexreader.ui.mainscreen.MangaListUiState
import com.example.mangadexreader.ui.mainscreen.MangaListViewModel
import com.example.mangadexreader.ui.reader.ReaderScreen
import com.example.mangadexreader.ui.theme.MangadexReaderTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MangadexReaderTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    // Gọi Composable chính của màn hình, truyền vào trạng thái hiện tại
                    NavHost(
                        navController = navController,
                        startDestination = ScreenRoutes.MangaList
                    ){
                        composable(route = ScreenRoutes.MangaList){
                            // 1. Tạo ViewModel một lần duy nhất
                            val mangaViewModel: MangaListViewModel = viewModel()
                            // 2. Lấy state từ chính ViewModel đó
                            val uiState by mangaViewModel.uiState.collectAsStateWithLifecycle()
                            // 3. Truyền chính xác viewModel đã tạo vào màn hình
                            MangaListScreen(uiState = uiState, navController = navController, viewModel = mangaViewModel)
                        }
                        composable(
                            route = ScreenRoutes.MangaDetail,
                            arguments = listOf(navArgument("mangaId") {type = NavType.StringType})){
                            MangaDetailScreen(navController = navController)
                        }
                        composable(
                            route = ScreenRoutes.MangaReader,
                            arguments = listOf(navArgument("chapterId") { type = NavType.StringType })
                        ) {
                            // Tạm thời đặt một màn hình giữ chỗ
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                ReaderScreen(navController = navController)
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Composable chính, quyết định hiển thị gì dựa trên trạng thái của UI
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MangaListScreen(uiState: MangaListUiState,
                    navController: NavController,
                    viewModel: MangaListViewModel
) {

    val listState = rememberLazyListState()

    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = searchQuery) {
        delay(500L) // Chờ nửa giây
        viewModel.fetchMangaList(searchQuery)
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Mangadex")})
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            TextField(
                searchQuery,
                onValueChange = {newQuery -> viewModel.onSearchQueryChanged(newQuery)},
                label = { Text("Tim kiem truyen...")},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            // Sử dụng 'when' để xử lý từng trạng thái một cách tường minh
            Box(){
                when (uiState) {
                    is MangaListUiState.Loading -> LoadingScreen()
                    is MangaListUiState.Success ->
                        MangaList(
                            mangaDataList = uiState.mangaList,
                            onItemClick = { mangaId ->
                                navController.navigate(ScreenRoutes.MangaDetail.replace("{mangaId}", mangaId))
                            },
                            onLoadMore = { viewModel.loadMoreItems() },
                            listState = listState
                        )
                    is MangaListUiState.Error -> ErrorScreen(message = uiState.message)
                }
            }
        }
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
fun MangaList(
    mangaDataList: List<MangaModels.MangaData>,
    onItemClick: (String) -> Unit,
    onLoadMore: () -> Unit,
    listState: LazyListState
) {
    val shouldLoadMore by remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
            lastVisibleItem != null && lastVisibleItem.index == listState.layoutInfo.totalItemsCount - 1
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) {
            onLoadMore()
        }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(mangaDataList) { manga ->
            MangaListItem(manga = manga, onItemClick = { onItemClick(manga.id) })
        }
    }
}


/**
 * Composable cho một item truyện trong danh sách
 */
@Composable
fun MangaListItem(manga: MangaModels.MangaData, onItemClick: (String) -> Unit) {
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
        modifier = Modifier
            .clickable { onItemClick(manga.id) }
            .fillMaxWidth(),
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