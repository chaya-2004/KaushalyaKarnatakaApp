package com.kaushalya.app.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kaushalya.app.R
import com.kaushalya.app.data.entities.PortfolioImageEntity
import com.kaushalya.app.databinding.ItemPortfolioBinding

class PortfolioAdapter(
    private val showDelete: Boolean = false,
    private val onDeleteClick: ((PortfolioImageEntity) -> Unit)? = null,
    private val onImageClick: ((PortfolioImageEntity) -> Unit)? = null
) : ListAdapter<PortfolioImageEntity, PortfolioAdapter.PortfolioViewHolder>(PortfolioDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PortfolioViewHolder {
        val binding = ItemPortfolioBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PortfolioViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PortfolioViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PortfolioViewHolder(private val binding: ItemPortfolioBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(image: PortfolioImageEntity) {
            binding.apply {
                Glide.with(ivPortfolio.context)
                    .load(image.imageUri)
                    .placeholder(R.drawable.ic_image_placeholder)
                    .centerCrop()
                    .into(ivPortfolio)

                if (showDelete) {
                    btnDelete.visibility = android.view.View.VISIBLE
                    btnDelete.setOnClickListener { onDeleteClick?.invoke(image) }
                } else {
                    btnDelete.visibility = android.view.View.GONE
                }

                ivPortfolio.setOnClickListener { onImageClick?.invoke(image) }
            }
        }
    }

    class PortfolioDiffCallback : DiffUtil.ItemCallback<PortfolioImageEntity>() {
        override fun areItemsTheSame(oldItem: PortfolioImageEntity, newItem: PortfolioImageEntity) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: PortfolioImageEntity, newItem: PortfolioImageEntity) =
            oldItem == newItem
    }
}
