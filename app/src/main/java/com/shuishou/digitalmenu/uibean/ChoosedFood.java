package com.shuishou.digitalmenu.uibean;

import android.graphics.drawable.Drawable;

import com.shuishou.digitalmenu.bean.Dish;

/**
 * Created by Administrator on 2016/12/25.
 */

public class ChoosedFood {
    private Dish dish;
    private int imageId;
    private Drawable image;
    private String additionalRequirements = "";
    private int amount = 1; //default value

    public ChoosedFood() {
    }

    public ChoosedFood(Dish dish) {
        this.dish = dish;
    }

    public String getName_cn() {
        return dish.getChineseName();
    }

    public String getName_en() {
        return dish.getEnglishName();
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getPrice() {
        return dish.getPrice();
    }

    public Dish getDish() {
        return dish;
    }

    public String getAdditionalRequirements() {
        return additionalRequirements;
    }

    public void setAdditionalRequirements(String additionalRequirements) {
        this.additionalRequirements = additionalRequirements;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChoosedFood that = (ChoosedFood) o;

        return dish.getId() == that.getDish().getId();
    }

    @Override
    public int hashCode() {
        return getDish().getId();
    }
}
