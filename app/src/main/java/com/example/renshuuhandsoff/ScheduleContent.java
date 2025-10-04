package com.example.renshuuhandsoff;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(primaryKeys = {"id","term"})
public class ScheduleContent {

    public int id;

    public int term;

    public Boolean studied;
    public Boolean reviewToday;

}
