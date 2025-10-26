package com.example.final_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView; // --- Import TextView ---
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random; // --- Import Random ---

public class MainActivity extends AppCompatActivity {

    private Button nextButton;
    private TextView factTextView; // --- Add this ---

    // --- Add your list of facts ---
    private String[] bloodFacts = {
            "Type O- is the universal red cell donor.",
            "Type AB+ is the universal plasma donor.",
            "Your blood type is inherited from your parents.",
            "About 85% of the world's population is Rh+.",
            "Blood makes up about 7-8% of your total body weight.",
            "Japan has a blood type personality theory called 'Ketsuekigata'."
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nextButton = findViewById(R.id.nextButton);
        factTextView = findViewById(R.id.factTextView); // --- Find the new TextView ---

        // --- Add this block to set a random fact ---
        Random random = new Random();
        int factIndex = random.nextInt(bloodFacts.length);
        factTextView.setText(bloodFacts[factIndex]);
        // ---

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, InputActivity.class);
                startActivity(intent);
            }
        });
    }
}