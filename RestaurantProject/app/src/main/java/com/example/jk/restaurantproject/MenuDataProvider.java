package com.example.jk.restaurantproject;

/**
 * Created by JK on 2015. 12. 4..
 */
public class MenuDataProvider {

    private String menuName;
    private String priceName;
    private String contentName;

    public String getContentName() {
        return contentName;
    }


    public MenuDataProvider()
    {

    }

    public MenuDataProvider(String menuName,String priceName,String contentName)
    {
        super();
        this.setMenuName(menuName);
        this.setPriceName(priceName);
        this.setContentName(contentName);
    }


    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getPriceName() {
        return priceName;
    }

    public void setPriceName(String priceName) {
        this.priceName = priceName;
    }

}
