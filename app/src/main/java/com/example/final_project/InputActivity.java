package com.example.final_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText; // --- Import EditText ---
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText; // --- Import TextInputEditText ---
import java.util.HashSet;
import java.util.Set;

public class InputActivity extends AppCompatActivity {

    DatabaseHelper myDb;
    Spinner spinnerP1ABO, spinnerP2ABO;
    RadioGroup rgP1Rh, rgP2Rh;
    Button predictButton, historyButton;
    TextInputEditText nameEditText; // --- NEW: Add name field variable ---

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        myDb = new DatabaseHelper(this);

        // Find UI elements
        nameEditText = findViewById(R.id.nameEditText); // --- NEW: Find name field ---
        spinnerP1ABO = findViewById(R.id.spinnerParent1ABO);
        spinnerP2ABO = findViewById(R.id.spinnerParent2ABO);
        rgP1Rh = findViewById(R.id.radioGroupParent1Rh);
        rgP2Rh = findViewById(R.id.radioGroupParent2Rh);
        predictButton = findViewById(R.id.predictButton);
        historyButton = findViewById(R.id.historyButton);

        predictButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // --- NEW: Get name from UI ---
                String name = nameEditText.getText().toString().trim();

                // Get blood group values from UI
                String p1ABO = spinnerP1ABO.getSelectedItem().toString();
                String p2ABO = spinnerP2ABO.getSelectedItem().toString();

                RadioButton rbP1Rh = findViewById(rgP1Rh.getCheckedRadioButtonId());
                RadioButton rbP2Rh = findViewById(rgP2Rh.getCheckedRadioButtonId());
                String p1Rh = rbP1Rh.getText().toString().contains("+") ? "+" : "-";
                String p2Rh = rbP2Rh.getText().toString().contains("+") ? "+" : "-";

                // --- Prediction Logic ---
                String predictedABO = predictABO(p1ABO, p2ABO);
                String predictedRh = predictRh(p1Rh, p2Rh);

                // --- UPDATED: Save to Database (with name) ---
                boolean isInserted = myDb.addPrediction(p1ABO, p1Rh, p2ABO, p2Rh, predictedABO, predictedRh, name);
                if (isInserted) {
                    Toast.makeText(InputActivity.this, "Prediction Saved", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(InputActivity.this, "Save Failed", Toast.LENGTH_SHORT).show();
                }

                // --- Go to Result Activity ---
                Intent intent = new Intent(InputActivity.this, ResultActivity.class);
                intent.putExtra("ABO_RESULT", predictedABO);
                intent.putExtra("RH_RESULT", predictedRh);
                startActivity(intent);
            }
        });

        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InputActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });
    }

    // --- (Prediction logic methods are unchanged) ---
    private String predictABO(String parent1, String parent2) {
        Set<String> possibleTypes = new HashSet<>();
        String[] alleles1 = getAlleles(parent1);
        String[] alleles2 = getAlleles(parent2);
        for (String a1 : alleles1) {
            for (String a2 : alleles2) {
                possibleTypes.add(getPhenotype(a1, a2));
            }
        }
        return possibleTypes.toString();
    }
    private String[] getAlleles(String aboType) {
        switch (aboType) {
            case "A": return new String[]{"A", "O"};
            case "B": return new String[]{"B", "O"};
            case "AB": return new String[]{"A", "B"};
            case "O": return new String[]{"O", "O"};
            default: return new String[]{"O"};
        }
    }
    private String getPhenotype(String allele1, String allele2) {
        if (allele1.equals("O")) { String temp = allele1; allele1 = allele2; allele2 = temp; }
        if (allele1.equals("B") && allele2.equals("A")) { String temp = allele1; allele1 = allele2; allele2 = temp; }
        String genotype = allele1 + allele2;
        switch (genotype) {
            case "AA": case "AO": return "A";
            case "BB": case "BO": return "B";
            case "AB": return "AB";
            case "OO": return "O";
            default: return "Unknown";
        }
    }
    private String predictRh(String parent1Rh, String parent2Rh) {
        if (parent1Rh.equals("-") && parent2Rh.equals("-")) { return "[-]"; }
        return "[+, -]";
    }
}