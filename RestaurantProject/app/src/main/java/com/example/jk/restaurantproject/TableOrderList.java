package com.example.jk.restaurantproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
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
 * Created by JK on 2015. 12. 6..
 */
public class TableOrderList extends Activity {

    ListView listView;
    TextView priceTextView;
    TextView tableName;
    CheckBox updateButton,deleteButton;
    int sum=0;

    OrderDataProduct order;
    ArrayList<OrderDataProduct> orderData=new ArrayList<OrderDataProduct>();
    CustomOrderListViewAdapter adapter;
    
    String from;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_orderlist);

        tableName=(TextView)findViewById(R.id.orderTableIdTextView);
        priceTextView=(TextView)findViewById(R.id.orderPriceTextView);
        listView=(ListView)findViewById(R.id.orderContentListView);

        updateButton=(CheckBox)findViewById(R.id.orderListUpdateCheckBox);
        deleteButton=(CheckBox)findViewById(R.id.orderListDeleteCheckBox);

        Intent intent=getIntent();
        
        from=intent.getStringExtra("FROM");
        
        tableName.setText(intent.getStringExtra("TABLE"));
        priceTextView.setText("값 : " + sum);



        adapter=new CustomOrderListViewAdapter(getApplicationContext(),orderData);
        order=new OrderDataProduct("깐풍기","2","10000",5000);
        adapter.add(order);

        listView.setAdapter(adapter);

        changeSumPrice();
        //String name,String tableNum,String phoneNum,String dateAndTime
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                order=adapter.getItem(position);
                if(deleteButton.isChecked())
                {
                    adapter.remove(order);
                    changeSumPrice();
                    adapter.notifyDataSetChanged();

                }

                if(updateButton.isChecked())
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(TableOrderList.this);
                    builder = new AlertDialog.Builder(TableOrderList.this);
                    builder.setMessage("UPDATE");
                    builder.setCancelable(false);


                    LinearLayout h1=new LinearLayout(TableOrderList.this);
                    h1.setOrientation(LinearLayout.HORIZONTAL);

                    TextView name=new TextView(TableOrderList.this);
                    name.setText("음식 이름 : ");
                    name.setTextSize(15);

                    TextView foodname=new TextView(TableOrderList.this);
                    foodname.setText(order.getFoodName());
                    foodname.setTextSize(15);

                    h1.addView(name);
                    h1.addView(foodname);

                    LinearLayout h2=new LinearLayout(TableOrderList.this);
                    h1.setOrientation(LinearLayout.HORIZONTAL);

                    TextView howmanyname=new TextView(TableOrderList.this);
                    howmanyname.setText("개수 : ");
                    howmanyname.setTextSize(15);

                    final EditText input = new EditText(TableOrderList.this);
                    input.setSingleLine(true);
                    input.setHint("바꿀 개수");
                    input.setInputType(InputType.TYPE_CLASS_NUMBER);
                    input.setSingleLine(true);

                    h2.addView(howmanyname);
                    h2.addView(input);



                    LinearLayout ll=new LinearLayout(TableOrderList.this);
                    ll.setOrientation(LinearLayout.VERTICAL);
                    ll.addView(h1);
                    ll.addView(h2);
                    builder.setView(ll);

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                           order.setHowMany(input.getText().toString());

                            int b=Integer.parseInt(order.getHowMany());
                            int c=order.getOriginalPrice()*b;

                            String to=Integer.toString(c);
                            order.setPrice(to);
                            changeSumPrice();
                            adapter.notifyDataSetChanged();

                            dialog.cancel();

                        }
                    });
                    AlertDialog alert = builder.create();


                    alert.show();

                }

            }
        });



    }

    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.orderListAddButton:
            {
                if(deleteButton.isChecked() || updateButton.isChecked())
                {
                    deleteButton.setChecked(false);
                    updateButton.setChecked(false);
                }
                Intent intent=new Intent(this,OrderCourseActivity.class);
                intent.putExtra("TABLE",tableName.getText().toString());
                startActivity(intent);
                break;
            }
            case R.id.orderListUpdateCheckBox:
            {
                if(deleteButton.isChecked())
                {
                    deleteButton.setChecked(false);
                }
                break;
            }
            case R.id.orderListDeleteCheckBox:
            {
                if(updateButton.isChecked())
                {
                    updateButton.setChecked(false);
                }
                break;
            }
            case R.id.orderListCleanUpButton:
            {
                if(deleteButton.isChecked() || updateButton.isChecked())
                {
                    deleteButton.setChecked(false);
                    updateButton.setChecked(false);
                }

                adapter.clear();
                break;
            }

            case R.id.orderFinishButton:
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder = new AlertDialog.Builder(this);
                builder.setMessage("계산 할 값");
                builder.setCancelable(false);



                final TextView input = new TextView(this);
                input.setText("합계  :    ");
                input.setTextSize(20);
                // builder.setView(input);

                final TextView result = new TextView(this);
                result.setText(Integer.toString(sum));
                result.setTextSize(20);
                // builder.setView(change);

                LinearLayout ll=new LinearLayout(this);
                ll.setOrientation(LinearLayout.HORIZONTAL);
                ll.addView(input);
                ll.addView(result);
                builder.setView(ll);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                        adapter.clear();
                        //계산하면 메인메뉴로 가게...

                        if (from.equals("staff")) {
                            Intent intent = new Intent(TableOrderList.this, StaffMainLayout.class);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(TableOrderList.this, AdminMainViewActivity.class);
                            startActivity(intent);
                        }


                    }
                });
                AlertDialog alert = builder.create();


                alert.show();
                break;
            }
        }
    }


    public void changeSumPrice()
    {
        sum=0;
        for(int i=0;i<adapter.getCount();i++)
        {
            sum+=adapter.getItem(i).getSum();
        }

        priceTextView.setText("값 : "+Integer.toString(sum));
    }
}
