package com.example.sudoku;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText[] cells = new EditText[81];
    private GridLayout gridLayout;
    private Button solveButton, resetButton, submitButton;
    private TextView timerTextView;
    private int seconds = 0;
    private boolean isTimerRunning = false;
    private Handler handler = new Handler();
    private long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridLayout = findViewById(R.id.gridLayout);
        solveButton = findViewById(R.id.solveButton);
        resetButton = findViewById(R.id.resetButton);
        submitButton = findViewById(R.id.submitButton); // Initialize Submit button
        timerTextView = findViewById(R.id.timeTextView);

        // Create the 81 EditText cells for the Sudoku board
        for (int i = 0; i < 81; i++) {
            cells[i] = new EditText(this);

            // Setting layout parameters for each cell
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 150; // Width of each cell
            params.height = 150; // Height of each cell
            params.rowSpec = GridLayout.spec(i / 9); // Row specification
            params.columnSpec = GridLayout.spec(i % 9); // Column specification
            params.setMargins(2, 2, 2, 2); // Adding margin for spacing between cells

            cells[i].setLayoutParams(params);
            cells[i].setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
            cells[i].setGravity(Gravity.CENTER); // Centering the text
            cells[i].setMaxLines(1);
            cells[i].setMaxEms(1); // Ensure only one digit per cell

            // Start timer when the first cell is interacted with
            cells[i].setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (!isTimerRunning) {
                        startTimer(); // Start the timer when the first cell is touched
                    }
                    return false; // Allow the touch to proceed
                }
            });

            gridLayout.addView(cells[i]);
        }

        // Solve button functionality
        solveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[][] board = new int[9][9];

                // Convert the EditText inputs into a 2D board array
                for (int i = 0; i < 81; i++) {
                    String text = cells[i].getText().toString();
                    board[i / 9][i % 9] = text.isEmpty() ? 0 : Integer.parseInt(text);
                }

                // Solve the Sudoku
                if (solveSudoku(board)) {
                    updateBoard(board); // Update the UI with the solved board
                    Toast.makeText(MainActivity.this, "Sudoku Solved!", Toast.LENGTH_SHORT).show();
                    stopTimer(); // Stop the timer when puzzle is solved
                } else {
                    Toast.makeText(MainActivity.this, "No Solution Exists!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Submit button functionality
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[][] board = new int[9][9];

                // Convert the EditText inputs into a 2D board array
                for (int i = 0; i < 81; i++) {
                    String text = cells[i].getText().toString();
                    board[i / 9][i % 9] = text.isEmpty() ? 0 : Integer.parseInt(text);
                }

                // Record the time taken to submit the solution
                long endTime = System.currentTimeMillis(); // End time after solving
                long timeTakenMillis = endTime - startTime;
                int seconds = (int) (timeTakenMillis / 1000); // Convert to seconds
                int minutes = seconds / 60;
                seconds = seconds % 60;

                String timeTaken = String.format("%02d:%02d", minutes, seconds);

                // Navigate to ThankYouActivity with the time taken
                Intent intent = new Intent(MainActivity.this, thankyou.class);
                intent.putExtra("TIME_TAKEN", timeTaken);
                startActivity(intent);
            }
        });

        // Reset button functionality
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetBoard(); // Reset the Sudoku board
                resetTimer(); // Reset the timer
            }
        });
    }

    // Timer logic
    private void startTimer() {
        isTimerRunning = true;
        startTime = System.currentTimeMillis(); // Record the start time when timer starts
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isTimerRunning) {
                    seconds++;
                    updateTimerDisplay();
                    handler.postDelayed(this, 1000); // Update every second
                }
            }
        }, 1000);
    }

    private void stopTimer() {
        isTimerRunning = false;
    }

    private void resetTimer() {
        isTimerRunning = false;
        seconds = 0;
        updateTimerDisplay();
        startTimer(); // Start the timer again
    }

    private void updateTimerDisplay() {
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        timerTextView.setText(String.format("%02d:%02d", minutes, remainingSeconds));
    }

    // Reset the board (clear all cells)
    private void resetBoard() {
        for (int i = 0; i < 81; i++) {
            cells[i].setText("");
        }
    }

    // Backtracking algorithm to solve the Sudoku puzzle
    private boolean solveSudoku(int[][] board) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (board[row][col] == 0) { // Find an empty cell
                    for (int num = 1; num <= 9; num++) {
                        if (isSafe(board, row, col, num)) { // Try a valid number
                            board[row][col] = num;

                            if (solveSudoku(board)) { // Recursively solve the puzzle
                                return true;
                            }

                            board[row][col] = 0; // Backtrack if solution fails
                        }
                    }
                    return false; // No solution found
                }
            }
        }
        return true; // Puzzle solved
    }

    // Check if it's safe to place the number in the cell
    private boolean isSafe(int[][] board, int row, int col, int num) {
        // Check row
        for (int x = 0; x < 9; x++) {
            if (board[row][x] == num) {
                return false;
            }
        }

        // Check column
        for (int x = 0; x < 9; x++) {
            if (board[x][col] == num) {
                return false;
            }
        }

        // Check 3x3 box
        int startRow = row - row % 3;
        int startCol = col - col % 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i + startRow][j + startCol] == num) {
                    return false;
                }
            }
        }
        return true;
    }

    // Update the board UI with the solved board
    private void updateBoard(int[][] board) {
        for (int i = 0; i < 81; i++) {
            int value = board[i / 9][i % 9];
            cells[i].setText(value == 0 ? "" : String.valueOf(value));
        }
    }
}