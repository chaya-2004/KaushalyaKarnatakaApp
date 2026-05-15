package com.kaushalya.app.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaushalya.app.data.entities.UserEntity
import com.kaushalya.app.data.repository.UserRepository
import kotlinx.coroutines.launch

class AuthViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    fun register(fullName: String, phone: String, email: String, password: String, userType: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val user = UserEntity(
                fullName = fullName,
                phone = phone,
                email = email,
                password = password,
                userType = userType
            )
            val result = userRepository.registerUser(user)
            if (result.isSuccess) {
                _authState.value = AuthState.RegisterSuccess(result.getOrNull()?.toInt() ?: 0, userType)
            } else {
                _authState.value = AuthState.Error(result.exceptionOrNull()?.message ?: "Registration failed")
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val user = userRepository.loginUser(email, password)
            if (user != null) {
                _authState.value = AuthState.LoginSuccess(user)
            } else {
                _authState.value = AuthState.Error("Invalid email or password")
            }
        }
    }

    sealed class AuthState {
        object Loading : AuthState()
        data class LoginSuccess(val user: UserEntity) : AuthState()
        data class RegisterSuccess(val userId: Int, val userType: String) : AuthState()
        data class Error(val message: String) : AuthState()
    }
}
