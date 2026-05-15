package com.kaushalya.app.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.kaushalya.app.data.database.AppDatabase
import com.kaushalya.app.data.repository.ReviewRepository
import com.kaushalya.app.data.repository.WorkerRepository
import com.kaushalya.app.databinding.FragmentSearchBinding
import com.kaushalya.app.ui.activities.WorkerDetailActivity
import com.kaushalya.app.ui.adapters.WorkerAdapter
import com.kaushalya.app.ui.viewmodel.ViewModelFactory
import com.kaushalya.app.ui.viewmodel.WorkerViewModel
import com.kaushalya.app.utils.hide
import com.kaushalya.app.utils.show

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val workerViewModel: WorkerViewModel by viewModels {
        val db = AppDatabase.getDatabase(requireContext())
        ViewModelFactory(
            workerRepository = WorkerRepository(db.workerDao()),
            reviewRepository = ReviewRepository(db.reviewDao())
        )
    }

    private lateinit var searchAdapter: WorkerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchAdapter = WorkerAdapter(
            onWorkerClick = { worker ->
                val intent = Intent(requireContext(), WorkerDetailActivity::class.java).apply {
                    putExtra("worker_id", worker.id)
                }
                startActivity(intent)
            },
            onFavoriteClick = { worker ->
                workerViewModel.toggleFavorite(worker.id, worker.isFavorite)
            }
        )

        binding.rvSearchResults.adapter = searchAdapter

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { performSearch(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrBlank()) {
                    binding.tvSearchHint.show()
                    binding.rvSearchResults.hide()
                } else {
                    performSearch(newText)
                }
                return true
            }
        })
    }

    private fun performSearch(query: String) {
        workerViewModel.searchWorkers(query)
        workerViewModel.filteredWorkers.observe(viewLifecycleOwner) { workers ->
            searchAdapter.submitList(workers)
            binding.tvSearchHint.hide()
            binding.rvSearchResults.show()
            if (workers.isEmpty()) {
                binding.tvNoResults.show()
            } else {
                binding.tvNoResults.hide()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
