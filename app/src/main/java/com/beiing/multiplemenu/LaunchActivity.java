package com.beiing.multiplemenu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
    }

    public void gravityTop(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void gravityBottom(View view) {
        startActivity(new Intent(this, Main2Activity.class));
    }
}
