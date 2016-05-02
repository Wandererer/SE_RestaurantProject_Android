package com.example.jk.restaurantproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JK on 2015. 12. 4..
 */



public class CustomListviewAdapter extends ArrayAdapter<MenuDataProvider>

{


    public CustomListviewAdapter(Context context, ArrayList<MenuDataProvider> menus)
    {
        super(context, R.layout.menulistview_layout,menus);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=LayoutInflater.from(getContext());
        View customeView= inflater.inflate(R.layout.menulistview_layout, parent, false);

        MenuDataProvider menu=getItem(position);
        TextView menuTextView=(TextView)customeView.findViewById(R.id.menuNameTextView);
        TextView priceTextView=(TextView)customeView.findViewById(R.id.menuPriceTextView);
        TextView contentTextView=(TextView)customeView.findViewById(R.id.menuContentTextView);

        menuTextView.setText(menu.getMenuName().toString());
        priceTextView.setText("가격 : "+menu.getPriceName().toString());
        contentTextView.setText("설명 : "+menu.getContentName().toString());
        return customeView;
    }
}


