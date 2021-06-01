package com.example.retornam20;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import javax.annotation.Nullable;

public class ChatRoomContentFragment extends Fragment {

    private static final String TEXT = "text";

    public static ChatRoomContentFragment newInstance() {
        return new ChatRoomContentFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_chatroom, container, false);

        return layout;
    }
}