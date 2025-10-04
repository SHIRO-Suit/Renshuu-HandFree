package com.example.renshuuhandsoff;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Term.class, Stat.class,TypeSpeech.class,Definition.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class WordsDatabase extends RoomDatabase {
    public abstract RenshuuDao renshuuDao();
}