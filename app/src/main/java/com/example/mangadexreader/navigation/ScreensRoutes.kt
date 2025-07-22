package com.example.mangadexreader.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

object ScreenRoutes {
    const val MangaList = "manga_list_screen"
    const val MangaDetail = "manga_detail_screen/{mangaId}"
    const val MangaReader = "manga_reader_screen/{chapterId}"
}

sealed class BottomNavItem(val title: String, val icon: ImageVector, val route: String) {
    object Home : BottomNavItem("Trang chủ", Icons.Default.Home, ScreenRoutes.MangaList)
    object Bookmark : BottomNavItem("Bookmarks", Icons.Default.Bookmark, "bookmark_screen")
    object Profile : BottomNavItem("Hồ sơ", Icons.Default.Person, "profile_screen")

}