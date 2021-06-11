package com.crazy.crazyalarm.closeModeActivity.jigsaw.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import com.crazy.crazyalarm.R
import com.crazy.crazyalarm.closeModeActivity.jigsaw.Utils.Utils
import com.crazy.crazyalarm.closeModeActivity.jigsaw.module.ImagePiece
import java.util.*

//import android.support.annotation.NonNull;
class PuzzleLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), View.OnClickListener {
    //游戏模式
    private val mGameMode = GAME_MODE_EXCHANGE

    //拼图布局为正方形，宽度为屏幕的宽度
    private var mViewWidth = 0

    //拼图游戏每一行的图片个数(默认为三个)
    var count = 3
        private set

    //每张图片的宽度
    private var mItemWidth = 0

    //拼图游戏bitmap集合
    private lateinit var mImagePieces: MutableList<ImagePiece>

    //用于给每个图片设置大小
    private var layoutParams: LayoutParams? = null

    //大图
    lateinit var bitmap: Bitmap
        private set

    //动画层
    private var mAnimLayout: RelativeLayout? = null

    //小图之间的margin
    private var mMargin = 0

    //这个view的padding
    private var mPadding = 0

    //选中的第一张图片
    private var mFirst: ImageView? = null

    //选中的第二张图片
    private var mSecond: ImageView? = null

    //是否添加了动画层
    private var isAddAnimatorLayout = false

