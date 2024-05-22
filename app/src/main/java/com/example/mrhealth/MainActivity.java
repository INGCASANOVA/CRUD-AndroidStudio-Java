package com.example.mrhealth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void irlogin (View vista){
        Intent miintent = new Intent(this, login.class);
        startActivity(miintent);
    }
    public void irregister (View vista){
        Intent miiintent = new Intent(this, register.class);
        startActivity(miiintent);
    }
}