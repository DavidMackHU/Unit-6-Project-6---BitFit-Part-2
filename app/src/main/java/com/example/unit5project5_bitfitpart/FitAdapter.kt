package com.example.unit5project5_bitfitpart

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class FitAdapter(
    private var items: List<BitFitEntity> = emptyList()
) : RecyclerView.Adapter<FitAdapter.ViewHolder>() {

    fun updateItems(newItems: List<BitFitEntity>) {
        items = newItems
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivItemPhoto: ImageView = view.findViewById(R.id.ivItemPhoto)
        val tvItemName: TextView = view.findViewById(R.id.tvItemName)
        val tvItemNotes: TextView = view.findViewById(R.id.tvItemNotes)
        val tvItemCalories: TextView = view.findViewById(R.id.tvItemCalories)
        val cardImageHolder: View = view.findViewById(R.id.cardImageHolder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_fit, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.tvItemName.text = item.name
        holder.tvItemCalories.text = "${item.calories} Calories"

        if (!item.notes.isNullOrEmpty()) {
            holder.tvItemNotes.text = item.notes
            holder.tvItemNotes.visibility = View.VISIBLE
        } else {
            holder.tvItemNotes.visibility = View.GONE
        }

        if (!item.photoUri.isNullOrEmpty()) {
            holder.cardImageHolder.visibility = View.VISIBLE
            Glide.with(holder.itemView.context)
                .load(Uri.parse(item.photoUri))
                .centerCrop()
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_gallery)
                .into(holder.ivItemPhoto)
        } else {
            holder.cardImageHolder.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = items.size
}