package com.example.converter_3.domian.models

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.converter_3.R

class RateAdapter :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_LIST = 1
        const val VIEW_TYPE_PLACEHOLDER = 2
    }

    var items: List<Any> = emptyList()

    override fun getItemViewType(position: Int): Int {
        return when (val item = items[position]) {
            is Pair<*, *> -> VIEW_TYPE_LIST
            is Problem -> VIEW_TYPE_PLACEHOLDER
            else -> throw java.lang.IllegalStateException("Con not find viewType for item $item")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_LIST -> RatesViewHolder(parent)
            VIEW_TYPE_PLACEHOLDER -> PlaceholderViewHolder(parent)
            else -> throw java.lang.IllegalStateException("Con not create ViewHolder for viewType $viewType")

        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        when (item) {
            is Pair<*, *> -> {
                val ratesViewHolder: RatesViewHolder = holder as RatesViewHolder
                ratesViewHolder.bind(item as Pair<String, Double>)
                holder.tvStar.setOnClickListener {
                    // Make a copy of the lists
                    val updatedRates = items.toMutableList()
                    // Move the clicked item to the top of the lists
                    val clickedItemIndex = holder.adapterPosition
                    val clickedItem = updatedRates.removeAt(clickedItemIndex)
                    updatedRates.add(0, clickedItem)
                    // Assign the modified copies back to the adapter properties
                    items = updatedRates
                    // Notify the adapter that the item has been moved
                    notifyItemMoved(clickedItemIndex, 0)
                    // Set the star image drawable
                    val img: Drawable? =
                        ContextCompat.getDrawable(holder.itemView.context, R.drawable.heart)
                    holder.tvStar.setImageDrawable(img)
                }
            }
            is Problem -> {
                val placeholderViewHolder: PlaceholderViewHolder = holder as PlaceholderViewHolder
            }
            else -> throw java.lang.IllegalStateException("Con not find viewType for item $item")
        }
    }
}


class RatesViewHolder(parentView: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parentView.context)
        .inflate(R.layout.rate_view, parentView, false)
) {

    var tvStar: ImageView = itemView.findViewById(R.id.heart_out_line)
    private val tvName: TextView = itemView.findViewById(R.id.name)
    private val tvRate: TextView = itemView.findViewById(R.id.rate)
    private val tvRateTo: TextView = itemView.findViewById(R.id.rate_to)

    fun bind(rate: Pair<String, Double>) {

        for (name in RatesFromEUR.listOfCurrencyName) {
            if (name.first == rate.first)
                tvName.text = name.second
        }

        tvRate.text = String.format("%.2f", rate.second)
        tvRateTo.text = rate.first

    }
}
