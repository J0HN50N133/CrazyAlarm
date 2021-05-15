package com.crazy.crazyalarm.clockUtils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.crazy.crazyalarm.clockUtils.AlarmManagerUtil.Norm

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val msg = intent.getStringExtra(AlarmManagerUtil.MSG)
        val intervalMillis = intent.getLongExtra(AlarmManagerUtil.INTERVALLMILLIS, 0)
        val noticeFlag = intent.getSerializableExtra(AlarmManagerUtil.NOTICEFLAG) as AlarmManagerUtil.NoticeFlag
        val mode = intent.getSerializableExtra(AlarmManagerUtil.MODE) as AlarmManagerUtil.Mode
        if (intervalMillis != 0L) {
            // 重复闹钟解决方案: 本次闹钟响的同时设置下一次闹钟
            AlarmManagerUtil.setAlarmTime(context,
                System.currentTimeMillis() + intervalMillis, intent)
        }

        // TODO: 闹钟响铃后模式在这里决定
        val clockIntent = when(mode){
            is Norm -> Intent(context, ClockAlarmActivity::class.java)
            else -> Intent(context, ClockAlarmActivity::class.java)
        }
        clockIntent.putExtra(AlarmManagerUtil.MSG, msg)
        clockIntent.putExtra(AlarmManagerUtil.NOTICEFLAG, noticeFlag)
        clockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(clockIntent)
    }
}