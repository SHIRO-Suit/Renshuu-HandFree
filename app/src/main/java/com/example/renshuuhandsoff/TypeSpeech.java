package com.example.renshuuhandsoff;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = "TypeName" , unique = true)})
public class TypeSpeech {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "TypeName")
    public String typeName;
}
