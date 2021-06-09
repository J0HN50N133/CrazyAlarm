package com.crazy.crazyalarm.view

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.TextView
import com.crazy.crazyalarm.R
import com.crazy.crazyalarm.databinding.SelectCyclePopWindowBinding
import org.w3c.dom.Text

@Suppress("DEPRECATION")
class SelectCycleFlagPopup : View.OnClickListener {
    private var binding :SelectCyclePopWindowBinding
    private lateinit var mPopupWindow :PopupWindow
    private var mContext: Context
    var selectCyclePopupOnClickListener :SelectCyclePopupOnClickListener? = null

    constructor(context: Context){
        mContext = context
        binding = SelectCyclePopWindowBinding.bind(
            LayoutInflater.from(mContext).inflate(R.layout.select_cycle_pop_window, null))
        mPopupWindow = PopupWindow(context).apply {
            setBackgroundDrawable(BitmapDrawable())
            width = WindowManager.LayoutParams.MATCH_PARENT
            height = WindowManager.LayoutParams.MATCH_PARENT
            isTouchable = true
            isFocusable = true
            isOutsideTouchable = true
            contentView = binding.root
            contentView.setOnTouchListener { _, _ ->
                mPopupWindow.isFocusable = false
                true
            }
        }
        binding.tvCycle0.setOnClickListener(this)
        binding.tvCycle1.setOnClickListener(this)
        binding.tvCycle2.setOnClickListener(this)
        binding.tvCycle3.setOnClickListener(this)
        binding.tvCycle4.setOnClickListener(this)
        binding.tvCycle5.setOnClickListener(this)
        binding.tvCycle6.setOnClickListener(this)
        binding.tvCycle7.setOnClickListener(this)
        binding.tvCycleSure.setOnClickListener(this)
        binding.tvCycleOnce.setOnClickListener(this)
    }
    fun checkAnItem (view: TextView){
        val nav_right = mContext.resources.getDrawable(R.drawable.cycle_check)
        nav_right.setBounds(0, 0, nav_right.minimumWidth, nav_right.minimumHeight)
        if(view.compoundDrawables[2] == null)
            view.setCompoundDrawables(null, null, nav_right, null)
        else
            view.setCompoundDrawables(null, null, null, null)
    }
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.tv_cycle_once->{
                selectCyclePopupOnClickListener?.obtainMessage(9,"")
            }
            R.id.tv_cycle_0->{
                selectCyclePopupOnClickListener?.obtainMessage(8, "")
            }
            R.id.tv_cycle_1->{
                checkAnItem(binding.tvCycle1)
                selectCyclePopupOnClickListener?.obtainMessage(0, "")
            }
            R.id.tv_cycle_2->{
                checkAnItem(binding.tvCycle2)
                selectCyclePopupOnClickListener?.obtainMessage(1, "")
            }
            R.id.tv_cycle_3->{
                checkAnItem(binding.tvCycle3)
                selectCyclePopupOnClickListener?.obtainMessage(2, "")
            }
            R.id.tv_cycle_4->{
                checkAnItem(binding.tvCycle4)
                selectCyclePopupOnClickListener?.obtainMessage(3, "")
            }
            R.id.tv_cycle_5->{
                checkAnItem(binding.tvCycle5)
                selectCyclePopupOnClickListener?.obtainMessage(4, "")
            }
            R.id.tv_cycle_6->{
                checkAnItem(binding.tvCycle6)
                selectCyclePopupOnClickListener?.obtainMessage(5, "")
            }
            R.id.tv_cycle_7->{
                checkAnItem(binding.tvCycle7)
                selectCyclePopupOnClickListener?.obtainMessage(6, "")
            }
            R.id.tv_cycle_sure->{
                val checkValue = { tv: TextView ->
                    if(tv.compoundDrawables[2] == null) 0 else 1
                }
                val remind =
                        ( (checkValue(binding.tvCycle1) shl 0)
                        + (checkValue(binding.tvCycle2) shl 1)
                        + (checkValue(binding.tvCycle3) shl 2)
                        + (checkValue(binding.tvCycle4) shl 3)
                        + (checkValue(binding.tvCycle5) shl 4)
                        + (checkValue(binding.tvCycle6) shl 5)
                        + (checkValue(binding.tvCycle7) shl 6))
                selectCyclePopupOnClickListener?.obtainMessage(7, remind.toString())
                dismiss()
            }
        }
    }
    fun dismiss() {
        if(mPopupWindow.isShowing){
            mPopupWindow.dismiss()
        }
    }
    fun setOnClickListener(obtain: (Int, String) -> Unit){
        this.selectCyclePopupOnClickListener = object : SelectCyclePopupOnClickListener {
            override fun obtainMessage(flag: Int, ret: String) {
                obtain(flag, ret)
            }
        }
    }
    fun showPopup(rootView: View) {
        // 第一个参数是要将PopupWindow放到的View，第二个参数是位置，第三第四是偏移值
        mPopupWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0)
    }
}
interface SelectCyclePopupOnClickListener {
    fun obtainMessage(flag :Int, ret :String);
}