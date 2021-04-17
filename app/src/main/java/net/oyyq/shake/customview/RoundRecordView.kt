package net.oyyq.shake.customview

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.graphics.toColorInt
import net.oyyq.shake.R
import net.oyyq.shake.dp

class RoundRecordView(context : Context, attrs : AttributeSet) :
        View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mPadding : Float
    private var mRoundCorner: Float
    private var mCoverColor : Int //遮罩的颜色
    private val porterDuffXfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OUT)
    private val bounds = RectF()
    private val clipPath = Path()

    init{
        //开启View级别的离屏缓冲, 并关闭硬件加速, 使用软件绘制
        setLayerType(LAYER_TYPE_HARDWARE, null)
        val ta = context.obtainStyledAttributes(attrs, R.styleable.ddRoundView)
        mPadding = ta.getDimension(R.styleable.ddRoundView_roundPadding, 40.dp)
        mRoundCorner = ta.getDimension(R.styleable.ddRoundView_roundCorner, 10.dp)
        mCoverColor = ta.getColor(R.styleable.ddRoundView_roundCoverColor, "#99000000".toColorInt())

        ta.recycle()
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
//        bounds.set(0f, 0f, width.toFloat(), height.toFloat())
//        clipPath.addRoundRect(mPadding, mPadding, width-mPadding, height-mPadding, mRoundCorner, mRoundCorner, Path.Direction.CW)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onDraw(canvas: Canvas) {

        //destination image
        canvas.drawRoundRect(mPadding, mPadding, width-mPadding, height-mPadding, mRoundCorner, mRoundCorner, paint)
        paint.color = mCoverColor
        paint.xfermode = porterDuffXfermode
        //source image
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
        paint.xfermode = null

       /* val count = canvas.saveLayer(bounds, paint)     //开启离屏缓冲
        canvas.save()
        canvas.drawColor(mCoverColor)
        canvas.clipPath(clipPath)
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.SRC)
        canvas.restore()
        canvas.restoreToCount(count)                 //关闭离屏缓冲

        */

    }

}