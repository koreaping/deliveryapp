package com.example.notifi;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface MainDao {
  @Insert(onConflict = REPLACE)
  void insert(MainData mainData);

  @Delete
    void delete(MainData mainData);

  @Delete
    void reset(List<MainData> mainData);

  @Query("UPDATE User SET name = :sName  WHERE ID = :sID")
    void update_name(int sID, String sName);

  @Query("UPDATE User SET num = :sNum WHERE ID = :sID")
  void update_num(int sID, String sNum);

  @Query("SELECT * FROM User")
    List<MainData> getAll();
}
