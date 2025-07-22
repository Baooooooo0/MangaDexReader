package com.example.mangadexreader.ui.authscreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.mangadexreader.data.TokenManager
import com.example.mangadexreader.repository.MangaRepository
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class SignInState(
    val isSigningIn: Boolean = false,
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null
)

class SignInViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = MangaRepository()
    private val tokenManager = TokenManager
    private val auth = Firebase.auth

    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    fun onSignInResult(credential: AuthCredential) {
        _state.update { it.copy(isSigningIn = true) } // Bắt đầu xử lý, hiện loading

        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _state.update { it.copy(isSignInSuccessful = true, isSigningIn = false) }
                } else {
                    _state.update { it.copy(signInError = task.exception?.message, isSigningIn = false) }
                }
            }
    }
}