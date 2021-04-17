package net.oyyq.shake.beautywave.helper

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Path


/**
 * water: Bitmap water图像
 * foam: Bitmap 海浪图像
 * y 海浪起始左上角坐标的y值
 * width 海浪显示的宽度
 * height 海浪显示的高度
 * numWaves 海浪整个宽度被分成多少份
 */

class Water constructor(water:Bitmap, foam:Bitmap,
    y:Float, width:Float, height:Float, numWaves: Int) : Renderable(water, 0f ,y) {

    var mWidth:Float = 0f
    var mHeight:Float= 0f
    var mWaterMesh : PathBitmapMesh? = null
    var mWaveHeight:Float = 0f
    var mWaterPath : Path = Path()
    var mNumWaves : Int = 0


    init{
        mWidth = width
        mHeight =height
        //mWaterMesh
        mWaveHeight = height/20;
        mNumWaves = numWaves

        createPath()
    }

    fun createPath() {
        mWaterPath.reset()
        mWaterPath.moveTo(0f, my)
        var step:Int = (mWidth / mNumWaves).toInt()
        var changeDirection:Boolean = true

        for(i in 0..mNumWaves-1){
            if(changeDirection){
                //1st ctr pt, 2nd ctr pt, dest pt
                mWaterPath.cubicTo(mx+step*i, my,mx+step*i+step/2f, my+mWaveHeight,
                        mx+step*i+step, my)
            } else {

                mWaterPath.cubicTo(mx+step*i, my,mx+step*i+step/2f, my-mWaveHeight,
                        mx+step*i+step, my)
            }
            changeDirection = !changeDirection

        }
    }


    override fun draw(canvas: Canvas){
        //mWaterMesh!!.draw(canvas)
    }

    override fun update() {

    }


}