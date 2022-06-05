package com.example.app.api;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.example.app.data.model.LoginRequest;
import com.example.app.screens.MainActivity;

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

public class LoginApiService extends AsyncTask<String, Void, Integer> {

    private boolean passwordMatched;
    private LoginRequest loginRequest;

    @SuppressLint("StaticFieldLeak")
    private Context context;

    public LoginApiService(LoginRequest loginRequest, Context context) {
        this.loginRequest = loginRequest;
        this.context = context;
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
            String user;
            StringBuilder sb = new StringBuilder();
            while ((user = bufferedReader.readLine()) != null) {
                System.out.println("user: " +  user);
                sb.append(user);
            }

            JSONObject json = new JSONObject(sb.toString());

            bufferedReader.close();
            inputStream.close();

            responseCode = conn.getResponseCode();

            conn.disconnect();

            if (json != null) {
                try {
                    JSONArray jsonArray = (JSONArray) json.get("users");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        String password = (String) jsonObject.get("password");

                        System.out.println(password);

                        if (password.equals(this.loginRequest.getPassword())) {
                            passwordMatched = true;
                        }
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
        if ((responseCode >= 200) && (responseCode <= 299) && passwordMatched) {
            msg = "Login was successful";
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra("username", this.loginRequest.getUsername());
            ActivityCompat.finishAffinity((Activity) context);
            context.startActivity(intent);
        } else {
            msg = "Login failed";
        }
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
