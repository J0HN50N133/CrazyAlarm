package com.crazy.crazyalarm.closeModeActivity.jigsaw.game

import android.content.Context
import android.widget.Toast
import com.crazy.crazyalarm.R
import com.crazy.crazyalarm.closeModeActivity.jigsaw.ui.PuzzleLayout
import com.crazy.crazyalarm.closeModeActivity.jigsaw.ui.PuzzleLayout.SuccessListener

//import android.support.annotation.NonNull;
class PuzzleGame(context: Context, puzzleLayout: PuzzleLayout) : Game, SuccessListener {
    private val puzzleLayou: PuzzleLayout?
    private var stateListener: GameStateListener? = null
    private val context: Context
    fun addGameStateListener(stateListener: GameStateListener?) {
        this.stateListener = stateListener
    }

    private fun checkNull(): Boolean {
        return puzzleLayou == null
    }

    override fun addLevel() {
        if (checkNull()) {
            return
        }
        if (!puzzleLayou!!.addCount()) {
            Toast.makeText(
                context,
                context.getString(R.string.already_the_most_level),
                Toast.LENGTH_SHORT
            ).show()
        }
        if (stateListener != null) {
            stateListener!!.setLevel(level)
        }
    }

    override fun reduceLevel() {
        if (checkNull()) {
            return
        }
        if (!puzzleLayou!!.reduceCount()) {
            Toast.makeText(
                context,
                context.getString(R.string.already_the_less_level),
                Toast.LENGTH_SHORT
            ).show()
        }
        if (stateListener != null) {
            stateListener!!.setLevel(level)
        }
    }

    //    @Override
    //    public void changeMode(String gameMode) {
    //        puzzleLayou.changeMode(gameMode);
    //    }
    override fun changeImage(res: Int) {
        puzzleLayou!!.changeRes(res)
    }

    val level: Int
        get() {
            if (checkNull()) {
                return 0
            }
            val count = puzzleLayou!!.count
            return count - 3 + 1
        }

    override fun success() {
        if (stateListener != null) {
            stateListener!!.gameSuccess(level)
        }
    }

    interface GameStateListener {
        fun setLevel(level: Int)
        fun gameSuccess(level: Int)
    }

    init {
        this.context = context.applicationContext
        puzzleLayou = puzzleLayout
        puzzleLayou.addSuccessListener(this)
    }
}