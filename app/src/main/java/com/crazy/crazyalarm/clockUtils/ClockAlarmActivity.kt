package com.crazy.crazyalarm.clockUtils

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Vibrator
import com.crazy.crazyalarm.R
import com.crazy.crazyalarm.clockUtils.AlarmManagerUtil.BothSoundAndVibrator
import com.crazy.crazyalarm.clockUtils.AlarmManagerUtil.NoticeFlag
import com.crazy.crazyalarm.clockUtils.AlarmManagerUtil.OnlySound
import com.crazy.crazyalarm.clockUtils.AlarmManagerUtil.OnlyVibrator

class ClockAlarmActivity: BasicRingActivity() {
    private var mediaPlayer: MediaPlayer? = null
    private var vibrator: Vibrator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clock_alarm)
        val msg = intent.getStringExtra(AlarmManagerUtil.MSG) as String
        val noticeFlag = intent.getSerializableExtra(AlarmManagerUtil.NOTICEFLAG) as AlarmManagerUtil.NoticeFlag
        showDialogInBroadcastReceiver(msg, noticeFlag)
    }

    private fun showDialogInBroadcastReceiver(msg: String, noticeFlag: NoticeFlag) {
        giveNotice(noticeFlag)
        val dialog: SimpleDialog = SimpleDialog(this)
        dialog.dialogSimpleBinding.dialogTitle.text = "闹钟"
        dialog.dialogSimpleBinding.dialogMessage.text = msg
        dialog.setConfirmListener{
            if (dialog.dialogSimpleBinding.dialogConfirm == it) {
                stopNotice(noticeFlag)
                dialog.dismiss()
                finish()
            }
        }
        dialog.show()
    }
//    private fun ring(){
//        mediaPlayer = MediaPlayer.create(this, R.raw.beep)
//        mediaPlayer?.isLooping = true
//        mediaPlayer?.start()
//    }
//    private fun vibrate(){
//        vibrator = getSystemService(Service.VIBRATOR_SERVICE) as Vibrator?
//        val vibrate = vibrator?.vibrate(
//            VibrationEffect.createWaveform(
//                longArrayOf(100, 10, 100, 600),
//                0
//            )
//        )
//    }
}
