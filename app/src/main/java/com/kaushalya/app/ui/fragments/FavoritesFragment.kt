package com.kaushalya.app.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.kaushalya.app.data.database.AppDatabase
import com.kaushalya.app.data.repository.ReviewRepository
import com.kaushalya.app.data.repository.WorkerRepository
import com.kaushalya.app.databinding.FragmentFavoritesBinding
import com.kaushalya.app.ui.activities.WorkerDetailActivity
import com.kaushalya.app.ui.adapters.WorkerAdapter
import com.kaushalya.app.ui.viewmodel.ViewModelFactory
import com.kaushalya.app.ui.viewmodel.WorkerViewModel
import com.kaushalya.app.utils.hide
import com.kaushalya.app.utils.show

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private val workerViewModel: WorkerViewModel by viewModels {
        val db = AppDatabase.getDatabase(requireContext())
        ViewModelFactory(
            workerRepository = WorkerRepository(db.workerDao()),
            reviewRepository = ReviewRepository(db.reviewDao())
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = WorkerAdapter(
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

        binding.rvFavorites.adapter = adapter

        workerViewModel.favoriteWorkers.observe(viewLifecycleOwner) { workers ->
            adapter.submitList(workers)
            if (workers.isEmpty()) {
                binding.tvEmpty.show()
                binding.rvFavorites.hide()
            } else {
                binding.tvEmpty.hide()
                binding.rvFavorites.show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
