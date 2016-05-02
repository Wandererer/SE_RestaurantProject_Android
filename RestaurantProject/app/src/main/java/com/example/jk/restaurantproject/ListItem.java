package com.example.jk.restaurantproject;

/**
 * Created by dlawk_000 on 2015-12-05.
 */

public class ListItem {
        private String[] mData;

        public ListItem(String[] data){

            mData = data;
        }

        public ListItem(String name){

            mData = new String[1];
            mData[0] = name;

        }
        public String[] getData(){
            return mData;
        }

        public String getData(int index){
            return mData[index];
        }

        public void setData(String[] data){
            mData = data;
        }
}