package com.example.renshuuhandsoff;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Stat {
    @PrimaryKey
    public int id;

    @ColumnInfo(name = "last time listened")
    public Date lastListened;

    @ColumnInfo(name = "next time listened")
    public Date nextListen;

    @ColumnInfo(name = "Error Count")
    public int errorCount;
    @ColumnInfo(name = "Correct Count")
    public int correctCount;

    

}
