package com.example.clark.newone.Tools;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.clark.newone.R;

public class ToolsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }
}
