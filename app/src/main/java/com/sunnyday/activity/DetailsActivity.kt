package com.sunnyday.activity

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.sunnyday.PrefsManager
import com.sunnyday.R
import com.sunnyday.model.WeatherData
import com.sunnyday.databinding.ActivityDetailsBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class DetailsActivity : AppCompatActivity() {

    private var weatherData: WeatherData? = null
    private var prefsManager: PrefsManager? = null

    private lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)

        setContentView(binding.root)
        weatherData = intent.getSerializableExtra("data") as WeatherData
        //title = weatherData?.getDayString(true)
        title = getString(R.string.app_name)

        binding.downloadButton.setOnClickListener(downloadListener)

        prefsManager = PrefsManager(this)
        binding.switchCF.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            prefsManager?.setFahrenheit(isChecked)
            setCelsiusFahrenheit()
        }
        binding.switchCF.isChecked = prefsManager?.isFahrenheit()!!
        setCelsiusFahrenheit()

        binding.chanceRain.text =
            (weatherData?.chance_rain!! * 100).toInt().toString() + "%"
        binding.sunrise.text =
            getFormattedTime(weatherData?.sunrise!!.toLong())
        binding.sunset.text =
            getFormattedTime(weatherData?.sunset!!.toLong())
        binding.title.text =
            weatherData?.description

        binding.dayText.text = weatherData?.getDayString(true)


        val drawable = ContextCompat.getDrawable(this, R.drawable.ic_launcher_background)
        val requestOptions = RequestOptions().error(drawable).placeholder(drawable)
        val directory = ContextWrapper(this).getDir("image", Context.MODE_PRIVATE)
        val f = File(directory, "day_" + weatherData?.day + ".PNG")

        if (f.exists()) {
            Glide.with(this).setDefaultRequestOptions(requestOptions).load(f)
                .into(binding.imageView)
            binding.downloadButton.visibility = View.GONE
        }

        try {
            val ims =
                assets.open(weatherData?.description?.toLowerCase(Locale.getDefault()) + ".png")
            val d = Drawable.createFromStream(ims, null)
            binding.weatherImage.setImageDrawable(d)
            ims.close()
        } catch (ex: IOException) {
            binding.weatherImage.visibility = View.GONE
            return
        }
    }

    // Update Celsius/Fahrenheit
    private fun setCelsiusFahrenheit() {
        val prefsManager = PrefsManager(this)
        if (prefsManager.isFahrenheit()) {
            val fahrenheitHigh = ((weatherData?.high!! * 9 / 5) + 32)
            val fahrenheitLow = ((weatherData?.low!! * 9 / 5) + 32)
            binding.low.text = fahrenheitLow.toString() + "\u2109"
            binding.high.text = fahrenheitHigh.toString() + "\u2109"
            binding.headingHigh.text = fahrenheitHigh.toString() + "\u2109"
        } else {
            binding.low.text = weatherData?.low.toString() + "\u2103"
            binding.high.text = weatherData?.high.toString() + "\u2103"
            binding.headingHigh.text = weatherData?.high.toString() + "\u2103"
        }
    }


    private val downloadListener = View.OnClickListener {
        downloadTheImage()
    }

    private fun downloadTheImage() {
        binding.downloadButton.isEnabled = false
        binding.downloadButton.text = getString(R.string.downloading)
        Glide.with(this)
            .asBitmap()
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .load(weatherData?.image)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    binding.imageView.setImageBitmap(resource)
                    binding.downloadButton.visibility = View.GONE
                    saveToInternalStorage(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {

                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    Toast.makeText(
                        applicationContext, getString(R.string.download_image_error),
                        Toast.LENGTH_LONG
                    ).show()
                    binding.downloadButton.isEnabled = true
                    binding.downloadButton.text = getString(R.string.download_image)
                    super.onLoadFailed(errorDrawable)
                }
            })

    }

    // Get the time from timestamp
    private fun getFormattedTime(timestamp: Long): String {
        val format = SimpleDateFormat(getString(R.string.hours_minutes))
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp * 1000
        return format.format(calendar.time)
    }


    // Save the user downloaded image to internal storage
    fun saveToInternalStorage(bitmapImage: Bitmap) {
        Thread {
            val cw = ContextWrapper(this)
            val directory = cw.getDir("image", Context.MODE_PRIVATE)
            val path = "day_" + weatherData?.day + ".PNG"
            val mypath = File(directory, path)
            var fos: FileOutputStream? = null
            try {
                fos = FileOutputStream(mypath)
                bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                try {
                    fos!!.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }.start()

    }
}
