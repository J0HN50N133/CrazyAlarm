package com.crazy.crazyalarm.clockUtils

import android.app.Service
import android.media.MediaPlayer
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.appcompat.app.AppCompatActivity
import com.crazy.crazyalarm.R

open class BasicRingActivity: AppCompatActivity(){
    private var mediaPlayer: MediaPlayer? = null
    private var vibrator: Vibrator? = null
    fun giveNotice(noticeFlag: NoticeFlag){
        when(noticeFlag) {
            AlarmManagerUtil.OnlySound -> {
                ring()
            }
            AlarmManagerUtil.OnlyVibrator -> {
                vibrate()
            }
            AlarmManagerUtil.BothSoundAndVibrator -> {
                ring()
                vibrate()
            }
        }
    }
    fun stopNotice(noticeFlag: NoticeFlag){
        when(noticeFlag){
            AlarmManagerUtil.OnlySound -> {
                mediaPlayer?.stop()
            }
            AlarmManagerUtil.OnlyVibrator -> {
                vibrator?.cancel()
            }
            AlarmManagerUtil.BothSoundAndVibrator -> {
                mediaPlayer?.stop()
                vibrator?.cancel()
            }
        }
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