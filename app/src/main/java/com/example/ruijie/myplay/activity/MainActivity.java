package com.example.ruijie.myplay.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

import com.example.ruijie.myplay.R;

/**
 * splash 刷新页面
 */
public class MainActivity extends AppCompatActivity {

    private ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pb = (ProgressBar) findViewById(R.id.splash_pb);
        Handler handler=new Handler();
//       三秒后进行跳转   这边正常是要加一个版本更新判断
//        还要进判断是用户是第一次进行使用
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));

//                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
               finish();
            }
        },3000);



    }



}
