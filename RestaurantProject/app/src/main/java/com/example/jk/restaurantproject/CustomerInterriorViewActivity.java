package com.example.jk.restaurantproject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ScrollView;

import java.util.ArrayList;

/**
 * Created by JK on 2015. 12. 5..
 */
public class CustomerInterriorViewActivity extends Activity {

    ScrollView vScroll;
    HorizontalScrollView hScroll;
    ImageView imgView;


    ArrayList<Interrior> interriorArrayList;
    Canvas canvas; //캔버스
    Paint paint; //페인트
    Bitmap bitmap; //비트맵
    ArrayList<Table> tableArrayList;
    Rect rect;
    Interrior interrior;

    Boolean scrollMove = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customerinterriorview);
        vScroll = (ScrollView) findViewById(R.id.customerInterriorViewScrollView);
        hScroll = (HorizontalScrollView) findViewById(R.id.customerInterriorViewHorizontalScrollView);
        imgView = (ImageView) findViewById(R.id.customerInterriorViewImageView);

        imgView.setBackgroundResource(R.drawable.blank2);

        canvas = new Canvas();
        //이거 두개 db에서 받아와야함
        interriorArrayList = new ArrayList<Interrior>();
        tableArrayList = new ArrayList<Table>();

        bitmap = Bitmap.createBitmap(1500, 500, Bitmap.Config.ARGB_8888);

        canvas = new Canvas(bitmap);
        imgView.setImageBitmap(bitmap);

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
        vScroll.setOnTouchListener(new View.OnTouchListener() {

            private float mx, my, curX, curY;
            private boolean started = false;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                imgView.getParent().requestDisallowInterceptTouchEvent(false);
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


    }


    public void draw() {
        imgView.invalidate();

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

            imgView.invalidate();
            imgView.setImageBitmap(bitmap);
        }
    }
}
