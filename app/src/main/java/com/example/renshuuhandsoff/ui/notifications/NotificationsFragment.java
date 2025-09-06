package com.example.renshuuhandsoff.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import com.example.renshuuhandsoff.RenshuuViewModel;
import com.example.renshuuhandsoff.databinding.FragmentNotificationsBinding;

import org.w3c.dom.Text;

import java.net.URL;
import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class NotificationsFragment extends Fragment {

    private RenshuuViewModel viewModel;
    private TextView result;
    private EditText apiKeyInput;
    private FragmentNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //final TextView textView = binding.textNotifications;
        //notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        viewModel = new ViewModelProvider(this).get(RenshuuViewModel.class);
        final Button btnConnect = binding.button;
        final Button btnSync = binding.button2;
        result  = binding.textView;
        apiKeyInput = binding.editTextTextPassword;
        apiKeyInput.setText(viewModel.getKey(getActivity()));



        viewModel.getProfile().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                result.setText(s);
            }
        });


        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.loadProfile(apiKeyInput.getText().toString(),getActivity());
            }
        });

        btnSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.Sync(getActivity());
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