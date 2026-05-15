package com.kaushalya.app.ui.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.snackbar.Snackbar
import com.kaushalya.app.R
import com.kaushalya.app.data.database.AppDatabase
import com.kaushalya.app.data.repository.*
import com.kaushalya.app.databinding.ActivityWorkerDetailBinding
import com.kaushalya.app.ui.adapters.PortfolioAdapter
import com.kaushalya.app.ui.adapters.ReviewAdapter
import com.kaushalya.app.ui.adapters.ServiceAdapter
import com.kaushalya.app.ui.viewmodel.*
import com.kaushalya.app.utils.SessionManager
import com.kaushalya.app.utils.hide
import com.kaushalya.app.utils.show
import kotlinx.coroutines.launch

class WorkerDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWorkerDetailBinding
    private var workerId: Int = -1
    private var isOwner: Boolean = false
    private lateinit var session: SessionManager

    private val workerViewModel: WorkerViewModel by viewModels {
        val db = AppDatabase.getDatabase(this)
        ViewModelFactory(
            workerRepository = WorkerRepository(db.workerDao()),
            reviewRepository = ReviewRepository(db.reviewDao())
        )
    }
    private val serviceViewModel: ServiceViewModel by viewModels {
        ViewModelFactory(serviceRepository = ServiceRepository(AppDatabase.getDatabase(this).serviceDao()))
    }
    private val reviewViewModel: ReviewViewModel by viewModels {
        val db = AppDatabase.getDatabase(this)
        ViewModelFactory(
            reviewRepository = ReviewRepository(db.reviewDao()),
            workerRepository = WorkerRepository(db.workerDao())
        )
    }
    private val portfolioViewModel: PortfolioViewModel by viewModels {
        ViewModelFactory(portfolioRepository = PortfolioRepository(AppDatabase.getDatabase(this).portfolioDao()))
    }

    // Image picker launcher for portfolio
    private val portfolioLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri: Uri = result.data?.data ?: return@registerForActivityResult
            portfolioViewModel.addImage(workerId, uri.toString())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkerDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        session = SessionManager(this)
        workerId = intent.getIntExtra("worker_id", -1)
        isOwner = intent.getBooleanExtra("is_owner", false)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (workerId == -1) { finish(); return }

        setupAdapters()
        loadWorkerData()
        observeData()
        setupClickListeners()
    }

    private fun setupAdapters() {
        binding.rvServices.adapter = ServiceAdapter(
            showActions = isOwner,
            onEditClick = { service ->
                val intent = Intent(this, AddEditServiceActivity::class.java).apply {
                    putExtra("worker_id", workerId)
                    putExtra("service_id", service.id)
                    putExtra("is_edit", true)
                }
                startActivity(intent)
            },
            onDeleteClick = { service ->
                AlertDialog.Builder(this)
                    .setTitle("Delete Service")
                    .setMessage("Are you sure you want to delete this service?")
                    .setPositiveButton("Delete") { _, _ ->
                        serviceViewModel.deleteService(service)
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            }
        )
        binding.rvReviews.adapter = ReviewAdapter()
        binding.rvPortfolio.adapter = PortfolioAdapter(showDelete = isOwner)
    }

    private fun loadWorkerData() {
        serviceViewModel.loadServicesForWorker(workerId)
        reviewViewModel.loadReviewsForWorker(workerId)
        portfolioViewModel.loadImagesForWorker(workerId)
    }

    private fun observeData() {
        lifecycleScope.launch {
            val worker = AppDatabase.getDatabase(this@WorkerDetailActivity).workerDao().getWorkerById(workerId)
            worker?.let { w ->
                binding.tvName.text = w.name
                binding.tvCategory.text = w.category
                binding.tvAddress.text = w.address
                binding.tvExperience.text = "${w.experience} years of experience"
                binding.tvAbout.text = w.aboutMe
                binding.tvPhone.text = w.phone
                binding.ratingBar.rating = w.averageRating
                binding.tvRatingCount.text = "${w.averageRating} (${w.totalReviews} reviews)"
                supportActionBar?.title = w.name

                if (w.profileImageUri.isNotEmpty()) {
                    Glide.with(this@WorkerDetailActivity)
                        .load(w.profileImageUri)
                        .placeholder(R.drawable.ic_person_placeholder)
                        .circleCrop()
                        .into(binding.ivProfile)
                }

                // Check if user is the owner
                val currentUserId = session.getUserId()
                if (isOwner || currentUserId == w.userId) {
                    binding.btnEdit.show()
                    binding.btnDelete.show()
                    binding.btnAddService.show()
                    binding.btnAddPortfolio.show()
                }

                // Phone & WhatsApp
                binding.btnCall.setOnClickListener {
                    startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:${w.phone}")))
                }
                binding.btnWhatsapp.setOnClickListener {
                    val url = "https://wa.me/91${w.phone}"
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                }
                binding.btnMaps.setOnClickListener {
                    val geoUri = Uri.parse("geo:0,0?q=${Uri.encode(w.address)}")
                    startActivity(Intent(Intent.ACTION_VIEW, geoUri))
                }

                binding.btnHireMe.setOnClickListener {
                    AlertDialog.Builder(this@WorkerDetailActivity)
                        .setTitle("Hire ${w.name}")
                        .setMessage("Do you want to request a call from ${w.name}? They will be notified immediately.")
                        .setPositiveButton("Request Call") { _, _ ->
                            Snackbar.make(binding.root, "✅ Notification sent to ${w.name}!", Snackbar.LENGTH_LONG).show()
                        }
                        .setNegativeButton("Cancel", null)
                        .show()
                }
            }
        }

        serviceViewModel.services.observe(this) { services ->
            (binding.rvServices.adapter as ServiceAdapter).submitList(services)
        }

        reviewViewModel.reviews.observe(this) { reviews ->
            (binding.rvReviews.adapter as ReviewAdapter).submitList(reviews)
        }

        portfolioViewModel.portfolioImages.observe(this) { images ->
            (binding.rvPortfolio.adapter as PortfolioAdapter).submitList(images)
            if (images.isEmpty()) binding.tvNoPortfolio.show() else binding.tvNoPortfolio.hide()
        }

        portfolioViewModel.operationResult.observe(this) { msg ->
            Snackbar.make(binding.root, msg, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun setupClickListeners() {
        binding.btnEdit.setOnClickListener {
            val intent = Intent(this, AddEditWorkerActivity::class.java).apply {
                putExtra("worker_id", workerId)
                putExtra("is_edit", true)
            }
            startActivity(intent)
        }

        binding.btnDelete.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Delete Profile")
                .setMessage("Are you sure you want to delete your worker profile?")
                .setPositiveButton("Delete") { _, _ ->
                    lifecycleScope.launch {
                        val worker = AppDatabase.getDatabase(this@WorkerDetailActivity)
                            .workerDao().getWorkerById(workerId)
                        worker?.let { workerViewModel.deleteWorker(it) }
                        finish()
                    }
                }
                .setNegativeButton("Cancel", null)
                .show()
        }

        binding.btnAddService.setOnClickListener {
            val intent = Intent(this, AddEditServiceActivity::class.java).apply {
                putExtra("worker_id", workerId)
            }
            startActivity(intent)
        }

        binding.btnAddPortfolio.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .createIntent { intent ->
                    portfolioLauncher.launch(intent)
                }
        }

        binding.btnAddReview.setOnClickListener {
            showReviewDialog()
        }
    }

    private fun showReviewDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_review, null)
        val nameInput = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etReviewerName)
        val commentInput = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etComment)
        val ratingBar = dialogView.findViewById<android.widget.RatingBar>(R.id.ratingBarInput)

        AlertDialog.Builder(this)
            .setTitle("Add Review")
            .setView(dialogView)
            .setPositiveButton("Submit") { _, _ ->
                val name = nameInput.text.toString().trim().ifEmpty { "Anonymous" }
                val comment = commentInput.text.toString().trim()
                val rating = ratingBar.rating
                reviewViewModel.submitReview(workerId, name, rating, comment)
            }
            .setNegativeButton("Cancel", null)
            .show()

        reviewViewModel.operationResult.observe(this) { msg ->
            Snackbar.make(binding.root, msg, Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
