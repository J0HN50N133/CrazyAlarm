package com.crazy.crazyalarm.clockUtils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.crazy.crazyalarm.clockUtils.AlarmManagerUtil.MathMode
import com.crazy.crazyalarm.clockUtils.AlarmManagerUtil.NormMode
import com.crazy.crazyalarm.closeModeActivity.MathActivity

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.e("Broadcast", "Receive Broadcast")
        val msg = intent.getStringExtra(AlarmManagerUtil.MSG)
        val intervalMillis = intent.getLongExtra(AlarmManagerUtil.INTERVALLMILLIS, 0)
        val mode = intent.getIntExtra(AlarmManagerUtil.MODE, AlarmManagerUtil.NormMode)
        val noticeFlag = intent.getIntExtra(AlarmManagerUtil.NOTICEFLAG, AlarmManagerUtil.BothSoundAndVibrator)
        if (intervalMillis != 0L) {
            // 重复闹钟解决方案: 本次闹钟响的同时设置下一次闹钟
            AlarmManagerUtil.setAlarmTime(context,
                System.currentTimeMillis() + intervalMillis, intent)
        }

        // TODO: 闹钟响铃后模式在这里决定
        val clockIntent = when(mode){
            NormMode -> Intent(context, ClockAlarmActivity::class.java)
            MathMode -> Intent(context, MathActivity::class.java)
            else -> Intent(context, ClockAlarmActivity::class.java)
        }
        clockIntent.putExtra(AlarmManagerUtil.MSG, msg)
        clockIntent.putExtra(AlarmManagerUtil.NOTICEFLAG, noticeFlag)
        clockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(clockIntent)
    }
}