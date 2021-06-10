package com.crazy.crazyalarm.clockUtils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import org.json.JSONObject
import java.io.Serializable
import java.lang.reflect.Method
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import kotlin.concurrent.thread

object AlarmManagerUtil {
    const val ALARM_ACTION = "com.crazy.crazyalarm.clock"

    // To put and get parameter more easily
    const val INTERVALLMILLIS = "intervalMillis"
    const val MSG = "msg"
    const val NOTICEFLAG = "soundOrVibrator"
    const val ID = "id"
    const val REPEAT = "repeat"
    const val IDARR = "idarr"
    const val PARENTID = "parentid"
    const val MODE = "mode"
    const val SETTING = "setting"
    const val DayInMillis: Long = 86400000L
    const val WeekInMillis: Long = 604800000L
    val rand = Random()
    fun setAlarmTime(context: Context, timeInMillis: Long, intent: Intent) {
        val am: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val sender: PendingIntent = PendingIntent.getBroadcast(
            context,
            intent.getIntExtra(ID, 0),
            intent, PendingIntent.FLAG_CANCEL_CURRENT
        )
        val interval = intent.getLongExtra(INTERVALLMILLIS, 0)
        am.setWindow(AlarmManager.RTC_WAKEUP, timeInMillis, interval, sender)
    }

    fun cancelAlarm(context: Context, pid: Int) {
        val intent = Intent(ALARM_ACTION)
        val idList = getChildOf(context, pid)
        for(id in idList){
            val pi: PendingIntent = PendingIntent.getBroadcast(
                context, id,
                intent, PendingIntent.FLAG_CANCEL_CURRENT)
            val am: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            am.cancel(pi)
        }
    }

    sealed class CycleFlag
    object Once : CycleFlag()
    object Daily : CycleFlag()
    object Weekly : CycleFlag()

    const val OnlySound: NoticeFlag = 0
    const val OnlyVibrator: NoticeFlag = 1
    const val BothSoundAndVibrator: NoticeFlag = 2
    const val NormMode: Mode = 0
    const val MathMode: Mode = 1
    const val JigsawMode: Mode = 2
    const val ScanMode: Mode = 3
    const val NothingMode: Mode = 4

