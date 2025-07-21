package com.example.mangadexreader.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.mangadexreader.navigation.ScreenRoutes
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun ProfileScreen(navController: NavController) {
    // Lấy thông tin người dùng hiện tại từ Firebase
    val user = Firebase.auth.currentUser

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (user != null) {
            // Hiển thị ảnh đại diện
            AsyncImage(
                model = user.photoUrl,
                contentDescription = "Ảnh đại diện",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Hiển thị tên và email
            Text(text = user.displayName ?: "Không có tên")
            Text(text = user.email ?: "Không có email")
            Spacer(modifier = Modifier.height(32.dp))

            // Nút Đăng xuất
            Button(onClick = {
                Firebase.auth.signOut()
                // Điều hướng về màn hình đăng nhập và xóa hết lịch sử cũ
                navController.navigate("sign_in") {
                    popUpTo(0)
                }
            }) {
                Text("Đăng xuất")
            }
        } else {
            // Trường hợp không có người dùng (dù hiếm khi xảy ra ở màn hình này)
            Text("Không có thông tin người dùng.")
        }
    }
}