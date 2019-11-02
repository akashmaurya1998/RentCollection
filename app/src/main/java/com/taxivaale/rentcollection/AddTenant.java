package com.taxivaale.rentcollection;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

public class AddTenant extends AppCompatActivity {

    EditText edtName, edtPerAdd, edtRoomNo, edtPhoneNo, edtDeposit;
    Button btnSave;
    ProgressDialog progressDialog;




    int recDeposit = 0, roomNo = 0, f,rDeposit,balDeposit;
    String phoneNo, name, perAdd;


    //  Function to hide Keyboard



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tenant);

        setTitle("Add a new tenant");

        edtDeposit = (EditText) findViewById(R.id.tvDeposit);
        edtPhoneNo = (EditText) findViewById(R.id.edtPhoneNo);
        edtRoomNo = (EditText) findViewById(R.id.edtTenantRoomNo);
        edtPerAdd = (EditText) findViewById(R.id.edtPerAdd);
        edtName = (EditText) findViewById(R.id.edtName);

        btnSave = (Button) findViewById(R.id.btnAddTenant);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                progressDialog = new ProgressDialog(AddTenant.this);
                progressDialog.setMessage("Processing...");
                progressDialog.show();
                ;

                final ParseObject room = new ParseObject("room");

                if (edtDeposit.getText().toString().equals("")|| edtRoomNo.getText().toString().equals("")){
                    Toast.makeText(AddTenant.this, "Deposit and room No cannot be empty", Toast.LENGTH_SHORT).show();
                }
                else {

                    recDeposit = Integer.parseInt(edtDeposit.getText().toString());
                    roomNo = Integer.parseInt(edtRoomNo.getText().toString());

                }

                phoneNo = edtPhoneNo.getText().toString();
                name = edtName.getText().toString();
                perAdd = edtPerAdd.getText().toString();




                final ParseQuery<ParseObject> query = ParseQuery.getQuery("Tenant");
                query.whereEqualTo("RoomNo", roomNo);
                query.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject tenant, ParseException e) {

                        if (e == null){
                            f = 1;
                            //Toast.makeText(AddTenant.this,"" + f, Toast.LENGTH_SHORT).show();
                            query3();
                        }

                        else if (e.getCode() == ParseException.OBJECT_NOT_FOUND){
                            f = 0;
                            query3();
                            //Toast.makeText(AddTenant.this,"" + f, Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(AddTenant.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });

    }

    private void query3() {
        ParseQuery<ParseObject> query3 = ParseQuery.getQuery("room");
        query3.whereEqualTo("RoomNo", roomNo);
        query3.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject room, ParseException e) {
                if (e == null){
                    Log.d("rDeposit", "Done");
                    rDeposit = room.getInt("Deposit");
                    balDeposit = rDeposit - recDeposit;
//                    Toast.makeText(AddTenant.this, "" + rDeposit, Toast.LENGTH_SHORT).show();
                    getDataComplete();
                }


            }
        });
    }

    private void getDataComplete() {
        final ParseObject tenant = new ParseObject("Tenant");
        if (f == 0){
//            Toast.makeText(AddTenant.this, "" + rDeposit, Toast.LENGTH_SHORT).show();
            tenant.put("RoomNo", roomNo);
            tenant.put("PermanentAddress", perAdd);
            tenant.put("PhoneNo", phoneNo);
            tenant.put("TenantName", name);
            tenant.put("Deposit", recDeposit);
            tenant.put("BalanceDeposit", balDeposit);

            tenant.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null){
                        //Toast.makeText(AddTenant.this, "New Tenant has been added", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        Toast.makeText(AddTenant.this, "" + rDeposit, Toast.LENGTH_SHORT).show();
//                                Intent intent = new Intent(AddTenant.this, AddTenant.class);
//                                startActivity(intent);
//                                finish();
                    }
                    else {
                        Toast.makeText(AddTenant.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

        else if (f == 1){
            ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Tenant");
            query1.whereEqualTo("RoomNo", roomNo);
            query1.getFirstInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject tenant, ParseException e) {
                    tenant.put("PermanentAddress", perAdd);
                    tenant.put("PhoneNo", phoneNo);
                    tenant.put("TenantName", name);
                    tenant.put("Deposit", recDeposit);
                    tenant.put("BalanceDeposit", balDeposit);
                    tenant.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null){
                                // Toast.makeText(AddTenant.this, "Tenant Updated", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                Toast.makeText(AddTenant.this, "" + rDeposit, Toast.LENGTH_SHORT).show();
//                                        Intent intent = new Intent(AddTenant.this, AddTenant.class);
//                                        startActivity(intent);
//                                        finish();
                            }
                            else {
                                Toast.makeText(AddTenant.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
        }
    }




}