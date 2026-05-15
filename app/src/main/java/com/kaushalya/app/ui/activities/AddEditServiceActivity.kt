package com.kaushalya.app.ui.activities

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.kaushalya.app.data.database.AppDatabase
import com.kaushalya.app.data.entities.ServiceEntity
import com.kaushalya.app.data.repository.ServiceRepository
import com.kaushalya.app.databinding.ActivityAddEditServiceBinding
import com.kaushalya.app.ui.viewmodel.ServiceViewModel
import com.kaushalya.app.ui.viewmodel.ViewModelFactory
import com.kaushalya.app.utils.Constants
import com.kaushalya.app.utils.showSnackbar

class AddEditServiceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditServiceBinding
    private var workerId: Int = -1

    private val serviceViewModel: ServiceViewModel by viewModels {
        ViewModelFactory(serviceRepository = ServiceRepository(AppDatabase.getDatabase(this).serviceDao()))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        workerId = intent.getIntExtra("worker_id", -1)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Add Service"

        // Price type dropdown
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, Constants.PRICE_TYPES)
        binding.actvPriceType.setAdapter(adapter)
        binding.actvPriceType.setText(Constants.PRICE_TYPES[0], false)

        binding.btnSaveService.setOnClickListener {
            saveService()
        }

        serviceViewModel.operationResult.observe(this) { msg ->
            binding.root.showSnackbar(msg)
            finish()
        }
    }

    private fun saveService() {
        val name = binding.etServiceName.text.toString().trim()
        val description = binding.etDescription.text.toString().trim()
        val priceStr = binding.etPrice.text.toString().trim()
        val priceType = binding.actvPriceType.text.toString().trim()

        if (name.isEmpty()) { binding.tilServiceName.error = "Required"; return }
        if (priceStr.isEmpty()) { binding.tilPrice.error = "Required"; return }

        val price = priceStr.toDoubleOrNull() ?: 0.0

        val service = ServiceEntity(
            workerId = workerId,
            serviceName = name,
            description = description,
            price = price,
            priceType = priceType
        )
        serviceViewModel.insertService(service)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
