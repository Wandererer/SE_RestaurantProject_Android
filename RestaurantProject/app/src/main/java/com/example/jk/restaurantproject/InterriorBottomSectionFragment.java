package com.example.jk.restaurantproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.FloatMath;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by JK on 2015. 11. 25..
 */
public class InterriorBottomSectionFragment extends Fragment implements Runnable  {

    //가장 중요한 기능

    private static final int ACTIVITY_START_CAMERA_APP=0;
    private String imageLocation="";

    int prevX,prevY; //이전 터치
    int selectRectNum; //선택한 사각형
    Boolean movableRect; //이동 가능한가 안한가
    Boolean scrollMove; //스크롤 이동가능한가 안한가
    ImageView img; // 이미지뷰

    Interrior interrior;
    InterriorList interriorList;
    ArrayList<ListItem> listItem = new ArrayList<>();
    Table table;
    int tableCount=0; //테이블 개수
    int windowCount=0;//창문 개수
    int wallCount=0;//벽 개수

    ImageView tableimg;

    private int tableId=0; //각 테이블 id
    private int wallId=0; //각 벽 id
    private int windowId=0;//각 창 id

    Table tableInfo;

    /*

     */

    ArrayList<Interrior> tempTable; //임시 테이블용 어레이
    ArrayList<Interrior> tempWindow; //임시 창문용 어레이
    ArrayList<Interrior> tempWall; //임시 벽용 어레이

    ArrayList<Table> tableArrayList;//테이블용 리스트


    FragmentCommunicator activityCommander; //프래그먼트 담


    int wholeInterriorCount=0;
    int canDraw=1;

    Boolean Move=true;
    Boolean ERASE=false;

    final static String TABLE="TABLE";
    final static String WINDOW="WINDOW";
    final static String WALL="WALL";

    final static String tableID="TABLE";
    final static String windowID="WINDOW";
    final static String wallID="WALLd";



    Canvas canvas; //캔버스
    Paint paint; //페인트
    Bitmap bitmap; //비트맵
    ArrayList<Interrior> interriorArrayList;
    ArrayList<Interrior>tempArrayInterrior;


    private int prevRectTopX=0,prevRectTopY=0,prevRectBottomX=0,prevRectBottomY=0;

    Thread thread;
    InterriorManage interriorManage;
    //private static InterriorBottomSectionFragment current;

    String resName=null;



    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        Intent intent;
        View view=inflater.inflate(R.layout.interrior_fragmentlayout,container,false);
        img=(ImageView)view.findViewById(R.id.imageView);
        img.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        movableRect=false; //사각형 움직이기 가능한가
        scrollMove=true; //스크롤 움직여도 되나
        canvas=new Canvas();
        img.setBackgroundResource(R.drawable.blank2);
        activityCommander=(FragmentCommunicator)getActivity();

        Log.d("MyLog", img.getWidth() + "imgwidfj " + img.getHeight());

        resName=getActivity().getIntent().getExtras().getString("resname");
        Log.d("RES",resName);
        interrior=new Interrior();
        interriorArrayList=new ArrayList<Interrior>();
        tempArrayInterrior=new ArrayList<Interrior>();
        interriorList=new InterriorList();

        tempTable=new ArrayList<Interrior>();
        tempWindow=new ArrayList<Interrior>();
        tempWall=new ArrayList<Interrior>();

        tableInfo=new Table();
        tableArrayList=new ArrayList<Table>();

      thread=new Thread(this);
        thread.start();

        try {
            resName = new String(URLEncoder.encode(resName, "UTF-8").getBytes("UTF-8"));
        }catch(Exception e){}


        interriorList.execute("http://203.249.22.53:8080/interriorList.php?resname=" + resName);

        interriorArrayList.add(new Interrior("TABLE", "TABLE0", new Rect(0, 0, 50, 50), 0, 0));
        wholeInterriorCount++;


        final HorizontalScrollView hScroll=(HorizontalScrollView)view.findViewById(R.id.interriorHorizontalScrollView);
        final ScrollView vScroll = (ScrollView)view.findViewById(R.id.interriorScrollView);

        vScroll.post(new Runnable() {
            @Override
            public void run() {
                vScroll.scrollTo(0, vScroll.getHeight() / 2);
            }
        });

        hScroll.post(new Runnable() {
            @Override
            public void run() {
                hScroll.scrollTo(hScroll.getWidth() / 2, 0);
            }
        });


        Log.d("MyLog", vScroll.getScrollX() + " vscrollposition " + vScroll.getScrollY());

