package com.example.jk.restaurantproject;

/**
 * Created by JK on 2015. 12. 5..
 */
public class ReservationDataProvider {

    private String name;
    private String tableNum;
    private String phoneNum;
    private String dateAndTime;

    public ReservationDataProvider()
    {

    }

    public ReservationDataProvider(String name,String tableNum,String phoneNum,String dateAndTime)
    {
        this.name=name;
        this.tableNum=tableNum;
        this.phoneNum=phoneNum;
        this.dateAndTime=dateAndTime;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getTableNum() {
        return tableNum;
    }

    public void setTableNum(String tableNum) {
        this.tableNum = tableNum;
    }
}
