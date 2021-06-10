package com.crazy.crazyalarm

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.crazy.crazyalarm.clockUtils.AlarmManagerUtil
import com.crazy.crazyalarm.clockUtils.ClockItem
import com.crazy.crazyalarm.clockUtils.Configuration
import com.crazy.crazyalarm.databinding.ActivityMainBinding
import java.time.Clock
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadConfiguration()
        var list = mutableListOf<ClockItem>()


    }

    fun loadConfiguration() {
        val SETTING = "setting"
        val MATH_CODE = "mathCode"
        val SCAN_STRING = "scanString"
        val prefs :SharedPreferences = getSharedPreferences(SETTING, Context.MODE_PRIVATE)
        val code = prefs.getInt(MATH_CODE, 0)
        Configuration.MathConf.modeCode = code
        Configuration.ScanConf.scanString = prefs.getString(SCAN_STRING, "") ?: ""
    }
}
