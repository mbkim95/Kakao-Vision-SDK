package com.example.kakaovisionsdk

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kakaovisionsdk.databinding.ItemApiBinding
import com.example.kakaovisionsdk.databinding.ItemHeaderBinding

class ApiAdapter(private val items: List<Item>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            API_HEADER -> HeaderHolder(ItemHeaderBinding.inflate(inflater, parent, false))
            API_ITEM -> ApiHolder(ItemApiBinding.inflate(inflater, parent, false))
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        when (holder) {
            is HeaderHolder -> {
                holder.bind(item as Item.Header)
            }
            is ApiHolder -> {
                holder.bind(item as Item.ApiItem)
            }
        }
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int = when (items[position]) {
        is Item.Header -> API_HEADER
        is Item.ApiItem -> API_ITEM
    }

    class HeaderHolder(private val binding: ItemHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Item.Header) {
            binding.title.text = item.title
        }
    }

    class ApiHolder(private val binding: ItemApiBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Item.ApiItem) {
            binding.title.text = item.label
            binding.root.setOnClickListener { item.apiFunction() }
        }
    }

    companion object {
        const val API_HEADER = 0
        const val API_ITEM = 1
    }
}

sealed class Item {
    data class ApiItem(val label: String, val apiFunction: () -> Unit) : Item()
    data class Header(val title: String) : Item()
}