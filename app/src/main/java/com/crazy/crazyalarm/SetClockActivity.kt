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
    companion object{
        private var cycle: AlarmManagerUtil.CycleFlag = AlarmManagerUtil.Once
        private var cycleDaysOfWeek: Int? = null
        private var ring: AlarmManagerUtil.NoticeFlag = AlarmManagerUtil.BothSoundAndVibrator
        private var mode: AlarmManagerUtil.Mode = AlarmManagerUtil.Norm
        private var hour: Int? = null
        private var minute: Int? = null
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetClockBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val instance = Calendar.getInstance()
        val hourOfDay = instance.get(Calendar.HOUR_OF_DAY)
        val currMinute = instance.get(Calendar.MINUTE)
        val onTimeSetListener = { _: TimePicker, _hour:Int, _minute: Int ->
            runOnUiThread{
                binding.dateTv.text = "$_hour : $_minute"
            }
            hour = _hour
            minute = _minute
        }
        timePickerDialog = TimePickerDialog(this, onTimeSetListener, hourOfDay, currMinute, true)
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
            ring = flag
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
                    val repeatVal = parseRepeat(repeat, 0)
                    runOnUiThread{
                        binding.tvRepeatValue.text = repeatVal
                    }
                    cycle = AlarmManagerUtil.Weekly
                    cycleDaysOfWeek = ret.toInt()
                    cycleFlagPopup.dismiss()
                }
                // 每天
                8->{
                    runOnUiThread{
                        binding.tvRepeatValue.text = "每天"
                    }
                    cycle = AlarmManagerUtil.Daily
                    cycleFlagPopup.dismiss()
                }
                // 只响一次
                9->{
                    runOnUiThread{
                        binding.tvRepeatValue.text = "仅此一次"
                    }
                    cycle = AlarmManagerUtil.Once
                    cycleFlagPopup.dismiss()
                }
            }
        }
    }

    private fun parseRepeat(r: Int, flag: Int): String {
        var repeat = r
        var cycle = ""
        var weeks = ""
        if (repeat == 0) {
            repeat = 127
        }
        if (repeat % 2 == 1) {
            cycle = "周一"
            weeks = "1"
        }
        if (repeat % 4 >= 2) {
            if ("" == cycle) {
                cycle = "周二"
                weeks = "2"
            } else {
                cycle = "$cycle,周二"
                weeks = "$weeks,2"
            }
        }
        if (repeat % 8 >= 4) {
            if ("" == cycle) {
                cycle = "周三"
                weeks = "3"
            } else {
                cycle = "$cycle,周三"
                weeks = "$weeks,3"
            }
        }
        if (repeat % 16 >= 8) {
            if ("" == cycle) {
                cycle = "周四"
                weeks = "4"
            } else {
                cycle = "$cycle,周四"
                weeks = "$weeks,4"
            }
        }
        if (repeat % 32 >= 16) {
            if ("" == cycle) {
                cycle = "周五"
                weeks = "5"
            } else {
                cycle = "$cycle,周五"
                weeks = "$weeks,5"
            }
        }
        if (repeat % 64 >= 32) {
            if ("" == cycle) {
                cycle = "周六"
                weeks = "6"
            } else {
                cycle = "$cycle,周六"
                weeks = "$weeks,6"
            }
        }
        if (repeat / 64 == 1) {
            if ("" == cycle) {
                cycle = "周日"
            } else {
                cycle = "$cycle,周日"
                weeks = "$weeks,7"
            }
        }
        if (cycle == "周一,周二,周三,周四,周五,周六,周日"){
            cycle = "每天"
        }
        return if (flag == 0) cycle else weeks
    }

    private fun setClock(){
        if (hour != null && minute != null){
            when(cycle){
                is AlarmManagerUtil.Daily,AlarmManagerUtil.Once->{
                    AlarmManagerUtil.setAlarm(
                        this,
                        hour!!,
                        minute!!,
                        cycle,
                        "闹钟响了",
                        0,
                        ring,
                        0,
                        mode
                    )
                }
                is AlarmManagerUtil.Weekly->{
                    val weekStr = parseRepeat(cycleDaysOfWeek!!, 1)
                    val days = weekStr.split(",")
                    for((i, day) in days.withIndex()){
                        AlarmManagerUtil.setAlarm(
                            this,
                            hour!!,
                            minute!!,
                            cycle,
                            "闹钟响了",
                            i,
                            ring,
                            day.toInt(),
                            mode
                        )
                    }
                }
            }
            Toast.makeText(this, "闹钟设置成功", Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(this, "憨憨！你还没选时间！", Toast.LENGTH_LONG).show()
        }
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
            mode = flag
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
}