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

@Suppress("DEPRECATION")
class SelectCycleFlagPopup : View.OnClickListener {
    private var binding :SelectCyclePopWindowBinding
    lateinit var mPopupWindow :PopupWindow
    var selectCyclePopupOnClickListener :SelectCyclePopupOnClickListener? = null
    private var mContext: Context

    constructor(context: Context){
        mContext = context
        binding = SelectCyclePopWindowBinding.bind(
            LayoutInflater.from(mContext)
                          .inflate(R.layout.select_cycle_pop_window, null))
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

    override fun onClick(v: View?) {
        val nav_right = mContext.resources.getDrawable(R.drawable.cycle_check)
        nav_right.setBounds(0, 0, nav_right.minimumWidth, nav_right.minimumHeight)
        when(v?.id){
            R.id.tv_cycle_once->{
                selectCyclePopupOnClickListener?.obtainMessage(9,"")
            }
            R.id.tv_cycle_0->{
                selectCyclePopupOnClickListener?.obtainMessage(8, "")
            }
            R.id.tv_cycle_1->{
                if(binding.tvCycle1.compoundDrawables[2] == null)
                    binding.tvCycle1.setCompoundDrawables(null, null, nav_right, null)
                else
                    binding.tvCycle1.setCompoundDrawables(null, null, null, null)
                selectCyclePopupOnClickListener?.obtainMessage(0, "")
            }
            R.id.tv_cycle_2->{
                if(binding.tvCycle2.compoundDrawables[2] == null)
                    binding.tvCycle2.setCompoundDrawables(null, null, nav_right, null)
                else
                    binding.tvCycle2.setCompoundDrawables(null, null, null, null)
                selectCyclePopupOnClickListener?.obtainMessage(1, "")
            }
            R.id.tv_cycle_3->{
                if(binding.tvCycle3.compoundDrawables[2] == null)
                    binding.tvCycle3.setCompoundDrawables(null, null, nav_right, null)
                else
                    binding.tvCycle3.setCompoundDrawables(null, null, null, null)
                selectCyclePopupOnClickListener?.obtainMessage(2, "")
            }
            R.id.tv_cycle_4->{
                if(binding.tvCycle4.compoundDrawables[2] == null)
                    binding.tvCycle4.setCompoundDrawables(null, null, nav_right, null)
                else
                    binding.tvCycle4.setCompoundDrawables(null, null, null, null)
                selectCyclePopupOnClickListener?.obtainMessage(3, "")
            }
            R.id.tv_cycle_5->{
                if(binding.tvCycle5.compoundDrawables[2] == null)
                    binding.tvCycle5.setCompoundDrawables(null, null, nav_right, null)
                else
                    binding.tvCycle5.setCompoundDrawables(null, null, null, null)
                selectCyclePopupOnClickListener?.obtainMessage(4, "")
            }
            R.id.tv_cycle_6->{
                if(binding.tvCycle6.compoundDrawables[2] == null)
                    binding.tvCycle6.setCompoundDrawables(null, null, nav_right, null)
                else
                    binding.tvCycle6.setCompoundDrawables(null, null, null, null)
                selectCyclePopupOnClickListener?.obtainMessage(5, "")
            }
            R.id.tv_cycle_7->{
                if(binding.tvCycle7.compoundDrawables[2] == null)
                    binding.tvCycle7.setCompoundDrawables(null, null, nav_right, null)
                else
                    binding.tvCycle7.setCompoundDrawables(null, null, null, null)
                selectCyclePopupOnClickListener?.obtainMessage(6, "")
            }
            R.id.tv_cycle_sure->{
                val CheckValue = { tv: TextView ->
                    if(tv.compoundDrawables[2] == null) 0 else 1
                }
                val remind =
                        ( (CheckValue(binding.tvCycle1) shl 1)
                        + (CheckValue(binding.tvCycle2) shl 2)
                        + (CheckValue(binding.tvCycle3) shl 3)
                        + (CheckValue(binding.tvCycle4) shl 4)
                        + (CheckValue(binding.tvCycle5) shl 5)
                        + (CheckValue(binding.tvCycle6) shl 6)
                        + (CheckValue(binding.tvCycle7) shl 7))
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
    fun setOnClickListener(l: SelectCyclePopupOnClickListener){
        this.selectCyclePopupOnClickListener = l
    }
    fun showPopup(rootView: View) {
        // 第一个参数是要将PopupWindow放到的View，第二个参数是位置，第三第四是偏移值
        mPopupWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0)
    }
}
interface SelectCyclePopupOnClickListener {
    fun obtainMessage(flag :Int, ret :String);
}