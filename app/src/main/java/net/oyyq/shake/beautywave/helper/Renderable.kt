package net.oyyq.shake.beautywave.helper

import android.graphics.Bitmap
import android.graphics.Canvas

open class Renderable constructor(bitmap: Bitmap, x:Float, y:Float) {

    var bitmap: Bitmap = bitmap
    //bitmap放置的x, y坐标
    var mx:Float = x
    var my:Float = y

    protected open fun draw(canvas: Canvas){
        canvas.save()
        canvas.drawBitmap(bitmap, mx, my, null)
        canvas.restore()
    }

    protected open fun update(){

    }

}