package com.crazy.crazyalarm.clockUtils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val msg = intent.getStringExtra(AlarmManagerUtil.MSG)
        val intervalMillis = intent.getLongExtra(AlarmManagerUtil.INTERVALLMILLIS, 0)
        if (intervalMillis != 0L) {
            // 重复闹钟解决方案: 本次闹钟响的同时设置下一次闹钟
            AlarmManagerUtil.setAlarmTime(context,
                System.currentTimeMillis() + intervalMillis, intent)
        }
        val noticeFlag = intent.getSerializableExtra(AlarmManagerUtil.NOTICEFLAG) as AlarmManagerUtil.NoticeFlag
        val clockIntent = Intent(context, ClockAlarmActivity::class.java)
        clockIntent.putExtra(AlarmManagerUtil.MSG, msg)
        clockIntent.putExtra(AlarmManagerUtil.NOTICEFLAG, noticeFlag)
        clockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(clockIntent)
    }
}