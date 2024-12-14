package com.example.sudoku1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ThankYouActivity extends AppCompatActivity {

    private TextView thankYouTextView, timeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you);

        thankYouTextView = findViewById(R.id.thankYouTextView);
        timeTextView = findViewById(R.id.timeTextView);

        // Get the time passed from the MainActivity
        Intent intent = getIntent();
        String timeTaken = intent.getStringExtra("TIME_TAKEN");

        // Display the time on the screen
        thankYouTextView.setText("Thank you for playing!");
        timeTextView.setText("Time Taken: " + timeTaken);
    }
}
