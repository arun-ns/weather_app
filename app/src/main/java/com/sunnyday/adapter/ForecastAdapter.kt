package com.sunnyday.adapter

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sunnyday.R
import com.sunnyday.adapter.ForecastAdapter.DayViewHolder
import com.sunnyday.activity.DetailsActivity
import com.sunnyday.databinding.ItemForecastBinding
import com.sunnyday.model.WeatherData
import java.io.File

class ForecastAdapter(var context: Context?, private var data: List<WeatherData>) :
    RecyclerView.Adapter<DayViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_forecast, parent, false)
        return DayViewHolder(
            ItemForecastBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val weatherData = data[position]
        holder.viewBinding.day.text = weatherData.getDayString(false)
        holder.viewBinding.title.text = weatherData.description
        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailsActivity::class.java)
            intent.putExtra("data", weatherData)
            context?.startActivity(intent)
        }

        val drawable = ContextCompat.getDrawable(context!!, R.drawable.placeholder)
        val requestOptions = RequestOptions().error(drawable).placeholder(drawable)
        val cw = ContextWrapper(context)
        val directory = cw.getDir("image", Context.MODE_PRIVATE)
        val f = File(directory, "day_" + weatherData.day + ".PNG")
        Glide.with(context!!).setDefaultRequestOptions(requestOptions).load(f)
            .into(holder.viewBinding.dayImage)

        Glide.with(context!!)
            .load("file:///android_asset/" + weatherData.description?.toLowerCase() + ".png")
            .into(holder.viewBinding.image)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class DayViewHolder(val viewBinding: ItemForecastBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
    }
}
