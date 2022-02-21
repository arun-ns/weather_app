package com.sunnyday.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import com.sunnyday.adapter.ForecastAdapter
import com.sunnyday.PrefsManager
import com.sunnyday.R
import com.sunnyday.databinding.FragmentForecastBinding
import com.sunnyday.model.WeatherData
import com.sunnyday.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*


class ForecastFragment(private val isHottest: Boolean) : Fragment() {

    private var viewBinding: FragmentForecastBinding? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentForecastBinding.inflate(inflater, container, false)
        val layoutManager = LinearLayoutManager(context)
        viewBinding?.recyclerView?.layoutManager = layoutManager
        viewBinding?.swipeRefreshLayout?.setOnRefreshListener { fetchDataFromServer() }
        fetchData()
        val verticalDecoration = DividerItemDecoration(
            context,
            DividerItemDecoration.VERTICAL
        )
        val verticalDivider = ContextCompat.getDrawable(activity!!, R.drawable.divider)
        verticalDecoration.setDrawable(verticalDivider!!)
        viewBinding?.recyclerView?.addItemDecoration(verticalDecoration)
        return viewBinding?.root
    }


    override fun onResume() {
        viewBinding?.recyclerView?.adapter?.notifyDataSetChanged()
        super.onResume()
    }

    private fun fetchData() {
        fetchDataFromServer()
    }

    private fun fetchDataFromServer() {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .baseUrl("https://5c5c8ba58d018a0014aa1b24.mockapi.io")
            .build()
        val service: ApiClient = retrofit.create(ApiClient::class.java)
        val repos: Call<List<WeatherData>> = service.getWeatherData()
        repos.enqueue(object : Callback<List<WeatherData>> {
            override fun onResponse(
                call: Call<List<WeatherData>>,
                response: Response<List<WeatherData>>
            ) {
                if (response.isSuccessful) {
                    var weatherDataList = response.body()
                    if (isHottest) {
                        weatherDataList = weatherDataList?.let { getFlitterData(it) }
                    }
                    val prefsManager = PrefsManager(context)
                    prefsManager.localJson = GsonBuilder().create().toJson(weatherDataList)
                    viewBinding?.recyclerView?.adapter =
                        weatherDataList?.let { ForecastAdapter(context, it) }
                } else {
                    Toast.makeText(context, "Unable to get weather information", Toast.LENGTH_SHORT)
                        .show()
                }
                viewBinding?.swipeRefreshLayout?.isRefreshing = false
            }

            override fun onFailure(call: Call<List<WeatherData>>, t: Throwable) {
                viewBinding?.swipeRefreshLayout?.isRefreshing = false
                Toast.makeText(context, "Unable to find Internet Connection", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun fetchDataFromLocalStorage(): String? {
        val prefsManager = PrefsManager(context)
        return prefsManager.localJson
    }


    private fun getFlitterData(weatherDataList: List<WeatherData>): List<WeatherData> {
        val filterWeatherData = ArrayList<WeatherData>()
        for (weatherData in weatherDataList) {
            if (weatherData.chance_rain < 0.49f) {
                filterWeatherData.add(weatherData)
            }
        }
        return filterWeatherData.sortedWith(compareBy({ it.high }, { it.high })).reversed()
    }

    override fun onDestroyView() {
        // Consider not storing the binding instance in a field, if not needed.
        viewBinding = null
        super.onDestroyView()
    }
}
