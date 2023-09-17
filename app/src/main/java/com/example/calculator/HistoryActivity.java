package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HistoryActivity extends AppCompatActivity {
    TextView historyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        MaterialButton historyButton = findViewById(R.id.back);
        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the HistoryActivity
                Intent intent = new Intent(HistoryActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

         historyTextView= findViewById(R.id.historyView);
        DatabaseReference historyRef = FirebaseDatabase.getInstance().getReference("calculator_history");

        historyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                StringBuilder historyBuilder = new StringBuilder();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    CalculationHistoryItem historyItem = snapshot.getValue(CalculationHistoryItem.class);
                    String expression = historyItem.getSolution();
                    String result = historyItem.getResult();

                    // Concatenate the expression and result with a line break
                    String historyString = expression + " = " + result + "\n";
                    historyBuilder.append(historyString);
                }

                // Set the history text to the TextView
                historyTextView.setText(historyBuilder.toString());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle the error here if needed
            }
        });
    }
}