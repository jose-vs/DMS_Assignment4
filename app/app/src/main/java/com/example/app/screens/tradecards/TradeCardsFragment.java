package com.example.app.screens.tradecards;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.app.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TradeCardsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TradeCardsFragment extends Fragment {

    private int mColumnCount = 1;
    private static final String ARG_COLUMN_COUNT = "column-count";



    public static TradeCardsFragment newInstance(int columnCount) {
        TradeCardsFragment fragment = new TradeCardsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank, container, false);
    }
}