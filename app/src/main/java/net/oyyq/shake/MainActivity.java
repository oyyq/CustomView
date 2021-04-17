package net.oyyq.shake;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;

import net.oyyq.shake.customview.CircleProgressBar;
import net.oyyq.shake.customview.circleHeart;
import net.oyyq.shake.customview.switchingButton;

public class MainActivity extends AppCompatActivity {

    private CircleProgressBar progressbar;
    private int progressInt = 0;

//    Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//        }
//    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        switchingButton swButton = findViewById(R.id.swButton);
//        swButton.setText(new String[]{"已经", "在家", "等你"});

        //progressBar用handler去update
//        progressbar = findViewById(R.id.cirbar);
//        progressbar.setMax(100);
//
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                progressInt++;
//                if(progressInt <= 100){
//                    progressbar.setProgress(progressInt);
//                    handler.postDelayed(this,100);
//                }else {
//                    handler.removeCallbacksAndMessages(this);
//                }
//            }
//        }, 100);


        circleHeart hh = findViewById(R.id.circleheart);
        ObjectAnimator animator = ObjectAnimator.ofObject(hh, "tslation0", new circleHeart.heartEvaluator(), 1f);
        animator.setStartDelay(1000);
        animator.setDuration(2000);
        animator.start();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //handler.removeCallbacks();
    }
}