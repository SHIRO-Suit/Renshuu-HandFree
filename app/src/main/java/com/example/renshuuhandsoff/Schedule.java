package com.example.renshuuhandsoff;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Schedule {
    @PrimaryKey
    public int id;

    @ColumnInfo(name = "Schedule Name")
    public String name;

    public int reviewTodayCount;

    public Date lastUpdate;

    public boolean valid;

}
