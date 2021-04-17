package net.oyyq.shake.customview

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageView
import net.oyyq.shake.R
import net.oyyq.shake.dp


/**
 * 微信摇一摇头像, 这么好玩吗
 */
class ShakeImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : AppCompatImageView (context, attrs, defStyleAttr){

    private var mContext = context
    private val path = Path()
    private var mImageCorner: Float
    private var paint = Paint().apply {
        isAntiAlias = true
    }
    private var avatar:Bitmap? = null

    init {
        setLayerType(LAYER_TYPE_HARDWARE, null)
        val ta  = context.obtainStyledAttributes(attrs, R.styleable.ddShakeView)
        mImageCorner = ta.getDimension(R.styleable.ddShakeView_shakeCorner, 5.dp)
    }

    private var offset = 3f
    private var dduration = 500
    private val animator = ObjectAnimator.ofFloat(this, "rotation", offset, 0f, -offset, 0f, offset, 0f, -offset, 0f, offset, 0f).apply {
        this.duration = dduration.toLong()
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        //avatar = getAvartar(width, R.drawable.avatar_rengwuxian)
        avatar = getAvatar(width, R.drawable.avatar_rengwuxian)
    }


    //画一个圆角头像 / 圆形头像
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onDraw(canvas: Canvas) {

       /*
        path.addRoundRect(0f, 0f, width.toFloat(), height.toFloat(), mImageCorner, mImageCorner, Path.Direction.CW)
        canvas.clipPath(path)
        super.onDraw(canvas)
        */

        canvas.drawCircle(width/2f, height/2f, if(height>width) width/2f else height/2f, paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        avatar?.let { canvas.drawBitmap(it, 0f, 0f, paint) }
        paint.xfermode = null

    }


    //让圆角头像相应双击事件
    private val gestureDetector  = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener(){

        //拦截true
        override fun onDown(e: MotionEvent?): Boolean {
            return true
        }

        //双击时回调
        override fun onDoubleTap(e: MotionEvent?): Boolean {
            //todo
            shake()
            return true
        }

    })


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(event)
    }


    fun shake(){
        animator.start()
    }

    fun getAvartar(width: Int, vectorDrawableId : Int) : Bitmap {
        var bitmap : Bitmap ? = null
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {

            var vectorDrawable = mContext.getDrawable(vectorDrawableId);
            bitmap = Bitmap.createBitmap(vectorDrawable!!.getIntrinsicWidth(),
                    vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888)

            var canvas = Canvas(bitmap)
            vectorDrawable.setBounds(0, 0, width, width)
            vectorDrawable.draw(canvas)

        } else {
            bitmap = BitmapFactory.decodeResource(mContext.getResources(), vectorDrawableId);
        }
        return bitmap!!

    }


    fun getAvatar( width: Int, vectorDrawableId: Int): Bitmap {

        var d = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            mContext.getDrawable(R.drawable.avatar_rengwuxian)
        } else {
            TODO("VERSION.SDK_INT < LOLLIPOP")
        }

//        if (d is BitmapDrawable) {
//            return (d as BitmapDrawable).bitmap
//        }
        val bitmap = Bitmap.createBitmap(d!!.intrinsicWidth, d.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        d!!.setBounds(0, 0, width, width)
        d.draw(canvas)

        return bitmap

    }



}