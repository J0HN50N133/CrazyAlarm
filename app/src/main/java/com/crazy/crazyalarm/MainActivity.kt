package com.crazy.crazyalarm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.crazy.crazyalarm.databinding.ActivityMainBinding
import com.crazy.crazyalarm.databinding.DialogSimpleBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}