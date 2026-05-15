package com.kaushalya.app.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kaushalya.app.R
import com.kaushalya.app.data.entities.WorkerEntity
import com.kaushalya.app.databinding.ItemWorkerBinding

class WorkerAdapter(
    private val onWorkerClick: (WorkerEntity) -> Unit,
    private val onFavoriteClick: (WorkerEntity) -> Unit
) : ListAdapter<WorkerEntity, WorkerAdapter.WorkerViewHolder>(WorkerDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkerViewHolder {
        val binding = ItemWorkerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WorkerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WorkerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class WorkerViewHolder(private val binding: ItemWorkerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(worker: WorkerEntity) {
            binding.apply {
                tvWorkerName.text = worker.name
                tvAddress.text = worker.address
                tvExperience.text = "${worker.experience} yrs exp"
                ratingBar.rating = worker.averageRating
                tvRatingCount.text = "(${worker.totalReviews})"

                // Load profile image with Glide
                if (worker.profileImageUri.isNotEmpty()) {
                    Glide.with(ivProfile.context)
                        .load(worker.profileImageUri)
                        .placeholder(R.drawable.ic_person_placeholder)
                        .circleCrop()
                        .into(ivProfile)
                } else {
                    ivProfile.setImageResource(R.drawable.ic_person_placeholder)
                }

                // Favorite icon state
                ivFavorite.setImageResource(
                    if (worker.isFavorite) R.drawable.ic_favorite_filled
                    else R.drawable.ic_favorite_border
                )

                // Category chip color
                chipCategory.text = worker.category

                root.setOnClickListener { onWorkerClick(worker) }
                ivFavorite.setOnClickListener { onFavoriteClick(worker) }
            }
        }
    }

    class WorkerDiffCallback : DiffUtil.ItemCallback<WorkerEntity>() {
        override fun areItemsTheSame(oldItem: WorkerEntity, newItem: WorkerEntity) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: WorkerEntity, newItem: WorkerEntity) =
            oldItem == newItem
    }
}
