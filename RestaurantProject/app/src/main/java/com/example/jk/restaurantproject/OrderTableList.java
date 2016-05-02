package com.example.jk.restaurantproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by JK on 2015. 12. 6..
 */
public class OrderTableList extends Activity {

    ListView tableListView;

    ArrayList<String> names=new ArrayList<String>();
    ArrayAdapter<String> adapter;

    String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordertablelist);

        tableListView=(ListView)findViewById(R.id.orderTableListView);

        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,names);
        tableListView.setAdapter(adapter);

        adapter.add("test0");
        adapter.notifyDataSetChanged();

        Intent intent=getIntent();
        from=intent.getStringExtra("FROM");


        tableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(OrderTableList.this, TableOrderList.class);
                intent.putExtra("TABLE", adapter.getItem(position));
                intent.putExtra("FROM",from);
                startActivity(intent);
            }
        });


    }



}
