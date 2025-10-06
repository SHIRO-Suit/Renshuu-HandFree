package com.example.renshuuhandsoff;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


@Dao
public interface RenshuuDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void insertTerm(Term term);
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void insertDefinitions(List<Definition> defs);
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void insertStat(Stat stat);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void insertTypeSpeech(TypeSpeech typeSpeech);

    @Query("SELECT id FROM typespeech WHERE typespeech.TypeName = :Name")
    public int GetTypeSpeechId(String Name); // pour mettre dans le champ du term

    @Query("SELECT * FROM Term  JOIN TypeSpeech ON Term.`Type of  Speech` = TypeSpeech.id JOIN Definition ON Term.id = Definition.`id term` ORDER BY indexDef")
    public List<WordResponse> getAllWords();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertSchedule(Schedule schedule);

    @Query("SELECT id FROM Schedule")

    public List<Integer> getAllScheduleIds();

    @Query("UPDATE Schedule SET valid = true WHERE id = :id")
    public void setScheduleValid(int id);
}
