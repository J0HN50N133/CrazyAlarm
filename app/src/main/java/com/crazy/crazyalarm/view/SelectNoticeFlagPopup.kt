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

class SelectNoticeFlagPopup @SuppressLint("ClickableViewAccessibility") constructor(context: Context) : OnClickListener {
    private lateinit var notice_way1: TextView
    private lateinit var notice_way2: TextView
    private val mPopupWindow: PopupWindow?
    private var selectNoticeWayPopupOnClickListener: SelectNoticeWayPopupOnClickListener? = null
    private val mContext: Context = context

    init {
        mPopupWindow = PopupWindow(context).apply {
            width = WindowManager.LayoutParams.MATCH_PARENT
            height = WindowManager.LayoutParams.MATCH_PARENT
            isTouchable = true
            isFocusable = true
            isOutsideTouchable = true
            contentView = initViews()
            contentView.setOnTouchListener { _, _ ->
                isFocusable = false
                dismiss()
                true
            }
        }
    }

    private fun initViews(): View {
        val view = LayoutInflater.from(mContext).inflate(R.layout.select_notice_flag_pop_window, null)
        notice_way1 = view.findViewById(R.id.tv_drugway_1) as TextView
        notice_way2 = view.findViewById(R.id.tv_drugway_2) as TextView
        notice_way1.setOnClickListener(this)
        notice_way2.setOnClickListener(this)
        return view
    }

    override fun onClick(view: View?) {
        when(view?.id) {
            R.id.tv_drugway_1->
                selectNoticeWayPopupOnClickListener?.obtainMessage(0)
            R.id.tv_drugway_2->
                selectNoticeWayPopupOnClickListener?.obtainMessage(1)
        }
        dismiss()
    }
    fun setOnclickListener(l: SelectNoticeWayPopupOnClickListener){
        this.selectNoticeWayPopupOnClickListener = l
    }
    fun dismiss() {
        if(mPopupWindow?.isShowing == true) {
            mPopupWindow.dismiss()
        }
    }
    fun showPopup(rootView: View){
        mPopupWindow?.showAtLocation(rootView, Gravity.BOTTOM, 0, 0)
    }
}

interface SelectNoticeWayPopupOnClickListener {
    fun obtainMessage(flag: Int)
}