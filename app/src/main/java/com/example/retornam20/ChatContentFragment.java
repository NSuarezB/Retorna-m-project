package com.example.retornam20;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import javax.annotation.Nullable;

public class ChatContentFragment extends Fragment {

    private static final String TEXT = "text";

    public static ChatContentFragment newInstance() {
        return new ChatContentFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_chat, container, false);

        return layout;
    }
}