package com.shuishou.digitalmenu;

/**
 * Created by Administrator on 2016/12/22.
 */

public final class InstantValue {

    public static final byte DISH_STATUS_NORMAL = 0;
    public static final byte DISH_STATUS_SOLDOUT = 1; //缺货
    public static final byte DISH_STATUS_ONSALE = 2;//促销

    public static final int DISPLAY_DISH_COLUMN_NUMBER = 4; //菜单界面每行显示的数目/列数

    public static final int DISPLAY_DISH_WIDTH = 240;
    public static final int DISPLAY_DISH_HEIGHT = 300;

    public static String URL_TOMCAT = null;
    public static final String CATALOG_DISH_PICTURE = "dishimage_original";
    public static final String LOCAL_CATALOG_DISH_PICTURE = "/data/data/com.shuishou.digitalmenu/dishimage/";
    public static final String FILE_SERVERURL = "/data/data/com.shuishou.digitalmenu/serverconfig";

}
