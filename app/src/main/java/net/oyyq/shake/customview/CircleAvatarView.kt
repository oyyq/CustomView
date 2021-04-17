package net.oyyq.shake.customview


import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi
import net.oyyq.shake.R
import net.oyyq.shake.dp


private val AVATAR_SIZE = 240.dp         //图片的大小
private val RADIUS = AVATAR_SIZE / 2     //裁剪圆形的半径

class CircleAvatarView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : View(context, attrs, defStyleAttr) {


    private val mContext: Context = context
    private val paint = Paint()
    private var avatar = getAvatar(AVATAR_SIZE.toInt(), R.drawable.avatar_rengwuxian)
    private val circlePath = Path()
    private val bounds = RectF()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        circlePath.addCircle(width/2f, height/2f, RADIUS, Path.Direction.CW)
        bounds.set(width/2f - RADIUS, height/2f - RADIUS, width / 2f + RADIUS, height / 2f + RADIUS)
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onDraw(canvas: Canvas) {
        /*
        canvas.save()
        //先"裁切"出一个圆, 也就是画Bitmap的范围
        canvas.clipPath(circlePath)
        //再在该圆上画出Bitmap
        canvas.drawBitmap(avatar, width / 2 - RADIUS, height / 2 - RADIUS, paint)
        canvas.restore()
        */

        var count = canvas.saveLayer(bounds, null)
        //canvas.drawCircle(width/2f, height/2f, RADIUS, paint)
        //paint.setStyle(Paint.Style.STROKE)
        canvas.drawPath(circlePath, paint)          //drawCircle和drawpath一样, 都是画出实心圆, 除非给Paint设置Style
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(avatar,width/2f- RADIUS, height/2f- RADIUS, paint)
        paint.xfermode = null
        canvas.restoreToCount(count)

    }


    fun getAvatar(width: Int, vectorDrawableId : Int):Bitmap {

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


//    val options = BitmapFactory.Options()
//    options.inJustDecodeBounds = true
//    BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.avatar_rengwuxian, options)
//
//    options.inJustDecodeBounds = false
//    options.inDensity = options.outWidth
//    options.inTargetDensity = width
//
//    return BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.avatar_rengwuxian, options)


    }


}


