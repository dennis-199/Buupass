package com.example.buupass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import io.alterac.blurkit.BlurLayout;

public class MainActivity extends AppCompatActivity {
    BlurLayout blurLayout;
    private TextView register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        blurLayout = findViewById(R.id.blurLayout);

    }
    @Override
    protected void onStart() {
        super.onStart();
        blurLayout.startBlur();
    }

    @Override
    protected void onStop() {
        blurLayout.pauseBlur();
        super.onStop();
    }

    public void Registerpage(View view) {
        Intent intent= new Intent(this, RegisterUser.class);
        startActivity(intent);
    }
}