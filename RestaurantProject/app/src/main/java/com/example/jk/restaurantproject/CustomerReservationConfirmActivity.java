package com.example.jk.restaurantproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by JK on 2015. 12. 6..
 */
public class CustomerReservationConfirmActivity extends Activity {

    ReservationDataProvider reservation;
    ArrayList<ReservationDataProvider> reservData=new ArrayList<ReservationDataProvider>();
    CustomReservationListViewAdapter adapter;


    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customerreservationconfirmlayout);

        listView=(ListView)findViewById(R.id.customerReservationListView);


        adapter=new CustomReservationListViewAdapter(getApplicationContext(),reservData);

        listView.setAdapter(adapter);

        reservation=new ReservationDataProvider("임재균","table1","sdflj","0123012");
        adapter.add(reservation);
        adapter.notifyDataSetChanged();



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final ReservationDataProvider data=adapter.getItem(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(CustomerReservationConfirmActivity.this);
                builder = new AlertDialog.Builder(CustomerReservationConfirmActivity.this);
                builder.setCancelable(false);


                builder.setNeutralButton("CHANGE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(CustomerReservationConfirmActivity.this, ReservationUpdateActivity.class);
                        intent.putExtra("TYPE","customer");
                        intent.putExtra("CNAME", data.getName());
                        intent.putExtra("CPHONE", data.getPhoneNum());
                        startActivity(intent);

                        dialog.cancel();

                    }
                });
                builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adapter.remove(data);
                        dialog.cancel();

                    }
                });
                AlertDialog alert = builder.create();

                alert.show();


            }
        });

    }
}
