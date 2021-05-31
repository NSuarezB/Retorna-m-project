package com.example.retornam20;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import javax.annotation.Nullable;

public class HomeContentFragment extends Fragment {

    private static final String TEXT = "text";

    public static HomeContentFragment newInstance() {
        HomeContentFragment frag = new HomeContentFragment();


        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_home, container, false);

        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getActivity().getSupportFragmentManager());
        ViewPager viewPager = layout.findViewById(R.id.pager);
        viewPager.setAdapter(myPagerAdapter);

        TabLayout tabs = layout.findViewById(R.id.tabLayout);
        tabs.setupWithViewPager(viewPager);

        return layout;
    }
}

class MyPagerAdapter extends FragmentStatePagerAdapter {

    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment;
        switch (i) {
            case 0:
                fragment = new DeixatsContentFragment();
                break;
            case 1:
                fragment = new RetornatsContentFragment();
                break;
            default:
                fragment = null;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Deixat";
            case 1:
                return "A retornar";
        }
        return null;
    }

}