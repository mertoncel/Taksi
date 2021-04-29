package com.example.taksi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class Tip2Activity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    EditText editStartDate;
    EditText editFinishDate;
    Button btnDateTime;
    Query queryTip2;
    String startDate = "";
    String endDate = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip2);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_tip2);

        editStartDate = findViewById(R.id.editStartDate);
        editFinishDate = findViewById(R.id.editFinishDate);

        btnDateTime = findViewById(R.id.btn_DateTime);


        editStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateTimeDialog(editStartDate);
            }
        });



        editFinishDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateTimeDialog(editFinishDate);
            }
        });



        btnDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mRecyclerView.setVisibility(View.VISIBLE);
                btnDateTime.setVisibility(View.INVISIBLE);
                editStartDate.setVisibility(View.INVISIBLE);
                editFinishDate.setVisibility(View.INVISIBLE);



                queryTip2 = FirebaseDatabase.getInstance().getReference("yellowtaxi")
                        .orderByChild("tpep_pickup_datetime")
                        .startAt(startDate)
                        .endAt(endDate);

                new FirebaseDatabaseHelper().tip2VEL(new FirebaseDatabaseHelper.DataStatus() {
                    @Override
                    public void DataIsLoaded(List<YellowTaxi> tip2List, List<String> keys) {
                        new RecyclerView_Config().setConfig(mRecyclerView,Tip2Activity.this,tip2List,keys);
                    }
                },queryTip2);
            }
        });




    }

    private void dateTimeDialog(EditText date_time_in) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        calendar.set(Calendar.MINUTE,minute);

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        date_time_in.setText(simpleDateFormat.format(calendar.getTime()));

                        if(date_time_in == editStartDate) {
                            startDate = simpleDateFormat.format(calendar.getTime());
                        }
                        else {
                            endDate = simpleDateFormat.format(calendar.getTime());
                        }

                    }
                };

                new TimePickerDialog(Tip2Activity.this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true).show();
            }
        };

        new DatePickerDialog(Tip2Activity.this, dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();


    }
}