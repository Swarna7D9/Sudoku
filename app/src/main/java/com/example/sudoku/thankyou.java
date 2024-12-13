package com.example.sudoku;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;

public class thankyou extends AppCompatActivity {

    private TextView thankYouTextView, timeTextView;
    ImageView im,im2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thankyou);
        im=findViewById(R.id.imageView);
        im2=findViewById(R.id.imageView2);
        thankYouTextView = findViewById(R.id.thankYouTextView);
        timeTextView = findViewById(R.id.timeTextView);

        // Get the time passed from the MainActivity
        Intent intent = getIntent();
        String timeTaken = intent.getStringExtra("TIME_TAKEN");

        // Display the time on the screen
        thankYouTextView.setText("Thank you for playing!");
        timeTextView.setText("Time Taken: " + timeTaken);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom);
        im.startAnimation(animation);
        Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade);
        im2.startAnimation(animation2);
    }
}