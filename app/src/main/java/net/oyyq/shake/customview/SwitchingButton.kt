package net.oyyq.shake.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.graphics.toColorInt
import net.oyyq.shake.R
import net.oyyq.shake.dp
import net.oyyq.shake.sp


class switchingButton @JvmOverloads
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int = 0)
    : View(context, attrs, defStyleAttr){

        private var mStrokeRadius:Float?=null
        private var mStrokeWidth: Float? = null
        private var mTextSize: Float? = null
        private var mSelectedColor:Int ? = null
        private var mSelectedTab:Int ? = null
        private var mFontMetrics:Paint.FontMetrics?= null
        private var mTextHeightOffset : Float? = null
        private var perWidth:Int? = null

        private var mTabTexts = arrayOf("L", "R")       //Kotlin初始化数组的方法
        private var mTabNum = mTabTexts.size


        private var DEFAULT_WIDTH_DP:Int = 120
        private var DEFAULT_HEIGHT_DP:Int = 40


        val mStrokePaint = Paint()         //anti_alias_flag 去毛边
        val mFillPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        val mSelectedTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        val mUnselectedTextPaint = Paint()


        init{
            val ta = context.obtainStyledAttributes(attrs, R.styleable.switchingMultiButton)
            mStrokeRadius= ta.getDimension(R.styleable.switchingMultiButton_strokeRadius, 0f.dp)
            mStrokeWidth = ta.getDimension(R.styleable.switchingMultiButton_strokeWidth, 0f.dp)
            mTextSize = ta.getDimension(R.styleable.switchingMultiButton_txSize2, 0f.sp)
            mSelectedColor = ta.getColor(R.styleable.switchingMultiButton_selectedColor, "#99000000".toColorInt())
            mSelectedTab = ta.getInt(R.styleable.switchingMultiButton_selectedTab, 0)

            initPaint()
        }



    fun initPaint(){
        mStrokePaint.apply {
            color = mSelectedColor!!
            style = Paint.Style.STROKE
            isAntiAlias = true
            strokeWidth = mStrokeWidth!!
        }


        mFillPaint.apply {
            color = mSelectedColor!!
            style = Paint.Style.FILL_AND_STROKE
            isAntiAlias = true
        }


        mSelectedTextPaint.apply {
            textSize = mTextSize!!
            color = "#ffffffff".toColorInt()
            isAntiAlias = true
        }

        mUnselectedTextPaint.apply {
            textSize = mTextSize!!
            color = "#99000000".toColorInt()
            isAntiAlias = true
        }

        //不知道这个 ?
        mTextHeightOffset = -(mSelectedTextPaint.ascent() + mSelectedTextPaint.descent())/2f
        mFontMetrics = mSelectedTextPaint.getFontMetrics()

    }


    //解决wrap_content下的尺寸设置, 这个时候父布局已经给switchingButton测量好了
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(getExpectSize(getDefaultWidth(), widthMeasureSpec), getExpectSize(getDefaultHeight(), heightMeasureSpec))
    }


    //随着字体大小变化, 变换长度的default width, 简单加减乘除
    @RequiresApi(Build.VERSION_CODES.N)
    fun getDefaultWidth():Int{
        var tabTextWidth = 0f
        for (s in mTabTexts){
            tabTextWidth = Math.max(tabTextWidth, mSelectedTextPaint.measureText(s))
        }
        return ((tabTextWidth+paddingLeft+paddingRight+ mStrokeWidth!!)*mTabTexts.size).toInt()
    }

    fun getDefaultHeight():Int {
        return (mFontMetrics!!.bottom - mFontMetrics!!.top + paddingTop + paddingBottom).toInt()
    }


    fun getExpectSize(size: Int, measureSpec: Int):Int{
        var result = size
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)

        when(specMode){
            MeasureSpec.EXACTLY -> result = specSize
            MeasureSpec.UNSPECIFIED -> result = size
            MeasureSpec.AT_MOST -> result = Math.min(size, specSize)
        }
        return result
    }



    //现在在mWidth, mHeight范围内进行画图, mWidth, mHeight未必占满整个屏幕
    override fun onDraw(canvas: Canvas) {
        var left = mStrokeWidth!!*0.5f
        var top = mStrokeWidth!!*0.5f
        var right = width - mStrokeWidth!!*0.5f
        var bottom = height - mStrokeWidth!!*0.5f
        val rectF = RectF(left, top, right, bottom)

        //px为单位,
        //canvas.drawRoundRect(rectF, mStrokeRadius!!, mStrokeRadius!!, mStrokePaint)
        perWidth = (width-mStrokeWidth!!.toInt()) / mTabTexts.size
        if(mStrokeRadius!! > 0) {
            canvas.drawLine(left + perWidth!! * 1f, top, (right - perWidth!!) * 1f, top, mStrokePaint)
            canvas.drawLine(left + perWidth!! * 1f, bottom, (right - perWidth!!) * 1f, bottom, mStrokePaint)
        }else {
            canvas.drawRoundRect(rectF, 10.dp, 10.dp, mStrokePaint)
        }


        for(i in 1..mTabTexts.size){

            if(i != mTabTexts.size) canvas.drawLine(left + perWidth!! * i * 1f, top, left + perWidth!! * i * 1f, bottom, mStrokePaint)

            val tabText = mTabTexts[i - 1]
            val tabTextWidth = mSelectedTextPaint.measureText(tabText)      //字符串在画笔测量下所占宽度

            val selected = (i == mSelectedTab)
            if(i == 1){
                if(mStrokeRadius!! > 0) drawleftPath(canvas, left, top, bottom, selected)
            } else if (i == mTabTexts.size){
                if(mStrokeRadius!! > 0) drawRightPath(canvas, right, top, bottom, selected)
            } else {
                if(selected)  canvas.drawRect(RectF(left + (i - 1) * perWidth!!, top, left + i * perWidth!!, bottom), mFillPaint)
            }

            //draw selected text
            var txLeft = left+(i-1)*perWidth!! + (perWidth!!-tabTextWidth)/2f
            if(selected){
                canvas.drawText(tabText, txLeft, height / 2f + mTextHeightOffset!!, mSelectedTextPaint)
            }else {
                //draw unselected text
                canvas.drawText(tabText, txLeft, height / 2f + mTextHeightOffset!!, mUnselectedTextPaint)
            }
        }
    }



    fun drawleftPath(canvas: Canvas, left: Float, top: Float, bottom: Float, selected: Boolean){
        var leftPath = Path()
        leftPath.moveTo(left + mStrokeRadius!!, top)
        leftPath.lineTo(left + perWidth!! * 1f, top)
        leftPath.lineTo(left + perWidth!! * 1f, bottom)
        leftPath.lineTo(left + mStrokeRadius!!, bottom)

        leftPath.arcTo(RectF(left, bottom - 2 * mStrokeRadius!!, left + 2 * mStrokeRadius!!, bottom), 90f, 90f)
        leftPath.lineTo(left, top + mStrokeRadius!!)
        leftPath.arcTo(RectF(left, top, left + 2 * mStrokeRadius!!, top + 2 * mStrokeRadius!!), 180f, 90f)

        if (selected) {
            canvas.drawPath(leftPath, mFillPaint)
            canvas.drawPath(leftPath, mStrokePaint)
        }
        else canvas.drawPath(leftPath, mStrokePaint)
    }


    fun drawRightPath(canvas: Canvas, right: Float, top: Float, bottom: Float, selected: Boolean){
        var rightPath = Path()

        rightPath.moveTo((right - perWidth!!) * 1f, top)
        rightPath.lineTo(right - mStrokeRadius!!, top)
        rightPath.arcTo(RectF(right - 2 * mStrokeRadius!!, top, right, top + 2 * mStrokeRadius!!), -90f, 90f)
        rightPath.lineTo(right, bottom - mStrokeRadius!!)
        rightPath.arcTo(RectF(right - 2 * mStrokeRadius!!, bottom - 2 * mStrokeRadius!!, right, bottom), 0f, 90f)
        rightPath.lineTo((right - perWidth!!) * 1f, bottom)
        rightPath.lineTo((right - perWidth!!) * 1f, top)

        if (selected) {
            canvas.drawPath(rightPath, mFillPaint)
            canvas.drawPath(rightPath, mStrokePaint)
        }
        else canvas.drawPath(rightPath, mStrokePaint)
    }


    fun setText(vararg tagTexts: String?): switchingButton? {
        return if (tagTexts.size > 1) {
            mTabTexts = tagTexts as Array<String>
            mTabNum = tagTexts.size
            requestLayout()
            this
        } else {
            throw IllegalArgumentException("the size of tagTexts should greater then 1")
        }
    }



    override fun onTouchEvent(event: MotionEvent): Boolean {
        if(event.action == MotionEvent.ACTION_DOWN)
            return true
        if(event.action == MotionEvent.ACTION_UP){
            var x = event.getX()
            for(i in 0..mTabTexts.size-1){
                if(x > perWidth!!*i && x < perWidth!!*(i+1)){
                    if(mSelectedTab == (i+1) ) return true
                    else {
                        mSelectedTab = i + 1
                        invalidate()
                        return true
                    }
                }
            }
        }

        return super.onTouchEvent(event)
    }


}