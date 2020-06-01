package com.example.kintore;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Bundle;
import android.widget.TextView;
import android.view.MotionEvent;
import android.view.GestureDetector;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
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

    private String rireki;
    private List<String> rireki_list = new ArrayList<String>();
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
                //現在のタイマーを一旦rirekiに保存
                rireki = dataFormat.format(count*period);
//                System.out.println(rireki);

                //ダイアログを表示
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("履歴にこのしますか？");

                //Okボタンで履歴を残す
                builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //rirekiをrireki_listに追加
                        rireki_list.add(rireki);
                        System.out.println(rireki_list);
                    }
                });
                //Cancelで履歴に残さない
                builder.setNegativeButton("NO", null)
                        .show();

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