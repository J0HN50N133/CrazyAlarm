package com.crazy.crazyalarm

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.crazy.crazyalarm.clockUtils.AlarmManagerUtil
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
        var list = mutableListOf<Clock>()

        val calendar = Calendar.getInstance()
//        binding.button2.setOnClickListener {
//            val intent = AlarmManagerUtil.setAlarm(
//                this,
//                calendar.get(Calendar.HOUR_OF_DAY),
//                calendar.get(Calendar.MINUTE)+1,
//                AlarmManagerUtil.Once,
//                "闹钟响了",
//                0,
//                AlarmManagerUtil.BothSoundAndVibrator,
//                0,
//                AlarmManagerUtil.Norm
//            )
//            Toast.makeText(this, "闹钟设置成功", Toast.LENGTH_SHORT).show()
//            val intent = Intent(this, SetClockActivity::class.java)
//            startActivity(intent)
//            val intent = Intent(AlarmManagerUtil.ALARM_ACTION)
//            intent.putExtra(AlarmManagerUtil.MSG, "闹钟响了")
//            intent.putExtra(AlarmManagerUtil.INTERVALLMILLIS, "0")
//            intent.putExtra(AlarmManagerUtil.NOTICEFLAG, AlarmManagerUtil.BothSoundAndVibrator)
//            intent.putExtra(AlarmManagerUtil.MODE, AlarmManagerUtil.Norm)
//            sendBroadcast(intent)
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
