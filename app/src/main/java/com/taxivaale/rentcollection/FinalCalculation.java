package com.taxivaale.rentcollection;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class FinalCalculation extends AppCompatActivity implements View.OnClickListener {

    EditText edtRoomNo, edtMeterUnit, edtDays, edtExtraFine, edtRecieved;
    TextView tvGetLB, tvCalRent, tvGetBal, tvExtraFine, tvGetDepo, tvFccalTotal;

    Button btnCalculate, btnFCReceive;

    public int roomNo, meterUnit, days, extraFine, receive, total, dbUnit, dbDeposit,lightBill, rent, actualRent, balance;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_calculation);

        setTitle("Do final calculation");

                                        //Initilizing EditTexts

        edtRoomNo = (EditText) findViewById(R.id.edtRoomNo);
        edtMeterUnit = (EditText) findViewById(R.id.edtMeterUnit);
        edtDays = (EditText) findViewById(R.id.edtExtraDays);
        edtExtraFine = (EditText) findViewById(R.id.edtExtraFine);
        edtRecieved = (EditText) findViewById(R.id.edtRecAmt);

                                        //Initializing TextViews

        tvGetLB = (TextView) findViewById(R.id.tvGetLB);
        tvCalRent = (TextView) findViewById(R.id.tvCalRent);
        tvGetBal = (TextView) findViewById(R.id.tvGetBal);
        tvExtraFine = (TextView) findViewById(R.id.tvCalExtraFine);
        tvGetDepo = (TextView) findViewById(R.id.tvGetDepo);
        tvFccalTotal = (TextView) findViewById(R.id.tvFcCalTotal);

                                        //Initialing Buttons

        btnCalculate = (Button) findViewById(R.id.btnCalculate);
        btnFCReceive = (Button) findViewById(R.id.btnFCRecieve);

                                        // Initializing progress dialogue

        progressDialog = new ProgressDialog(FinalCalculation.this);
        progressDialog.setMessage("Calculating...");

                                        //set On Click Listener
        btnCalculate.setOnClickListener(this);
        btnFCReceive.setOnClickListener(this);
    }

                                        //Getting Data from Parse Server

    public void getTenantData(){
        roomNo = Integer.parseInt(edtRoomNo.getText().toString());
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Tenant");
        query.whereEqualTo("RoomNo", roomNo);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject tenant, ParseException e) {
                if (e == null){
                    dbDeposit = tenant.getInt("Deposit");
                    tvGetDepo.setText(" " + dbDeposit);
                }
                else {
                    Toast.makeText(FinalCalculation.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public  void  getRoomData(){
        roomNo = Integer.parseInt(edtRoomNo.getText().toString());
        meterUnit = Integer.parseInt(edtMeterUnit.getText().toString());
        ParseQuery<ParseObject> query = ParseQuery.getQuery("room");
        query.whereEqualTo("RoomNo", roomNo);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject room, ParseException e) {
                if (e == null){
                    dbUnit = room.getInt("Unit");
                    actualRent = room.getInt("Rent");
                    balance = room.getInt("Balance");
                    tvGetBal.setText(" " + balance);
                }
                else {
                    Toast.makeText(FinalCalculation.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        lightBill = (meterUnit - dbUnit) * 10;
        rent = (actualRent / 30) * days;
    }

    public void getAllData(){
        getRoomData();
        getTenantData();
    }
                                        //Functions to set textviews


                                        //Fumctions to be done when btnCalculate clicked
    public void btnCalculate(){
        //Initializing variables
        getAllData();

        days = Integer.parseInt(edtDays.getText().toString());
        extraFine = Integer.parseInt(edtExtraFine.getText().toString());


        total = (lightBill + rent + extraFine + balance) - dbDeposit;


        tvGetLB.setText(" " + lightBill);
        tvCalRent.setText(" " + rent);
        tvExtraFine.setText(" " + extraFine);
        tvFccalTotal.setText(" " + total);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCalculate :
                btnCalculate();
                break;

            case R.id.btnFCRecieve :
                Toast.makeText(FinalCalculation.this, "Under Progress", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
