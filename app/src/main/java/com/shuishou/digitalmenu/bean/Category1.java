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

@Table("category1")
public class Category1 implements Serializable{
    @PrimaryKey(value = AssignType.BY_MYSELF)
    private int id;

    @Column("chinese_name")
    private String chineseName;

    @Column("english_name")
    private String englishName;

    @Column("sequence")
    private int sequence;

    @Mapping(Relation.OneToMany)
    private ArrayList<Category2> category2s;

    public Category1(){

    }

    public Category1(int id, String chineseName, String englishName, int sequence){
        this.id = id;
        this.chineseName = chineseName;
        this.englishName = englishName;
        this.sequence = sequence;
    }

    public List<Category2> getCategory2s() {
        return category2s;
    }

    public void setCategory2s(ArrayList<Category2> category2s) {
        this.category2s = category2s;
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

    public void addCategory2(Category2 c2){
        if (category2s == null)
            category2s = new ArrayList<Category2>();
        category2s.add(c2);
    }
    @Override
    public String toString() {
        return "Category1 [chineseName=" + chineseName + ", englishName=" + englishName + "]";
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
        Category1 other = (Category1) obj;
        if (id != other.id)
            return false;
        return true;
    }
}
