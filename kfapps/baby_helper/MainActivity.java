package com.kfapps.baby_helper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;

import java.sql.Array;

public class MainActivity extends Activity
{
    private FeedingDB dbhandler;
    private EditText editText;
    private CalendarView calenderView;
    private String selectedDate;
    private SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.feedingDetails);
        calenderView = findViewById(R.id.calendarView);


        calenderView.setOnDateChangeListener(new CalendarView.OnDateChangeListener()
        {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth)
            {
                selectedDate = Integer.toString(year) + Integer.toString(month) + Integer.toString(dayOfMonth);
                ReadDatabase(calendarView);
            }
        });
        try
        {
            dbhandler = new FeedingDB(this, "calendarDB", null, 1);
            sqLiteDatabase = dbhandler.getWritableDatabase();
            sqLiteDatabase.execSQL("CREATE TABLE FeedingCalendar(Date TEXT, Feeding TEXT)");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

    }

    public void AddFeedingDB (View view)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put("Date",selectedDate);
        contentValues.put("Feeding", editText.getText().toString());
        sqLiteDatabase.insert("FeedingCalendar", null,contentValues);

    }

    public void ReadDatabase(View view)
    {
        String query = "Select Feeding from FeedingCalendar where Date =" + selectedDate;
        try
        {
            Cursor cursor = sqLiteDatabase.rawQuery(query, null,null);
            cursor.moveToFirst();
            editText.setText(cursor.getString(0));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            editText.setText("");
        }
    }
    public void viewDay(View view)
    {
        ReadDatabase(calenderView);
    }
}
