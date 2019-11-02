package com.taxivaale.rentcollection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.ParseInstallation;

public class HomeScreen extends AppCompatActivity implements View.OnClickListener{

    Button btnRentCal, btnRoomInfo, btnFinalCal, btnAddTenant;

    TextView tvWeDevelop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        tvWeDevelop = (TextView) findViewById(R.id.tvWedevelop);
        tvWeDevelop.setMovementMethod(LinkMovementMethod.getInstance());


        btnAddTenant = (Button) findViewById(R.id.btnAddTenant);
        btnFinalCal = (Button) findViewById(R.id.btnFinalCal);
        btnRoomInfo = (Button) findViewById(R.id.btnRoomInfo);
        btnRentCal = (Button) findViewById(R.id.btnRentCal);

        btnAddTenant.setOnClickListener(this);
        btnFinalCal.setOnClickListener(this);
        btnRoomInfo.setOnClickListener(this);
        btnRentCal.setOnClickListener(this);
        ParseInstallation.getCurrentInstallation().saveInBackground();


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btnRentCal :
                Intent intent = new Intent(HomeScreen.this, MainActivity.class);
                startActivity(intent);
                break;

            case R.id.btnRoomInfo :
                Intent intent1 = new Intent(HomeScreen.this, Rooms.class);
                startActivity(intent1);
                break;

            case R.id.btnAddTenant :
                Intent intent2 = new Intent(HomeScreen.this,AddTenant.class);
                startActivity(intent2);
                break;

            case R.id.btnFinalCal :
                Intent intent3 = new Intent(HomeScreen.this, FinalCalculation.class);
                startActivity(intent3);
                break;

        }
    }
}
