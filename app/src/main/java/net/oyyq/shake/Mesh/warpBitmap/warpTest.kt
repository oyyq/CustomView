package net.oyyq.shake.Mesh.warpBitmap

import android.content.Context
import android.graphics.*
import android.os.Build
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import net.oyyq.shake.R
import net.oyyq.shake.dp



private val AVATAR_SIZE = 400.dp
class warpTest : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(MyView(this, R.drawable.avatar_rengwuxian))
    }


    private class MyView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
        : View(context, attrs, defStyleAttr){
        private lateinit var bitmap:Bitmap
        private val mContext:Context = context
        var myPaint = Paint().apply {
            isAntiAlias = true
        }

        private val WIDTH = 20
        private val HEIGHT = 20

        private val COUNT = (WIDTH + 1) * (HEIGHT + 1)
        //定义一个数组,记录Bitmap上的21*21个点的坐标
        private val verts = FloatArray(COUNT * 2)

        //定义一个数组,记录Bitmap上的21*21个点经过扭曲后的坐标
        //对图片扭曲的关键就是修改该数组里元素的值
        private val orig = FloatArray(COUNT * 2)

        constructor(context: Context, resourceId: Int) : this(context) {
            setFocusable(true)
            //bitmap = BitmapFactory.decodeResource(Resources.getSystem(), resourceId)
            bitmap = getAvatar(AVATAR_SIZE.toInt(), resourceId)

            var bitmapWidth =bitmap.width
            var bitmapHeight = bitmap.height
            var index = 0

            for(y in 0..HEIGHT){
                var fy = bitmapHeight*y/HEIGHT.toFloat()
                for(x in 0..WIDTH){
                    var fx = bitmapWidth * x/WIDTH.toFloat()
                    //初始化orig,verts数组, 均匀保存21*21个点坐标
                    orig[index * 2 + 0]=fx; verts[index * 2 + 0] = fx
                    orig[index * 2 + 1] = fy;  verts[index * 2 + 1] = fy
                    index += 1
                }
            }

            setBackgroundColor(Color.WHITE)
        }


        override fun onDraw(canvas: Canvas) {
            //canvas.drawBitmap(bitmap, 0f,0f,myPaint)
            canvas.drawBitmapMesh(bitmap, WIDTH, HEIGHT, verts, 0, null, 0, null)
        }


        //工具方法,用于根据触摸事件的位置(cx, cy)计算verts数组里各元素的值
        fun warp(cx : Float, cy : Float){

            for(i in 0..COUNT*2-2 step 2)
            {
                var dx = cx - orig[i + 0];
                var dy = cy - orig[i + 1];
                var dd = dx * dx + dy * dy;
                //计算每个坐标点与当前点(cx,cy)之间的距离
                var d = Math.sqrt(dd.toDouble());
                //计算扭曲度，距离当前点(cx,cy)越远，扭曲度越小
                var pull = 80000 / ((dd * d).toFloat());
                //对verts数组(保存bitmap　上21 * 21个点经过扭曲后的坐标)重新赋值
                if(pull >= 1)
                {
                    verts[i + 0] = cx;
                    verts[i + 1] = cy;
                }
                else
                {
                    //控制各顶点向触摸事件发生点偏移
                    verts[i + 0] = orig[i + 0] + dx * pull;
                    verts[i + 1] = orig[i + 1] + dx * pull;
                }
            }
            //通知View组件重绘
            invalidate();
        }


        override fun onTouchEvent(event: MotionEvent): Boolean {
            warp(event.getX(), event.getY())
            Log.e("touch", event.getX().toString() + "  " + event.getY().toString())
            return true
        }



        fun getAvatar(width: Int, vectorDrawableId : Int):Bitmap {

            var bitmap: Bitmap? = null
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

    }






}