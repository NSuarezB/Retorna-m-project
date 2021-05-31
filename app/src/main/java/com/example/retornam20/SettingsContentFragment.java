package com.example.retornam20;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import javax.annotation.Nullable;

public class SettingsContentFragment extends Fragment {

    private static final String TEXT = "text";

    public static SettingsContentFragment newInstance() {
        SettingsContentFragment frag = new SettingsContentFragment();



        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_settings, container, false);

        if (getArguments() != null) {
            ((TextView) layout.findViewById(R.id.text)).setText(getArguments().getString(TEXT));
        }

        return layout;
    }
}