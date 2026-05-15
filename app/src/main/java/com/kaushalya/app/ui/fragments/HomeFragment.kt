package com.kaushalya.app.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.kaushalya.app.R
import com.kaushalya.app.data.database.AppDatabase
import com.kaushalya.app.data.entities.WorkerEntity
import com.kaushalya.app.data.repository.ReviewRepository
import com.kaushalya.app.data.repository.WorkerRepository
import com.kaushalya.app.databinding.FragmentHomeBinding
import com.kaushalya.app.ui.activities.WorkerDetailActivity
import com.kaushalya.app.ui.adapters.WorkerAdapter
import com.kaushalya.app.ui.viewmodel.ViewModelFactory
import com.kaushalya.app.ui.viewmodel.WorkerViewModel
import com.kaushalya.app.utils.Constants
import com.kaushalya.app.utils.SessionManager
import com.kaushalya.app.utils.hide
import com.kaushalya.app.utils.show

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val workerViewModel: WorkerViewModel by viewModels {
        val db = AppDatabase.getDatabase(requireContext())
        ViewModelFactory(
            workerRepository = WorkerRepository(db.workerDao()),
            reviewRepository = ReviewRepository(db.reviewDao())
        )
    }

    private lateinit var workerAdapter: WorkerAdapter
    private lateinit var topRatedAdapter: WorkerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapters()
        setupCategoryChips()
        observeData()

        val session = SessionManager(requireContext())
        binding.tvGreeting.text = "Hello, ${session.getUserName().split(" ").first()} 👋"

        binding.fabAddWorker.visibility = if (session.isWorker()) View.VISIBLE else View.GONE
        binding.fabAddWorker.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addEditWorkerActivity)
        }
    }

    private fun setupAdapters() {
        workerAdapter = WorkerAdapter(
            onWorkerClick = { openWorkerDetail(it) },
            onFavoriteClick = { workerViewModel.toggleFavorite(it.id, it.isFavorite) }
        )
        topRatedAdapter = WorkerAdapter(
            onWorkerClick = { openWorkerDetail(it) },
            onFavoriteClick = { workerViewModel.toggleFavorite(it.id, it.isFavorite) }
        )
        binding.rvWorkers.adapter = workerAdapter
        binding.rvTopRated.adapter = topRatedAdapter
    }

    private fun setupCategoryChips() {
        Constants.WORKER_CATEGORIES.forEach { category ->
            val chip = Chip(requireContext()).apply {
                text = category
                isCheckable = true
                if (category == "All") isChecked = true
            }
            chip.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    if (category == "All") {
                        observeAllWorkers()
                    } else {
                        workerViewModel.filterByCategory(category)
                        workerViewModel.filteredWorkers.observe(viewLifecycleOwner) { workers ->
                            updateWorkerList(workers)
                        }
                    }
                }
            }
            binding.chipGroupCategories.addView(chip)
        }
    }

    private fun observeData() {
        observeAllWorkers()
        workerViewModel.topRatedWorkers.observe(viewLifecycleOwner) { workers ->
            topRatedAdapter.submitList(workers)
        }
    }

    private fun observeAllWorkers() {
        workerViewModel.allWorkers.observe(viewLifecycleOwner) { workers ->
            updateWorkerList(workers)
        }
    }

    private fun updateWorkerList(workers: List<WorkerEntity>) {
        workerAdapter.submitList(workers)
        if (workers.isEmpty()) {
            binding.tvEmptyState.show()
            binding.rvWorkers.hide()
        } else {
            binding.tvEmptyState.hide()
            binding.rvWorkers.show()
        }
    }

    private fun openWorkerDetail(worker: WorkerEntity) {
        val intent = Intent(requireContext(), WorkerDetailActivity::class.java).apply {
            putExtra("worker_id", worker.id)
        }
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
