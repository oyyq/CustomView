package net.oyyq.shake.Mesh.inhale

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation
import net.oyyq.shake.R
import net.oyyq.shake.dp


val AVATART_WIDTH = 100.dp

class BitmapMesh  {

    //Kotlin静态内部类不需修饰, 直接创建, 非静态内部类, 需用inner修饰
    class SampleView  @JvmOverloads constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int = 0)
        : View(context, attrs, defStyleAttr){


        private val WIDTH = 40;
        private val HEIGHT = 40;
        private val timeSeg = 200

        private lateinit var mBitmap : Bitmap;
        private val mMatrix = Matrix();
        private val mInverse = Matrix();

        public var mIsDebug = false
                set(value){
                    field = value
                }


        private val mPaint = Paint()
        private var mInhalePt = floatArrayOf(0f, 0f)
        private lateinit var mInhaleMesh:InhaleMesh


        init {
            setFocusable(true);
            mBitmap = getAvatar(context, AVATART_WIDTH.toInt(), R.drawable.avatar_rengwuxian)

            mInhaleMesh = InhaleMesh(WIDTH, HEIGHT, timeSeg)
            //mInhaleMesh.setBitmapSize(mBitmap.getWidth()/8, mBitmap.getHeight()/8);
            mInhaleMesh.setBitmapSize(mBitmap.getWidth(), mBitmap.getHeight());
            mInhaleMesh.setInhaleDir(InhaleMesh.InhaleDir.DOWN);
        }


        fun setInhaleDir(dir: InhaleMesh.InhaleDir){

            mInhaleMesh.setInhaleDir(dir)

            var w = mBitmap.getWidth();
            var h = mBitmap.getHeight();
            var endX = 0f;
            var endY = 0f;
            var dx = 10;
            var dy = 10;
            //mMatrix.reset();

            when(dir){
                InhaleMesh.InhaleDir.DOWN -> {
                    endX = w / 2f;
                    endY = height - 20f;
                }
                InhaleMesh.InhaleDir.UP -> {
                    dy = height - h - 20;
                    endX = w / 2f;
                    endY = -dy + 10f; }
                InhaleMesh.InhaleDir.RIGHT -> {
                    endX = width - 20f;
                    endY = h / 2f;
                }
                InhaleMesh.InhaleDir.LEFT -> {
                    dx = width - w - 20;
                    endX = -dx + 10f;
                    endY = h / 2f;
                }
            }


//            mMatrix.setTranslate(dx, dy);
//            mMatrix.invert(mInverse);
            buildPaths(endX, endY);
            buildMesh(w * 1f, h * 1f);
            invalidate();

        }


        fun buildMesh(w: Float, h: Float){
            mInhaleMesh.buildMeshes(w, h)
        }


        fun buildPaths(endX: Float, endY: Float){
            mInhalePt[0] = endX;
            mInhalePt[1] = endY;
            mInhaleMesh.buildPaths(endX, endY);
        }


        override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
            super.onSizeChanged(w, h, oldw, oldh)

            var bmpW = mBitmap.getWidth()*1f;
            var bmpH = mBitmap.getHeight()*1f;

//            mMatrix.setTranslate(10, 10);
//            mMatrix.invert(mInverse);

            mPaint.setColor(Color.RED);
            mPaint.setStrokeWidth(2f);
            mPaint.setAntiAlias(true);


            buildPaths(bmpW / 2f, h - 20f);
            buildMesh(bmpW, bmpH);
        }


        override fun onDraw(canvas: Canvas) {

            //canvas.concat(mMatrix);

           canvas.drawBitmapMesh(mBitmap,
                    mInhaleMesh.getWidth(),
                    mInhaleMesh.getHeight(),
                    mInhaleMesh.getVertices(),
                    0, null, 0, null);

            // Draw the target point.
            mPaint.setColor(Color.RED);
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(mInhalePt[0], mInhalePt[1], 5f, mPaint);

            if (mIsDebug) {
                // Draw the mesh vertices.
                canvas.drawPoints(mInhaleMesh.getVertices(), mPaint);

                // Draw the paths
                mPaint.setColor(Color.BLUE);
                mPaint.setStyle(Paint.Style.STROKE);

                var paths = mInhaleMesh.getPaths()
                for (path in paths) {
                    canvas.drawPath(path, mPaint)
                }
            }
        }


        /**
         * 自定义的startAnimation方法,
         * 定制PathAnimation, TODO 过程中不停回调invalidate()
         */
        public fun startAnimation(reverse:Boolean) :Boolean{

            var anim = this.animation
            if (null != anim && !anim.hasEnded()) {
                return false;
            }

            var animation = PathAnimation(0, timeSeg+HEIGHT + 1, reverse,
                 object: PathAnimation.IAnimationUpdateListener{
                     override fun onAnimUpdate(index: Int) {

                         mInhaleMesh.buildMeshes(index)
                         invalidate()

                     }
                 }
            )

            animation.duration = 1000
            startAnimation(animation)

            return true
        }

    }



    class PathAnimation constructor(var fromIndex:Int,var endIndex:Int, var reverse:Boolean, var listener:IAnimationUpdateListener)
        : Animation() {
        public interface IAnimationUpdateListener {
            fun onAnimUpdate(index: Int)
        }

        private var mFromIndex = 0;
        private var mEndIndex = 0;
        private var mReverse = false;
        private var mListener:IAnimationUpdateListener? = null

        init {
            mFromIndex = fromIndex
            mEndIndex = endIndex
            mReverse = reverse
            mListener = listener
        }


        override fun getTransformation(currentTime: Long, outTransformation: Transformation?): Boolean {
            return super.getTransformation(currentTime, outTransformation)
        }


        /**
         * 正向, 反向动画
         */
        override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            super.applyTransformation(interpolatedTime, t)

            var curIndex = 0
            var tinterpolatedTime = 0f
            var interpolator = this.getInterpolator();
            if (null != interpolator)
            {
                var value = interpolator.getInterpolation(interpolatedTime);
                tinterpolatedTime = value;
            }

            if (mReverse) {
                tinterpolatedTime = 1.0f - interpolatedTime;
            }

            curIndex = (mFromIndex + (mEndIndex - mFromIndex) * tinterpolatedTime).toInt();

            if (null != mListener) {
                Log.i("leehong2", "onAnimUpdate  =========== curIndex = " + curIndex);
                mListener?.onAnimUpdate(curIndex);
            }

        }



    }



}