        hScroll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return false;
            }
        });
        // 그리기 전

        // ArrayList<Table> tableArrayList;
        //ArrayList<Interrior> interriorArrayList;  처음에 값 받아서 넣어주기.




        vScroll.setOnTouchListener(new View.OnTouchListener() {

            private float mx, my, curX, curY;
            private boolean started = false;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                img.getParent().requestDisallowInterceptTouchEvent(false);
                curX = event.getX();
                curY = event.getY();
                int dx = (int) (mx - curX);
                int dy = (int) (my - curY);
                if (scrollMove) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_MOVE: {
                            Log.d("MyLog", "scrollMove");
                            if (started) {
                                vScroll.scrollBy(0, dy);
                                hScroll.scrollBy(dx, 0);
                            } else
                                started = true;

                            mx = curX;
                            my = curY;
                            break;
                        }

                        case MotionEvent.ACTION_UP: {
                            vScroll.scrollBy(0, dy);
                            hScroll.scrollBy(dx, 0);
                            started = false;
                            break;
                        }

                        default:
                            break;

                    }
                }
                return true;
            }
        });



        bitmap= Bitmap.createBitmap(1500,500, Bitmap.Config.ARGB_8888);


        canvas=new Canvas(bitmap);
        paint=new Paint();
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);

        drawAgain();


        img.setOnTouchListener(new View.OnTouchListener() {


            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int posX = (int) event.getX();
                int posY = (int) event.getY();

                Log.d("MyLog", posX + " " + posY);
                Log.d("Rect", posX + " " + posY);


                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        sortingInterrior();

                        // img.getParent().requestDisallowInterceptTouchEvent(true);


                        for (int i = 0; i < interriorArrayList.size(); i++) {
                            Rect r = interriorArrayList.get(i).getRect();
                            Log.d("Rect", r.left + " rect " + r.right);


                            if (r.contains(posX / 3, posY / 3)) {
                                Move = true;

                                Log.d("MyLog", "recttouch");
                                Log.d("Rect", "recttouch");

                                prevRectTopX = r.left;
                                prevRectTopY = r.top;
                                prevRectBottomX = r.right;
                                prevRectBottomY = r.bottom;
                                selectRectNum = i;
                                Log.d("Rect", selectRectNum + "selectnum");

                                if (ERASE == Boolean.TRUE) {//삭제가 선택되었고 선택된것들만 삭제시킴
                                    if (interriorArrayList.get(selectRectNum).getType() == TABLE) {
                                        tableCount--;
                                        for (int j = 0; j < tempTable.size(); j++) {
                                            if (interriorArrayList.get(selectRectNum) == tempTable.get(j)) {
                                                tempTable.remove(j);
                                                for (int k = 0; k < tableArrayList.size(); k++) {
                                                    if (interriorArrayList.get(selectRectNum).getNumId() == tableArrayList.get(k).tableNum) {
                                                        tableArrayList.remove(k);
                                                        break;

                                                    }

                                                }
                                                break;
                                            }
                                        }
                                    } else if (interriorArrayList.get(selectRectNum).getType() == WINDOW) {
                                        windowCount--;
                                        for (int j = 0; j < tempWindow.size(); j++) {
                                            if (interriorArrayList.get(selectRectNum) == tempWindow.get(j)) {
                                                tempWindow.remove(j);
                                                break;
                                            }
                                        }
                                    } else if (interriorArrayList.get(selectRectNum).getType() == WALL) {
                                        wallCount--;
                                        for (int j = 0; j < tempWall.size(); j++) {
                                            if (interriorArrayList.get(selectRectNum) == tempWall.get(j)) {
                                                tempWall.remove(j);
                                                break;
                                            }
                                        }
                                    }

                                    interriorArrayList.remove(selectRectNum);


                                    movableRect = false;
                                    scrollMove = true;
                                    wholeInterriorCount--;
                                    canvas.drawColor(Color.WHITE);
                                    img.invalidate();

                                    for (int j = 0; j < interriorArrayList.size(); j++) {
                                        if (wholeInterriorCount == 0)
                                            break;

                                        interrior = interriorArrayList.get(j);
                                        Rect t = interrior.getRect();
                                        int colorType = interrior.getPaint();

                                        if (colorType == 0)
                                            paint.setColor(Color.parseColor("#964B00"));
                                        else if (colorType == 1)
                                            paint.setColor(Color.parseColor("#F5DEB3"));
                                        else if (colorType == 2)
                                            paint.setColor(Color.parseColor("#00BFFF"));

                                        canvas.drawRect(t, paint);

                                        img.invalidate();
                                        img.setImageBitmap(bitmap);
                                    }


                                    break;
                                }

                                movableRect = true;

                                scrollMove = false;
                                break;
                            }
                        }


                        break;
                    }

                    case MotionEvent.ACTION_MOVE: {

                        Log.d("MyLog", "imgMOVE");
                        prevX = posX;
                        prevY = posY;
                        posX = (int) event.getX();
                        posY = (int) event.getY();


                        if (movableRect) {
                            img.getParent().requestDisallowInterceptTouchEvent(true); //이게 없으면
                            //터치 인터럽트 계속 일어나서 안됨
                            scrollMove = false;
                            Log.d("MyLog", "MOVE");

                            posX = (posX + (posX - prevX)) / 3;
                            posY = (posY + (posY - prevY)) / 3;


                            Log.d("MyLog", posX + "이동 " + posY);

                            Rect temp = interriorArrayList.get(selectRectNum).getRect();
                            Interrior interriorTemp = interriorArrayList.get(selectRectNum);
                            temp.set(posX - (temp.width() / 2), posY - (temp.height() / 2), posX + (temp.width() / 2), posY + (temp.height() / 2));
                            interriorTemp.setRect(temp);
                            temp = null;
                            interriorArrayList.set(selectRectNum, interriorTemp);


                            for (int i = 0; i < interriorArrayList.size(); i++) {
                                if (interriorArrayList.get(i).getRect().left != tempArrayInterrior.get(i).getRect().left) {
                                    Log.d("Rect", "drawagain");
                                }
                            }


                            //bitmap.recycle();


                            canvas.drawColor(Color.WHITE);

                            for (int i = 0; i < interriorArrayList.size(); i++) {
                                interrior = interriorArrayList.get(i);
                                Rect r = interrior.getRect();
                                int colorType = interrior.getPaint();

                                if (colorType == 0)
                                    paint.setColor(Color.parseColor("#964B00"));
                                else if (colorType == 1)
                                    paint.setColor(Color.parseColor("#F5DEB3"));
                                else if (colorType == 2)
                                    paint.setColor(Color.parseColor("#00BFFF"));

                                canvas.drawRect(r, paint);

                                img.invalidate();
                                img.setImageBitmap(bitmap);
                            }

                        }
                        break;

                    }

                    case MotionEvent.ACTION_UP: {
                        img.getParent().requestDisallowInterceptTouchEvent(false);
                        int returnPrevious = -1;

                        Log.d("MyLog", "touch false");
                        Log.d("Rect", "touch false");
                        posX = (int) event.getX();
                        posY = (int) event.getY();


                        if (movableRect) {


                            Rect r = interriorArrayList.get(selectRectNum).getRect();
                            String tempChoseType = interriorArrayList.get(selectRectNum).getType();
                            for (int i = 0; i < interriorArrayList.size(); i++) {
                                String searchType = interriorArrayList.get(i).getType();
                                if (selectRectNum == i)
                                    continue;


                                if ((tempChoseType.equals("WINDOW") || tempChoseType.equals("WALL")) &&
                                        (searchType.equals("WALL") || searchType.equals("WINDOW")))
                                    break;


                                Rect temp = interriorArrayList.get(i).getRect();


                                if (r.intersect(temp) || temp.intersect(r)
                                        ) {
                                    canvas.drawColor(Color.WHITE);

                                    Log.d("MyLog", "enter");

                                    AlertDialog.Builder myAlert = new AlertDialog.Builder(getActivity());
                                    myAlert.setMessage("겹지면 안됩니다")
                                            .setPositiveButton("Continue..", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            })
                                            .create();


                                    myAlert.show();
                                    returnPrevious = 1;
                                    break;
                                }


                            }

                            if (returnPrevious == 1) {
                                Rect tR = new Rect(prevRectTopX, prevRectTopY, prevRectBottomX, prevRectBottomY);

                                Interrior tempInterrior = interriorArrayList.get(selectRectNum);
                                tempInterrior.setRect(tR);
                                interriorArrayList.set(selectRectNum, tempInterrior);


                                for (int j = 0; j < interriorArrayList.size(); j++) {
                                    Log.d("Rect", interriorArrayList.get(j).getType());
                                    Log.d("Rect", interriorArrayList.get(j).getRect().left + " ");
                                    interrior = interriorArrayList.get(j);
                                    Rect tempRectangle = interrior.getRect();
                                    int colorType = interrior.getPaint();

                                    if (colorType == 0)
                                        paint.setColor(Color.parseColor("#964B00"));
                                    else if (colorType == 1)
                                        paint.setColor(Color.parseColor("#F5DEB3"));
                                    else if (colorType == 2)
                                        paint.setColor(Color.parseColor("#00BFFF"));

                                    canvas.drawRect(tempRectangle, paint);

                                    img.invalidate();
                                    img.setImageBitmap(bitmap);
                                }

                            }

                            returnPrevious = -1;


                        }
                        selectRectNum = -1;
                        Move = false;
                        movableRect = false;
                        scrollMove = true;

                        tempArrayInterrior = interriorArrayList;

                        break;
                    }

                    default:
                        break;

                }


                return true;
            }
        });








        return view;




    }



    @Override
    public void run() {

        while(true) {


           // sortingInterrior();

        }


    }


    private void sortingInterrior()
    {
        ArrayList<Interrior> table=new ArrayList<Interrior>();
        ArrayList<Interrior> window=new ArrayList<Interrior>();
        ArrayList<Interrior> wall=new ArrayList<Interrior>();

            for(int j=0;j<interriorArrayList.size();j++)
            {
                if(interriorArrayList.get(j).getType()==TABLE)
                    table.add(interriorArrayList.get(j));
                else  if(interriorArrayList.get(j).getType()==WINDOW)
                    window.add(interriorArrayList.get(j));
                else  if(interriorArrayList.get(j).getType()==WALL)
                    wall.add(interriorArrayList.get(j));
            }
        interriorArrayList.clear();

        for(int i=0;i<table.size();i++)
        {
            interriorArrayList.add(table.get(i));
        }
        for(int i=0;i<wall.size();i++)
        {
            interriorArrayList.add(wall.get(i));
        }
        for(int i=0;i<window.size();i++)
        {
            interriorArrayList.add(window.get(i));
        }

    }


    public void drawAgain()
    {
        img.invalidate();

        canvas.drawColor(Color.WHITE);


        for(int i=0;i<interriorArrayList.size();i++)
        {
            if(wholeInterriorCount==0)
                break;

            interrior=interriorArrayList.get(i);
            Rect r=interrior.getRect();
            int colorType=interrior.getPaint();

            if(colorType==0)
                paint.setColor(Color.parseColor("#964B00"));
            else if(colorType==1)
                paint.setColor(Color.parseColor("#F5DEB3"));
            else if(colorType==2)
                paint.setColor(Color.parseColor("#00BFFF"));

            canvas.drawRect(r, paint);

            img.invalidate();
            img.setImageBitmap(bitmap);
        }
    }

    public void settingResName(String s)
    {
            resName=s;
    }



