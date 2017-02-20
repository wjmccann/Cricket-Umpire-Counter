package com.wjmccann.cricketumpirecounter;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {

            public void run(){
                Intent intent = new Intent(MainActivity.this, Counter.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }

    public void onSplashClick(View v){
        Intent intent = new Intent(this, Counter.class);
        startActivity(intent);
        finish();
    }
}
