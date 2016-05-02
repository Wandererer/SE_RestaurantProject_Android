package com.example.jk.restaurantproject;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioButton;

/**
 * Created by JK on 2015. 11. 25..
 */
public class InterriorTopSectionFragment extends Fragment implements View.OnClickListener {


    FragmentCommunicator activityCommander;
    ImageButton table;
    ImageButton gWall;
    ImageButton sWall;
    ImageButton sWindow;
    ImageButton gWindow;
    CheckBox eraseButton;
    Button saveButton;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        View view=inflater.inflate(R.layout.button_fragmentlayout,container,false);

        activityCommander=(FragmentCommunicator)getActivity();
        table=(ImageButton) view.findViewById(R.id.tableButton);
        gWall=(ImageButton)view.findViewById(R.id.gwallButton);
        sWall=(ImageButton)view.findViewById(R.id.swallButton);
        sWindow=(ImageButton)view.findViewById(R.id.swindowButton);
       gWindow = (ImageButton)view.findViewById(R.id.gwindowButton);
        eraseButton=(CheckBox)view.findViewById(R.id.eraseCheckBox);
        saveButton=(Button)view.findViewById(R.id.interriorSaveButton);



        if(table==null)
        {
            table=(ImageButton) getActivity().findViewById(R.id.tableButton);
        }

        table.setOnClickListener(this);
        gWall.setOnClickListener(this);
        sWall.setOnClickListener(this);
        sWindow.setOnClickListener(this);
       gWindow.setOnClickListener(this);
        saveButton.setOnClickListener(this);


        eraseButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(eraseButton.isChecked()==true)
                {
                    activityCommander.respond("erase");
                }
                else
                {
                    activityCommander.respond("noterase");
                }
            }
        });

        return view;
    }

    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.tableButton: {
                activityCommander.respond("table");
                Log.d("MyLog", "tablebuttonclicked");
                //img.setImageBitmap(bitmap);
                //mPaintSurface.setdrawTable(1);

                break;
            }

            case R.id.gwallButton: {
                //mPaintSurface.setGWall(1);
                // paint.setColor(Color.parseColor("#F5DEB3"));
                // canvas.drawRect(100, 100, 300, 200, paint);
                // img.setImageBitmap(bitmap);
                activityCommander.respond("gWall");
                Log.d("MyLog", "gwallbuttonclicked");


                break;
            }

            case R.id.swallButton: {
                //paint.setColor(Color.parseColor("#F5DEB3"));
                // canvas.drawRect(400, 400, 500, 500, paint);
                //img.setImageBitmap(bitmap);
                //surfaceView.draw(canvas);
                //mPaintSurface.setSWall(1);
                activityCommander.respond("sWall");
                Log.d("MyLog", "swallbuttonclicked");

                break;
            }

            case R.id.gwindowButton: {
                // paint.setColor(Color.parseColor("#00BFFF"));
                // canvas.drawRect(500, 500, 600, 600, paint);
                // img.setImageBitmap(bitmap);
                activityCommander.respond("gWindow");

                break;
            }


            case R.id.swindowButton: {
                // paint.setColor(Color.parseColor("#00BFFF"));
                // canvas.drawRect(500, 700, 700, 600, paint);
                // img.setImageBitmap(bitmap);
                activityCommander.respond("sWindow");

                break;
            }

            case R.id.interriorSaveButton:
            {
                activityCommander.respond("save");
                break;
            }

        }
    }

}
