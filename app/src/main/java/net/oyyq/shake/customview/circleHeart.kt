package net.oyyq.shake.customview

import android.animation.TypeEvaluator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

/*

LinearInterpolator ll = new LinearInterpolator();
ObjectAnimator animator = ObjectAnimator.ofFloat(tv, "rotation",
0f, 360f);
animator.setInterpolator(ll);
animator.setDuration(5000);
animator.start();
ObjectAnimator animator2 = ObjectAnimator.ofFloat(tv2, "rotation",
0f, 360f);
animator2.setDuration(5000);
animator2.start();


//TODO 多个属性动画同时执行的方法

ValueAnimator animator = ValueAnimator.ofFloat(0f,200f);
        animator.setDuration(200);
        animator.addUpdateListener(new AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //动画在执行的过程当中，不断地调用此方法
            animation.getAnimatedFraction()//百分比
            //得到duration时间内 values当中的某一个中间值。0f~100f
                float value = (float) animation.getAnimatedValue();//
                iv.setScaleX(0.5f+value/200);//0.5~1
                iv.setScaleY(0.5f+value/200);//0.5~1
            }
        });
        animator.start();


        //3）方法3
        //float... values:代表关键帧的值
        PropertyValuesHolder holder1 = PropertyValuesHolder.ofFloat("alpha", 1f,0.7f,1f);
        PropertyValuesHolder holder2 = PropertyValuesHolder.ofFloat("scaleX", 1f,0.7f,1f);
        PropertyValuesHolder holder3 = PropertyValuesHolder.ofFloat("scaleY", 1f,0.7f,1f);
        PropertyValuesHolder holder3 = PropertyValuesHolder.ofFloat("translationX", 0f,300f);

        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(iv, holder1,holder2,holder3);
        animator.setDuration(1000);
        animator.addUpdateListener(new AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
            // TODO Auto-generated method stub
                float animatedValue = (float) animation.getAnimatedValue();
                float animatedFraction = animation.getAnimatedFraction();
                long playTime = animation.getCurrentPlayTime();

                System.out.println("animatedValue:"+animatedValue+",  playTime:"+playTime);
            }
        });
        animator.start();


    //4)方法4：-----------------动画集合--------------------
    ObjectAnimator animator1 = ObjectAnimator.ofFloat(iv,"alpha", 1f,0.7f,1f);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(iv,"scaleX", 1f,0.7f,1f);
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(iv,"scaleY", 1f,0.7f,1f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(500);
        //animatorSet.play(anim);//执行当个动画       animatorSet.playTogether(animator1,animator2,animator3);//同时执行
        animatorSet.playSequentially(animator1,animator2,animator3);//依次执行动画
        animatorSet.start();

*/


/**
 * 3阶Bezier曲线实现圆形转心形
 * timeStamp: 2020-4-17 9:37:05
 */
class circleHeart @JvmOverloads constructor(context: Context, attrs: AttributeSet, defStyleattr: Int = 0) :
        View(context, attrs, defStyleattr){

    private val C = 0.551915024494f
    private lateinit var mPaint: Paint

    private var mCenterX = 0f            //圆心坐标
    private  var mCenterY = 0f

    private val mCircleRadius = 200f // 圆的半径
    private val mDifference = mCircleRadius * C

    private val mData = FloatArray(8) // 顺时针记录绘制圆形的四个数据点

    private val mCtrl = FloatArray(16) // 顺时针记录绘制圆形的八个控制点


    public var tslation0 = 0f              //由TypeEvaluator改变的值
        set(value) {
            field = value
            invalidate()
        }


    private val aplitude0 = 120f
    private val aplitude1 = 80f
    private val aplitude2 = 20f


    init {

        mPaint = Paint()
        mPaint.setColor(Color.BLACK)
        mPaint.setStrokeWidth(8f)
        mPaint.setStyle(Paint.Style.STROKE)
        mPaint.setTextSize(60f)


        // 初始化数据点, 4个
        mData[0] = 0f;
        mData[1] = mCircleRadius;

        mData[2] = mCircleRadius;
        mData[3] = 0f;

        mData[4] = 0f;
        mData[5] = -mCircleRadius;

        mData[6] = -mCircleRadius;
        mData[7] = 0f;

        // 初始化控制点, 8个
        mCtrl[0]  = mData[0]+mDifference;
        mCtrl[1]  = mData[1];

        mCtrl[2]  = mData[2];
        mCtrl[3]  = mData[3]+mDifference;

        mCtrl[4]  = mData[2];
        mCtrl[5]  = mData[3]-mDifference;

        mCtrl[6]  = mData[4]+mDifference;
        mCtrl[7]  = mData[5];

        mCtrl[8]  = mData[4]-mDifference;
        mCtrl[9]  = mData[5];

        mCtrl[10] = mData[6];
        mCtrl[11] = mData[7]-mDifference;

        mCtrl[12] = mData[6];
        mCtrl[13] = mData[7]+mDifference;

        mCtrl[14] = mData[0]-mDifference;
        mCtrl[15] = mData[1];

    }



    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        mCenterX = w / 2f
        mCenterY = h / 2f
    }


    override fun onDraw(canvas: Canvas) {

        canvas.translate(mCenterX, mCenterY); // 将坐标系移动到画布中央
        canvas.scale(1f, -1f);                 // 翻转Y轴

        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(8f);


        val path = Path()
        path.moveTo(mData[0], mData[1]-aplitude0*tslation0)





        path.cubicTo(mCtrl[0], mCtrl[1], mCtrl[2], mCtrl[3], mData[2], mData[3])
        path.cubicTo(mCtrl[4] -aplitude2*tslation0, mCtrl[5], mCtrl[6],   mCtrl[7]+aplitude1*tslation0, mData[4], mData[5])
        path.cubicTo(mCtrl[8],mCtrl[9]+aplitude1*tslation0, mCtrl[10]+aplitude2*tslation0, mCtrl[11], mData[6], mData[7])
        path.cubicTo(mCtrl[12], mCtrl[13], mCtrl[14], mCtrl[15], mData[0],  mData[1]-aplitude0*tslation0)


        canvas.drawPath(path, mPaint)
    }

    class heartEvaluator : TypeEvaluator<Float>{
        override fun evaluate(fraction: Float, startValue: Float, endValue: Float): Float {
            return (endValue-startValue)*fraction + startValue
        }

    }

}



