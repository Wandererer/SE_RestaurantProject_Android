package com.example.jk.restaurantproject;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by JK on 2015. 12. 1..
 */
public class InterriorActivity extends Activity implements FragmentCommunicator{

    FragmentManager manager;

    // ImageView img;
    //private SurfaceView surfaceView;
    //  PaintSurface mPaintSurface;
    // private SurfaceHolder surfaceHolder;
    // private int drawNumber=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interrior);


        Intent intent=getIntent();
        this.setResName(intent.getStringExtra("resname"));
    }

    @Override
    public void respond(String s) {
        InterriorBottomSectionFragment bottomFragment= (InterriorBottomSectionFragment)
                getFragmentManager().findFragmentById(R.id.bottomFragment);
        bottomFragment.drawthis(s);

        Log.d("MyLog", "respondcalled");
    }
    public void goTableSetting()
    {
        Intent intent=new Intent(this,TableSettingView.class);
        startActivity(intent);
    }

    @Override
    public void setResName(String s) {

        Log.d("AAAA",s+" bottom call");
        InterriorBottomSectionFragment bottomFragment= (InterriorBottomSectionFragment)
                getFragmentManager().findFragmentById(R.id.bottomFragment);
        bottomFragment.settingResName(s);

    }
}