//String type, Rect interrior, Paint color
    public void drawthis(String c)
    {
        if(c.equals("table"))
        {
            Log.d("Rect","candrawTable");
            if(tempTable.size()>0)
            {

                for (int i = 0; i < tempTable.size(); i++)
                {
                    if (tempTable.get(i).getNumId() != i)
                    {
                        tableId=i;
                        break;
                    }
                }
            }


            if(wholeInterriorCount>0 && ERASE==Boolean.FALSE)
            {
                Log.d("Rect","인테리어개수0개보다많음");
                Rect a = new Rect(725, 225, 775, 275);
                for(int i=0;i<interriorArrayList.size();i++)
                {
                    if(a.intersect(interriorArrayList.get(i).getRect()))
                    {
                        Log.d("Rect","다른거랑겹침");
                        AlertDialog.Builder myAlert = new AlertDialog.Builder(getActivity());
                        myAlert.setMessage("생성 위치에 겹지면 안됩니다")
                                .setPositiveButton("Continue..", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .create();


                        myAlert.show();
                        canDraw=-1;
                        break;
                    }
                    canDraw=1;

                }


            }

            Log.d("MyLog", "make talbe");
            if(canDraw==1&& ERASE==Boolean.FALSE) {
                Log.d("Rect","그린다");
                wholeInterriorCount++;
                paint.setColor(Color.parseColor("#964B00"));
                Rect a = new Rect(725, 225, 775, 275);

                // Rect a=new Rect(0,0,500,500);
                interrior = new Interrior(TABLE, tableID + tableId, a, 0,tableId); //테이블 타입, 테이블아이디=테이블+몇번째꺼 ,사각형 a,0번 컬러(갈색), 몇번째꺼
                Log.d("MyLog", tableID + tableCount + "   table id");
                tempTable.add(tableId, interrior);
                tableInfo=new Table(tableID + tableId,tableId);//테이블+테이블번호, 테이블번호
               //String type,int topx,int topy,int bottomx,int bottomy,int colortype,int numid

                tableArrayList.add(tableId, tableInfo);
                tableId=tempTable.size();



                interriorArrayList.add(0, interrior);
                canvas.drawRect(a, paint);
                tempArrayInterrior=interriorArrayList;

                for(int i=0;i<tempTable.size();i++)
                {
                    Log.d("Rect",tempTable.get(i).getNumId()+" tableid");
                }

                img.setImageBitmap(bitmap);
                tableCount++;

                Intent intent =new Intent(getActivity(),TableSettingView.class);
                intent.putExtra("TableStringID",tableInfo.tableID);
                intent.putExtra("Covers","4");
                startActivity(intent);

                //FragmentManager manager=getFragmentManager();
                //TableAlertDialog tableDialog=new TableAlertDialog();
                //tableDialog.show(manager,"MyDialog");

            }
        }



        else if(c.equals("sWall"))
        {

            if(tempWall.size()>0)
            {

                for (int i = 0; i < tempWall.size(); i++)
                {
                    if (tempWall.get(i).getNumId() != i)
                    {
                        wallId=i;
                        break;
                    }
                }
            }


            if(wholeInterriorCount>0 && ERASE==Boolean.FALSE )
            {
                Rect a=new Rect(725,225,775,325) ;
                for(int i=0;i<interriorArrayList.size();i++)
                {
                    if(a.intersect(interriorArrayList.get(i).getRect()))
                    {
                        AlertDialog.Builder myAlert = new AlertDialog.Builder(getActivity());
                        myAlert.setMessage("생성 위치에 겹지면 안됩니다")
                                .setPositiveButton("Continue..", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .create();


                        myAlert.show();
                        canDraw=-1;
                        break;
                    }
                    canDraw=1;
                }



            }
            if(canDraw==1&& ERASE==Boolean.FALSE) {
                wholeInterriorCount++;
                paint.setColor(Color.parseColor("#F5DEB3"));
                Rect a = new Rect(725, 225, 775, 325);

                canvas.drawRect(a, paint);

                interrior = new Interrior(WALL, wallID + wallId, a, 1,wallId);
                //WALL , WALL+번호, 사각형 a , 1번 색깔, 번호
                wallId=tempWall.size();

                for(int i=0;i<tempWall.size();i++)
                {
                    Log.d("Rect",tempWall.get(i).getNumId()+" wallid");
                }

                interriorArrayList.add(interrior);
                tempWall.add(wallId,interrior);



                wallCount++;     tempArrayInterrior=interriorArrayList;

                img.setImageBitmap(bitmap);
            }

        }

        else if(c.equals("gWall"))
        {
            if(tempWall.size()>0)
            {

                for (int i = 0; i < tempWall.size(); i++)
                {
                    if (tempWall.get(i).getNumId() != i)
                    {
                        wallId=i;
                        break;
                    }
                }
            }

            if(wholeInterriorCount>0 && ERASE==Boolean.FALSE )
            {
                Rect a=new Rect(725,225,825,275) ;
                for(int i=0;i<interriorArrayList.size();i++)
                {
                    if(a.intersect(interriorArrayList.get(i).getRect()))
                    {
                        AlertDialog.Builder myAlert = new AlertDialog.Builder(getActivity());
                        myAlert.setMessage("생성 위치에 겹지면 안됩니다")
                                .setPositiveButton("Continue..", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .create();


                        myAlert.show();
                        canDraw=-1;
                        break;
                    }
                    canDraw=1;
                }



            }
            Log.d("MyLog", "gwallbuttonclicedpaintGWALL");
            if(canDraw==1&& ERASE==Boolean.FALSE) {
                wholeInterriorCount++;
                paint.setColor(Color.parseColor("#F5DEB3"));
                Rect a = new Rect(725, 225, 825, 275);
                canvas.drawRect(a, paint);
                interrior = new Interrior(WALL, wallID + wallId, a, 1,wallId);
                //WALL , WALL+번호, 사각형 a , 1번 색깔, 번호
                wallId=tempWall.size();

                interriorArrayList.add(interrior);
                tempWall.add(wallId,interrior);

                for(int i=0;i<tempWall.size();i++)
                {
                    Log.d("Rect",tempWall.get(i).getNumId()+" wallid");
                }

                wallCount++;     tempArrayInterrior=interriorArrayList;
                img.setImageBitmap(bitmap);
            }

        }

        else if(c.equals("sWindow"))
        {
            if(tempWindow.size()>0)
            {

                for (int i = 0; i < tempWindow.size(); i++)
                {
                    if (tempWindow.get(i).getNumId() != i)
                    {
                        windowId=i;
                        break;
                    }
                }
            }

            if(wholeInterriorCount>0 && ERASE==Boolean.FALSE )
            {
                Rect a = new Rect(725, 225, 765, 300);
                for(int i=0;i<interriorArrayList.size();i++)
                {
                    if(a.intersect(interriorArrayList.get(i).getRect()))
                    {
                        AlertDialog.Builder myAlert = new AlertDialog.Builder(getActivity());
                        myAlert.setMessage("생성 위치에 겹지면 안됩니다")
                                .setPositiveButton("Continue..", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .create();


                        myAlert.show();
                        canDraw=-1;
                        break;
                    }
                    canDraw=1;

                }


            }
            if(canDraw==1 && ERASE==Boolean.FALSE) {
                wholeInterriorCount++;
                paint.setColor(Color.parseColor("#00BFFF"));
                Rect a = new Rect(725, 225, 765, 300);
                canvas.drawRect(a, paint);
                interrior = new Interrior(WINDOW, windowID + windowId, a, 2,windowId);
                //WINDOW , WINDOW+번호, 사각형 a, 색깔, 번호

                windowId=tempWindow.size();

                interriorArrayList.add(interrior);
                tempWindow.add(windowId,interrior);


                for(int i=0;i<tempWindow.size();i++)
                {
                    Log.d("Rect",tempWindow.get(i).getNumId()+" wallid");
                }
                tempArrayInterrior=interriorArrayList;
                img.setImageBitmap(bitmap);
            }

        }


        else if(c.equals("gWindow"))
        {

            if(tempWindow.size()>0)
            {

                for (int i = 0; i < tempWindow.size(); i++)
                {
                    if (tempWindow.get(i).getNumId() != i)
                    {
                        windowId=i;
                        break;
                    }
                }
            }

            if(wholeInterriorCount>0 && ERASE==Boolean.FALSE )
            {
                Rect a=new Rect(725,225,800,265) ;
                for(int i=0;i<interriorArrayList.size();i++)
                {
                    if(a.intersect(interriorArrayList.get(i).getRect()))
                    {
                        AlertDialog.Builder myAlert = new AlertDialog.Builder(getActivity());
                        myAlert.setMessage("생성 위치에 겹지면 안됩니다")
                                .setPositiveButton("Continue..", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .create();


                        myAlert.show();
                        canDraw=-1;
                        break;
                    }
                    canDraw=1;

                }


            }
            if(canDraw==1&& ERASE==Boolean.FALSE) {
                wholeInterriorCount++;
                paint.setColor(Color.parseColor("#00BFFF"));
                Rect a = new Rect(725, 225, 800, 265);
                canvas.drawRect(a, paint);
                interrior = new Interrior(WINDOW, windowID + windowId, a, 2,windowId);
                //WINDOW , WINDOW+번호, 사각형 a, 색깔, 번호


                interriorArrayList.add(interrior);
                tempWindow.add(windowId,interrior);
                windowId=tempWindow.size();

                for(int i=0;i<tempWindow.size();i++)
                {
                    Log.d("Rect",tempWindow.get(i).getNumId()+" wallid");
                }
                windowCount++;     tempArrayInterrior=interriorArrayList;
                img.setImageBitmap(bitmap);
            }

        }
        else if(c.equals("erase"))
        {
            ERASE=true;
            canDraw=2;
        }
        else if(c.equals("noterase"))
        {
            ERASE=false;
            canDraw=1;
        }

        else if (c.equals("save"))
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("SAVE");
            builder.setCancelable(false);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 저장해주기.
                            String resname, type, topx, topy, bottomx, bottomy, colortype, tableid, tname, tablenum, covers;
                            Interrior interrior;

                            try { // 인터리어 테이블 초기화.
                                 //resName = new String(URLEncoder.encode(resName, "UTF-8").getBytes("UTF-8"));
                                interriorManage = new InterriorManage();
                                interriorManage.execute("http://203.249.22.53:8080/initInterrior.php?resname=" + resName);
                            } catch (Exception e) {
                            }

                            try { // 테이블 테이블 초기화.
                                //resname = new String(URLEncoder.encode(resName, "UTF-8").getBytes("UTF-8"));
                                interriorManage = new InterriorManage();
                                interriorManage.execute("http://203.249.22.53:8080/initTable.php?resname=" + resName);
                            } catch (Exception e) {
                            }

                            for (int i = 0; i < tableArrayList.size(); i++) {
                                table = tableArrayList.get(i);
                                try {
                                   // resname = new String(URLEncoder.encode(resName, "UTF-8").getBytes("UTF-8"));
                                    tableid = new String(URLEncoder.encode(table.tableID, "UTF-8").getBytes("UTF-8"));
                                    tablenum = new String(URLEncoder.encode(Integer.toString(table.tableNum), "UTF-8").getBytes("UTF-8"));
                                    covers = new String(URLEncoder.encode(Integer.toString(table.covers), "UTF-8").getBytes("UTF-8"));
                                    interriorManage = new InterriorManage();
                                    interriorManage.execute("http://203.249.22.53:8080/addTable.php?resname=" + resName + "&tableid=" + tableid + "&tablenum=" + tablenum + "&covers=" + covers);
                                } catch (Exception e) {
                                }
                            }
                            for (int j = 0; j < interriorArrayList.size(); j++) {
                                interrior = interriorArrayList.get(j);
                                try {
                                    //resname = new String(URLEncoder.encode(resName, "UTF-8").getBytes("UTF-8"));
                                    type = new String(URLEncoder.encode(interrior.getType(), "UTF-8").getBytes("UTF-8"));
                                    tname = new String(URLEncoder.encode(interrior.getStringID(), "UTF-8").getBytes("UTF-8"));
                                    topx = new String(URLEncoder.encode(Integer.toString(interrior.getRect().left), "UTF-8").getBytes("UTF-8"));
                                    topy = new String(URLEncoder.encode(Integer.toString(interrior.getRect().top), "UTF-8").getBytes("UTF-8"));
                                    bottomx = new String(URLEncoder.encode(Integer.toString(interrior.getRect().right), "UTF-8").getBytes("UTF-8"));
                                    bottomy = new String(URLEncoder.encode(Integer.toString(interrior.getRect().bottom), "UTF-8").getBytes("UTF-8"));
                                    colortype = new String(URLEncoder.encode(Integer.toString(interrior.colorType), "UTF-8").getBytes("UTF-8"));
                                    tableid = new String(URLEncoder.encode(Integer.toString(interrior.numId), "UTF-8").getBytes("UTF-8"));
                                    interriorManage = new InterriorManage();
                                    interriorManage.execute("http://203.249.22.53:8080/addInterrior.php?resname=" + resName + "&type=" + type + "&tname=" + tname + "&topx=" + topx + "&topy=" + topy + "&bottomx=" + bottomx + "&bottomy=" + bottomy + "&colortype=" + colortype + "&tableid=" + tableid);
                                } catch (Exception e) {
                                }
                            }
                            //tableArrayList;
                            //interriorArrayList;
                            dialog.cancel();
                        }
                    }

            );
                AlertDialog alert = builder.create();
                alert.show();
            }

        }

    public void setRESname()
    {
    }



    private static void recycleBitmap(ImageView v)
    {
        Drawable d=v.getDrawable();
        if(d instanceof BitmapDrawable)
        {
            Bitmap b=((BitmapDrawable)d).getBitmap();
            b.recycle();
        }
        d.setCallback(null);
    }

    public void onDstroy()
    {
        recycleBitmap(img);
        super.onDestroy();
    }

    public void onStop()
    {
        super.onStop();
        img.setImageDrawable(null);

    }




    private void DrawTableSaveInfoDialog(Table table)
    {
        final Dialog dialog=new Dialog(this.getActivity().getBaseContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.activity_tablesetting);
        //TextView tableNumber=(TextView)dialog.findViewById(R.id.tableNumberName);
      //  tableNumber.setText(table.tableID);
        //TextView covers=(TextView)dialog.findViewById(R.id.coversTextView);
        //covers.setText("인원수 : "+table.covers);
        tableimg=(ImageView)dialog.findViewById(R.id.tablePicture);
        tableimg.setOnLongClickListener(new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            Intent callCamera=new Intent();
            callCamera.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            File photoFile=null;
            try{
                photoFile=createImageFile();
            }catch(IOException e)
            {
                e.printStackTrace();
            }
            callCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
            startActivityForResult(callCamera, ACTIVITY_START_CAMERA_APP);

            return true;
        }
    });

        Button saveButton=(Button)dialog.findViewById(R.id.tableSaveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        dialog.setCancelable(false);
        dialog.show();

    }


    File createImageFile() throws IOException {
        String timeStmap=new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String ImageFileName="IMAGE_"+timeStmap+"_";
        File storageDirecory= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image=File.createTempFile(ImageFileName, ".jgp", storageDirecory);

        imageLocation=image.getAbsolutePath();


        return image;
    }

    private Bitmap setReduceImageSize()
    {
        int targetImageWidth= tableimg.getWidth();
        int targetImageHeight=tableimg.getHeight();

        BitmapFactory.Options bmOptions=new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds=true;
        BitmapFactory.decodeFile(imageLocation,bmOptions);
        int cameraImageWidth=bmOptions.outWidth;
        int cameraImageHeight=bmOptions.outHeight;

        int scaleFactor= Math.min(cameraImageWidth / targetImageWidth, cameraImageHeight / targetImageHeight);
        bmOptions.inSampleSize=scaleFactor;
        bmOptions.inJustDecodeBounds=false;

        // Bitmap photoReducedBitmap=BitmapFactory.decodeFile(imageLocation, bmOptions);
        // imgView.setImageBitmap(photoReducedBitmap);

        return BitmapFactory.decodeFile(imageLocation,bmOptions);

    }



    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode==ACTIVITY_START_CAMERA_APP && resultCode==getActivity().RESULT_OK)
        {
            rotateImage(setReduceImageSize());
        }
    }

    private  void rotateImage(Bitmap bitmap)
    {
        ExifInterface exifInterface=null;



        try{
            exifInterface=new ExifInterface(imageLocation);
        }catch(IOException e)
        {
            e.printStackTrace();
        }

        int orientation= exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
        Matrix matrix=new Matrix();
        int rotate=0;
        //matrix.postScale(bitmap.getWidth(),bitmap.getHeight());

        switch (orientation)
        {

            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                //matrix.setRotate(90,bitmap.getWidth(),bitmap.getHeight());
                //matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                Log.d("CAMERA", "180");
                rotate=180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(270);
                Log.d("CAMERA","270");
                rotate=270;
                break;


        }


        Bitmap rotateBitmap  ;


        if(rotate==180)
            Log.d("CAMERA","180");



        rotateBitmap=android.graphics.Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);

        Log.d("CAMERA",bitmap.getWidth() +" bitamp "+bitmap.getHeight());
        Log.d("CAMERA", rotateBitmap.getWidth() + " rotate " + rotateBitmap.getHeight());
        tableimg.setImageBitmap(rotateBitmap);
    }
    private class InterriorManage extends AsyncTask<String, Integer,String> {

        @Override
        protected String doInBackground(String... urls) {
            StringBuilder resultText = new StringBuilder();
            try{
                // 연결 url 설정
                URL url = new URL(urls[0]);
                // 커넥션 객체 생성
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                // 연결되었으면.
                if(conn != null){
                    conn.setConnectTimeout(10000);
                    conn.setUseCaches(false);
                    // 연결되었음 코드가 리턴되면.
                    if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                        for(;;){
                            // 웹상에 보여지는 텍스트를 라인단위로 읽어 저장.
                            String line = br.readLine();
                            if(line == null) break;
                            resultText.append(line);
                        }
                        br.close();
                    }
                    conn.disconnect();
                }
            } catch(Exception ex){
                ex.printStackTrace();
            }
            return resultText.toString();
        }

        protected void onPostExecute(String str){
            Log.d("AAAA", str);
        }
    }
    private class InterriorList extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... urls) { // 이놈의 반환값이 onPostExecute의 매개변수가 된다. jsonarray가 반환.
            StringBuilder jsonHtml = new StringBuilder();
            try {
                // 연결 url 설정
                URL url = new URL(urls[0]);
                // 커넥션 객체 생성
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                // 연결되었으면.
                if (conn != null) {
                    conn.setConnectTimeout(10000);
                    conn.setUseCaches(false);
                    // 연결되었음 코드가 리턴되면.
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                        for (; ; ) {
                            // 웹상에 보여지는 텍스트를 라인단위로 읽어 저장.
                            String line = br.readLine();
                            if (line == null) break;
                            // 저장된 텍스트 라인을 jsonHtml에 붙여넣음
                            jsonHtml.append(line + "\n");
                        }
                        br.close();
                    }
                    conn.disconnect();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return jsonHtml.toString();
        }

        protected void onPostExecute(String str) {
        String type,tname,topx,topy,bottomx,bottomy,colortype,tableid;
            String course;
            interriorArrayList.clear();
            try {
                JSONObject root = new JSONObject(str);
                JSONArray ja = root.getJSONArray("results");

                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = ja.getJSONObject(i);
                    type = jo.getString("type");
                    tname = jo.getString("tname");
                    topx = jo.getString("topx");
                    topy = jo.getString("topy");
                    bottomx = jo.getString("bottomx");
                    bottomy = jo.getString("bottomy");
                    colortype = jo.getString("colortype");
                    tableid = jo.getString("tableid");



                    Rect a=new Rect(Integer.parseInt(topx.toString()),Integer.parseInt(topy.toString()),Integer.parseInt(bottomx.toString()),Integer.parseInt(bottomy.toString()));

                    interriorArrayList.add(new Interrior(type.toString(),tname.toString(),a,Integer.parseInt(colortype.toString()),Integer.parseInt(tableid.toString())));
                    wholeInterriorCount++;

                }

                //txt += "name : " + listItem.get(i).getData(0) + "\n";
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
