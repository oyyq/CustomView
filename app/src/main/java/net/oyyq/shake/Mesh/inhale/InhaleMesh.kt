package net.oyyq.shake.Mesh.inhale

import android.graphics.Path
import android.graphics.PathMeasure

class InhaleMesh constructor(WIDTH: Int, HEIGHT: Int, timeSeg:Int) : Mesh(WIDTH, HEIGHT) {
    /**
     * 吸入方向
     */
    enum class InhaleDir{
        UP,
        DOWN,
        LEFT,
        RIGHT
    }


    var timeSeg = timeSeg+HEIGHT    //将2条path等分成timeSeg + HEIGHT(Bitmap纵向Mesh份数)份, 2条path上应该分别有timeSeg+HEIGHT+1个分点
    //2条路径
    var mFirstPath = Path()
    var mSecondPath = Path()
    var mFirstPathMeasure = PathMeasure()
    var mSecondPathMeasure = PathMeasure()
    var mInhaleDir = InhaleDir.DOWN
        get() { return field }
        set(value) {field = value}



    fun setInhaleDir(dir: InhaleMesh.InhaleDir){
        mInhaleDir = dir
    }


    override fun buildPaths(endX: Float, endY: Float) {
        if(mBmpWidth <= 0 || mBmpHeight <= 0){
            throw IllegalArgumentException("Bitmap size must be > 0, do you call setBitmapSize(int, int) method?")
        }

        when(mInhaleDir){
            InhaleDir.UP ->{
                buildPathsUp(endX, endY)
            }
            InhaleDir.DOWN -> {
                buildPathsDown(endX, endY)
            }
            InhaleDir.LEFT -> {
                buildPathsLeft(endX, endY)
            }
            InhaleDir.RIGHT -> {
                buildPathsRight(endX, endY)
            }
        }

    }


    override fun buildMeshes(index: Int) {
        if(mBmpWidth <= 0 || mBmpHeight <= 0){
            throw IllegalArgumentException("Bitmap size must be > 0, do you call setBitmapSize(int, int) method?")
        }
        when(mInhaleDir){
            InhaleDir.UP, InhaleDir.DOWN -> buildMeshByPathOnVertical(index)
            InhaleDir.LEFT, InhaleDir.RIGHT -> buildMeshByPathOnHorizontal(index)
        }
    }

    /**
     * buildPath** : 画2条路线
     */
    fun buildPathsUp(endX:Float, endY: Float) {}

    fun buildPathsDown(endX:Float, endY: Float) {
        //forceClosed: false => 不闭合
        mFirstPathMeasure.setPath(mFirstPath, false);
        mSecondPathMeasure.setPath(mSecondPath, false);

        var w = mBmpWidth
        var h = mBmpHeight

        mFirstPath.reset();
        mSecondPath.reset();
        //path.moveTo, lineTo, quadTo 必掌握, 必会
        mFirstPath.moveTo(0f, 0f);
        mSecondPath.moveTo(w*1f, 0f);

        mFirstPath.lineTo(0f, h*1f);
        mSecondPath.lineTo(w*1f, h*1f);

        mFirstPath.quadTo(0f, (endY + h) / 2, endX, endY);
        mSecondPath.quadTo(w*1f, (endY + h) / 2, endX, endY);

    }


    fun buildPathsLeft(endX:Float, endY: Float) {}

    fun buildPathsRight(endX:Float, endY: Float) {
        mFirstPathMeasure.setPath(mFirstPath, false)
        mSecondPathMeasure.setPath(mSecondPath, false)

        var w = mBmpWidth
        var h = mBmpHeight

        mFirstPath.moveTo(0f,0f)
        mSecondPath.moveTo(0f, h*1f)

        mFirstPath.lineTo(w*1f, 0f)
        mSecondPath.lineTo(w*1f, h*1f)


        mFirstPath.quadTo((w+endX)/2f, 0f, endX, endY)
        mSecondPath.quadTo((w+endX)/2f,h*1f, endX, endY)
    }


    /**
     * 在buildPath**之后调用
     * timeIndex: 0..timeSeg
     */
    fun buildMeshByPathOnVertical(timeIndex: Int){

        mFirstPathMeasure.setPath(mFirstPath, false)
        mSecondPathMeasure.setPath(mSecondPath, false)

        val fplen =  mFirstPathMeasure.length
        val splen = mSecondPathMeasure.length

        var fpseglen = fplen*1f/timeSeg
        var spseglen = splen*1f/timeSeg

        var pos1 = floatArrayOf(0f, 0f)
        var pos2 = floatArrayOf(0f, 0f)
        var mesleft = floatArrayOf(0f, 0f)
        var mesright = floatArrayOf(0f, 0f)


        if(mInhaleDir == InhaleDir.DOWN) {
            mFirstPathMeasure.getPosTan(timeIndex * fpseglen, pos1, null)       //左上
            mSecondPathMeasure.getPosTan(timeIndex * spseglen, pos2, null)      //右上
//            mFirstPathMeasure.getPosTan((timeIndex+HEIGHT) * fpseglen, pos3, null)       //左下
//            mSecondPathMeasure.getPosTan((timeIndex+HEIGHT) * spseglen, pos4, null)      //右下

            var index = 0;
            for(y in 0..HEIGHT){
//                var yrt = y*1f/ HEIGHT
                mFirstPathMeasure.getPosTan((timeIndex+y)*fpseglen, mesleft, null)
                mSecondPathMeasure.getPosTan((timeIndex+y)*spseglen, mesright, null)

                for(x in 0..WIDTH){
                    var xrt = x*1f/WIDTH
                    mVerts[index*2+0] = (mesright[0]-mesleft[0])*xrt+mesleft[0]
                    mVerts[index*2+1] = (mesright[1]-mesleft[1])*xrt+mesleft[1]
                    index++;
                }
            }
        } else{
            //mInhaleDir == InhaleDir.UP 不想做了, 雷同
        }

    }


    fun buildMeshByPathOnHorizontal(timeIndex: Int){

    }


    fun getPaths() : Array<Path>{
        return arrayOf(mFirstPath, mSecondPath)
    }


}