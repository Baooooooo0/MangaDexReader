package com.example.mangadexreader.ui.detailscreen

import android.util.EventLogTags.Description
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.mangadexreader.data.MangaModels

@Composable
fun MangaDetailScreen(){
    val viewModel = viewModel<MangaDetailViewModel>()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    // Sửa ở đây: thêm .value
    when (val currentState = uiState.value){ // Dùng `val currentState` để code dễ đọc hơn
        is MangaDetailUiState.Loading -> LoadingState()
        // dùng currentState đã được gán giá trị
        is MangaDetailUiState.Success -> SuccessState(manga = currentState.manga)
        // dùng currentState đã được gán giá trị
        is MangaDetailUiState.Error -> ErrorState(message = currentState.message)
    }
}

@Composable
fun LoadingState(){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun SuccessState(manga: MangaModels.MangaData){
    val coverFileName = manga.coverFileName
    val imageUrl: String? =
        if (
            coverFileName != null
        ){
            "https://uploads.mangadex.org/covers/${manga.id}/${coverFileName}.256.jpg"
        } else{
            null
        }
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        AsyncImage(
            imageUrl,
            contentDescription = "Manga Cover",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(3f / 4f)
        )
        Text(
            text = manga.attributes.title["en"] ?: manga.attributes.title.values.firstOrNull() ?: "No Title",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, top = 12.dp, end = 15.dp)
        )

        Text(
            text = manga.attributes.description["en"] ?: manga.attributes.description.values.firstOrNull() ?: "No Description",
            fontSize = 16.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
        )

        Log.d("DetailDebug", "Title: ${manga.attributes.title["en"]}")
        Log.d("DetailDebug", "Description: ${manga.attributes.description["en"]}")

    }
}

@Composable
fun ErrorState(message: String){
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