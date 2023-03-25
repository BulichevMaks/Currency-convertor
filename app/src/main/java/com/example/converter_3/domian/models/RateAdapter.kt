package com.example.converter_3.domian.models

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.converter_3.R


class RateAdapter(private val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_LIST = 1
        const val VIEW_TYPE_PLACEHOLDER = 2
        const val VIEW_TYPE_FAVOURITES = 3
        fun getImgHeartON(context: Context): Drawable? {
            return ContextCompat.getDrawable(context, R.drawable.heart)
        }

        fun getImgHeartOff(context: Context): Drawable? {
            return ContextCompat.getDrawable(context, R.drawable.heart_outline)
        }
    }

    private val imgHeartON: Drawable? = getImgHeartON(context)
    private val imgHeartOff: Drawable? = getImgHeartOff(context)
    var items: List<Any> = emptyList()


    private val positions = mutableMapOf<Int, Boolean>()
    override fun getItemViewType(position: Int): Int {
        val item = items[position]
        return when (item) {
            is Favourites -> VIEW_TYPE_FAVOURITES
            is Rates -> VIEW_TYPE_LIST
            is Problem -> VIEW_TYPE_PLACEHOLDER
            else -> throw java.lang.IllegalStateException("Con not find viewType for item $item")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            VIEW_TYPE_FAVOURITES -> FavouritesViewHolder(context, parent)
            VIEW_TYPE_LIST -> RatesViewHolder(
                context,
                parent,
                positions,
                imgHeartON!!,
                imgHeartOff!!
            )
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
            is Favourites -> {
                val favouritesViewHolder: FavouritesViewHolder = holder as FavouritesViewHolder
                favouritesViewHolder.bind(item)

            }
            is Rates -> {
                val ratesViewHolder: RatesViewHolder = holder as RatesViewHolder
                ratesViewHolder.bind(item)

                holder.tvStar.setOnClickListener {

                    when (holder.tvStar.drawable) {
                        imgHeartON -> {
                            holder.tvStar.setImageDrawable(imgHeartOff)
                            var iterator = ListOfFavouritesRates.listOfFavouritesRates.iterator()
                            while (iterator.hasNext()) {
                                val it = iterator.next().name
                                if (item.name == it) {
                                    iterator.remove()
                                }
                            }
                            notifyDataSetChanged()
                            positions[position] = false
                        }
                        imgHeartOff -> {
                            holder.tvStar.setImageDrawable(imgHeartON)

                            ListOfFavouritesRates.listOfFavouritesRates.add(
                                Favourites(
                                    item.name,
                                    item.rate
                                )
                            )

                            notifyDataSetChanged()
                            positions[position] = true
                        }
                    }
                }
            }
            is Problem -> {
                val placeholderViewHolder: PlaceholderViewHolder = holder as PlaceholderViewHolder
            }
            else -> throw java.lang.IllegalStateException("Con not find viewType for item $item")
        }
    }
}

