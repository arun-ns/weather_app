package com.sunnydaytest

import com.sunnyday.network.ApiClient
import com.sunnyday.model.WeatherData
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.CountDownLatch


class ExampleUnitTest {

    private var mCountDownLatch: CountDownLatch? = null

    @Before
    fun setup() {
        mCountDownLatch = CountDownLatch(1);
    }

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test(timeout = 5000)
    fun getWeatherData() {
        val retrofit = Retrofit.Builder().addConverterFactory(
            GsonConverterFactory.create()
        )
            .baseUrl("https://5c5c8ba58d018a0014aa1b24.mockapi.io/")
            .build()
        val service = retrofit.create(ApiClient::class.java)

        val repos: Call<List<WeatherData>> = service.getWeatherData()
        repos.enqueue(object : Callback<List<WeatherData>> {
            override fun onResponse(
                call: Call<List<WeatherData>>,
                response: Response<List<WeatherData>>
            ) {
                if (response.isSuccessful) {
                    val weatherDataList = response.body()
                    assertNotNull(weatherDataList);
                } else {
                    fail("Unable to get weather information")
                }
            }

            override fun onFailure(call: Call<List<WeatherData>>, t: Throwable) {
                fail("Unable to find Internet Connection")
            }
        })
        mCountDownLatch?.await()
    }

    @Test(timeout = 5000)
    fun getWeatherDataFailsWhenAPIChange() {
        val retrofit = Retrofit.Builder().addConverterFactory(
            GsonConverterFactory.create()
        )
            .baseUrl("https://dffdgdfgfdg.mockapi.io/")
            .build()
        val service = retrofit.create(ApiClient::class.java)

        val repos: Call<List<WeatherData>> = service.getWeatherData()
        repos.enqueue(object : Callback<List<WeatherData>> {
            override fun onResponse(
                call: Call<List<WeatherData>>,
                response: Response<List<WeatherData>>
            ) {
                if (response.isSuccessful) {
                    val weatherDataList = response.body()
                    assertNotNull(weatherDataList);
                } else {
                    fail("Unable to get weather information")
                }
            }

            override fun onFailure(call: Call<List<WeatherData>>, t: Throwable) {
                fail("Unable to find Internet Connection")
            }
        })
        mCountDownLatch?.await()
    }


    @Test(timeout = 5000)
    fun getWeatherDataFailsNoInternet() {
        val retrofit = Retrofit.Builder().addConverterFactory(
            GsonConverterFactory.create()
        )
            .baseUrl("https://5c5c8ba58d018a0014aa1b24.mockapi.io/")
            .build()
        val service = retrofit.create(ApiClient::class.java)

        val repos: Call<List<WeatherData>> = service.getWeatherData()
        repos.enqueue(object : Callback<List<WeatherData>> {
            override fun onResponse(
                call: Call<List<WeatherData>>,
                response: Response<List<WeatherData>>
            ) {
                if (response.isSuccessful) {
                    val weatherDataList = response.body()
                    assertNotNull(weatherDataList);
                } else {
                    fail("Unable to get weather information")
                }
            }

            override fun onFailure(call: Call<List<WeatherData>>, t: Throwable) {
                fail("Unable to find Internet Connection")
            }
        })
        mCountDownLatch?.await()
    }
}
