package com.example.jk.restaurantproject;

import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by JK on 2015. 11. 30..
 */
public class Interrior {


    enum paintColor{brown,skin,sky};
    private String type;
    private String id;
    private Rect interrior;
    int numId;
    int colorType;

    Interrior()
    {

    }

    Interrior(String type,String id ,Rect interrior, int color,int numId)

    {
        this.type=type;
        this.id=id;
        this.interrior=interrior;
        this.colorType=color;
        this.numId=numId;
    }

    public String getStringID()
    {
        return id;
    }

    public int getNumId()
    {
        return numId;
    }

    public String getType()
    {
        return type;
    }

    public Rect getRect()
    {
        return interrior;
    }

    public void setRect(Rect r)
    {
        interrior=r;
    }

    public int getPaint()
    {
        return colorType;
    }

}
