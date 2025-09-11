package com.example.renshuuhandsoff.ui.home;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.renshuuhandsoff.PausableThread;
import com.example.renshuuhandsoff.RenshuuViewModel;
import com.example.renshuuhandsoff.WordResponse;
import com.example.renshuuhandsoff.databinding.FragmentHomeBinding;

import java.util.Locale;

public class HomeFragment extends Fragment {

    RenshuuViewModel renshuuViewModel;
    TextToSpeech ttsEnglish;
    TextToSpeech ttsJapanese;
    UtteranceProgressListener progressEnglish;
    UtteranceProgressListener progressJapanese;
    private FragmentHomeBinding binding;
    WordResponse lastWordResponse;
    int spacingms = 2000;
    int spacingEndms = 5000;

    PausableThread WordDistributionThread;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        renshuuViewModel = new ViewModelProvider(this).get(RenshuuViewModel.class);

        //region TTS Declaration
        ttsJapanese = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR){
                    ttsJapanese.setLanguage(Locale.JAPANESE);
                    progressJapanese = new UtteranceProgressListener() {
                        @Override
                        public void onDone(String utteranceId) {
                            Log.v("DEBUG","DONE TTS");
                            new Thread(){
                                @Override
                                public void run(){
                                    try{
                                        sleep(spacingms);
                                    }catch(InterruptedException e){}
                                    ttsEnglish.speak(lastWordResponse.defs.get(0).definition,TextToSpeech.QUEUE_FLUSH,null,"rhfutteranceEnglish");
                                }



                            }.start();
                        }

                        @Override
                        public void onError(String utteranceId){};

                        @Override
                        public void onStart(String utteranceId) {}
                    };
                    ttsJapanese.setOnUtteranceProgressListener(progressJapanese);
                }
            }

        });
        ttsEnglish = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR){
                    ttsEnglish.setLanguage(Locale.ENGLISH);
                    progressEnglish = new UtteranceProgressListener() {
                        @Override
                        public void onDone(String utteranceId) {
                            new Thread(){
                                @Override
                                public void run(){
                                    try{
                                        sleep(spacingEndms);
                                    }catch(InterruptedException e){}
                                    WordDistributionThread.wakeUp();
                                }



                            }.start();
                        }

                        @Override
                        public void onError(String utteranceId){};

                        @Override
                        public void onStart(String utteranceId) {}
                    };
                    ttsEnglish.setOnUtteranceProgressListener(progressEnglish);
                }
            }

        });
        //endregion
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        final Button btn = binding.button3;

        renshuuViewModel.GetFirstWord().observe(getViewLifecycleOwner(), new Observer<WordResponse>() {
            @Override
            public void onChanged(WordResponse w) {
                ttsJapanese.speak(w.term.kana,TextToSpeech.QUEUE_FLUSH,null,"rhfutteranceJapanese");
                lastWordResponse = w;
                //Ajouter affichage du mot
            }
        });


        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                WordDistributionThread = renshuuViewModel.FetchAllWords(getActivity());
                //t1.speak(text,TextToSpeech.QUEUE_FLUSH,null);
            }
        });

        return root;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}