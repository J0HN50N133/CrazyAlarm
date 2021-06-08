package com.crazy.crazyalarm

import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.crazy.crazyalarm.databinding.ActivityMainBinding
import com.crazy.crazyalarm.view.SelectCycleFlagPopup
import com.crazy.crazyalarm.view.SelectCyclePopupOnClickListener
import com.crazy.crazyalarm.view.SelectNoticeFlagPopup
import com.crazy.crazyalarm.view.SelectNoticeWayPopupOnClickListener
import java.util.*

class MainActivity : AppCompatActivity() , View.OnClickListener{
    private lateinit var binding: ActivityMainBinding
    private lateinit var timePickerDialog: TimePickerDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
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
        binding.repeatRl.setOnClickListener(this)
        binding.setBtn.setOnClickListener(this)
    }
    private fun selectNoticeWay(){
        val noticePopup = SelectNoticeFlagPopup(this)
        noticePopup.showPopup(binding.root)
        noticePopup.setOnclickListener(object : SelectNoticeWayPopupOnClickListener {
            override fun obtainMessage(flag: Int) {
                when(flag) {
                    0->{
                        runOnUiThread{
                            binding.tvRingValue.text = "震动"
                        }
                    }
                    1->{
                        runOnUiThread{
                            binding.tvRingValue.text = "铃声"
                        }
                    }
                }
            }
        })
    }
    private fun selectCycle(){
        val cycleFlagPopup = SelectCycleFlagPopup(this)
        cycleFlagPopup.showPopup(binding.root)
        cycleFlagPopup.setOnClickListener(object : SelectCyclePopupOnClickListener {
            override fun obtainMessage(flag: Int, ret: String) {

            }
        }

        )
    }
    private fun setClock(){
        Toast.makeText(this, "闹钟设置成功", Toast.LENGTH_LONG).show()
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.date_tv->{
                timePickerDialog.show()
            }
            R.id.repeat_rl->{
                selectCycle()
                Log.d("cycle", "你点击了重复")
            }
            R.id.ring_rl->{
                selectNoticeWay()
                Log.d("noticeFlag", "你点击了提醒方式")
            }
            R.id.set_btn->{
                setClock()
            }
        }
    }
}