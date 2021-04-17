package net.oyyq.shake

import android.content.res.Resources
import android.util.TypedValue


val Float.dp
    get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this,
            Resources.getSystem().displayMetrics)


val Int.dp
    get() = this.toFloat().dp


val Float.sp
    get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            this,
            Resources.getSystem().displayMetrics
    )

val Int.sp
    get() = this.toFloat().sp




