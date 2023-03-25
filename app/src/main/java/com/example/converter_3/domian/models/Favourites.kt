package com.example.converter_3.domian.models

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.converter_3.R

data class Favourites(
    val name: String,
    var rate: Double)

class FavouritesViewHolder(val context: Context,
                           parentView: ViewGroup
) :
    RecyclerView.ViewHolder(
        LayoutInflater.from(parentView.context)
            .inflate(R.layout.rate_view_favourites, parentView, false)
    ) {

    private val tvRate: TextView = itemView.findViewById(R.id.rateF)
    private val tvRateTo: TextView = itemView.findViewById(R.id.rate_toF)
    private val tvName: TextView = itemView.findViewById(R.id.nameF)
    private val tvFlag: ImageView = itemView.findViewById(R.id.flagF)

    fun bind(item: Favourites) {
        Log.d("Log BIND", "bind_layoutPosition: $this  $layoutPosition")

        for (name in RatesFrom.listOfCurrencyName) {
            if (name.first == item.name)
                tvName.text = name.second
            when (item.name) {
                "AED" -> tvFlag.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.aed))
                "AFN" -> tvFlag.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.af))
                "ALL" -> tvFlag.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.al))
                "USD" -> tvFlag.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.usd))
                "RUB" -> tvFlag.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.rub))
                "EUR" -> tvFlag.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.eur))
                "GBP" -> tvFlag.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.gb))
                "AMD" -> tvFlag.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.am))
                "ANG" -> tvFlag.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.nl))
                "AUD" -> tvFlag.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.au))
                "RSD" -> tvFlag.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.rs))
                else -> tvFlag.setImageDrawable(
                    ContextCompat.getDrawable(context,
                    android.R.drawable.ic_menu_gallery
                ))
            }
        }

        tvRate.text = String.format("%.2f", item.rate)
        tvRateTo.text = item.name

    }
}