package com.example.jk.restaurantproject;

/**
 * Created by JK on 2015. 12. 6..
 */
public class OrderDataProduct {
    String foodName;

    String howMany;
    String price;
    int sum=0;

    public int getOriginalPrice() {
        return originalPrice;
    }

    public int getSum()
    {
        return originalPrice*Integer.parseInt(howMany);
    }

    public void setOriginalPrice(int originalPrice) {
        this.originalPrice = originalPrice;
    }

    int originalPrice;


    public String getHowMany() {
        return howMany;
    }

    public OrderDataProduct(){}

    public OrderDataProduct(String food,String many,String price,int originalPrice)
    {
        super();
        this.setFoodName(food);
        this.setPrice(price);
        this.setHowMany(many);
        this.setOriginalPrice(originalPrice);
    }

    public void setHowMany(String howMany) {
        this.howMany = howMany;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }



    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }
}
