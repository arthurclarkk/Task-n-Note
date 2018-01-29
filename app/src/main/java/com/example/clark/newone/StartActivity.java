package com.example.clark.newone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class StartActivity extends AppCompatActivity implements View.OnClickListener {

    Button mButtonStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        getSupportActionBar().hide();

        mButtonStart = (Button) findViewById(R.id.buttonStart);

        mButtonStart.setOnClickListener(this);

    }
    @Override
    public void onClick(View view) {
        Intent mButtonStart = new Intent(StartActivity.this, LoginActivity.class);
        startActivity(mButtonStart);
        finish();
    }
}