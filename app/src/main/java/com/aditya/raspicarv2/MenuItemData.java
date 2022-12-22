package com.aditya.raspicarv2;

import android.graphics.drawable.Drawable;

public class MenuItemData {

    String menuItemData;
    int menuItemImage;

    public MenuItemData(String menuItemData, int menuItemImage) {
        this.menuItemData = menuItemData;
        this.menuItemImage = menuItemImage;
    }

    public String getMenuItemData() {
        return menuItemData;
    }

    public int getMenuItemImage() {
        return menuItemImage;
    }


}

