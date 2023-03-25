package com.example.converter_3.domian.models

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.converter_3.R



class Rates(
    val name: String,
    val rate: Double
)

class RatesViewHolder(
    private val context: Context,
    parentView: ViewGroup,
    private val positions: Map<Int, Boolean>,
    private val imgHeartON: Drawable,
    private val imgHeartOff: Drawable
) :
    RecyclerView.ViewHolder(
        LayoutInflater.from(parentView.context)
            .inflate(R.layout.rate_view, parentView, false)
    ) {
    private val imgHeart: Drawable? = imgHeartON

    private val imgHeartOutLine: Drawable? = imgHeartOff


    var tvStar: ImageView = itemView.findViewById(R.id.heart_out_line)
    private val tvName: TextView = itemView.findViewById(R.id.name)
    private val tvFlag: ImageView = itemView.findViewById(R.id.flag)
    private val tvRateTo: TextView = itemView.findViewById(R.id.rate_to)

    fun bind(item: Rates) {

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
                else -> tvFlag.setImageDrawable(ContextCompat.getDrawable(context,
                    android.R.drawable.ic_menu_gallery
                ))
            }

        }

        if (positions[layoutPosition] == true) {
            tvStar.setImageDrawable(imgHeart)
        } else {
            tvStar.setImageDrawable(imgHeartOutLine)
        }
        tvRateTo.text = item.name
    }
}
