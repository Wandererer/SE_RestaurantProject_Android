package com.example.jk.restaurantproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by JK on 2015. 12. 6..
 */
public class CustomOrderListViewAdapter extends ArrayAdapter<OrderDataProduct> {
    public CustomOrderListViewAdapter(Context context,ArrayList<OrderDataProduct> order)
    {
        super(context,R.layout.orderlistview_layout,order);
    }

    public View getView(int position,View convertView, ViewGroup parent)
    {
        LayoutInflater inflater=LayoutInflater.from(getContext());
        View customeView=inflater.inflate(R.layout.orderlistview_layout, parent, false);

        OrderDataProduct order=getItem(position);
        TextView nameTextView=(TextView)customeView.findViewById(R.id.orderMenuNameTextView);
        TextView howMany=(TextView)customeView.findViewById(R.id.orderHowManyTextView);
        TextView price=(TextView)customeView.findViewById(R.id.orderPriceTextView);

        nameTextView.setText("이름 :"+order.getFoodName().toString());
        howMany.setText("개수 : "+order.getHowMany().toString());
        price.setText("가격 : "+order.getPrice().toString());



        return customeView;
    }
}
