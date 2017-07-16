package com.example.android.habittracker;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.android.habittracker.habittracker.HabitSQLite;
import com.example.android.habittracker.habittracker.habit.habitEntry;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private EditText date;
    private DatePickerDialog datePickerDialog;
    private EditText habit;
    private EditText time;
    private Spinner repetition;
    private Spinner remainder;

    private int mrepetition = 0;
    private int mremainder = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        habit = (EditText) findViewById(R.id.habit);
        ImageView dateimg = (ImageView) findViewById(R.id.imgdate);
        date = (EditText) findViewById(R.id.date);
        dateimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c;
                c = Calendar.getInstance();

                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                date.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }

        });
        ImageView timeimg = (ImageView) findViewById(R.id.imgtime);
        time = (EditText) findViewById(R.id.time);
        timeimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        if (selectedMinute == 0) {
                            time.setText(selectedHour + ":00");
                        } else if (selectedMinute < 10) {
                            time.setText(selectedHour + ":0" + selectedMinute);
                        } else {
                            time.setText(selectedHour + ":" + selectedMinute);
                        }
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });
        final Switch switchremainder = (Switch) findViewById(R.id.remainder);
        switchremainder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean ischecked) {
                if (ischecked) {
                    remainder.setVisibility(View.VISIBLE);
                    remainder = (Spinner) findViewById(R.id.spinner_remainder);
                    setupSpinnerRemainder();
                } else {
                    remainder.setVisibility(View.GONE);
                }
            }
        });

        repetition = (Spinner) findViewById(R.id.spinner_repetition);
        setupSpinnerRepetition();
        remainder = (Spinner) findViewById(R.id.spinner_remainder);
        setupSpinnerRemainder();
        Button save = (Button) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertHabit();
                displayDatabaseInfo();
            }
        });
        Button cancel = (Button) findViewById(R.id.cancel);
        final Context context = MainActivity.this;
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                habit.setText("");
                time.setText("");
                date.setText("");
                repetition = (Spinner) findViewById(R.id.spinner_repetition);
                setupSpinnerRepetition();
                remainder = (Spinner) findViewById(R.id.spinner_remainder);
                setupSpinnerRemainder();
                switchremainder.setChecked(false);
                remainder.setVisibility(View.GONE);
                Toast.makeText(context, "Habit is cancelled", Toast.LENGTH_SHORT).show();
            }
        });
        displayDatabaseInfo();
    }

    private void setupSpinnerRepetition() {

        ArrayAdapter repetitionSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_repetition_options, android.R.layout.simple_spinner_item);

        repetitionSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        repetition.setAdapter(repetitionSpinnerAdapter);

        repetition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.daily))) {
                        mrepetition = habitEntry.REPETITION_DAILY;
                    } else if (selection.equals(getString(R.string.weekly))) {
                        mrepetition = habitEntry.REPETITION_WEEKLY;
                    } else if (selection.equals(getString(R.string.monthly))) {
                        mrepetition = habitEntry.REPETITION_MONTHLY;
                    } else {
                        mrepetition = habitEntry.REPETITION_YEARLY;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mrepetition = 0;
            }
        });
    }

    private void setupSpinnerRemainder() {

        ArrayAdapter remainderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_remainder_options, android.R.layout.simple_spinner_item);

        remainderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        remainder.setAdapter(remainderSpinnerAdapter);

        remainder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.min5))) {
                        mremainder = habitEntry.REMAINDER_5MIN;
                    } else if (selection.equals(getString(R.string.no))) {
                        mremainder = habitEntry.REMAINDER_NO;
                    } else if (selection.equals(getString(R.string.min10))) {
                        mremainder = habitEntry.REMAINDER_10MIN;
                    } else if (selection.equals(getString(R.string.min30))) {
                        mremainder = habitEntry.REMAINDER_30MIN;
                    } else {
                        mremainder = habitEntry.REMAINDER_1HR;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mremainder = 0;
            }
        });
    }

    public void insertHabit() {
        String mhabit = habit.getText().toString().trim();
        String mdate = date.getText().toString().trim();
        String mtime = time.getText().toString().trim();
        HabitSQLite mDbHelper = new HabitSQLite(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(habitEntry.COLUMN_HABIT_NAME, mhabit);
        values.put(habitEntry.COLUMN_HABIT_DATE, mdate);
        values.put(habitEntry.COLUMN_HABIT_TIME, mtime);
        values.put(habitEntry.COLUMN_HABIT_REPETITION, mrepetition);
        values.put(habitEntry.COLUMN_HABIT_REMAINDER, mremainder);
        long newRowId = db.insert(habitEntry.TABLE_NAME, null, values);
        if (newRowId == -1)
            Toast.makeText(this, "Error with saving Habit", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "Habit saved with row id: " + newRowId, Toast.LENGTH_SHORT).show();
    }

    private void displayDatabaseInfo() {

        HabitSQLite mDbHelper = new HabitSQLite(this);

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                habitEntry._ID,
                habitEntry.COLUMN_HABIT_NAME,
                habitEntry.COLUMN_HABIT_DATE,
                habitEntry.COLUMN_HABIT_TIME,
                habitEntry.COLUMN_HABIT_REPETITION,
                habitEntry.COLUMN_HABIT_REMAINDER
        };
        Cursor cursor = db.query(habitEntry.TABLE_NAME, projection, null, null, null, null, null);

        try {
            TextView displayView = (TextView) findViewById(R.id.text);
            displayView.setText("The Habits table contains " + cursor.getCount() + " Habits.\n\n");
            displayView.append(habitEntry._ID + " - " +
                    habitEntry.COLUMN_HABIT_NAME + " - " +
                    habitEntry.COLUMN_HABIT_DATE + " - " +
                    habitEntry.COLUMN_HABIT_TIME + " - " +
                    habitEntry.COLUMN_HABIT_REPETITION + " - " +
                    habitEntry.COLUMN_HABIT_REMAINDER + "\n");
            int idColumnIndex = cursor.getColumnIndex(habitEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(habitEntry.COLUMN_HABIT_NAME);
            int dateColumnIndex = cursor.getColumnIndex(habitEntry.COLUMN_HABIT_DATE);
            int timeColumnIndex = cursor.getColumnIndex(habitEntry.COLUMN_HABIT_TIME);
            int repetitionColumnIndex = cursor.getColumnIndex(habitEntry.COLUMN_HABIT_REPETITION);
            int remainderColumnIndex = cursor.getColumnIndex(habitEntry.COLUMN_HABIT_REMAINDER);
            while (cursor.moveToNext()) {
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                String currentdate = cursor.getString(dateColumnIndex);
                String currenttime = cursor.getString(timeColumnIndex);
                String currentrepetition = cursor.getString(repetitionColumnIndex);
                int currentremainder = cursor.getInt(remainderColumnIndex);
                displayView.append(("\n" + currentID + "  -  " +
                        currentName + "  -  " +
                        currentdate + "  -  " +
                        currenttime + "  -  " +
                        currentrepetition + "  -  " +
                        currentremainder));
            }
        } finally {
            cursor.close();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }
}
