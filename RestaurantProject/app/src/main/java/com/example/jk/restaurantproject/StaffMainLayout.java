package com.example.jk.restaurantproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by JK on 2015. 12. 6..
 */
public class StaffMainLayout extends Activity {

    TextView resName;
    ImageView restaurantMainView;
    //Button interriorButton,orderButton,reservationButton,menuButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staffmainlayout);

        resName=(TextView)findViewById(R.id.staffMainlayoutRestaurantNameTextView);
        restaurantMainView=(ImageView)findViewById(R.id.staffMainLayoutRestaurantImageView);
        Intent intent = getIntent();
        resName.setText(intent.getStringExtra("resname"));

    }

    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.staffInterrirorButton:
            {
                Intent intent=new Intent(this,CustomerInterriorViewActivity.class);
                startActivity(intent);
                break;
            }

            case R.id.staffOrderManageButton:
            {
                Intent intent=new Intent(this,OrderTableList.class);
                intent.putExtra("FROM","staff");
                startActivity(intent);
                break;
            }

            case R.id.staffReservationManagementButton:
            {

                Intent intent=new Intent(this,AdminReservationManageActivity.class);
                intent.putExtra("FROM","staff");
                startActivity(intent);
                break;
            }

            case R.id.staffMenuButton:
            {
                Intent intent=new Intent(this,CustomerMenuViewActivity.class);
                intent.putExtra("resname",resName.getText().toString());
                intent.putExtra("FROM","staff");
                startActivity(intent);

                break;
            }
        }
    }
}
