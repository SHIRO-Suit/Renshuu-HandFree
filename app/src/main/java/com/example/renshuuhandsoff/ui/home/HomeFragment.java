package com.example.renshuuhandsoff.ui.home;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.renshuuhandsoff.RenshuuViewModel;
import com.example.renshuuhandsoff.databinding.FragmentHomeBinding;

import java.util.Locale;

public class HomeFragment extends Fragment {

    RenshuuViewModel renshuuViewModel;

    TextToSpeech t1;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        renshuuViewModel = new ViewModelProvider(this).get(RenshuuViewModel.class);


        t1 = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR){
                    t1.setLanguage(Locale.JAPANESE);
                }
            }
        });
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        final Button btn = binding.button3;

        renshuuViewModel.GetFirstWord().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {

                t1.speak(s,TextToSpeech.QUEUE_FLUSH,null);
            }
        });


        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                renshuuViewModel.FetchAllWords(getActivity());
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