    //是否正在进行动画
    private var isAnimation = false
    var res = R.mipmap.sdhy
        private set

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(mViewWidth, mViewWidth)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        for (i in 0 until childCount) {
            if (getChildAt(i) is ImageView) {
                val imageView = getChildAt(i) as ImageView
                imageView.layout(imageView.left, imageView.top, imageView.right, imageView.bottom)
            } else {
                val relativeLayout = getChildAt(i) as RelativeLayout
                relativeLayout.layout(0, 0, mViewWidth, mViewWidth)
            }
        }
    }

    /**
     * 初始化初始变量
     *
     * @param context
     */
    private fun init(context: Context) {
        mMargin = Utils.dp2px(context, DEFAULT_MARGIN)
        mViewWidth = Utils.getScreenWidth(context)[0]
        mPadding = Utils.getMinLength(
            paddingBottom, paddingLeft, paddingRight, paddingTop
        )
        mItemWidth = (mViewWidth - mPadding * 2 - mMargin * (count - 1)) / count
    }

    /**
     * 将大图切割成多个小图
     */
    private fun initBitmaps() {
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(resources, res)
        }
        mImagePieces = Utils.splitImage(context, bitmap, count, mGameMode)
        sortImagePieces()
    }

    /**
     * 对ImagePieces进行排序
     */
    private fun sortImagePieces() {
        try {
            Collections.sort(mImagePieces) { lhs, rhs -> if (Math.random() > 0.5) 1 else -1 }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (mGameMode == GAME_MODE_NORMAL) {
                //如果是第二种模式就将空图放在最后
                var tempImagePieces: ImagePiece? = null
                var tempIndex = 0
                for (i in mImagePieces!!.indices) {
                    val imagePiece = mImagePieces!![i]
                    if (imagePiece.type == ImagePiece.TYPE_EMPTY) {
                        tempImagePieces = imagePiece
                        tempIndex = i
                        break
                    }
                }
                if (tempImagePieces == null) return
                mImagePieces!!.removeAt(tempIndex)
                mImagePieces!!.add(mImagePieces!!.size, tempImagePieces)
            }
        }
    }

    /**
     * 设置图片的大小和layout的属性
     */
    private fun initBitmapsWidth() {
        var line = 0
        var left = 0
        var top = 0
        var right = 0
        var bottom = 0
        for (i in mImagePieces!!.indices) {
            val imageView = ImageView(context)
            imageView.setImageBitmap(mImagePieces!![i].bitmap)
            layoutParams = LayoutParams(mItemWidth, mItemWidth)
            imageView.layoutParams = layoutParams
            if (i != 0 && i % count == 0) {
                line++
            }
            left = if (i % count == 0) {
                i % count * mItemWidth
            } else {
                i % count * mItemWidth + i % count * mMargin
            }
            top = mItemWidth * line + line * mMargin
            right = left + mItemWidth
            bottom = top + mItemWidth
            imageView.right = right
            imageView.left = left
            imageView.bottom = bottom
            imageView.top = top
            imageView.id = i
            imageView.setOnClickListener(this)
            mImagePieces!![i].imageView = imageView
            addView(imageView)
        }
    }

    //
    //    public void changeMode(@NonNull String gameMode) {
    //        if (gameMode.equals(mGameMode)) {
    //            return;
    //        }
    //        this.mGameMode = gameMode;
    //        reset();
    //    }
    fun reset() {
        mItemWidth = (mViewWidth - mPadding * 2 - mMargin * (count - 1)) / count
        if (mImagePieces != null) {
            mImagePieces!!.clear()
        }
        isAddAnimatorLayout = false
//        bitmap = null
        removeAllViews()
        initBitmaps()
        initBitmapsWidth()
    }

    /**
     * 添加count 最多每行7个
     */
    fun addCount(): Boolean {
        count++
        if (count > 7) {
            count--
            return false
        }
        reset()
        return true
    }

    /**
     * 改变图片
     */
    fun changeRes(res: Int) {
        this.res = res
        reset()
    }

    /**
     * 减少count 最少每行三个，否则普通模式无法游戏
     */
    fun reduceCount(): Boolean {
        count--
        if (count < 3) {
            count++
            return false
        }
        reset()
        return true
    }

    override fun onClick(v: View) {
        if (isAnimation) {
            //还在运行动画的时候，不允许点击
            return
        }
        if (v !is ImageView) {
            return
        }
        if (GAME_MODE_NORMAL == mGameMode) {
            val imagePiece = mImagePieces!![v.id]
            if (imagePiece.type == ImagePiece.TYPE_EMPTY) {
                //普通模式，点击到空图不做处理
                return
            }
            if (mFirst == null) {
                mFirst = v
            }
            checkEmptyImage(mFirst)
        } else {
            //点的是同一个View
            if (mFirst === v) {
                mFirst!!.setColorFilter(Color.parseColor("#FF4081"), PorterDuff.Mode.LIGHTEN)
                mFirst = null
                return
            }
            if (mFirst == null) {
                mFirst = v
                //选中之后添加一层颜色
                mFirst!!.setColorFilter(Color.parseColor("#4169E100"))
                //                mFirst.setColorFilter(Color.parseColor("#55FF0000"));
            } else {
                mSecond = v
                exChangeView()
            }
        }
    }

    private fun checkEmptyImage(imageView: ImageView?) {
        val index = imageView!!.id
        val line = mImagePieces!!.size / count
        var imagePiece: ImagePiece? = null
        if (index < count) {
            //第一行（需要额外计算，下一行是否有空图）
            imagePiece = checkCurrentLine(index)
            //判断下一行同一列的图片是否为空
            imagePiece = checkOtherline(index + count, imagePiece)
        } else if (index < (line - 1) * count) {
            //中间的行（需要额外计算，上一行和下一行是否有空图）
            imagePiece = checkCurrentLine(index)
            //判断上一行同一列的图片是否为空
            imagePiece = checkOtherline(index - count, imagePiece)
            //判断下一行同一列的图片是否为空
            imagePiece = checkOtherline(index + count, imagePiece)
        } else {
            //最后一行（需要额外计算，上一行是否有空图））
            imagePiece = checkCurrentLine(index)
            //检查上一行同一列有没有空图
            imagePiece = checkOtherline(index - count, imagePiece)
        }
        if (imagePiece == null) {
            //周围没有空的imageView
            mFirst = null
            mSecond = null
        } else {
            //记录下第二张ImageView
            mSecond = imagePiece.imageView
            //选中第二个图片，开启动两张图片替换的动画
            exChangeView()
        }
    }

    /**
     * 检查上其他行同一列有没有空图
     *
     * @return
     */
    private fun checkOtherline(index: Int, imagePiece: ImagePiece?): ImagePiece? {
        return imagePiece ?: getCheckEmptyImageView(index)
    }

    /**
     * 检查当前行有没有空的图片
     *
     * @param index
     * @return
     */
    private fun checkCurrentLine(index: Int): ImagePiece? {
        var imagePiece: ImagePiece? = null
        //第一行
        if (index % count == 0) {
            //第一个
            imagePiece = getCheckEmptyImageView(index + 1)
        } else if (index % count == count - 1) {
            //最后一个
            imagePiece = getCheckEmptyImageView(index - 1)
        } else {
            imagePiece = getCheckEmptyImageView(index + 1)
            if (imagePiece == null) {
                imagePiece = getCheckEmptyImageView(index - 1)
            }
        }
        return imagePiece
    }

    private fun getCheckEmptyImageView(index: Int): ImagePiece? {
        val imagePiece = mImagePieces!![index]
        return if (imagePiece.type == ImagePiece.TYPE_EMPTY) {
            //找到空的imageView
            imagePiece
        } else null
    }

    private fun addAnimationImageView(imageView: ImageView?): ImageView {
        val getImage = ImageView(context)
        val firstParams = RelativeLayout.LayoutParams(mItemWidth, mItemWidth)
        firstParams.leftMargin = imageView!!.left - mPadding
        firstParams.topMargin = imageView.top - mPadding
        val firstBitmap = mImagePieces!![imageView.id].bitmap
        getImage.setImageBitmap(firstBitmap)
        getImage.layoutParams = firstParams
        mAnimLayout!!.addView(getImage)
        return getImage
    }

    /**
     * 添加动画层，并且添加平移的动画
     */
    private fun exChangeView() {

        //添加动画层
        setUpAnimLayout()
        //添加第一个图片
        val first = addAnimationImageView(mFirst)
        //添加另一个图片
        val second = addAnimationImageView(mSecond)
        val secondXAnimator = ObjectAnimator.ofFloat(
            second,
            "TranslationX",
            0f,
            -(mSecond!!.left - mFirst!!.left).toFloat()
        )
        val secondYAnimator = ObjectAnimator.ofFloat(
            second,
            "TranslationY",
            0f,
            -(mSecond!!.top - mFirst!!.top).toFloat()
        )
        val firstXAnimator = ObjectAnimator.ofFloat(
            first,
            "TranslationX",
            0f,
            (mSecond!!.left - mFirst!!.left).toFloat()
        )
        val firstYAnimator = ObjectAnimator.ofFloat(
            first,
            "TranslationY",
            0f,
            (mSecond!!.top - mFirst!!.top).toFloat()
        )
        val secondAnimator = AnimatorSet()
        secondAnimator.play(secondXAnimator).with(secondYAnimator).with(firstXAnimator)
            .with(firstYAnimator)
        secondAnimator.duration = 300
        val firstPiece = mImagePieces!![mFirst!!.id]
        val secondPiece = mImagePieces!![mSecond!!.id]
        val firstType = firstPiece.type
        val secondType = secondPiece.type
        val firstBitmap = mImagePieces!![mFirst!!.id].bitmap
        val secondBitmap = mImagePieces!![mSecond!!.id].bitmap
        //        final int firstIndex = mImagePieces.get(mFirst.getId()).getIndex();
//        final int secondIndex = mImagePieces.get(mFirst.getId()).getIndex();
        secondAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                val fristIndex = firstPiece.index
                val secondeIndex = secondPiece.index
                if (mFirst != null) {
                    mFirst!!.setColorFilter(Color.parseColor("#FF4081"),PorterDuff.Mode.LIGHTEN)
                    mFirst!!.visibility = VISIBLE
                    mFirst!!.setImageBitmap(secondBitmap)
                    firstPiece.bitmap = secondBitmap
                    firstPiece.index = secondeIndex
                }
                if (mSecond != null) {
                    mSecond!!.visibility = VISIBLE
                    mSecond!!.setImageBitmap(firstBitmap)
                    secondPiece.bitmap = firstBitmap
                    secondPiece.index = fristIndex
                }
                if (mGameMode == GAME_MODE_NORMAL) {
                    firstPiece.type = secondType
                    secondPiece.type = firstType
                }
                mAnimLayout!!.removeAllViews()
                mAnimLayout!!.visibility = GONE
                mFirst = null
                mSecond = null
                isAnimation = false
                invalidate()
                if (checkSuccess()) {
                    Toast.makeText(context, "成功!", Toast.LENGTH_SHORT).show()
                    if (mSuccessListener != null) {
                        mSuccessListener!!.success()
                    }
                }
            }

            override fun onAnimationStart(animation: Animator) {
                super.onAnimationStart(animation)
                isAnimation = true
                mAnimLayout!!.visibility = VISIBLE
                mFirst!!.visibility = INVISIBLE
                mSecond!!.visibility = INVISIBLE
            }
        })
        secondAnimator.start()
    }

    /**
     * 构造动画层 用于点击之后的动画
     * 为什么要做动画层？ 要保证动画在整个view上面执行。
     */
    private fun setUpAnimLayout() {
        if (mAnimLayout == null) {
            mAnimLayout = RelativeLayout(context)
        }
        if (!isAddAnimatorLayout) {
            isAddAnimatorLayout = true
            addView(mAnimLayout)
        }
    }

    /**
     * 检测是否成功
     */
    private fun checkSuccess(): Boolean {
        var isSuccess = true
        for (i in mImagePieces!!.indices) {
            val imagePiece = mImagePieces!![i]
            if (i != imagePiece.index) {
                isSuccess = false
            }
        }
        return isSuccess
    }

    private var mSuccessListener: SuccessListener? = null
    fun addSuccessListener(successListener: SuccessListener?) {
        mSuccessListener = successListener
    }

    interface SuccessListener {
        fun success()
    }

    companion object {
        const val GAME_MODE_NORMAL = "gameModeNormal"
        const val GAME_MODE_EXCHANGE = "gameModeExchange"
        private const val DEFAULT_MARGIN = 3
    }

    init {
        init(context)
        initBitmaps()
        initBitmapsWidth()
    }
}