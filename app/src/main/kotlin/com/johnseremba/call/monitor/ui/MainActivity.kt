package com.johnseremba.call.monitor.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.johnseremba.call.monitor.databinding.ActivityMainBinding
import com.johnseremba.call.monitor.di.AppKoinComponent

class MainActivity : AppCompatActivity(), AppKoinComponent {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
