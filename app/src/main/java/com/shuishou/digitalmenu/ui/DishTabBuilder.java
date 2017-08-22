package com.shuishou.digitalmenu.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBar;
import android.text.Spannable;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.shuishou.digitalmenu.InstantValue;
import com.shuishou.digitalmenu.R;
import com.shuishou.digitalmenu.bean.Category1;
import com.shuishou.digitalmenu.bean.Category2;
import com.shuishou.digitalmenu.bean.Dish;
import com.shuishou.digitalmenu.io.IOOperator;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/6/9.
 */

public class DishTabBuilder {
    private MainActivity mainActivity;
    private HashMap<Integer, FoodCellComponent> hmFoodComp = new HashMap<Integer, FoodCellComponent>();
    public DishTabBuilder(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    public void buildMenuPanel(final TabHost thCategory1, final List<Category1> category1s, final byte language){
        thCategory1.clearAllTabs();
        if (category1s == null || category1s.isEmpty())
            return;
        for (int i = 0; i < category1s.size(); i++) {
            final Category1 c1 = category1s.get(i);

            TabHost.TabSpec ts = thCategory1.newTabSpec(c1.getChineseName());
            ChangeLanguageTextView text = new ChangeLanguageTextView(mainActivity, c1.getChineseName(), c1.getEnglishName());
            text.setTextSize(30);
            text.setGravity(Gravity.CENTER);
//            text.setBackgroundResource(R.color.colorCategory1Background);
            text.show(language);
            text.setLayoutParams(new LinearLayout.LayoutParams(250, 40));
            ts.setIndicator(text);

            ts.setContent(new TabHost.TabContentFactory() {
                @Override
                public View createTabContent(String s) {
                    return buildTabContentForCategory1(mainActivity, c1, language);
                }
            });
            thCategory1.addTab(ts);
        }
        thCategory1.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                for (int i = 0; i < thCategory1.getTabWidget().getChildCount(); i++) {
                    thCategory1.getTabWidget().getChildAt(i).setBackgroundResource(R.color.colorTabUnselected);
                }
                thCategory1.getTabWidget().getChildAt(thCategory1.getCurrentTab()).setBackgroundResource(R.color.colorTabSelected);
            }
        });

        //set the first tab as selected and change background color
        thCategory1.setCurrentTab(0);
        thCategory1.getTabWidget().getChildAt(0).setBackgroundResource(R.color.colorTabSelected);
    }

    private View buildTabContentForCategory1(final Context context, Category1 c1, byte language){
        final TabHost th = (TabHost) LayoutInflater.from(context).inflate(R.layout.category2_layout, null);
        th.setup();
//        List<Category2> c2s = mainActivity.getDbOperator().queryCategory2ByParentId(c1.getId());
        List<Category2> c2s = c1.getCategory2s();
        if (c2s == null || c2s.isEmpty())
            return th;

        for (int i = 0; i<c2s.size(); i++){
            final Category2 c2 = c2s.get(i);
            TabHost.TabSpec ts = th.newTabSpec(c2.getChineseName());
            ChangeLanguageTextView text = new ChangeLanguageTextView(context, c2.getChineseName(), c2.getEnglishName());
            text.setTextSize(25);
            text.setGravity(Gravity.CENTER);
//            text.setBackgroundResource(R.color.colorCategory2Background);
            text.show(language);
            text.setLayoutParams(new LinearLayout.LayoutParams(200, 40));
            ts.setIndicator(text);

            ts.setContent(new TabHost.TabContentFactory() {
                @Override
                public View createTabContent(String s) {
                    return buildTabContentForCategory2(context, c2);
                }
            });
            th.addTab(ts);
        }
//        th.setBackgroundResource(R.color.colorCategory2Background);
        FrameLayout tabcontent = (FrameLayout) th.findViewById(android.R.id.tabcontent);
//        tabcontent.setBackgroundResource(R.color.colorCategory2Container);
        tabcontent.setBackgroundResource(R.drawable.b5);
        th.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                for (int i = 0; i < th.getTabWidget().getChildCount(); i++) {
                    th.getTabWidget().getChildAt(i).setBackgroundResource(R.color.colorTabUnselected);
                }
                th.getTabWidget().getChildAt(th.getCurrentTab()).setBackgroundResource(R.color.colorTabSelected);
            }
        });

        //set the first tab as selected and change background color
        th.setCurrentTab(0);
        th.getTabWidget().getChildAt(0).setBackgroundResource(R.color.colorTabSelected);
        return th;
    }

    private View buildTabContentForCategory2(Context context, Category2 c2){
        ScrollView sv = new ScrollView(context);
        sv.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        TableLayout tl = new TableLayout(context);

//        List<Dish> dishes = mainActivity.getDbOperator().queryDishByParentId(c2.getId());
        List<Dish> dishes = c2.getDishes();
        if (dishes == null || dishes.isEmpty())
            return tl;

        TableRow tr = null;
        TableRow.LayoutParams trlp = new TableRow.LayoutParams();
        trlp.topMargin = 7;
        trlp.leftMargin = 7;
        trlp.width = InstantValue.DISPLAY_DISH_WIDTH;
        trlp.height = InstantValue.DISPLAY_DISH_HEIGHT;
        for(int i = 0; i< dishes.size(); i++){
            Dish dish = dishes.get(i);
            if (i % InstantValue.DISPLAY_DISH_COLUMN_NUMBER == 0){
                tr = new TableRow(context);
                tl.addView(tr);
            }

            FoodCellComponent fc = new FoodCellComponent(context, dish);
            tr.addView(fc, trlp);
            //这里要把fc先加入进tablerow才可以设置background,否则fc会被background的size撑大
            if (dish.getPictureName() != null) {
                Drawable d = IOOperator.getDishImageDrawable(mainActivity.getResources(), InstantValue.LOCAL_CATALOG_DISH_PICTURE + dish.getPictureName());
                fc.setPicture(d);
            }
            hmFoodComp.put(dish.getId(), fc);
        }

        sv.addView(tl);
        return sv;
    }

    public void changeDishSoldOutStatus(int dishId, boolean isSoldOut){
        FoodCellComponent fc = hmFoodComp.get(dishId);
        if (fc != null){
            fc.setSoldOutVisibility(isSoldOut);
        }
    }
}
