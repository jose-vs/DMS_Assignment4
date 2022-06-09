package com.example.app.api;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.fragment.app.ListFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TradeCardService extends AsyncTask<String, Void, Integer> {
    @SuppressLint("StaticFieldLeak")
    private Context context;
    private ListFragment fragment;
    private List<Map<String, String>> cardList;

    public TradeCardService(ListFragment fragment) {
        this.fragment = fragment;
        this.context = fragment.getContext();
    }


    @Override
    protected Integer doInBackground(String... params) {
        int responseCode = 0;

        try {
            URL loginUrl = new URL(params[0]);
            HttpURLConnection conn
                    = (HttpURLConnection) loginUrl.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");

            InputStream inputStream = conn.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String blogs;
            StringBuilder sb = new StringBuilder();
            while ((blogs = bufferedReader.readLine()) != null) {
                sb.append(blogs);
            }

            JSONObject json = new JSONObject(sb.toString());

            bufferedReader.close();
            inputStream.close();

            responseCode = conn.getResponseCode();

            conn.disconnect();

            if (json != null) {
                try {

                    JSONArray jsonArray = (JSONArray) json.get("cards");

                    cardList = new ArrayList<>(jsonArray.length());

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                        String id =  String.valueOf(jsonObject.get("id"));
                        String name = (String) jsonObject.get("name");
                        String description = (String) jsonObject.get("description");
                        String user = (String) jsonObject.get("user");
                        String dateAcquired = (String) jsonObject.get("dateAcquired");

                        Map<String, String> card = new HashMap<>();

                        card.put("id", id);
                        card.put("name", name);
                        card.put("description", description);
                        card.put("user", user);
                        card.put("dateAcquired", dateAcquired);

                        cardList.add(card);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return responseCode;
    }

    @Override
    protected void onPostExecute(Integer responseCode) {
        super.onPostExecute(responseCode);

        String msg;
        if ((responseCode >= 200) && (responseCode <= 299)) {
            msg = "Cards Fetched";

            SimpleAdapter adapter = new SimpleAdapter(context, cardList,
                    android.R.layout.simple_list_item_2,
                    new String[]{"name", "id"},
                    new int[]{android.R.id.text1, android.R.id.text2});
            fragment.setListAdapter(adapter);
            adapter.notifyDataSetChanged();

        } else {
            msg = "Failed to fetch cards";
        }
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}

