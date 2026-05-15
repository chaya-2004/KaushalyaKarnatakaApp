package com.kaushalya.app.ui.activities

import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.kaushalya.app.R
import com.kaushalya.app.data.database.AppDatabase
import com.kaushalya.app.data.entities.WorkerEntity
import com.kaushalya.app.data.repository.ReviewRepository
import com.kaushalya.app.data.repository.WorkerRepository
import com.kaushalya.app.databinding.ActivityAddEditWorkerBinding
import com.kaushalya.app.ui.viewmodel.ViewModelFactory
import com.kaushalya.app.ui.viewmodel.WorkerViewModel
import com.kaushalya.app.utils.Constants
import com.kaushalya.app.utils.SessionManager
import com.kaushalya.app.utils.showSnackbar
import kotlinx.coroutines.launch

class AddEditWorkerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditWorkerBinding
    private var workerId: Int = -1
    private var isEdit: Boolean = false
    private var selectedImageUri: String = ""
    private lateinit var session: SessionManager

    private val workerViewModel: WorkerViewModel by viewModels {
        val db = AppDatabase.getDatabase(this)
        ViewModelFactory(
            workerRepository = WorkerRepository(db.workerDao()),
            reviewRepository = ReviewRepository(db.reviewDao())
        )
    }

    // Image Picker using Activity Result API
    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it.toString()
            Glide.with(this).load(it).circleCrop().into(binding.ivProfileImage)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditWorkerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        session = SessionManager(this)
        workerId = intent.getIntExtra("worker_id", -1)
        isEdit = intent.getBooleanExtra("is_edit", false)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = if (isEdit) "Edit Profile" else "Create Profile"

        setupCategoryDropdown()

        if (isEdit && workerId != -1) {
            loadExistingData()
        }

        binding.ivProfileImage.setOnClickListener {
            imagePickerLauncher.launch("image/*")
        }

        binding.btnSave.setOnClickListener {
            saveWorker()
        }
    }

    private fun setupCategoryDropdown() {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            Constants.WORKER_CATEGORIES.drop(1) // Remove "All"
        )
        binding.actvCategory.setAdapter(adapter)
    }

    private fun loadExistingData() {
        lifecycleScope.launch {
            val worker = AppDatabase.getDatabase(this@AddEditWorkerActivity)
                .workerDao().getWorkerById(workerId)
            worker?.let { w ->
                binding.etName.setText(w.name)
                binding.etPhone.setText(w.phone)
                binding.actvCategory.setText(w.category, false)
                binding.etAddress.setText(w.address)
                binding.etExperience.setText(w.experience.toString())
                binding.etAboutMe.setText(w.aboutMe)
                selectedImageUri = w.profileImageUri

                if (w.profileImageUri.isNotEmpty()) {
                    Glide.with(this@AddEditWorkerActivity)
                        .load(w.profileImageUri)
                        .circleCrop()
                        .into(binding.ivProfileImage)
                }
            }
        }
    }

    private fun saveWorker() {
        val name = binding.etName.text.toString().trim()
        val phone = binding.etPhone.text.toString().trim()
        val category = binding.actvCategory.text.toString().trim()
        val address = binding.etAddress.text.toString().trim()
        val experienceStr = binding.etExperience.text.toString().trim()
        val aboutMe = binding.etAboutMe.text.toString().trim()

        // Validation
        if (name.isEmpty()) { binding.tilName.error = "Required"; return }
        if (phone.length != 10) { binding.tilPhone.error = "Enter valid 10-digit number"; return }
        if (category.isEmpty()) { binding.tilCategory.error = "Select a category"; return }
        if (address.isEmpty()) { binding.tilAddress.error = "Required"; return }

        val experience = experienceStr.toIntOrNull() ?: 0

        if (isEdit && workerId != -1) {
            lifecycleScope.launch {
                val existing = AppDatabase.getDatabase(this@AddEditWorkerActivity)
                    .workerDao().getWorkerById(workerId)
                existing?.let { w ->
                    val updated = w.copy(
                        name = name, phone = phone, category = category,
                        address = address, experience = experience, aboutMe = aboutMe,
                        profileImageUri = selectedImageUri.ifEmpty { w.profileImageUri }
                    )
                    workerViewModel.updateWorker(updated)
                }
            }
        } else {
            val worker = WorkerEntity(
                userId = session.getUserId(),
                name = name, phone = phone, category = category,
                address = address, experience = experience, aboutMe = aboutMe,
                profileImageUri = selectedImageUri
            )
            workerViewModel.insertWorker(worker)
        }

        workerViewModel.operationResult.observe(this) { result ->
            when (result) {
                is WorkerViewModel.OperationResult.Success -> {
                    if (!isEdit) session.saveWorkerId(result.id)
                    binding.root.showSnackbar(result.message)
                    finish()
                }
                else -> {}
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
