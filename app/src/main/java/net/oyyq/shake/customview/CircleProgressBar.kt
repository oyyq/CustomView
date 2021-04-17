package net.oyyq.shake.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import androidx.core.graphics.toColorInt
import net.oyyq.shake.R
import net.oyyq.shake.dp
import net.oyyq.shake.sp


/**
 * 圆形(不是Path线)进度条
 * 1. 圆环颜色, 半径, 宽度
 * 2. 进度颜色, 宽度
 * 3. 字体颜色, 宽度
 */
@RequiresApi(Build.VERSION_CODES.O)
class CircleProgressBar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0):
    ProgressBar(context, attrs, defStyleAttr){

    private val DEFAULT_TEXT_COLOR = "#99000000"
    private val DEFAULT_TEXT_SIZE = 14
    private val DEFAULT_UNREACH_COLOR = "#99990000"
    private val DEFAULT_UNREACH_HEIGHT = 2
    private val DEFAULT_REACH_COLOR = DEFAULT_TEXT_COLOR
    private val DEFAULT_REACH_HEIGHT = 3
    private val DEFAULT_RADIUS = 32


    protected var mTextColor : Int?= null
    protected var mTextSize : Float ?= null
    protected var mUnreachColor : Int? = null
    protected var mUnreachHeight : Float? = null
    protected var mReachColor : Int? = null
    protected var mReachHeight : Float ?= null
    protected var mRadius : Float? = null


    private val paint = Paint()

    init {
        Log.e("color1", "#666666".toColorInt().toString())
        Log.e("color2", "#99666600".toColorInt().toString())

        val ta = context.obtainStyledAttributes(attrs, R.styleable.ddProgressBar)
        mTextColor = ta.getColor(R.styleable.ddProgressBar_txColor, "#99666600".toColorInt())
        mTextSize = ta.getDimension(R.styleable.ddProgressBar_txSize, DEFAULT_TEXT_SIZE.sp)

        mUnreachColor = ta.getColor(R.styleable.ddProgressBar_circleColor, DEFAULT_UNREACH_COLOR.toColorInt())
        mUnreachHeight = ta.getDimension(R.styleable.ddProgressBar_circleWidth, DEFAULT_UNREACH_HEIGHT.dp)

        mReachColor = ta.getColor(R.styleable.ddProgressBar_proColor, DEFAULT_REACH_COLOR.toColorInt())
        mReachHeight = ta.getDimension(R.styleable.ddProgressBar_proWidth, DEFAULT_REACH_HEIGHT.dp)

        mRadius = ta.getDimension(R.styleable.ddProgressBar_circleRadius, DEFAULT_RADIUS.dp)

        ta.recycle()
        paint.apply {
            isAntiAlias = true                  //防锯齿
            textSize = mTextSize as Float
            isDither = true
            style = Paint.Style.STROKE          //空心
            strokeCap = Paint.Cap.ROUND
        }

    }


    //onMeasure不复写, 但你要清楚, CircleProgressBar在布局中的layout_height & layout_width 是wrap_content是要怎么办


    override fun onDraw(canvas: Canvas) {
        var text = progress.toString() + "%"
        val textWeight: Float = paint.measureText(text)
        val textHeight: Float = (paint.descent() + paint.ascent()) / 2

        paint.setStyle(Paint.Style.STROKE);
        mUnreachColor?.let { paint.setColor(it) };
        mUnreachHeight?.let { paint.setStrokeWidth(it) };
        mRadius?.let { canvas.drawCircle(width / 2f, height / 2f, it, paint) };


        paint.setStyle(Paint.Style.FILL)
        paint.setColor(mTextColor!!.toInt())
        canvas.drawText(text,width/2-textWeight/2, height/2-textHeight/2,paint);


        paint.setStyle(Paint.Style.STROKE);
        mReachColor?.let { paint.setColor(it) };
        mReachHeight?.let { paint.setStrokeWidth(it) };

        val sweepAngle = progress * 1.0f / max * 360
        canvas.drawArc(RectF(width / 2 - mRadius!!, height / 2 - mRadius!!, width / 2 + mRadius!! , height / 2 + mRadius!!), (-90).toFloat(), sweepAngle, false, paint)

    }



}