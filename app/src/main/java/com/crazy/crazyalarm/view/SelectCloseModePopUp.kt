package com.crazy.crazyalarm.view

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.view.*
import android.widget.PopupWindow
import com.crazy.crazyalarm.R
import com.crazy.crazyalarm.clockUtils.AlarmManagerUtil
import com.crazy.crazyalarm.databinding.SelectCloseModePopWindowBinding

@Suppress("DEPRECATION")
class SelectCloseModePopUp : View.OnClickListener{
    private var binding: SelectCloseModePopWindowBinding
    private lateinit var mPopupWindow :PopupWindow
    private var selectCloseModeOnClickListener: SelectCloseModeOnClickListener? = null
    private var mContext: Context
    constructor(context: Context){
        mContext = context
        binding = SelectCloseModePopWindowBinding.bind(
            LayoutInflater.from(mContext).inflate(R.layout.select_close_mode_pop_window, null)
        )
        mPopupWindow = PopupWindow(context).apply {
            setBackgroundDrawable(BitmapDrawable())
            width = WindowManager.LayoutParams.MATCH_PARENT
            height = WindowManager.LayoutParams.MATCH_PARENT
            isTouchable = true
            isFocusable = true
            isOutsideTouchable = true
            contentView = binding.root
            contentView.setOnTouchListener{ _,_ ->
                mPopupWindow.isFocusable = false
                true
            }
        }
        binding.tvMathMode.setOnClickListener(this)
        binding.tvJigsawMode.setOnClickListener(this)
        binding.tvNormMode.setOnClickListener(this)
        binding.tvScanMode.setOnClickListener(this)
    }
    override fun onClick(view: View) {
        when(view.id){
            R.id.tv_norm_mode->{
                selectCloseModeOnClickListener?.obtainMsg(AlarmManagerUtil.Norm)
            }
            R.id.tv_jigsaw_mode->{
                selectCloseModeOnClickListener?.obtainMsg(AlarmManagerUtil.Jigsaw)
            }
            R.id.tv_math_mode->{
                selectCloseModeOnClickListener?.obtainMsg(AlarmManagerUtil.Math)
            }
            R.id.tv_scan_mode->{
                selectCloseModeOnClickListener?.obtainMsg(AlarmManagerUtil.Scan)
            }
        }
    }
    fun dismiss() {
        if (mPopupWindow.isShowing)
            mPopupWindow.dismiss()
    }
    fun setOnClickListener(obtain: (AlarmManagerUtil.Mode)->Unit){
        selectCloseModeOnClickListener = object : SelectCloseModeOnClickListener {
            override fun obtainMsg(mode: AlarmManagerUtil.Mode) {
                obtain(mode)
            }
        }
    }
    fun showPopup(rootView: View) {
        mPopupWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0)
    }
}

interface SelectCloseModeOnClickListener{
    fun obtainMsg(mode: AlarmManagerUtil.Mode)
}