package com.example.kintore;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.Bundle;
import android.widget.TextView;
import android.view.MotionEvent;
import android.view.GestureDetector;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Handler handler = new Handler();

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            count ++;
            timerText.setText(dataFormat.
                    format(count*period));
            //periodミリ秒後に処理されるようにthis(runnable)をMessage Queueに渡す
            handler.postDelayed(this, period);
        }
    };
    //ダブルタップを検出
    private GestureDetector gestureDetector;

    private TextView timerText;
    private SimpleDateFormat dataFormat =
            new SimpleDateFormat("mm:ss.S", Locale.US);

    private int count, period;
    private boolean ON = true;

    //画面が変わったとき(初めて生成されたとき)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        count = 0;
        period = 100;

        timerText = findViewById(R.id.timer);
        timerText.setText(dataFormat.format(0));

        gestureDetector = new GestureDetector(this,new GestureDetector.SimpleOnGestureListener() {
            //ダブルタップ
            @Override
            public boolean onDoubleTap(MotionEvent event) {
                handler.removeCallbacks(runnable);
                timerText.setText(dataFormat.format(0));
                count = 0;
                ON = true;

                //System.out.println("yahho");

                return super.onDoubleTap(event);
            }
            //長押し
            @Override
            public void onLongPress(MotionEvent event) {

                //System.out.println("ohayo");

                super.onLongPress(event);
            }
        });
    }
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //スタート
                if (ON) {
                    //Message QueueにRunnableを渡し続ける
                    handler.post(runnable);
                    ON = false;
                //ストップ
                } else {
                    //Message Queueのrunnableを取り除く
                    handler.removeCallbacks(runnable);
                    ON = true;
                }
                break;
        }
        //基本的な動作でなかった場合gestureDetectorにイベントを渡す
        gestureDetector.onTouchEvent(motionEvent);
        return super.onTouchEvent(motionEvent);
    }
}