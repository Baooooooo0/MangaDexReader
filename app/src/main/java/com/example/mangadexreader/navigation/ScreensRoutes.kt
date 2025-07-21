package com.example.mangadexreader.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

object ScreenRoutes {
    const val MangaList = "manga_list_screen"
    const val MangaDetail = "manga_detail_screen/{mangaId}"
    const val MangaReader = "manga_reader_screen/{chapterId}"
}

sealed class BottomNavItem(val title: String, val icon: ImageVector, val route: String) {
    object Home : BottomNavItem("Trang chủ", Icons.Default.Home, ScreenRoutes.MangaList)
    object Favorite : BottomNavItem("Yêu thích", Icons.Default.Favorite, "favorite_screen")
}