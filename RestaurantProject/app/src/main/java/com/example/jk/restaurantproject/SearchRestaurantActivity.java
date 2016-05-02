package com.example.jk.restaurantproject;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

/**
 * Created by JK on 2015. 11. 30..
 */
public class SearchRestaurantActivity extends Activity {

    static final String[] restaurant=new String[] {"vips", "outback","tgiFriday"};

    AutoCompleteTextView auto;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchrestaurantname);

        auto=(AutoCompleteTextView)findViewById(R.id.auto);
        text=(TextView)findViewById(R.id.testText);
        ArrayAdapter adapter=new ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line,restaurant);
        auto.setAdapter(adapter);


    }

    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.restaurantSerachButton:
            {
                text.setText(auto.getText());
            }
        }
    }
}
