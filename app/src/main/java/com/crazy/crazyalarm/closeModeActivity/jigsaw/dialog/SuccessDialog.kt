package com.crazy.crazyalarm.closeModeActivity.jigsaw.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import com.crazy.crazyalarm.R

class SuccessDialog : DialogFragment() {
    var buttonClickListener: OnButtonClickListener? = null
    override fun onCreateDialog(savedInstanceState: Bundle): Dialog {
        val builder = AlertDialog.Builder(activity)
            .setTitle(getString(R.string.success))
        builder.setMessage(getString(R.string.success_description))
            .setPositiveButton(getString(R.string.next_level)) { dialog, which ->
                if (buttonClickListener != null) {
                    buttonClickListener!!.nextLevelClick()
                }
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, which ->
                if (buttonClickListener != null) {
                    buttonClickListener!!.cancelClick()
                }
            }
        return builder.create()
    }

    fun addButtonClickListener(listener: OnButtonClickListener?) {
        buttonClickListener = listener
    }

    interface OnButtonClickListener {
        fun nextLevelClick()
        fun cancelClick()
    }
}