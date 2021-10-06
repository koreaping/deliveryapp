package com.example.notifi;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "User")
public class MainData implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo
    private String name;
    private String num;

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public String getNum(){
        return  num;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setNum(String num){
        this.num = num;
    }
}
