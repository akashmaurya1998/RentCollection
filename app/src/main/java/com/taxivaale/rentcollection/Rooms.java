package com.taxivaale.rentcollection;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class Rooms extends AppCompatActivity implements View.OnClickListener {

    EditText roomNo;
    Button btnView;
    TextView tvGetCt,tvGetLB, tvGetLT, tvGetMN, tvGetGD, tvGetBD;

    InputMethodManager inputMethodManager;

    String tenantName, mobileNo;
    int gDeposit, balDeposit, lastBill, lastTotal;

    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms);

        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        setTitle("Rooms");

        progressDialog = new ProgressDialog(Rooms.this);
        progressDialog.setMessage("Wait a sec...");

        roomNo = findViewById(R.id.edtTenantRoomNo);

        tvGetBD = findViewById(R.id.tvGetBD);
        tvGetGD = findViewById(R.id.tvGetGD);
        tvGetCt = findViewById(R.id.tvGetCT);
        tvGetLB = findViewById(R.id.lightBill);
        tvGetLT = findViewById(R.id.tvGetLT);
        tvGetMN = findViewById(R.id.tvGetMN);
        btnView = findViewById(R.id.btnView);

        btnView.setOnClickListener(this);
    }


                            // To get data from tenant

    public void getTenantData(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Tenant");
        query.whereEqualTo("RoomNo", Integer.parseInt(roomNo.getText().toString()));
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject tenant, ParseException e) {
                if (e == null){
                    tenantName = tenant.getString("TenantName");
                    mobileNo = tenant.getString("PhoneNo");
                    gDeposit = tenant.getInt("Deposit");
                    balDeposit = tenant.getInt("BalanceDeposit");

                    tvGetCt.setText(tenantName);
                    tvGetMN.setText(mobileNo);
                    tvGetGD.setText(" " + gDeposit);
                    tvGetBD.setText(" " + balDeposit);
                }
                else {
                    Toast.makeText(Rooms.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });
    }


                            // To get data from room

    public void getRoomData(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("room");
        query.whereEqualTo("RoomNo", Integer.parseInt(roomNo.getText().toString()));
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject room, ParseException e) {
                if (e == null){
                    lastBill = room.getInt("LightBill");
                    lastTotal = room.getInt("Total");

                    tvGetLT.setText(" " + lastTotal);
                    tvGetLB.setText(" " + lastBill);
                    progressDialog.dismiss();
                }
                else {
                    Toast.makeText(Rooms.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

                            // To get and set values in TextViews
    public void setValues(){

        tvGetLT.setVisibility(View.VISIBLE);
        tvGetGD.setVisibility(View.VISIBLE);
        tvGetBD.setVisibility(View.VISIBLE);
        tvGetCt.setVisibility(View.VISIBLE);
        tvGetLB.setVisibility(View.VISIBLE);
        tvGetMN.setVisibility(View.VISIBLE);

    }

                            // Function to hide Keyboard
    public void hideKeyBoard(View view) {

        if (view != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onClick(View v) {
        hideKeyBoard(this.getCurrentFocus());
        progressDialog.show();


        getTenantData();
        getRoomData();
        setValues();

    }

}
