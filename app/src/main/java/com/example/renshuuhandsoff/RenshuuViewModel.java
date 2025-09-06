package com.example.renshuuhandsoff;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class RenshuuViewModel extends ViewModel {
    private MutableLiveData<String> profile = new MutableLiveData<>();
    private MutableLiveData<String> firstWord = new MutableLiveData<>();

    private RenshuuRepository repo = new RenshuuRepository();

    public LiveData<String> getProfile(){
        return profile;
    }
    public void loadProfile(String token, Context context){
        repo.fetchProfile(token,profile, context);
    }

    public String getKey(Context context){
        return repo.getKey(context);
    }
    public void Sync(Context context){
        repo.Sync(context);
    }

    public LiveData<String> GetFirstWord() {
        return firstWord;
    }
    public void FetchAllWords(Context c){
        repo.FetchAllWords(c,firstWord);
    }
}
