package com.crazy.crazyalarm.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.TextView
import com.crazy.crazyalarm.R
import com.crazy.crazyalarm.clockUtils.AlarmManagerUtil
import com.crazy.crazyalarm.databinding.SelectNoticeFlagPopWindowBinding

class SelectNoticeFlagPopup: OnClickListener {
    var binding: SelectNoticeFlagPopWindowBinding
    private var mPopupWindow: PopupWindow? = null
    private var selectNoticeWayPopupOnClickListener: SelectNoticeWayPopupOnClickListener? = null
    private var mContext: Context

    constructor(context: Context){
        mContext = context
        binding = SelectNoticeFlagPopWindowBinding.bind(
            LayoutInflater.from(mContext).inflate(R.layout.select_notice_flag_pop_window, null)
        )
        mPopupWindow = PopupWindow(context).apply {
            width = WindowManager.LayoutParams.MATCH_PARENT
            height = WindowManager.LayoutParams.MATCH_PARENT
            isTouchable = true
            isFocusable = true
            isOutsideTouchable = true
            contentView = binding.root
            contentView.setOnTouchListener { _, _ ->
                isFocusable = false
                dismiss()
                true
            }
        }
        binding.tvDrugway1.setOnClickListener(this)
        binding.tvDrugway2.setOnClickListener(this)
        binding.tvDrugway3.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when(view?.id) {
            R.id.tv_drugway_1->
                selectNoticeWayPopupOnClickListener?.obtainMessage(AlarmManagerUtil.OnlyVibrator)
            R.id.tv_drugway_2->
                selectNoticeWayPopupOnClickListener?.obtainMessage(AlarmManagerUtil.OnlySound)
            R.id.tv_drugway_3->
                selectNoticeWayPopupOnClickListener?.obtainMessage(AlarmManagerUtil.BothSoundAndVibrator)
        }
    }
    fun setOnclickListener(obtain: (AlarmManagerUtil.NoticeFlag) -> Unit){
        this.selectNoticeWayPopupOnClickListener = object :SelectNoticeWayPopupOnClickListener {
            override fun obtainMessage(flag: AlarmManagerUtil.NoticeFlag) {
                obtain(flag)
            }
        }
    }
    fun dismiss() {
        if(mPopupWindow?.isShowing == true) {
            mPopupWindow?.dismiss()
        }
    }
    fun showPopup(rootView: View){
        mPopupWindow?.showAtLocation(rootView, Gravity.BOTTOM, 0, 0)
    }
}

interface SelectNoticeWayPopupOnClickListener {
    fun obtainMessage(obtain: AlarmManagerUtil.NoticeFlag)
}