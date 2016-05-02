package com.example.jk.restaurantproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by JK on 2015. 12. 5..
 */
public class CustomReservationListViewAdapter extends ArrayAdapter<ReservationDataProvider> {

    public CustomReservationListViewAdapter(Context context,ArrayList<ReservationDataProvider> reserv)
    {
        super(context,R.layout.reservatonlistview_layout,reserv);
    }

    public View getView(int position,View convertView, ViewGroup parent)
    {
        LayoutInflater inflater=LayoutInflater.from(getContext());
        View customeView=inflater.inflate(R.layout.reservatonlistview_layout, parent, false);

        ReservationDataProvider reserv=getItem(position);
        TextView nameTextView=(TextView)customeView.findViewById(R.id.reservationCustomerNameTextView);
        TextView tableNumTextView=(TextView)customeView.findViewById(R.id.reservationTableNumberTextView);
        TextView phoneNumTextView=(TextView)customeView.findViewById(R.id.reservationPhoneNumberTextView);
        TextView dateAndTIme=(TextView)customeView.findViewById(R.id.reservationTimeTextView);

        nameTextView.setText("이름 :"+reserv.getName().toString());
        tableNumTextView.setText("테이블 : "+reserv.getTableNum().toString());
        phoneNumTextView.setText("핸드폰번호 : "+reserv.getPhoneNum().toString());
        dateAndTIme.setText("예약시간 :"+reserv.getDateAndTime().toString());



        return customeView;
    }
}
