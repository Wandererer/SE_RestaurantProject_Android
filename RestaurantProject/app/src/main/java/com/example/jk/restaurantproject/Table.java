package com.example.jk.restaurantproject;

/**
 * Created by JK on 2015. 12. 2..
 */
public class Table {
    int covers;
    String tableID;
    int tableNum;

    Table()
    {
    }

    Table(String tableId,int tableNum)
    {
        this.covers=4;
        this.tableID=tableId;
        this.tableNum=tableNum;
    }

}
