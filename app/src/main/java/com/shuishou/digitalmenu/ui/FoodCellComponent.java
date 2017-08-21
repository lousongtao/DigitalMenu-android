package com.shuishou.digitalmenu.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shuishou.digitalmenu.R;
import com.shuishou.digitalmenu.bean.Dish;

import static android.content.ContentValues.TAG;

/**
 * Created by Administrator on 2016/12/31.
 */

public class FoodCellComponent extends LinearLayout {

    private final static int ICON_SELECT_BUTTON =R.drawable.select_btn_icon;
    private ChangeLanguageTextView foodNameText;
    private TextView foodPriceText;
    private ImageButton chooseButton;
    private ImageView ivSpecial;
    private ImageView ivNew;
    private ImageView ivSoldOut;
    private ImageView ivChili1;
    private ImageView ivChili2;
    private ImageView ivChili3;

    private Dish dish;

    public FoodCellComponent(Context context, Dish _dish){
        super(context);
        this.dish = _dish;
        LayoutInflater.from(context).inflate(R.layout.foodcell_layout, this);
        foodNameText = (ChangeLanguageTextView) findViewById(R.id.foodNameText);
        foodPriceText = (TextView) findViewById(R.id.foodPriceText);
        ivSoldOut = (ImageView) findViewById(R.id.imgSoldOut);
        ivNew = (ImageView) findViewById(R.id.imgNew);
        ivSpecial = (ImageView) findViewById(R.id.imgSpecial);
        ivChili1 = (ImageView) findViewById(R.id.imgChili1);
        ivChili2 = (ImageView) findViewById(R.id.imgChili2);
        ivChili3 = (ImageView) findViewById(R.id.imgChili3);
        chooseButton = (ImageButton) findViewById(R.id.chooseBtn);
        chooseButton.setImageResource(ICON_SELECT_BUTTON);
        chooseButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.getInstance().onFoodSelected(dish);
            }
        });

        foodNameText.setTxtChinese(dish.getChineseName());
        foodNameText.setTxtEnglish(dish.getEnglishName());
        foodPriceText.setText("$ " + String.valueOf(dish.getPrice()));
        foodNameText.show(MainActivity.getInstance().getLanguage());
        ivNew.setVisibility(dish.isNew() ? View.VISIBLE : View.INVISIBLE);
        ivSoldOut.setVisibility(dish.isSoldOut() ? View.VISIBLE : View.INVISIBLE);
        ivSpecial.setVisibility(dish.isSpecial() ? View.VISIBLE : View.INVISIBLE);
        ivChili1.setVisibility(dish.getHotLevel() > 0 ? View.VISIBLE : View.INVISIBLE);
        ivChili2.setVisibility(dish.getHotLevel() > 1 ? View.VISIBLE : View.INVISIBLE);
        ivChili3.setVisibility(dish.getHotLevel() > 2 ? View.VISIBLE : View.INVISIBLE);
    }

    public void setSoldOutVisibility(boolean isSoldOut){
        dish.setSoldOut(isSoldOut);
        ivSoldOut.setVisibility(dish.isSoldOut() ? View.VISIBLE : View.INVISIBLE);
    }
}
