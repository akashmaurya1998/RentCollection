package com.taxivaale.rentcollection;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

public class Rooms extends AppCompatActivity {

    EditText roomNo;
    EditText rentAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms);

        setTitle("Rooms");

        roomNo = findViewById(R.id.edtTenantRoomNo);
        rentAmount = findViewById(R.id.edtRentAmount);

    }

    public void save(View buttonView){



        ParseQuery<ParseObject> query = ParseQuery.getQuery("room");
        query.whereEqualTo("RoomNo", Integer.parseInt(roomNo.getText().toString()));
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null){
                    Toast.makeText(Rooms.this, "Entry for this room has been already made", Toast.LENGTH_SHORT).show();
                }

                else{
                    ParseObject room =new ParseObject("room");
                    room.put("RoomNo", Integer.parseInt(roomNo.getText().toString()));
                    room.put("Rent", Integer.parseInt(rentAmount.getText().toString()));



                    room.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {

                            if (e == null){
                                Toast.makeText(Rooms.this, "Room Data is Saved", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });



    }
}
