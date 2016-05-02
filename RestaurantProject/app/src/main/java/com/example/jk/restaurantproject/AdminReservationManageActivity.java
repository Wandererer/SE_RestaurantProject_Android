package com.example.jk.restaurantproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by JK on 2015. 12. 5..
 */
public class AdminReservationManageActivity extends Activity {

    Button reservationAddButton;
    CheckBox reservationDeleteButton,reservationUpdateButton;
    ListView reservationListView;

    ReservationDataProvider reservation;
    ArrayList<ReservationDataProvider> reservData=new ArrayList<ReservationDataProvider>();
    CustomReservationListViewAdapter adapter;

    String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservationmanage);

        reservationAddButton=(Button)findViewById(R.id.adminReservationAddButton);
        reservationDeleteButton=(CheckBox)findViewById(R.id.adminReservationDeleteCheckButton);
        reservationUpdateButton=(CheckBox)findViewById(R.id.adminReservationUpdateCheckButton);

        reservationListView=(ListView)findViewById(R.id.adminReservationListView);

        adapter=new CustomReservationListViewAdapter(getApplicationContext(),reservData);

        reservationListView.setAdapter(adapter);
        //String name,String tableNum,String phoneNum,String dateAndTime
        reservation=new ReservationDataProvider("임재균","TABLE0","01071067859","201201230");

        adapter.add(reservation);
        adapter.notifyDataSetChanged();

        Intent intent=getIntent();
        from=intent.getStringExtra("FROM");



        reservationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                reservation=adapter.getItem(position);
                if(reservationUpdateButton.isChecked())
                {
                    Intent intent=new Intent(AdminReservationManageActivity.this,ReservationUpdateActivity.class);
                    intent.putExtra("TYPE",from);
                    intent.putExtra("CNAME",reservation.getName());
                    intent.putExtra("CPHONE",reservation.getPhoneNum());
                    startActivity(intent);

                }
                if(reservationDeleteButton.isChecked())
                {
                    adapter.remove(reservation);
                    adapter.notifyDataSetChanged();
                }

            }
        });

    }

    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.adminReservationAddButton:
            {
                Intent intent =new Intent(this,ReservationInsertActivity.class);
                startActivity(intent);

                if(reservationDeleteButton.isChecked())
                {
                    reservationDeleteButton.setChecked(false);
                }


                if(reservationUpdateButton.isChecked())
                {
                    reservationUpdateButton.setChecked(false);
                }


                break;
            }
            case R.id.adminReservationUpdateCheckButton:
            {
               if(reservationDeleteButton.isChecked())
               {
                   reservationDeleteButton.setChecked(false);
               }



                break;
            }
            case R.id.adminReservationDeleteCheckButton:
            {
                if(reservationUpdateButton.isChecked())
                {
                    reservationUpdateButton.setChecked(false);
                }

            }
        }
    }
}
