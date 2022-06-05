package com.example.app.api;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.example.app.data.model.RegisterRequest;
import com.example.app.screens.login.LoginActivity;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class RegisterApiService extends AsyncTask<String, Void, Integer> {

    private RegisterRequest registerRequest;

    @SuppressLint("StaticFieldLeak")
    private Context context;

    public RegisterApiService(RegisterRequest registerRequest, Context context) {
        this.registerRequest = registerRequest;
        this.context = context;
    }
    @Override
    protected Integer doInBackground(String... params) {

        int responseCode = 0;

        try {
            String postData = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(this.registerRequest.getUsername(), "UTF-8") + "&";
            postData += URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(this.registerRequest.getName(), "UTF-8") + "&";
            postData += URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(this.registerRequest.getEmail(), "UTF-8") + "&";
            postData += URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(this.registerRequest.getPassword(), "UTF-8");

            URL registerUrl = new URL(params[0]);
            HttpURLConnection conn = (HttpURLConnection) registerUrl.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            // Send the request to the server
            OutputStream outputStream = conn.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
            bufferedWriter.write(postData);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            responseCode = conn.getResponseCode();

            System.out.println(responseCode);

            conn.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return responseCode;

    }

    @Override
    protected void onPostExecute(Integer responseCode) {
        super.onPostExecute(responseCode);

        String msg;
        if ((responseCode >= 200) && (responseCode <= 299)) {
            msg = "Account creation was successful";
            Intent myIntent = new Intent(context, LoginActivity.class);
            myIntent.putExtra("username", registerRequest.getUsername());
            ActivityCompat.finishAffinity((Activity) context);
            context.startActivity(myIntent);
        } else {
            msg = "Account creation failed";
        }
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
