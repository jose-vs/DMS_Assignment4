package com.example.app.screens;


import android.annotation.SuppressLint;
import android.content.Intent;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app.R;
import com.example.app.screens.buycards.BuyCardActivity;
import com.example.app.screens.fragments.UserOwnedCardsFragment;
import com.example.app.screens.setupbluetooth.SetupBluetoothActivity;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("Main " + (String) getIntent().getStringExtra("username"));
        setContentView(R.layout.activity_main);




        Button b1 = (Button) this.findViewById(R.id.buyCards);
        b1.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, BuyCardActivity.class);
            intent.putExtra("username", getIntent().getStringExtra("username"));
            startActivity(intent);
        });
        Button b2 = (Button) this.findViewById(R.id.retrieveCard);
        b2.setOnClickListener(v -> {
            /**
             * Something
             */
        });
        Button b3 = (Button) this.findViewById(R.id.setupBluetooth);
        b3.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SetupBluetoothActivity.class);
            intent.putExtra("username", getIntent().getStringExtra("username"));
            startActivity(intent);
        });

    }

}