    /**
     * @param cycleFlag 周期性表示，Once表示一次性闹钟， Daily表示每天重复， Weekly表示每周重复
     * @param hour 时
     * @param minute 分
     * @param prompt 提示信息
     * @param id 闹钟id
     * @param noticeFlag 震动还是声音
     * @param week week=0表示一次性闹钟，非0的情况下是几就表示以一周为周期重复响铃
     * @param repeat 一周内哪些天重复, -1为仅一次, 127为每天
     */
    fun setAlarm(
        context: Context,
        hour: Int,
        minute: Int,
        cycleFlag: CycleFlag,
        prompt: String,
        id: Int,
        parentID: Int,
        repeat: Int,
        noticeFlag: NoticeFlag,
        week: Int,
        mode: Mode
    ) {
        val am: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val calendar: Calendar = Calendar.getInstance()
        var intervalMillis: Long = 0
        calendar.set(
            calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH), hour, minute, 10
        )
        intervalMillis = when (cycleFlag) {
            is Once -> 0
            is Daily -> DayInMillis
            is Weekly -> WeekInMillis
        }
        val intent = Intent(ALARM_ACTION)
        intent.putExtra(INTERVALLMILLIS, intervalMillis)
        intent.putExtra(MSG, prompt)
        intent.putExtra(ID, id)
        intent.putExtra(PARENTID, parentID)
        intent.putExtra(NOTICEFLAG, noticeFlag)
        intent.putExtra(MODE, mode)
        intent.putExtra(REPEAT, repeat)
        intent.setPackage(context.packageName)
        val sender = PendingIntent.getBroadcast(
            context, id,
            intent, PendingIntent.FLAG_CANCEL_CURRENT
        )
        am.setWindow(
            AlarmManager.RTC_WAKEUP,
            calMethod(week, calendar.timeInMillis),
            10,
            sender
        )
        Log.e("setAlarm", "闹钟已经设置")
    }

    /**
     * @param weekFlag 周几
     * @param dateTime 时间戳(当天年月日+时分秒)
     * @return 闹钟起始时间的时间戳
     */
    private fun calMethod(weekFlag: Int, dateTime: Long): Long {
        var time: Long = 0
        // 重复性闹钟
        if (weekFlag != 0) {
            val c = Calendar.getInstance()
            var week: Int = c.get(Calendar.DAY_OF_WEEK)
            week = when (week) {
                1 -> 7
                2 -> 1
                3 -> 2
                4 -> 3
                5 -> 4
                6 -> 5
                7 -> 6
                else -> week
            }
            // the alarm day is today
            if (weekFlag == week) {
                if (dateTime > System.currentTimeMillis()) {
                    time = dateTime
                } else {
                    time = dateTime + WeekInMillis
                }
                // the day hasn't reach
            } else if (weekFlag > week) {
                time = dateTime + (weekFlag - week) * DayInMillis
                // the day has reached
            } else if (weekFlag < week) {
                time = dateTime + (weekFlag - week) * DayInMillis + WeekInMillis
            }
        } else {
            // 一次性闹钟
            Log.e("dateTime", dateTime.toString())
            Log.e("currentimemillis", System.currentTimeMillis().toString())
            time = if (dateTime > System.currentTimeMillis()) {
                dateTime
            } else {
                dateTime + DayInMillis
            }
        }
        return time
    }

    fun generateId(context: Context): Int{
        var id = rand.nextInt()
        while (isIdInGroup(context, id)){
            id = rand.nextInt()
        }
        return id
    }
    private fun getChildOf(context: Context, pid: Int): MutableList<Int> {
        val pref = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE)
        val idGroup = pref.getString(IDARR, "")
        val idarr = idGroup?.split(",")
        var ChildIDList = mutableListOf<Int>()
        val intent = Intent(ALARM_ACTION)
        (idarr)?.forEach { id ->
            val broadcast = PendingIntent.getBroadcast(
                context, id.toInt(),
                intent, PendingIntent.FLAG_CANCEL_CURRENT
            )
            val intent = getIntent(broadcast)
            if (intent.getIntExtra(PARENTID, -1) == pid){
                ChildIDList.add(intent.getIntExtra(ID, -1))
            }
        }
        return ChildIDList
    }
    /**
     * @param id id不能重复
     */
    private fun insertId(context: Context, id: Int) {
        val pref = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE)
        val idGroup = pref.getString(IDARR, "") ?: ""

        pref.edit().run {
            if (idGroup == "")
                putString(IDARR, "$id")
            else
                putString(IDARR, "$idGroup,$id")
            apply()
        }
    }
    fun getAllParentID(context: Context): MutableList<Int> {
        val pref = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE)
        val idGroup = pref.getString(IDARR, "")
        val idarr = idGroup?.split(",")
        var ParentIDList = mutableListOf<Int>()
        val intent = Intent(ALARM_ACTION)
        (idarr)?.forEach { id ->
            val broadcast = PendingIntent.getBroadcast(
                context, id.toInt(),
                intent, PendingIntent.FLAG_CANCEL_CURRENT
            )
            val intent = getIntent(broadcast)
            if (intent.getIntExtra(ID, -1) == intent.getIntExtra(PARENTID, -100)){
                ParentIDList.add(intent.getIntExtra(ID, -1))
            }
        }
        return ParentIDList
    }
    public fun getIntent(context: Context, id: Int): Intent{
        val broadcast = PendingIntent.getBroadcast(
            context, id,
            Intent(ALARM_ACTION), PendingIntent.FLAG_CANCEL_CURRENT
        )
        return getIntent(broadcast)
    }
    private fun getIntent(pendingIntent: PendingIntent): Intent{
        val getIntentOfPend = PendingIntent::class.java.getDeclaredMethod("getIntent")
        return getIntentOfPend.invoke(pendingIntent) as Intent
    }
    private fun deleteID(context: Context, id: Int) {
        if(!isIdInGroup(context, id)){
            return
        }else{
            val pref = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE)
            val idGroup = pref.getString(IDARR, "") ?: ""
            var idarr = idGroup.split(",")
            var newidarr = mutableListOf<String>()
            for (s in idarr) {
                if (s.toInt() != id){
                    newidarr.add(s)
                }
            }
            pref.edit().run {
                val joinToString = newidarr.joinToString(",")
                putString(IDARR, joinToString)
            }
        }
    }

    private fun isIdInGroup(context: Context, id: Int): Boolean {
        val pref = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE)
        val idGroup = pref.getString(IDARR, "") ?: ""
        val arr = idGroup.split(',')
        if (id.toString() in arr)
            return true
        return false
    }
}

typealias NoticeFlag = Int
typealias Mode = Int
