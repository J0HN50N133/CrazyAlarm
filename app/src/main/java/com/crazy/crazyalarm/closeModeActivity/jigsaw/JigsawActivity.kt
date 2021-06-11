package com.crazy.crazyalarm.closeModeActivity.jigsaw

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.crazy.crazyalarm.MainActivity
import com.crazy.crazyalarm.R
import com.crazy.crazyalarm.clockUtils.AlarmManagerUtil
import com.crazy.crazyalarm.clockUtils.BasicRingActivity
import com.crazy.crazyalarm.closeModeActivity.jigsaw.Utils.Utils.readBitmap
import com.crazy.crazyalarm.closeModeActivity.jigsaw.dialog.SelectImageDialog
import com.crazy.crazyalarm.closeModeActivity.jigsaw.dialog.SuccessDialog
import com.crazy.crazyalarm.closeModeActivity.jigsaw.dialog.SuccessDialog.OnButtonClickListener
import com.crazy.crazyalarm.closeModeActivity.jigsaw.game.PuzzleGame
import com.crazy.crazyalarm.closeModeActivity.jigsaw.game.PuzzleGame.GameStateListener
import com.crazy.crazyalarm.closeModeActivity.jigsaw.ui.PuzzleLayout

class JigsawActivity() : BasicRingActivity(), GameStateListener {
    private var puzzleLayout: PuzzleLayout? = null
    private var puzzleGame: PuzzleGame? = null
    private var srcImg: ImageView? = null
    private var tvLevel: TextView? = null
    private var selectImageDialog: SelectImageDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        initView()
        initListener()
        val it = Intent(this, MainActivity::class.java)
        giveNotice(
            it.getIntExtra(
                AlarmManagerUtil.NOTICEFLAG,
                AlarmManagerUtil.BothSoundAndVibrator
            )
        )
    }

    private fun initView() {
        puzzleLayout = findViewById<View>(R.id.puzzleLayout) as PuzzleLayout
        puzzleGame = PuzzleGame(this, (puzzleLayout)!!)
        srcImg = findViewById<View>(R.id.ivSrcImg) as ImageView
        tvLevel = findViewById<View>(R.id.tvLevel) as TextView
        tvLevel!!.text = "难度等级：" + puzzleGame!!.level
        srcImg!!.setImageBitmap(
            readBitmap(
                applicationContext, puzzleLayout!!.res, 4
            )
        )
    }

    private fun initListener() {
        puzzleGame!!.addGameStateListener(this)
        if (selectImageDialog == null) {
            selectImageDialog = SelectImageDialog()
            selectImageDialog!!.addItemClickListener(object :
                SelectImageDialog.OnItemClickListener {
                override fun itemClick(postion: Int, res: Int) {
                    //更新布局
                    puzzleGame!!.changeImage(res)
                    srcImg!!.setImageBitmap(
                        readBitmap(
                            applicationContext, res, 4
                        )
                    )
                }
            })
        }
        srcImg!!.setOnClickListener(View.OnClickListener {
            selectImageDialog!!.showDialog(
                fragmentManager, "dialog", 0
            )
        })
    }

    fun addLevel(view: View?) {
        puzzleGame!!.addLevel()
    }

    fun reduceLevel(view: View?) {
        puzzleGame!!.reduceLevel()
    }

    fun changeImage(view: View?) {}
    override fun setLevel(level: Int) {
        tvLevel!!.text = "难度等级：$level"
    }

    override fun gameSuccess(level: Int) {
        val successDialog = SuccessDialog()
        successDialog.show(fragmentManager, "successDialog")
        successDialog.addButtonClickListener(object : OnButtonClickListener {
            override fun nextLevelClick() {
                puzzleGame!!.addLevel()
                successDialog.dismiss()
            }

            override fun cancelClick() {
                successDialog.dismiss()
            }
        })
    }
}