package com.example.final_project;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    ListView historyListView;
    Button clearHistoryButton;
    DatabaseHelper myDb;
    ArrayList<String> historyList;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        historyListView = findViewById(R.id.historyListView);
        clearHistoryButton = findViewById(R.id.clearHistoryButton);
        myDb = new DatabaseHelper(this);
        historyList = new ArrayList<>();

        loadHistoryData();

        clearHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showClearHistoryConfirmationDialog();
            }
        });
    }

    private void showClearHistoryConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Clear History")
                .setMessage("Are you sure you want to delete all predictions?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Yes, Clear", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myDb.clearHistory();
                        loadHistoryData(); // Reload the list
                        Toast.makeText(HistoryActivity.this, "History Cleared", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void loadHistoryData() {
        historyList.clear();
        Cursor cursor = myDb.getAllPredictions();

        // --- 1. Initialize counter ---
        int counter = 1;

        if (cursor.getCount() == 0) {
            historyList.add("No predictions saved yet.");
        } else {
            while (cursor.moveToNext()) {
                // Get data from database columns
                String name = cursor.getString(7);
                String p1 = "P1: " + cursor.getString(1) + cursor.getString(2);
                String p2 = "P2: " + cursor.getString(3) + cursor.getString(4);
                String resABO = cursor.getString(5);
                String resRh = cursor.getString(6);

                String historyEntry;
                String header;
                String body = p1 + "  &  " + p2 + "\n" +
                        "Result -> ABO: " + resABO + ", Rh: " + resRh;

                // --- 2. Add counter to the string ---
                if (name != null && !name.isEmpty()) {
                    // If name exists, add it to the top
                    header = counter + ". Name: " + name;
                } else {
                    // If no name, just number the prediction
                    header = counter + ". Prediction";
                }

                historyEntry = header + "\n" + body;
                historyList.add(historyEntry);

                // --- 3. Increment counter ---
                counter++;
            }
        }
        cursor.close();

        // Check if adapter already exists
        if (adapter == null) {
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, historyList);
            historyListView.setAdapter(adapter);
        } else {
            // If adapter already exists, just notify it that the data changed
            adapter.notifyDataSetChanged();
        }
    }
}