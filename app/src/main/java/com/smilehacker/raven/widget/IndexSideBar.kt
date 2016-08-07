package com.smilehacker.raven.widget
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

import com.smilehacker.raven.R

/**
 * Created by kleist on 14-9-21.
 */
class IndexSideBar : View {

    private var mViewHeight: Int = 0
    private var mViewWidth: Int = 0

    private val mIndexTextPaint: Paint by lazy { Paint() }
    private val mSideIndexBgPaint: Paint by lazy { Paint() }

    private var mIsPressed: Boolean = false
    private var mCurrentPos = 0

    private var mListener: OnIndexListener? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    fun setOnIndexListener(listener: OnIndexListener) {
        mListener = listener
    }

    var selectIndex : Char = IndexChar[0].first()
        set(value) {
            if (value != field ) {
                invalidate()
            }
            field = value
        }

    private fun init() {
        mIndexTextPaint.isAntiAlias = true
        mIndexTextPaint.textAlign = Paint.Align.CENTER

        mSideIndexBgPaint.color = resources.getColor(R.color.transparent)
        mSideIndexBgPaint.style = Paint.Style.FILL
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw = paddingLeft + paddingRight + suggestedMinimumWidth
        val w = View.resolveSizeAndState(minw, widthMeasureSpec, 1)

        val minh = paddingTop + paddingBottom + suggestedMinimumHeight
        val h = View.resolveSizeAndState(minh, heightMeasureSpec, 1)

        if (mViewHeight != h || mViewWidth != w) {
            mViewHeight = h
            mViewWidth = w
            invalidate()
        }

        setMeasuredDimension(w, h)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        drawSideIndex(canvas)
        drawSideBackground(canvas)
    }

    private fun drawSideIndex(canvas: Canvas) {
        val height = mViewHeight / IndexChar.size
        mIndexTextPaint.textSize = (height / 2).toFloat()

        for (i in IndexChar.indices) {
            if (IndexChar[i].first().toLowerCase() == selectIndex.toLowerCase()) {
                mIndexTextPaint.color = resources.getColor(R.color.blu_80)
                mIndexTextPaint.textSize = 28f
            } else {
                mIndexTextPaint.color = resources.getColor(R.color.black)
                mIndexTextPaint.textSize = 20f
            }
            canvas.drawText(IndexChar[i], (mViewWidth / 2).toFloat(), (height * (i + 0.6)).toFloat(), mIndexTextPaint)
        }
    }

    private fun drawSideBackground(canvas: Canvas) {
        if (mIsPressed) {
            canvas.drawRect(0f, 0f, mViewWidth.toFloat(), mViewHeight.toFloat(), mSideIndexBgPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mIsPressed = true
                invalidate()

                calculateTouchPos(y)

                if (mListener != null) {
                    mListener!!.onTouchDown()
                }
            }
            MotionEvent.ACTION_MOVE -> {
                invalidate()
                calculateTouchPos(y)
            }
            MotionEvent.ACTION_UP -> {
                mIsPressed = false
                invalidate()

                if (mListener != null) {
                    mListener!!.onTouchUp()
                }
            }
        }
        return true
    }

    private fun calculateTouchPos(y: Float) {
        val height = mViewHeight / IndexChar.size
        var pos = (y / height).toInt()
        pos = if (pos >= IndexChar.size) IndexChar.size - 1 else pos
        pos = if (pos < 0) 0 else pos
        if (mCurrentPos != pos) {
            mCurrentPos = pos
            mListener?.onSelect(pos, IndexChar[pos])
        }
    }

    interface OnIndexListener {
        fun onSelect(pos: Int, index: String)

        fun onTouchDown()

        fun onTouchUp()
    }

    companion object {
        val IndexChar = arrayOf("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z")
    }
}
