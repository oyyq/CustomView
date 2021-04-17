package net.oyyq.shake.Mesh.inhale


import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import net.oyyq.shake.R


/**
 * Drawable转Bitmap, 正确获取Bitmap尺寸
 */

fun getAvatar(context: Context, width: Int, vectorDrawableId: Int): Bitmap {

    val d: Drawable = context.resources.getDrawable(R.drawable.avatar_rengwuxian)

    if (d is BitmapDrawable) {
        return (d as BitmapDrawable).bitmap
    }
    val bitmap = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    d.setBounds(0, 0, canvas.getWidth(), canvas.getHeight())
    d.draw(canvas)

    return bitmap

}