package com.example.renshuuhandsoff;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class WordResponse {
    @Embedded
    public Term term;
    @Relation(
            parentColumn = "Type of  Speech",
            entityColumn = "TypeName"
    )

    public TypeSpeech type;
    @Relation(
            parentColumn = "id",
            entityColumn = "id term"
    )

    public List<Definition> defs;
}
