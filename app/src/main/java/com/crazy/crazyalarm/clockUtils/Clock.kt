package com.crazy.crazyalarm.clockUtils

import android.content.Context

class Clock(context: Context, id: Int) {
    val mode: Int
    val noticeFlag: Int
    val repeat: Int

    init {
        val intent = AlarmManagerUtil.getIntent(context, id)
        noticeFlag = intent.getIntExtra(AlarmManagerUtil.NOTICEFLAG, 0)
        repeat = intent.getIntExtra(AlarmManagerUtil.REPEAT, 0)
        mode = intent.getIntExtra(AlarmManagerUtil.MODE, 0)
    }
}