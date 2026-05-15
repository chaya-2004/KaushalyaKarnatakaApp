package com.kaushalya.app.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.kaushalya.app.MainActivity
import com.kaushalya.app.data.database.AppDatabase
import com.kaushalya.app.data.repository.UserRepository
import com.kaushalya.app.databinding.ActivityLoginBinding
import com.kaushalya.app.ui.viewmodel.AuthViewModel
import com.kaushalya.app.ui.viewmodel.ViewModelFactory
import com.kaushalya.app.utils.SessionManager
import com.kaushalya.app.utils.hide
import com.kaushalya.app.utils.isValidEmail
import com.kaushalya.app.utils.show
import com.kaushalya.app.utils.showSnackbar

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var sessionManager: SessionManager

    private val authViewModel: AuthViewModel by viewModels {
        val db = AppDatabase.getDatabase(this)
        ViewModelFactory(userRepository = UserRepository(db.userDao()))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        setupClickListeners()
        observeViewModel()
    }

    private fun setupClickListeners() {
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (!validateInputs(email, password)) return@setOnClickListener

            authViewModel.login(email, password)
        }

        binding.tvSignup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }

    private fun validateInputs(email: String, password: String): Boolean {
        var isValid = true
        if (email.isEmpty() || !email.isValidEmail()) {
            binding.tilEmail.error = "Enter a valid email"
            isValid = false
        } else binding.tilEmail.error = null

        if (password.isEmpty() || password.length < 6) {
            binding.tilPassword.error = "Password must be at least 6 characters"
            isValid = false
        } else binding.tilPassword.error = null

        return isValid
    }

    private fun observeViewModel() {
        authViewModel.authState.observe(this) { state ->
            when (state) {
                is AuthViewModel.AuthState.Loading -> {
                    binding.progressBar.show()
                    binding.btnLogin.isEnabled = false
                }
                is AuthViewModel.AuthState.LoginSuccess -> {
                    binding.progressBar.hide()
                    binding.btnLogin.isEnabled = true
                    sessionManager.saveLoginSession(
                        state.user.id,
                        state.user.fullName,
                        state.user.email,
                        state.user.userType
                    )
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                is AuthViewModel.AuthState.Error -> {
                    binding.progressBar.hide()
                    binding.btnLogin.isEnabled = true
                    binding.root.showSnackbar(state.message)
                }
                else -> {
                    binding.progressBar.hide()
                    binding.btnLogin.isEnabled = true
                }
            }
        }
    }
}
