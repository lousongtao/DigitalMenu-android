package com.shuishou.digitalmenu.bean;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/28.
 */

@Table("menu_version")
public class MenuVersion implements Serializable {
    @PrimaryKey(value = AssignType.BY_MYSELF)
    private int id;

    @Column("version")
    private int version;

    public MenuVersion(){

    }
    public MenuVersion(int id, int version){
        this.id = id;
        this.version = version;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
