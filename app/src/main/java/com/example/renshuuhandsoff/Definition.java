package com.example.renshuuhandsoff;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(primaryKeys = {"id term","indexDef"})
public class Definition {

    @ColumnInfo(name = "indexDef")
    public int index;

    @ColumnInfo(name = "id term")
    public int id_term;

    @ColumnInfo(name = "definition")
    public String definition;


    

}
