package com.example.retornam20;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.retornam20.adapter.TabPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import javax.annotation.Nullable;

public class HomeContentFragment extends Fragment {
    public static HomeContentFragment newInstance() {
        return new HomeContentFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_home, container, false);

        TabPagerAdapter myPagerAdapter = new TabPagerAdapter(getActivity().getSupportFragmentManager());
        ViewPager viewPager = layout.findViewById(R.id.pager);
        viewPager.setAdapter(myPagerAdapter);

        TabLayout tabs = layout.findViewById(R.id.tabLayout);
        tabs.setupWithViewPager(viewPager);

        return layout;
    }
}

