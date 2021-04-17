package net.oyyq.shake.Mesh.inhale


/**
 * 将一张Bitmap Mesh成散点
 * 将吸入动画分成20帧(如), 计算出每一个帧动画的mesh顶点数组, 根据该mesh顶点数组drawBitmapMesh
 * 由连续变化的mesh得到吸扩效果
 */
open abstract class Mesh {
    protected var WIDTH = 40   //横向Mesh默认点数
    protected var HEIGHT = 40;    //纵向Mesh默认点数

    protected var mBmpWidth   = 0;
    protected var mBmpHeight  = 0;
    protected lateinit var mVerts:FloatArray

    constructor(width:Int, height:Int){
        WIDTH = width
        HEIGHT = height
        mVerts = FloatArray((WIDTH + 1)*(HEIGHT + 1)*2, {0f})
    }


    /**
     * TODO bug是在刷新的时候, mVerts所有元素赋值为0 了, 要检查下这里是怎么搞的
     */
    fun getVertices():FloatArray{
        return mVerts
    }

    fun getWidth():Int {
        return WIDTH
    }

    fun getHeight():Int{
        return HEIGHT
    }

    companion object{
        public fun setXY(array:FloatArray, index:Int, x:Float, y:Float){
            array[index*2 + 0] = x
            array[index*2 + 1] = y
        }
    }

    fun setBitmapSize(w:Int, h:Int){
        mBmpWidth = w
        mBmpHeight = h
    }

    open abstract fun buildPaths(endX:Float, endY:Float)

    open abstract fun buildMeshes(index:Int)

    //等比例放缩
    fun buildMeshes (w:Float, h:Float){
        var index = 0

        for (y in 0..HEIGHT)
        {
            var fy = y * h / HEIGHT;
            for (x in 0..WIDTH)
            {
                var fx = x * w / WIDTH

                setXY(mVerts, index, fx, fy);

                index += 1;
            }
        }

    }



}
