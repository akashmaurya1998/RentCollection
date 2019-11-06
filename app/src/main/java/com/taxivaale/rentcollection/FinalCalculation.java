package com.taxivaale.rentcollection;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

public class FinalCalculation extends AppCompatActivity implements View.OnClickListener {

    EditText edtRoomNo, edtMeterUnit, edtDays, edtExtraFine, edtRecieved;
    TextView tvGetLB, tvCalRent, tvGetBal, tvExtraFine, tvGetDepo, tvFccalTotal;

    Button btnCalculate, btnFCRemove;

    public float  meterUnit, days, extraFine, total, rent, dbUnit, dbDeposit,lightBill, actualRent, balance, lastTotal, lastUnit;

    public int roomNo;

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

                                        //Initializing TextViews

        tvGetLB = (TextView) findViewById(R.id.tvGetLB);
        tvCalRent = (TextView) findViewById(R.id.tvCalRent);
        tvGetBal = (TextView) findViewById(R.id.tvGetBal);
        tvExtraFine = (TextView) findViewById(R.id.tvCalExtraFine);
        tvGetDepo = (TextView) findViewById(R.id.tvGetDepo);
        tvFccalTotal = (TextView) findViewById(R.id.tvFcCalTotal);

                                        //Initialing Buttons

        btnCalculate = (Button) findViewById(R.id.btnCalculate);
        btnFCRemove = (Button) findViewById(R.id.btnFCRemove);

                                        // Initializing progress dialogue

        progressDialog = new ProgressDialog(FinalCalculation.this);
        progressDialog.setMessage("Processing...");

                                        //set On Click Listener
        btnCalculate.setOnClickListener(this);
        btnFCRemove.setOnClickListener(this);
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
        days = Integer.parseInt(edtDays.getText().toString());
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
                    lastTotal = room.getInt("Total");
                    lastUnit = room.getInt("Unit");

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


        extraFine = Integer.parseInt(edtExtraFine.getText().toString());


        total = (lightBill + rent + extraFine + balance) - dbDeposit;


        tvGetLB.setText(" " + lightBill);
        tvCalRent.setText(" " + rent);
        tvExtraFine.setText(" " + extraFine);
        tvFccalTotal.setText(" " + total);

    }



    public void btnFCRemove(){
        roomNo = Integer.parseInt(edtRoomNo.getText().toString());
        meterUnit = Integer.parseInt(edtMeterUnit.getText().toString());


        getAllData();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("room");
        query.whereEqualTo("RoomNo", roomNo);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject room, ParseException e) {
                if (e == null){

                    room.put("Unit", meterUnit);
                    room.put("Balance", 0);
                    room.put("Total", total);
                    room.put("LightBill", lightBill);
                    room.put("Recieved", total);
                    room.put("lunit", lastUnit);
                    room.put("LastTotal", String.valueOf(lastTotal));

                    room.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null){
                                progressDialog.dismiss();
                                Toast.makeText(FinalCalculation.this, "Done", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(FinalCalculation.this,  e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                } else {
                    Toast.makeText(FinalCalculation.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        Toast.makeText(FinalCalculation.this, "Done", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCalculate :
                btnCalculate();
                break;

            case R.id.btnFCRemove:
                progressDialog.show();
                btnFCRemove();

                break;
        }
    }
}
