package com.kaushalya.app.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kaushalya.app.data.entities.ReviewEntity
import com.kaushalya.app.databinding.ItemReviewBinding
import java.text.SimpleDateFormat
import java.util.*

class ReviewAdapter(
    private val onDeleteClick: ((ReviewEntity) -> Unit)? = null
) : ListAdapter<ReviewEntity, ReviewAdapter.ReviewViewHolder>(ReviewDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ReviewViewHolder(private val binding: ItemReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(review: ReviewEntity) {
            binding.apply {
                tvReviewerName.text = review.reviewerName
                tvComment.text = review.comment
                ratingBar.rating = review.rating

                val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                tvDate.text = sdf.format(Date(review.createdAt))

                if (onDeleteClick != null) {
                    btnDeleteReview.visibility = android.view.View.VISIBLE
                    btnDeleteReview.setOnClickListener { onDeleteClick.invoke(review) }
                } else {
                    btnDeleteReview.visibility = android.view.View.GONE
                }
            }
        }
    }

    class ReviewDiffCallback : DiffUtil.ItemCallback<ReviewEntity>() {
        override fun areItemsTheSame(oldItem: ReviewEntity, newItem: ReviewEntity) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ReviewEntity, newItem: ReviewEntity) =
            oldItem == newItem
    }
}
