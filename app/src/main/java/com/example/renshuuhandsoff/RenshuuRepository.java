package com.example.renshuuhandsoff;

import static java.lang.Thread.sleep;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Debug;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Room;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RenshuuRepository {

    String apiKey = "";
    public String getKey( Context context){
        SharedPreferences prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        String apiKey = prefs.getString("api_key", "");
        this.apiKey = apiKey;
        return apiKey;
    }
    public void fetchProfile(String token, MutableLiveData<String> result,Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://api.renshuu.org/v1/profile");
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestProperty("Authorization", "Bearer " + token);
                    con.setRequestMethod("GET");

                    if( "OK".equals(con.getResponseMessage())){
                        SharedPreferences prefs =  context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
                        prefs.edit().putString("api_key", token).apply();
                        apiKey = token;
                    }
                    JSONObject root = NetworkUtils.getJsonResponse(con);
                    String profileName = root.getString("real_name");

                    // post result to LiveData
                    result.postValue(profileName);

                } catch (Exception e) {
                    e.printStackTrace();
                    result.postValue("Error: " + e.getMessage());
                }
            }
        }).start();
    }

    public void FetchAllWords(Context context,MutableLiveData<String> firstWord){
        WordsDatabase db = Room.databaseBuilder(context,
                        WordsDatabase.class, "WordDatabase")
                .build();


//        db.termDao().getAllWords();
        new  Thread(new Runnable() {
            @Override
            public void run() {
                for (WordResponse w : db.termDao().getAllWords() ){
                    String s = w.term.kana;
                    firstWord.postValue(
                            s);
                    try{
                    sleep(2000);
                    }catch (InterruptedException e) {

                    }
                }


            }
        }).start();
    }
    public void Sync(Context context){
        FetchAllKnownTerms(context);
    }


    public void FetchAllKnownTerms(Context context){

        WordsDatabase db = Room.databaseBuilder(context,
                WordsDatabase.class, "WordDatabase")
                .build();
        Log.v("DEBUG", "DB PHASE PASSED");
        new Thread(new Runnable() {
            @Override
            public void run() {

                int current_page = 0;
                int total_pages = 1;
                int safeguard = 0;
                do{
                try {
                    current_page++;
                    URL url = new URL("https://api.renshuu.org/v1/list/all/vocab?pg="+current_page);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestProperty("Authorization", "Bearer " + apiKey);
                    //con.setRequestProperty("pg",String.valueOf(current_page));

                    con.setRequestMethod("GET");

                    if( !"OK".equals(con.getResponseMessage())){
                        Log.v("DEBUG","Bad response : " + con.getResponseMessage());
                        return;
                    }

                    JSONObject root = NetworkUtils.getJsonResponse(con);
                    Log.v("DEBUG", "RESPONSE CONTENT : " + root.toString());
                    if (!root.has("contents"))return;
                    JSONObject contents = root.getJSONObject("contents");
                    JSONObject api_usage = root.getJSONObject("api_usage"); // plus tard, on verra pour la value live et l'update avec l'observer
                    current_page = contents.getInt("pg");
                    total_pages = contents.getInt("total_pg");
                    JSONArray terms = contents.getJSONArray("terms");
                    for(int i =0; i<terms.length();i++){
                        JSONObject term =  terms.getJSONObject(i);
                        int id = term.getInt("id");
                        String kanji = term.getString("kanji_full");
                        String hiragana = term.getString("hiragana_full");
                        String type = term.getString("typeofspeech");
                        if (type.isEmpty()) continue;
                        JSONArray definitions = term.getJSONArray("def");
                        JSONObject userData = term.getJSONObject("user_data");
                        JSONObject studyVectors = userData.getJSONObject("study_vectors");
                        JSONObject Listening = studyVectors.getJSONObject("Listening");
                        int correctCount = Listening.getInt("correct_count");
                        int missedCount = Listening.getInt("missed_count");
                        Date lastQuizzed;
                        if(Listening.getString("last_quizzed").equals("Not yet"))
                            lastQuizzed = new Date(Long.MAX_VALUE);
                        else
                            lastQuizzed = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).parse(Listening.getString("last_quizzed"));
                        Date nextQuiz = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).parse(Listening.getString("next_quiz"));

                        TypeSpeech typeSpeech = new TypeSpeech();
                        typeSpeech.typeName = type;
                        db.termDao().insertTypeSpeech(typeSpeech);
                        Term t = new Term();
                        t.id = id;
                        t.kana = hiragana;
                        t.kanji = kanji;
                        t.typeofspeech = db.termDao().GetTypeSpeechId(type);
                        db.termDao().insertTerm(t);
                        Stat stat = new Stat();
                        stat.id = id;
                        stat.errorCount = missedCount;
                        stat.nextListen = nextQuiz;
                        stat.lastListened = lastQuizzed;
                        stat.correctCount = correctCount;
                        db.termDao().insertStat(stat);
                        List<Definition> defList = new ArrayList<Definition>();
                        for(int j = 0; j<definitions.length();j++){
                            String defStr = definitions.getString(j);
                            Definition definition = new Definition();
                            definition.id_term = id;
                            definition.index = j;
                            definition.definition = defStr;
                            defList.add(definition);
                        }
                        db.termDao().insertDefinitions(defList);



                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    //result.postValue("Error: " + e.getMessage());
                }
                safeguard++;
                if(safeguard >20)return;
                }while(current_page != total_pages);
            }
        }).start();

    }

}

