package com.example.jk.restaurantproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JK on 2015. 12. 6..
 */
public class ReservationUpdateActivity extends Activity {

    DatePicker datePicker;
    TimePicker timePicker;
    TextView tableName,customerName,phoneNumber;
    NumberPicker minutePicker;

    Button tableSelect,update;

    private Boolean mEvent=false;
    String timeMsg;
    String dateMsg;
    String tableID;
    String comeFrom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservationupdatelayout);

        update=(Button)findViewById(R.id.allReservationUpdateButton);


        datePicker=(DatePicker)findViewById(R.id.reservationUpdateDatePicker);
        timePicker=(TimePicker)findViewById(R.id.reservationUpdateTimePicker);
        tableName=(TextView)findViewById(R.id.reservationUpdateTableNameTextView);
        customerName=(TextView)findViewById(R.id.reservationUpdateCustomerName);
        phoneNumber=(TextView)findViewById(R.id.reservationUpdatePhoneNumber);


        Intent intent=getIntent();
        comeFrom=intent.getStringExtra("TYPE");
        customerName.setText(intent.getStringExtra("CNAME"));
        phoneNumber.setText(intent.getStringExtra("CPHONE"));

        tableID=intent.getStringExtra("TABLEID");
        if(tableID!=null)
        {
            tableName.setText(tableID);
        }


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
            Log.d("ERROR", e.getMessage());
        }

        datePicker.init(datePicker.getYear(),datePicker.getMonth(), datePicker.getDayOfMonth(),new DatePicker.OnDateChangedListener()
        {
            public void onDateChanged(DatePicker view,int year ,int monthOfYear, int dayOfMonth) {
                dateMsg=String.format("%d월 %d일 ",monthOfYear+1,dayOfMonth);

            }
        });


        timePicker=(TimePicker)findViewById(R.id.reservationUpdateTimePicker);

        setTimePickerInterval(timePicker);

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                timeMsg=String.format("%d : %d",hourOfDay,minute*10);
                Toast.makeText(ReservationUpdateActivity.this, dateMsg + timeMsg, Toast.LENGTH_SHORT).show();
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
            case R.id.allReservationUpdateButton:
            {
                //두곳에서 하나에 들어오게 하고 각각 받아오는 타입으로 원하는 곳으로 보냄
                if(comeFrom.equals("admin") || comeFrom.equals("staff")) {
                    Intent intent = new Intent(this, AdminReservationManageActivity.class);
                    if(comeFrom.equals("admin"))
                    {
                        intent.putExtra("FROM","admin");
                    }
                    else
                    intent.putExtra("FROM","staff");
                    startActivity(intent);
                }
                else if(comeFrom.equals("customer"))
                {
                    Intent intent = new Intent(this, CustomerReservationConfirmActivity.class);
                    startActivity(intent);
                }

                break;
            }

            case R.id.reservationUpdateTableSelectButton:
            {
                Intent intent=new Intent(this,TableChooseActivity.class);
                intent.putExtra("TABLESELECT","ADMINUPDATE");
                startActivity(intent);

                break;
            }
        }
    }
}
