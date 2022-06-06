package com.example.app.screens;


import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.app.R;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private UUID appBlueToothID = UUID.fromString("1ccb43a8-34fd-49d6-a8fa-acf1b480c28c");


    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("Main " + (String) getIntent().getStringExtra("username"));
        setContentView(R.layout.activity_main);

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Your device does not support bluetooth", Toast.LENGTH_LONG).show();
        } else {

            if (!bluetoothAdapter.isEnabled()) {
                Intent enableBluetooth = new Intent
                        (BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivity(enableBluetooth);
            }
            else
                Toast.makeText(this, "Bluetooth already on", Toast.LENGTH_LONG).show();
        }

    }

}
