package com.kfapps.baby_helper;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity
{
    EditText DateOfFeeding, TimeOfFeeding, AmountOfFeeding;
    Button SaveFeeding, ShowFeeding;
    String FeedingDate, FeedingTime, FeedingAmount;
    SQLiteDatabase SQLITEDATABASE;
    Boolean CheckEditTextEmpty;
    String SQLiteQuery;
    private CalendarView calenderView;
    private String selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DateOfFeeding = (EditText) findViewById(R.id.editText1);
        TimeOfFeeding = (EditText) findViewById(R.id.editText2);
        AmountOfFeeding = (EditText) findViewById(R.id.editText3);
        calenderView = findViewById(R.id.calendarView);
        SaveFeeding = (Button) findViewById(R.id.button1);
        ShowFeeding = (Button) findViewById(R.id.button2);

        //Adds Feeding to DB when SaveFeeding Button is clicked
        SaveFeeding.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                DBCreate();
                SubmitData2SQLiteDB();
            }
        });
        //Grabs Date from selected calender converts to string and assigns to DateOfFeeding box
        calenderView.setOnDateChangeListener(new CalendarView.OnDateChangeListener()
        {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth)
            {
                selectedDate = Integer.toString(month + 1) + "/" + Integer.toString(dayOfMonth) + "/" + Integer.toString(year);
                DateOfFeeding.setText(selectedDate);

            }
        });
        //Sends To GetDataActivity as well as the selected date as a string
        ShowFeeding.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String RetrieveFeedingDate = selectedDate;
                Intent intent = new Intent(MainActivity.this, GetDataActivity.class);
                intent.putExtra("RetrieveFeedingDate", selectedDate);
                startActivity(intent);

            }
        });
    }

    //opens or creates DB
    public void DBCreate()
    {
        SQLITEDATABASE = openOrCreateDatabase("FeedingDatabase", Context.MODE_PRIVATE, null);
        SQLITEDATABASE.execSQL("CREATE TABLE IF NOT EXISTS feedingTable(date_feeding VARCHAR PRIMARY KEY, time_feeding VARCHAR, amount_feeding VARCHAR);");
    }

    //Submits entered data to DB on Save feeding click
    public void SubmitData2SQLiteDB()
    {
        FeedingDate = DateOfFeeding.getText().toString();
        FeedingTime = TimeOfFeeding.getText().toString();
        FeedingAmount = AmountOfFeeding.getText().toString();
        CheckEditTextEmptyOrNot(FeedingDate, FeedingTime, FeedingAmount);


        //Verifies all fields filled and prompts user accordingly
        if(CheckEditTextEmpty == true)
        {
            SQLiteQuery = "INSERT INTO feedingTable (date_feeding, time_feeding, amount_feeding) VALUES('"+FeedingDate+"', '"+FeedingTime+"', '"+FeedingAmount+"');";
            SQLITEDATABASE.execSQL(SQLiteQuery);
            Toast.makeText(MainActivity.this, "Data Submit Successful", Toast.LENGTH_LONG).show();
            ClearEditTextAfterDoneTask();
        }
        else
        {
            Toast.makeText(MainActivity.this, "Please Fill All Fields", Toast.LENGTH_LONG).show();
        }
    }
    public void CheckEditTextEmptyOrNot (String FeedingDate,String FeedingTime, String FeedingAmount)
    {
        if(TextUtils.isEmpty(FeedingDate) || TextUtils.isEmpty(FeedingTime) || TextUtils.isEmpty(FeedingAmount))
        {
            CheckEditTextEmpty = false;
        }
        else
        {
            CheckEditTextEmpty = true;
        }
    }
    public void ClearEditTextAfterDoneTask()
    {
        DateOfFeeding.getText().clear();
        TimeOfFeeding.getText().clear();
        AmountOfFeeding.getText().clear();
    }
}


