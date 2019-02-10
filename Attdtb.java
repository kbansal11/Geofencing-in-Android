package com.example.kartik.geo;

import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Attdtb extends AppCompatActivity {

    Helper dispdb;
    TextView t2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        dispdb = new Helper(this);
        t2 = (TextView)findViewById(R.id.textView2);
        viewall();
    }

    private void viewall() {

        Cursor cursor = dispdb.getAllRecords();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
            return;
        }

        StringBuffer finalData = new StringBuffer();
        finalData.append("Date  ");
        finalData.append("Time\n");
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            finalData.append(cursor.getInt(cursor.getColumnIndex(dispdb.Date)));
            finalData.append("   ");
            finalData.append(cursor.getString(cursor.getColumnIndex(dispdb.Time)));
            finalData.append("   ");
            finalData.append("\n");
        }
        t2.setText(finalData);

    }

    protected void onStart()
    {

        super.onStart();
        dispdb.openDb();
    }

    protected void onStop()
    {
        super.onStop();
        dispdb.closeDb();
    }

}
