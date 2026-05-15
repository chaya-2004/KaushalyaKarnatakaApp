package com.kaushalya.app.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.kaushalya.app.MainActivity
import com.kaushalya.app.data.database.AppDatabase
import com.kaushalya.app.data.repository.UserRepository
import com.kaushalya.app.databinding.ActivitySignupBinding
import com.kaushalya.app.ui.viewmodel.AuthViewModel
import com.kaushalya.app.ui.viewmodel.ViewModelFactory
import com.kaushalya.app.utils.*

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var sessionManager: SessionManager

    private val authViewModel: AuthViewModel by viewModels {
        val db = AppDatabase.getDatabase(this)
        ViewModelFactory(userRepository = UserRepository(db.userDao()))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        setupClickListeners()
        observeViewModel()
    }

    private fun setupClickListeners() {
        binding.btnSignup.setOnClickListener {
            val name = binding.etFullName.text.toString().trim()
            val phone = binding.etPhone.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val confirmPassword = binding.etConfirmPassword.text.toString().trim()
            val userType = if (binding.rbWorker.isChecked) "Worker" else "Customer"

            if (!validateInputs(name, phone, email, password, confirmPassword)) return@setOnClickListener

            authViewModel.register(name, phone, email, password, userType)
        }

        binding.tvLogin.setOnClickListener {
            finish()
        }
    }

    private fun validateInputs(
        name: String, phone: String, email: String,
        password: String, confirmPassword: String
    ): Boolean {
        var isValid = true

        if (name.isEmpty()) {
            binding.tilFullName.error = "Enter your full name"
            isValid = false
        } else binding.tilFullName.error = null

        if (!phone.isValidPhone()) {
            binding.tilPhone.error = "Enter a valid 10-digit phone number"
            isValid = false
        } else binding.tilPhone.error = null

        if (!email.isValidEmail()) {
            binding.tilEmail.error = "Enter a valid email"
            isValid = false
        } else binding.tilEmail.error = null

        if (!password.isValidPassword()) {
            binding.tilPassword.error = "Password must be at least 6 characters"
            isValid = false
        } else binding.tilPassword.error = null

        if (password != confirmPassword) {
            binding.tilConfirmPassword.error = "Passwords do not match"
            isValid = false
        } else binding.tilConfirmPassword.error = null

        return isValid
    }

    private fun observeViewModel() {
        authViewModel.authState.observe(this) { state ->
            when (state) {
                is AuthViewModel.AuthState.Loading -> {
                    binding.progressBar.show()
                    binding.btnSignup.isEnabled = false
                }
                is AuthViewModel.AuthState.RegisterSuccess -> {
                    binding.progressBar.hide()
                    binding.btnSignup.isEnabled = true
                    sessionManager.saveLoginSession(
                        state.userId,
                        binding.etFullName.text.toString().trim(),
                        binding.etEmail.text.toString().trim(),
                        state.userType
                    )
                    binding.root.showSnackbar("Account created successfully!")
                    startActivity(Intent(this, MainActivity::class.java))
                    finishAffinity()
                }
                is AuthViewModel.AuthState.Error -> {
                    binding.progressBar.hide()
                    binding.btnSignup.isEnabled = true
                    binding.root.showSnackbar(state.message)
                }
                else -> {
                    binding.progressBar.hide()
                    binding.btnSignup.isEnabled = true
                }
            }
        }
    }
}
