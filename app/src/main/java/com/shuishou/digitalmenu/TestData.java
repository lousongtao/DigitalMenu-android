package com.shuishou.digitalmenu;

import com.shuishou.digitalmenu.bean.Category1;
import com.shuishou.digitalmenu.bean.Category2;
import com.shuishou.digitalmenu.bean.Dish;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/22.
 */

public class TestData {

    public static List<Category1> makeCategory1() {
        int c1id = 0;
        int c2id = 0;
        int dishid = 0;
        ArrayList<Category1> c1 = new ArrayList<Category1>();
        Category1 c11 = new Category1(++c1id, "大类1", "Main Category1", 1);
        Category1 c12 = new Category1(++c1id, "大类2", "Main Category2", 2);
        Category1 c13 = new Category1(++c1id, "大类3", "Main Category3", 3);
        Category1 c14 = new Category1(++c1id, "大类4", "Main Category4", 4);
        Category1 c15 = new Category1(++c1id, "大类5", "Main Category5", 5);
        Category1 c16 = new Category1(++c1id, "大类6", "Main Category6", 6);
        Category1 c17 = new Category1(++c1id, "大类7", "Main Category7", 7);
        Category1 c18 = new Category1(++c1id, "大类8", "Main Category8", 8);
        c1.add(c11);
        c1.add(c12);
        c1.add(c13);
        c1.add(c14);
        c1.add(c15);
        c1.add(c16);
        c1.add(c17);
        c1.add(c18);

        Category2 c21_1 = new Category2(++c2id, "小类1-1", "Sub Category1-1", 1, c11);
        Category2 c21_2 = new Category2(++c2id, "小类1-2", "Sub Category1-2", 2, c11);
        Category2 c21_3 = new Category2(++c2id, "小类1-3", "Sub Category1-3", 3, c11);
        Category2 c21_4 = new Category2(++c2id, "小类1-4", "Sub Category1-4", 4, c11);
        Category2 c21_5 = new Category2(++c2id, "小类1-5", "Sub Category1-5", 5, c11);
        Category2 c21_6 = new Category2(++c2id, "小类1-6", "Sub Category1-6", 6, c11);
        c11.addCategory2(c21_1);
        c11.addCategory2(c21_2);
        c11.addCategory2(c21_3);
        c11.addCategory2(c21_4);
        c11.addCategory2(c21_5);
        c11.addCategory2(c21_6);

        Category2 c22_1 = new Category2(++c2id, "小类2-1", "Sub Category2-1", 1, c12);
        Category2 c22_2 = new Category2(++c2id, "小类2-2", "Sub Category2-2", 2, c12);
        Category2 c22_3 = new Category2(++c2id, "小类2-3", "Sub Category2-3", 3, c12);
        c12.addCategory2(c22_1);
        c12.addCategory2(c22_2);
        c12.addCategory2(c22_3);

        Category2 c23_1 = new Category2(++c2id, "小类3-1", "Sub Category3-1", 1, c13);
        Category2 c23_2 = new Category2(++c2id, "小类3-2", "Sub Category3-2", 2, c13);
        Category2 c23_3 = new Category2(++c2id, "小类3-3", "Sub Category3-3", 3, c13);
        c13.addCategory2(c23_1);
        c13.addCategory2(c23_2);
        c13.addCategory2(c23_3);

        Category2 c24_1 = new Category2(++c2id, "小类4-1", "Sub Category4-1", 1, c14);
        c14.addCategory2(c24_1);

        //build dishes
//        Dish dish1_1_1 = new Dish(++dishid, "菜1-1-1", "dish1-1-1", 1, 5.5, "", c21_1);
//        Dish dish1_1_2 = new Dish(++dishid, "菜1-1-2", "dish1-1-2", 2, 5.2, "", c21_1);
//        Dish dish1_1_3 = new Dish(++dishid, "菜1-1-3", "dish1-1-3", 3, 5.4, "", c21_1);
//        Dish dish1_1_4 = new Dish(++dishid, "菜1-1-4", "dish1-1-4", 4, 5.6, "", c21_1);
//        Dish dish1_1_5 = new Dish(++dishid, "菜1-1-5", "dish1-1-5", 5, 5.5, "", c21_1);
//        Dish dish1_1_6 = new Dish(++dishid, "菜1-1-6", "dish1-1-6", 6, 5.2, "", c21_1);
//        Dish dish1_1_7 = new Dish(++dishid, "菜1-1-7", "dish1-1-7", 7, 5.4, "", c21_1);
//        Dish dish1_1_8 = new Dish(++dishid, "菜1-1-8", "dish1-1-8", 8, 5.6, "", c21_1);
//        c21_1.addDish(dish1_1_1);
//        c21_1.addDish(dish1_1_2);
//        c21_1.addDish(dish1_1_3);
//        c21_1.addDish(dish1_1_4);
//        c21_1.addDish(dish1_1_5);
//        c21_1.addDish(dish1_1_6);
//        c21_1.addDish(dish1_1_7);
//        c21_1.addDish(dish1_1_8);
//        Dish dish1_2_1 = new Dish(++dishid, "菜1-2-1", "dish1-2-1", 2, 5.5, "", c22_2);
//        c21_2.addDish(dish1_2_1);
//        Dish dish1_3_1 = new Dish(++dishid, "菜1-3-1", "dish1-3-1", 3, 5.2, "", c22_2);
//        c21_3.addDish(dish1_3_1);
//        Dish dish2_1_1 = new Dish(++dishid, "菜2-1-1", "dish2-1-1", 4, 5.4, "", c22_2);
//        c22_1.addDish(dish2_1_1);
//        Dish dish2_2_1 = new Dish(++dishid, "菜2-2-1", "dish2-2-1", 5, 5.6, "", c22_2);
//        c22_2.addDish(dish2_2_1);
        return c1;
    }

    public static Dish getDish(int dishid) {
        List<Category1> c1s = makeCategory1();
        for (Category1 c1 : c1s) {
            List<Category2> c2s = c1.getCategory2s();
            if (c2s != null) {
                for (Category2 c2 : c2s) {
                    List<Dish> dishes = c2.getDishes();
                    if (dishes != null) {
                        for (Dish dish : dishes) {
                            if (dish.getId() == dishid)
                                return dish;
                        }
                    }
                }
            }
        }
        return null;
    }
}
