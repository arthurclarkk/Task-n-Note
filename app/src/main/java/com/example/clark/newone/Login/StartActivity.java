package com.example.clark.newone.Login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.clark.newone.R;


public class StartActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        getSupportActionBar().hide();

        final Intent i = new Intent(StartActivity.this, LoginActivity.class);

        Thread timer = new Thread(){
            public void run(){
                try {
                    sleep(1000);
                }catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {

                    startActivity(i);
                    finish();

                }
            }
        };

        timer.start();


    }

}
