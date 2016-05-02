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
public class OrderCourseActivity extends Activity{

    ListView listView;
    ArrayList<String> names=new ArrayList<String>();
    ArrayAdapter<String> adapter;
    String tableName;

    String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordercourselayout);
        listView=(ListView)findViewById(R.id.orderCourseListView);


        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,names);
        listView.setAdapter(adapter);

        Intent intent=getIntent();
        tableName=intent.getStringExtra("TABLE");
        from=intent.getStringExtra("FROM");

        adapter.add("테스트");
        adapter.add("2");
        adapter.notifyDataSetChanged();



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(OrderCourseActivity.this,OrderMenuActivity.class);
                intent.putExtra("COURSE",adapter.getItem(position));
                intent.putExtra("TABLE",tableName);
                intent.putExtra("FROM",from);
                startActivity(intent);
            }
        });



    }
}
