package com.shivam.deliveryapp;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        final Intent intent=new Intent(MainActivity.this,the_main_screen.class);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
                finish();
            }
        },3000);



    }
}
