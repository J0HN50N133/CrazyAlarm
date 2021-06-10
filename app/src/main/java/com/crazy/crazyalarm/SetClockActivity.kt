package com.crazy.crazyalarm

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.crazy.crazyalarm.clockUtils.AlarmManagerUtil
import com.crazy.crazyalarm.databinding.ActivitySetClockBinding
import com.crazy.crazyalarm.view.SelectCloseModePopUp
import com.crazy.crazyalarm.view.SelectCycleFlagPopup
import com.crazy.crazyalarm.view.SelectNoticeFlagPopup
import java.util.*

class SetClockActivity : AppCompatActivity() , View.OnClickListener{
    private lateinit var binding: ActivitySetClockBinding
    private lateinit var timePickerDialog: TimePickerDialog
    private var cycle: Int? = null
    private var ring: AlarmManagerUtil.NoticeFlag? = null
    private val mode: AlarmManagerUtil.Mode? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetClockBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val instance = Calendar.getInstance()
        val hourOfDay = instance.get(Calendar.HOUR_OF_DAY)
        val minute = instance.get(Calendar.MINUTE)
        val onTimeSetListener = { _: TimePicker, _hour:Int, _minute: Int ->
            runOnUiThread{
                binding.dateTv.text = "$_hour : $_minute"
            }
        }
        timePickerDialog = TimePickerDialog(this, onTimeSetListener, hourOfDay, minute, true)
        binding.dateTv.setOnClickListener(this)
        binding.ringRl.setOnClickListener(this)
        binding.closeRl.setOnClickListener(this)
        binding.repeatRl.setOnClickListener(this)
        binding.setBtn.setOnClickListener(this)
    }
    private fun selectNoticeWay(){
        val noticePopup = SelectNoticeFlagPopup(this)
        runOnUiThread {
            noticePopup.showPopup(binding.root)
        }
        noticePopup.setOnclickListener { flag: AlarmManagerUtil.NoticeFlag ->
            when (flag) {
                AlarmManagerUtil.OnlyVibrator -> {
                    runOnUiThread {
                        binding.tvRingValue.text = "震动"
                    }
                    ring = AlarmManagerUtil.OnlyVibrator
                }
                AlarmManagerUtil.OnlySound -> {
                    runOnUiThread {
                        binding.tvRingValue.text = "铃声"
                    }
                    ring = AlarmManagerUtil.OnlySound
                }
                AlarmManagerUtil.BothSoundAndVibrator->{
                    runOnUiThread {
                        binding.tvRingValue.text = "震动和铃声"
                    }
                    ring = AlarmManagerUtil.BothSoundAndVibrator
                }
            }
            runOnUiThread {
                noticePopup.dismiss()
            }
        }
    }
    private fun selectCycle(){
        val cycleFlagPopup = SelectCycleFlagPopup(this)
        runOnUiThread{
            cycleFlagPopup.showPopup(binding.root)
        }
        cycleFlagPopup.setOnClickListener { flag: Int, ret: String->
            when(flag) {
                // 确认
                7->{
                    val repeat = ret.toInt()
                    val repeatVal = parseRepeat(repeat)
                    runOnUiThread{
                        binding.tvRepeatValue.setText(repeatVal)
                    }
                    cycleFlagPopup.dismiss()
                }
                // 每天
                8->{
                    cycleFlagPopup.dismiss()
                }
                // 只响一次
                9->{
                    cycleFlagPopup.dismiss()
                }
            }
        }
    }

    private fun parseRepeat(r: Int): String {
        var repeat = r
        var cycle = ""
        if (repeat == 0) {
            repeat = 127;
        }
        if (repeat % 2 == 1) {
            cycle = "周一";
        }
        if (repeat % 4 >= 2) {
            if ("".equals(cycle)) {
                cycle = "周二";
            } else {
                cycle = "$cycle,周二"
            }
        }
        if (repeat % 8 >= 4) {
            if ("".equals(cycle)) {
                cycle = "周三";
            } else {
                cycle = "$cycle,周三"
            }
        }
        if (repeat % 16 >= 8) {
            if ("".equals(cycle)) {
                cycle = "周四";
            } else {
                cycle = "$cycle,周四"
            }
        }
        if (repeat % 32 >= 16) {
            if ("".equals(cycle)) {
                cycle = "周五";
            } else {
                cycle = "$cycle,周五"
            }
        }
        if (repeat % 64 >= 32) {
            if ("".equals(cycle)) {
                cycle = "周六";
            } else {
                cycle = "$cycle,周六"
            }
        }
        if (repeat / 64 == 1) {
            if ("".equals(cycle)) {
                cycle = "周日"
            } else {
                cycle = "$cycle,周日"
            }
        }
        return cycle
    }

    private fun setClock(){
        Toast.makeText(this, "闹钟设置成功", Toast.LENGTH_LONG).show()
    }
    private fun selectCloseMode(){
        val closePopup = SelectCloseModePopUp(this)
        runOnUiThread{
            closePopup.showPopup(binding.root)
        }
        closePopup.setOnClickListener { flag:AlarmManagerUtil.Mode->
            when (flag) {
                is AlarmManagerUtil.Norm -> {
                    runOnUiThread{
                        binding.tvCloseValue.setText("普通模式")
                    }
                }
                is AlarmManagerUtil.Math -> {
                    runOnUiThread{
                        binding.tvCloseValue.setText("数学模式")
                    }
                }
                is AlarmManagerUtil.Jigsaw -> {
                    runOnUiThread{
                        binding.tvCloseValue.setText("拼图模式")
                    }
                }
                is AlarmManagerUtil.Scan -> {
                    runOnUiThread{
                        binding.tvCloseValue.setText("扫描模式")
                    }
                }
            }
            closePopup.dismiss()
        }
    }
    override fun onClick(view: View?) {
        when(view?.id){
            R.id.date_tv->{
                timePickerDialog.show()
            }
            R.id.repeat_rl->{
                selectCycle()
            }
            R.id.ring_rl->{
                selectNoticeWay()
            }
            R.id.close_rl->{
                selectCloseMode()
            }
            R.id.set_btn->{
                setClock()
            }
        }
    }
//    //用来测试别的activity
//    fun jumpToTest(view: View?){
//        val intent = Intent(this,SettingActivity::class.java)
//        startActivity(intent)
//    }
}