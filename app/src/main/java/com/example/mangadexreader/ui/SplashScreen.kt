package com.example.mangadexreader.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.mangadexreader.navigation.ScreenRoutes
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun SplashScreen(navController: NavController) {
    // Luôn kiểm tra trạng thái đăng nhập trong một LaunchedEffect
    // key1 = true đảm bảo nó chỉ chạy một lần khi Composable xuất hiện
    LaunchedEffect(key1 = true) {
        // Hỏi Firebase xem người dùng hiện tại là ai
        val currentUser = Firebase.auth.currentUser

        // Quyết định đường đi
        val destination = if (currentUser != null) {
            // Nếu đã có người dùng, đi đến màn hình danh sách chính
            ScreenRoutes.MangaList
        } else {
            // Nếu chưa, đi đến màn hình đăng nhập
            "sign_in" // Sử dụng route bạn đã định nghĩa cho SignInScreen
        }

        // Thực hiện điều hướng và xóa màn hình chờ khỏi lịch sử
        navController.navigate(destination) {
            popUpTo(0) { inclusive = true }
        }
    }

    // Giao diện của màn hình chờ chỉ đơn giản là một vòng xoay
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}