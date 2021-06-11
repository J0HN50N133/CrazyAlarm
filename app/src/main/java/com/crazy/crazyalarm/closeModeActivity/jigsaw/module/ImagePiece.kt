package com.crazy.crazyalarm.closeModeActivity.jigsaw.module

import android.graphics.Bitmap
import android.widget.ImageView

class ImagePiece {
    var type = TYPE_NORMAL
    var index = 0
    var bitmap: Bitmap? = null
    var imageView: ImageView? = null
    override fun toString(): String {
        return super.toString()
    }

    companion object {
        const val TYPE_NORMAL = 0
        const val TYPE_EMPTY = 1
    }
}