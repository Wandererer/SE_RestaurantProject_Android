package com.example.jk.restaurantproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by JK on 2015. 12. 6..
 */
public class OrderMenuActivity extends Activity {

    TextView courseName;
    ListView listView;
    String tablename;

    ArrayList<MenuDataProvider> menuData=new ArrayList<MenuDataProvider>();
    CustomListviewAdapter myAdapter;
    MenuDataProvider menu;
    OrderDataProduct order;

    String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordermenulayout);

        courseName=(TextView)findViewById(R.id.orderCourseNameTextView);
        listView=(ListView)findViewById(R.id.orderMenuListView);

        myAdapter=new CustomListviewAdapter(getApplicationContext(),menuData);
        listView.setAdapter(myAdapter);

        Intent intent=getIntent();
        from=intent.getStringExtra("FROM");
        courseName.setText(intent.getStringExtra("COURSE"));
        tablename=intent.getStringExtra("TABLE");

/*

  public OrderDataProduct(String food,String many,String price,int originalPrice)
    {
        super();
        this.setFoodName(food);
        this.setPrice(price);
        this.setHowMany(many);
        this.setOriginalPrice(originalPrice);
    }
 */


        menu=new MenuDataProvider ("깐풍기","5000","테스트임니다");
        myAdapter.add(menu);
        order=new OrderDataProduct(menu.getMenuName(),"2",menu.getPriceName(),Integer.parseInt(menu.getPriceName()));
        myAdapter.notifyDataSetChanged();








        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                menu=myAdapter.getItem(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(OrderMenuActivity.this);
                builder = new AlertDialog.Builder(OrderMenuActivity.this);
                builder.setMessage("INSERT");
                builder.setCancelable(false);


                LinearLayout h1=new LinearLayout(OrderMenuActivity.this);
                h1.setOrientation(LinearLayout.HORIZONTAL);

                final TextView name=new TextView(OrderMenuActivity.this);
                name.setText("음식 이름 : ");
                name.setTextSize(15);

                final TextView foodname=new TextView(OrderMenuActivity.this);
                foodname.setText(menu.getMenuName());
                foodname.setTextSize(15);

                h1.addView(name);
                h1.addView(foodname);

                LinearLayout h2=new LinearLayout(OrderMenuActivity.this);
                h1.setOrientation(LinearLayout.HORIZONTAL);

                final TextView howmanyname=new TextView(OrderMenuActivity.this);
                howmanyname.setText("개수 : ");
                howmanyname.setTextSize(15);

                final EditText input = new EditText(OrderMenuActivity.this);
                input.setSingleLine(true);
                input.setHint("개수");
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                input.setSingleLine(true);

                h2.addView(howmanyname);
                h2.addView(input);



                LinearLayout ll=new LinearLayout(OrderMenuActivity.this);
                ll.setOrientation(LinearLayout.VERTICAL);
                ll.addView(h1);
                ll.addView(h2);
                builder.setView(ll);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        //이게 새로운 주문한거에 추가됨
                        order=new OrderDataProduct(menu.getMenuName(),input.getText().toString(),menu.getPriceName(),Integer.parseInt(menu.getPriceName()));

                        dialog.cancel();
                        Intent intent=new Intent(OrderMenuActivity.this,TableOrderList.class);
                        intent.putExtra("TABLE",tablename);
                        startActivity(intent);

                    }
                });
                AlertDialog alert = builder.create();


                alert.show();




            }
        });



    }
}
