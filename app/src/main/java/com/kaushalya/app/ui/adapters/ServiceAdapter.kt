package com.kaushalya.app.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kaushalya.app.data.entities.ServiceEntity
import com.kaushalya.app.databinding.ItemServiceBinding
import java.text.NumberFormat
import java.util.Locale

class ServiceAdapter(
    private val showActions: Boolean = false,
    private val onEditClick: ((ServiceEntity) -> Unit)? = null,
    private val onDeleteClick: ((ServiceEntity) -> Unit)? = null
) : ListAdapter<ServiceEntity, ServiceAdapter.ServiceViewHolder>(ServiceDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val binding = ItemServiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ServiceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ServiceViewHolder(private val binding: ItemServiceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(service: ServiceEntity) {
            binding.apply {
                tvServiceName.text = service.serviceName
                tvDescription.text = service.description

                val fmt = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
                val priceText = "${service.priceType}: ${fmt.format(service.price)}"
                tvPrice.text = priceText

                if (showActions) {
                    btnEdit.visibility = android.view.View.VISIBLE
                    btnDelete.visibility = android.view.View.VISIBLE
                    btnEdit.setOnClickListener { onEditClick?.invoke(service) }
                    btnDelete.setOnClickListener { onDeleteClick?.invoke(service) }
                } else {
                    btnEdit.visibility = android.view.View.GONE
                    btnDelete.visibility = android.view.View.GONE
                }
            }
        }
    }

    class ServiceDiffCallback : DiffUtil.ItemCallback<ServiceEntity>() {
        override fun areItemsTheSame(oldItem: ServiceEntity, newItem: ServiceEntity) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ServiceEntity, newItem: ServiceEntity) =
            oldItem == newItem
    }
}
