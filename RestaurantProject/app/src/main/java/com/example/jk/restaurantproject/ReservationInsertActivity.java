package com.example.jk.restaurantproject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.text.Format;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by JK on 2015. 12. 5..
 */
public class ReservationInsertActivity extends Activity {

    Button idSearch,tableSeletButton;
    //DatePickerDialog datePickerDialog;
    DatePicker datePicker;
    TimePicker timePicker;
    TextView tableName;
    NumberPicker minutePicker;
    private Boolean mEvent=false;
    String timeMsg;
    String dateMsg;
    String tableID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservationinsertlayout);

        idSearch=(Button)findViewById(R.id.idSearchButton);
        tableName=(TextView)findViewById(R.id.reservationInsertTableNameTextView);
        tableSeletButton=(Button)findViewById(R.id.tableSeletButton);

        datePicker=(DatePicker)findViewById(R.id.reservationDatePicker);


        Intent intent=getIntent();
        tableID=intent.getStringExtra("TABLEID");
        if(tableID!=null)
        {
            tableName.setText(tableID);
        }


        // ((ViewGroup)datePickerDialog.getDatePicker()).findViewById(Resources.getSystem().getIdentifier("year","id","android")).setVisibility(View.GONE);
        try{
            Field[] f=datePicker.getClass().getDeclaredFields();
            for(Field filed:f)
            {
                if(filed.getName().equals("mYearPicker") || filed.getName().equals("mYearSpinner"))
                {
                    filed.setAccessible(true);
                    Object dayPicker=new Object();
                    dayPicker=filed.get(datePicker);
                    ((View)dayPicker).setVisibility(View.GONE);
                }
            }
        } catch (IllegalAccessException e) {
            Log.d("ERROR",e.getMessage());
        }

        datePicker.init(datePicker.getYear(),datePicker.getMonth(), datePicker.getDayOfMonth(),new DatePicker.OnDateChangedListener()
        {
            public void onDateChanged(DatePicker view,int year ,int monthOfYear, int dayOfMonth) {
                dateMsg=String.format("%d월 %d일 ",monthOfYear+1,dayOfMonth);

            }
        });


        timePicker=(TimePicker)findViewById(R.id.reservationTimePicker);

        setTimePickerInterval(timePicker);

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                timeMsg=String.format("%d : %d",hourOfDay,minute*10);
                Toast.makeText(ReservationInsertActivity.this, dateMsg+timeMsg, Toast.LENGTH_SHORT).show();
            }
        });



    }


    private void setTimePickerInterval(TimePicker timePicker)
    {
        try{
            Class<?> classForid=Class.forName("com.android.internal.R$id");

            Field field=classForid.getField("minute");
            minutePicker=(NumberPicker)timePicker.findViewById(field.getInt(null));

            minutePicker.setMinValue(0);
            minutePicker.setMaxValue(5);
            List<String> display=new ArrayList<String>();

            for(int i=0;i<60;i+=10)
            {
                display.add(String.format("%02d",i));
            }

            minutePicker.setDisplayedValues(new String[]{"0","10","20","30","40","50"});
            minutePicker.setWrapSelectorWheel(true);




        }catch (Exception e)
        {

        }
    }

    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.idSearchButton:
            {

                break;
            }

            case R.id.tableSeletButton:
            {
                Intent intent=new Intent(this,TableChooseActivity.class);
                intent.putExtra("TABLESELECT","ADMININSERT");
                startActivity(intent);

                break;
            }

            case R.id.reservationButton:
            {

                Intent intent=new Intent(this,AdminReservationManageActivity.class);


                startActivity(intent);

                break;
            }
        }
    }
}
