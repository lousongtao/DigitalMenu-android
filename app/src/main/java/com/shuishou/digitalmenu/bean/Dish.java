package com.shuishou.digitalmenu.bean;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.Mapping;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;
import com.litesuits.orm.db.enums.Relation;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/22.
 */

@Table("dish")
public class Dish implements Serializable{
    @PrimaryKey(value = AssignType.BY_MYSELF)
    private int id;

    @Column("chinese_name")
    private String chineseName;

    @Column("english_name")
    private String englishName;

    @Column("sequence")
    private int sequence;

    @Column("category2_id")
    @Mapping(Relation.ManyToOne)
    private Category2 category2;

    @Column("price")
    private double price;

    @Column("picture_name")
    private String pictureName;

    @Column("isNew")
    private boolean isNew = false;

    @Column("isSpecial")
    private boolean isSpecial = false;

    @Column("isSoldOut")
    private boolean isSoldOut;

    @Column("hotLevel")
    private int hotLevel;

    public Dish(){

    }

//    public Dish(int id, String chineseName, String englishName, int sequence, double price, String pictureName, Category2 category2){
//        this.id = id;
//        this.chineseName = chineseName;
//        this.englishName = englishName;
//        this.sequence = sequence;
//        this.price = price;
//        this.pictureName = pictureName;
//        this.category2 = category2;
//    }

    public int getHotLevel() {
        return hotLevel;
    }

    public void setHotLevel(int hotLevel) {
        this.hotLevel = hotLevel;
    }

    public boolean isSoldOut() {
        return isSoldOut;
    }

    public void setSoldOut(boolean soldOut) {
        isSoldOut = soldOut;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean isNew) {
        this.isNew = isNew;
    }

    public boolean isSpecial() {
        return isSpecial;
    }

    public void setSpecial(boolean isSpecial) {
        this.isSpecial = isSpecial;
    }

    public String getPictureName() {
        return pictureName;
    }

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
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

    public Category2 getCategory2() {
        return category2;
    }

    public void setCategory2(Category2 category2) {
        this.category2 = category2;
    }

    @Override
    public String toString() {
        return "Dish [chineseName=" + chineseName + ", englishName=" + englishName + "]";
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
        Dish other = (Dish) obj;
        if (id != other.id)
            return false;
        return true;
    }
}
