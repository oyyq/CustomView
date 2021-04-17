package net.oyyq.shake.Mesh.inhale

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import net.oyyq.shake.R


/**
 * 怎么在各种"状态"下 做出来不同的属性动画 ?
 */
class InhaleAnimationActivity : AppCompatActivity() {

    private var mSampleView:  BitmapMesh.SampleView ?= null
    private var DEBUG_MODE = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_inhale)

        var btn : Button? = findViewById(R.id.btn)
        mSampleView = findViewById(R.id.sample)
        mSampleView!!.mIsDebug = true

        btn!!.setOnClickListener(object : View.OnClickListener {
            var mReverse = false;

            @Override
            override fun onClick(v:View)
            {
                if (mSampleView!!.startAnimation(mReverse)) {
                    mReverse = !mReverse;
                }
            }
        });

    }

}