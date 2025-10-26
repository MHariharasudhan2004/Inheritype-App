package com.example.final_project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    // --- Fact Bank ---
    private static final String FACT_A = "Type A: Has A antigens on red blood cells. Can safely donate to A and AB.\n\n";
    private static final String FACT_B = "Type B: Has B antigens on red blood cells. Can safely donate to B and AB.\n\n";
    private static final String FACT_AB = "Type AB: Has both A and B antigens. Known as the 'universal recipient' for plasma.\n\n";
    private static final String FACT_O = "Type O: Has no A or B antigens. Type O- is the 'universal donor' for red blood cells.\n\n";
    private static final String FACT_RH_POSITIVE = "Rh Positive (+): Has the Rh protein. About 85% of people are Rh+.\n\n";
    private static final String FACT_RH_NEGATIVE = "Rh Negative (-): Lacks the Rh protein. Can only receive Rh- blood.\n\n";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        TextView predictionResultTextView = findViewById(R.id.predictionResultTextView);
        TextView factsTextView = findViewById(R.id.factsTextView);

        // Get the data passed from InputActivity
        Intent intent = getIntent();
        String aboResult = intent.getStringExtra("ABO_RESULT");
        String rhResult = intent.getStringExtra("RH_RESULT");

        // 1. Set the main prediction text
        String mainResult = "Possible ABO Types: " + aboResult + "\n" +
                "Possible Rh Types: " + rhResult;
        predictionResultTextView.setText(mainResult);

        // 2. Build and set the facts text
        String facts = getFacts(aboResult, rhResult);
        factsTextView.setText(facts);
    }

    /**
     * Builds a string of facts based on the predicted blood groups.
     */
    private String getFacts(String abo, String rh) {
        StringBuilder sb = new StringBuilder();

        // Check which ABO types are possible
        if (abo.contains("A")) sb.append(FACT_A);
        if (abo.contains("B")) sb.append(FACT_B);
        if (abo.contains("AB")) sb.append(FACT_AB);
        if (abo.contains("O")) sb.append(FACT_O);

        // Check which Rh types are possible
        if (rh.contains("+")) sb.append(FACT_RH_POSITIVE);
        if (rh.contains("-")) sb.append(FACT_RH_NEGATIVE);

        if (sb.length() == 0) {
            return "No facts available for this combination.";
        }
        return sb.toString();
    }
}