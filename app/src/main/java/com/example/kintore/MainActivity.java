package com.example.kintore;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.Bundle;
import android.widget.TextView;
import android.view.MotionEvent;

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
            handler.postDelayed(this, period);
        }
    };

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
    }
    @Override
    public boolean onTouchEvent(MotionEvent motionEnvet) {

        switch (motionEnvet.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //スタート
                if (ON) {
                    handler.post(runnable);
                    ON = false;
                //リセット
                } else {
                    handler.removeCallbacks(runnable);
                    timerText.setText(dataFormat.format(0));
                    count = 0;
                    ON = true;
                }
                break;
        }
        return true;
    }
}