package com.example.final_project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "BloodGroup.db";
    public static final String TABLE_NAME = "prediction_history";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "PARENT1_ABO";
    public static final String COL_3 = "PARENT1_RH";
    public static final String COL_4 = "PARENT2_ABO";
    public static final String COL_5 = "PARENT2_RH";
    public static final String COL_6 = "PREDICTION_ABO";
    public static final String COL_7 = "PREDICTION_RH";
    public static final String COL_8 = "USER_NAME"; // --- NEW COLUMN ---

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // --- UPDATED TABLE CREATION QUERY ---
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_2 + " TEXT, " +
                COL_3 + " TEXT, " +
                COL_4 + " TEXT, " +
                COL_5 + " TEXT, " +
                COL_6 + " TEXT, " +
                COL_7 + " TEXT, " +
                COL_8 + " TEXT)"; // Added new column
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This will drop the old table and create a new one.
        // For a real app, you would use ALTER TABLE to add the column.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // --- UPDATED METHOD to accept 'name' ---
    public boolean addPrediction(String p1_abo, String p1_rh, String p2_abo, String p2_rh, String result_abo, String result_rh, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, p1_abo);
        contentValues.put(COL_3, p1_rh);
        contentValues.put(COL_4, p2_abo);
        contentValues.put(COL_5, p2_rh);
        contentValues.put(COL_6, result_abo);
        contentValues.put(COL_7, result_rh);
        contentValues.put(COL_8, name); // Add name to database

        long insertResult = db.insert(TABLE_NAME, null, contentValues);
        return insertResult != -1;
    }

    public Cursor getAllPredictions() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY " + COL_1 + " DESC", null);
    }

    public void clearHistory() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
    }
}