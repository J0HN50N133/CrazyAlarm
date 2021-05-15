package com.crazy.crazyalarm.clockUtils

import android.app.Activity
import android.app.Service
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import com.crazy.crazyalarm.R
import com.crazy.crazyalarm.clockUtils.AlarmManagerUtil.BothSoundAndVibrator
import com.crazy.crazyalarm.clockUtils.AlarmManagerUtil.NoticeFlag
import com.crazy.crazyalarm.clockUtils.AlarmManagerUtil.OnlyVibrator
import com.crazy.crazyalarm.clockUtils.AlarmManagerUtil.OnlySound
import com.crazy.crazyalarm.databinding.ActivityClockAlarmBinding
import java.net.ServerSocket
import kotlin.collections.arrayListOf as arrayListOf

class ClockAlarmActivity: Activity() {
    private var mediaPlayer: MediaPlayer? = null
    private var vibrator: Vibrator? = null
    private lateinit var binding: ActivityClockAlarmBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClockAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val msg = intent.getStringExtra(AlarmManagerUtil.MSG) as String
        val noticeFlag = intent.getSerializableExtra(AlarmManagerUtil.NOTICEFLAG) as AlarmManagerUtil.NoticeFlag
        showDialogInBroadcastReceiver(msg, noticeFlag)
    }

    private fun showDialogInBroadcastReceiver(msg: String, noticeFlag: NoticeFlag) {
        when(noticeFlag) {
            is OnlySound -> {
                ring()
            }
            is OnlyVibrator -> {
                vibrate()
            }
            is BothSoundAndVibrator -> {
                ring()
                vibrate()
            }
        }
        val dialog: SimpleDialog = SimpleDialog(this)
        dialog.binding.dialogTitle.text = "闹钟"
        dialog.binding.dialogMessage.text = msg
        dialog.setConfirmListener{
            if (dialog.binding.dialogConfirm == it) {
                when(noticeFlag){
                    is OnlySound -> {
                        mediaPlayer?.stop()
                    }
                    is OnlyVibrator -> {
                        vibrator?.cancel()
                    }
                    is BothSoundAndVibrator -> {
                        mediaPlayer?.stop()
                        vibrator?.cancel()
                    }
                }
                dialog.dismiss()
                finish()
            }
        }
        dialog.show()
    }
    private fun ring(){
        mediaPlayer = MediaPlayer.create(this, R.raw.beep)
        mediaPlayer?.isLooping = true
        mediaPlayer?.start()
    }
    private fun vibrate(){
        vibrator = getSystemService(Service.VIBRATOR_SERVICE) as Vibrator?
        val vibrate = vibrator?.vibrate(
            VibrationEffect.createWaveform(
                longArrayOf(100, 10, 100, 600),
                0
            )
        )
    }
}
