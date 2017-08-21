package com.shuishou.digitalmenu.bean;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.Mapping;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;
import com.litesuits.orm.db.enums.Relation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/22.
 */
@Table("category2")
public class Category2 implements Serializable{
    @PrimaryKey(value = AssignType.BY_MYSELF)
    private int id;

    @Column("chinese_name")
    private String chineseName;

    @Column("english_name")
    private String englishName;

    @Column("sequence")
    private int sequence;

    @Mapping(Relation.OneToMany)
    private ArrayList<Dish> dishes;

    @Mapping(Relation.ManyToOne)
    @Column("category1_id")
    private Category1 category1;

    public Category2(){

    }

    public Category2(int id, String chineseName, String englishName, int sequence, Category1 category1){
        this.id = id;
        this.chineseName = chineseName;
        this.englishName = englishName;
        this.sequence = sequence;
        this.category1 = category1;
    }

    public Category1 getCategory1() {
        return category1;
    }

    public void setCategory1(Category1 category1) {
        this.category1 = category1;
    }

    @Override
    public String toString() {
        return "Category2 [chineseName=" + chineseName + ", englishName=" + englishName + "]";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChineseName() {
        return chineseName;
    }

    public void setChineseName(String chineseName) {
        this.chineseName = chineseName;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public List<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(ArrayList<Dish> dishes) {
        this.dishes = dishes;
    }

    public void addDish(Dish dish){
        if (dishes == null)
            dishes = new ArrayList<Dish>();
        dishes.add(dish);
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Category2 other = (Category2) obj;
        if (id != other.id)
            return false;
        return true;
    }
}
