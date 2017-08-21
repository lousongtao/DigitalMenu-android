package com.shuishou.digitalmenu.db;

import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.shuishou.digitalmenu.bean.Category1;
import com.shuishou.digitalmenu.bean.Category2;
import com.shuishou.digitalmenu.bean.Desk;
import com.shuishou.digitalmenu.bean.Dish;
import com.shuishou.digitalmenu.ui.MainActivity;

import java.util.List;

/**
 * Created by Administrator on 2017/6/9.
 */

public class DBOperator {
    private MainActivity mainActivity;
    private static LiteOrm liteOrm;

    public DBOperator(MainActivity mainActivity){
        this.mainActivity = mainActivity;
        if (liteOrm == null){
            liteOrm = LiteOrm.newCascadeInstance(mainActivity, "digitalmenu.db");
        }
        liteOrm.setDebugged(true);
    }

//    public void saveObjectBySingle(Object o){
//        liteOrm.single().save(o);
//    }
//
//    public void saveObjectsBySingle(List objects){
//        liteOrm.single().save(objects);
//    }

    public void saveObjectByCascade(Object o){
        liteOrm.cascade().save(o);
    }

    public void saveObjectsByCascade(List objects){
        liteOrm.cascade().save(objects);
    }

    public void updateObject(Object o){
        liteOrm.update(o);
    }

    public Object queryObjectById(int id, Class c){
        return liteOrm.queryById(id, c);
    }
    public void deleteAllData(Class c){
        liteOrm.deleteAll(c);
    }
    public void deleteObject(Object o){
        liteOrm.delete(o);
    }
    public List<Category1> queryAllMenu(){
        List<Category1> c1s = liteOrm.cascade().query(Category1.class);
        return c1s;
    }

    public Dish queryDishById(int dishId){
        Dish dish = liteOrm.queryById(dishId, Dish.class);
        return dish;
    }

    public List<Dish> queryDishByParentId(int category2Id){
        return liteOrm.query(new QueryBuilder<Dish>(Dish.class).where("category2Id = " + category2Id ));
    }

    public List<Category2> queryCategory2ByParentId(int category1Id){
        return liteOrm.query(new QueryBuilder<Category2>(Category2.class).where("category1Id = " + category1Id ));
    }

    public void clearMenu(){
        liteOrm.deleteAll(Dish.class);
        liteOrm.deleteAll(Category2.class);
        liteOrm.deleteAll(Category1.class);
    }

    public void clearDesk(){
        liteOrm.deleteAll(Desk.class);
    }

    public List<Desk> queryDesks(){
        return liteOrm.query(Desk.class);
    }

    public LiteOrm getLiteOrm(){
        return liteOrm;
    }
}
