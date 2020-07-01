package com.example.javaredditclone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String message = "blah blah blah";
        setTextViewMessage(message);
    }

    private void setTextViewMessage(String message) {
        TextView textView = (TextView) findViewById(R.id.tv_main);
        textView.setText(message);
    }
}