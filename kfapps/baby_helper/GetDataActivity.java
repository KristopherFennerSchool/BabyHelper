package com.kfapps.baby_helper;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;


public class GetDataActivity extends AppCompatActivity {

    Button nextbutton, previousbutton, returnbutton ;
    EditText FeedingDate;
    EditText FeedingAmount;
    EditText FeedingTime;
    SQLiteDatabase SQLITEDATABASE ;
    String GetSQLiteQuery;
    Cursor cursor ;
    String DateOfFeeding, AmountOfFeeding, TimeOfFeeding ;
    String RequestedFeedingDate;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_data);

        //Assign text fields & buttons to Variables
        nextbutton = (Button)findViewById(R.id.button1);
        previousbutton = (Button)findViewById(R.id.button2);
        returnbutton = (Button)findViewById(R.id.button3);

        FeedingDate = (EditText)findViewById(R.id.editText1);
        FeedingAmount = (EditText)findViewById(R.id.editText2);
        FeedingTime = (EditText)findViewById(R.id.editText3);

        //Pull the selected date from calendar from main activity in a string
        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            RequestedFeedingDate = extras.getString("RetrieveFeedingDate");
        }

        //Open Database and create Query
        SQLITEDATABASE = openOrCreateDatabase("FeedingDatabase", Context.MODE_PRIVATE, null);
        GetSQLiteQuery = "SELECT * FROM feedingTable";

        //Place cursor at location in Query
        cursor = SQLITEDATABASE.rawQuery(GetSQLiteQuery, null);


        //Display Results
        GetSQLiteDatabaseRecords();

        //Moves to next result in DB query when button is clicked
        nextbutton.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view)
            {

                if (!cursor.isLast())
                {

                    cursor.moveToNext();
                }

                GetSQLiteDatabaseRecords();

            }
        });

        //Moves to previous entry in DB when clicked
        previousbutton.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view)
            {

                if (!cursor.isFirst())
                {
                    cursor.moveToPrevious();
                }
                GetSQLiteDatabaseRecords();

            }
        });

        //When clicked returns to mainactivity
        returnbutton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(GetDataActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    //Assigns DB records to text to display
    public void GetSQLiteDatabaseRecords()
    {
            FeedingDate.setText(cursor.getString(1).toString());

            FeedingTime.setText(cursor.getString(2).toString());

            FeedingAmount.setText(cursor.getString(3).toString());
    }


}
