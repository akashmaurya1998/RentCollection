package com.taxivaale.rentcollection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class MainActivity extends AppCompatActivity  {


    Button btnRooms, btnCalculate, btnLastMonthInfo, btnRecieve;
    EditText edtroomNo, edtUnit, edtRecieved;

    TextView tvTotal, tvlightBill, tvBalance, tvRent;

    int preUnit, rent, bal, unit, total, recieved, dtotal, dUnit, balance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         btnRooms = findViewById(R.id.btnRooms);
         edtroomNo = findViewById(R.id.edtTenantRoomNo);
         edtUnit = findViewById(R.id.edtUnit);
         btnLastMonthInfo = findViewById(R.id.btnLastMonthInfo);
         tvTotal =findViewById(R.id.tvTotal);
         btnCalculate = findViewById(R.id.btnCalculate);
         tvlightBill = findViewById(R.id.tvLightBill);
         tvBalance = findViewById(R.id.tvBalance);
         tvRent = findViewById(R.id.tvRent);
         btnRecieve = findViewById(R.id.btnRecieved);
         edtRecieved = findViewById(R.id.edtRecieved);
         ParseInstallation.getCurrentInstallation().saveInBackground();




        btnRooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, Rooms.class);
                startActivity(intent);
            }
        });

        btnLastMonthInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ActivityLastMonth.class);
                startActivity(intent);
            }
        });

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtUnit.getText().toString() != null && !edtUnit.getText().toString().equals("")){
                    unit = Integer.parseInt(edtUnit.getText().toString());
                }
                ParseQuery<ParseObject> query = ParseQuery.getQuery("room");

                query.whereEqualTo("RoomNo", Integer.parseInt(edtroomNo.getText().toString()));

                query.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject room, ParseException e) {
                        if (e == null){
                            preUnit = room.getInt("lunit");
                            bal = room.getInt("Balance");
                            rent = room.getInt("Rent");
                            dUnit = room.getInt("Unit");

                            int lightBill = ((unit - dUnit) * 8);
                            total = lightBill + bal + rent;



                            tvlightBill.setText(" "+ lightBill);
                            tvTotal.setText(" "+ total);
                            tvBalance.setText(" " + bal);
                            tvRent.setText(" " + rent);


                            tvBalance.setVisibility(View.VISIBLE);
                            tvRent.setVisibility(View.VISIBLE);
                            tvlightBill.setVisibility(View.VISIBLE);
                            tvTotal.setVisibility(View.VISIBLE);


                        }
                    }
                });
            }
        });

        btnRecieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ParseQuery<ParseObject> query = ParseQuery.getQuery("room");

                query.whereEqualTo("RoomNo", Integer.parseInt(edtroomNo.getText().toString()));

                query.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject room, ParseException e) {

                        if (e == null){
                            dtotal = room.getInt("Total");
                            dUnit = room.getInt("Unit");
                            recieved = Integer.parseInt(edtRecieved.getText().toString());
                            balance = total - recieved;
                            Toast.makeText(MainActivity.this, " "+ unit, Toast.LENGTH_SHORT).show();


                            if (dtotal == 0){
                                room.put("Total", total);

                                room.saveInBackground();
                            }
                            else {
                                room.put("LastTotal", Integer.toString(dtotal));
                                room.put("lunit", dUnit);
                                room.put("Total", total);
                                room.put("Unit", unit);
                                room.put("Recieved", recieved);
                                room.put("Balance", balance);

                                room.saveInBackground();

                            }
                        }

                        else {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });



            }
        });
    }




}
