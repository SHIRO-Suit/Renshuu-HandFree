package com.example.renshuuhandsoff;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Term{
    @PrimaryKey
    public int id;

    @ColumnInfo(name = "Kanji")
    public String kanji;

    @ColumnInfo(name = "Kana")
    public String kana;

    @ColumnInfo(name = "Type of  Speech")
    public int typeofspeech;

    

}
