package com.example.androidlogin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class SideEffectMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.side_effect_main);
    }

    public void child(View v){
        Intent intent = new Intent(getApplicationContext(), SideEffectChild.class);
        startActivity(intent);
    }
    public void pregnant(View v){
        Intent intent = new Intent(getApplicationContext(), SideEffectPregnant.class);
        startActivity(intent);
    }
    public void senior(View v){
        Intent intent = new Intent(getApplicationContext(), SideEffectSenior.class);
        startActivity(intent);
    }
}
