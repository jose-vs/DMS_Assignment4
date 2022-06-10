package com.example.app.screens.fragments;

import static com.example.app.data.Utility.API_URL;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.ListFragment;

import com.example.app.R;
import com.example.app.api.FetchCardService;
import com.example.app.api.TradeCardService;

import java.util.HashMap;

public class UserOwnedCardsFragment extends ListFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card_view, container, false);

        return view;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        /**
         * handle trading
         */
        HashMap<String, String> item = (HashMap<String, String>) l.getAdapter().getItem(position);

        tradeCard(Integer.valueOf(item.get("id")), item.get("user"));

    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {
        new FetchCardService(this).execute(API_URL + "cards/ojse");
    }

    private void tradeCard(Integer id, String user) {
        new TradeCardService().execute(API_URL +"cards/" + id + "/" + user);
        refresh();
    }


}
