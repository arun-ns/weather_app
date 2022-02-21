package com.sunnyday.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sunnyday.adapter.TabsAdapter
import com.sunnyday.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val adapter = TabsAdapter(this, supportFragmentManager)
        binding.pager.adapter = adapter
        binding.tabs.setupWithViewPager(binding.pager)
    }
}
