package com.example.android.habittracker.habittracker;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HabitSQLite extends SQLiteOpenHelper {
    private final static int database_version = 1;
    private final static String database_name = "habits.db";
    private static final String SQl_create_entries = "CREATE TABLE " + habit.habitEntry.TABLE_NAME + " (" + habit.habitEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + habit.habitEntry.COLUMN_HABIT_NAME + " TEXT NOT NULL," + habit.habitEntry.COLUMN_HABIT_DATE + " TEXT NOT NULL," + habit.habitEntry.COLUMN_HABIT_TIME + " TEXT NOT NULL," + habit.habitEntry.COLUMN_HABIT_REPETITION + " INTEGER NOT NULL DEFAULT 0," + habit.habitEntry.COLUMN_HABIT_REMAINDER + " INTEGER NOT NULL DEFAULT 0" + ")";
    private static final String SQL_delete_entries = "DROP TABLE IF EXISTS " + habit.habitEntry.TABLE_NAME;

    public HabitSQLite(Context context) {
        super(context, database_name, null, database_version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQl_create_entries);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int upversion, int downversion) {
        db.execSQL(SQL_delete_entries);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
