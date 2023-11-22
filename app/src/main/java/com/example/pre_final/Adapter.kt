package com.example.pre_final

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ItemAdapter : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    private var items: List<Item> = emptyList()
    private var onItemClick: ((Item) -> Unit)? = null

    fun setItems(items: List<Item>) {
        this.items = items
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: (Item) -> Unit) {
        onItemClick = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemNamed: TextView = itemView.findViewById(R.id.itemNamed)
        private val itemDesc: TextView = itemView.findViewById(R.id.itemDesc)
        private val itemPrice: TextView = itemView.findViewById(R.id.itemPrice)
        private val itemPhoto: ImageView = itemView.findViewById(R.id.itemPhoto)
        private val viewButton: Button = itemView.findViewById(R.id.viewButton)

        init {
            // Set click listener for the entire card
            itemView.setOnClickListener {
                onItemClick?.invoke(items[adapterPosition])
            }

            // Set click listener for the "View" button
            viewButton.setOnClickListener {
                onItemClick?.invoke(items[adapterPosition])
            }
        }

        fun bind(item: Item) {
            itemNamed.text = "${item.name}"
            itemDesc.text = "${item.description}"
            itemPrice.text = "${item.price} Php"

            if (!item.url.isNullOrBlank()) {
                Glide.with(itemView.context)
                    .load(item.url)
                    .into(itemPhoto)
            }
        }
    }
}

