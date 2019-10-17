package com.ceng319.firebasecode;
// TODO:  Read the following website as reference.
// TODO: https://firebase.google.com/docs/database/android/start/

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReadDB extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    DataStructure mData;

    private TextView name;
    private TextView temperature;
    private TextView humidity;
    private TextView message;
    private TextView timestamp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_db);
        this.setTitle("Read from Database");
        getDatabase();
        findAllViews();
        reterieveData();
    }

    private void findAllViews(){
        name = findViewById(R.id.readname);
        temperature = findViewById(R.id.readtemperature);
        humidity = findViewById(R.id.readhumidity);
        message = findViewById(R.id.readmessage);
        timestamp = findViewById(R.id.readtimestamp);
    }

    private void getDatabase(){
        // TODO: Find the reference form the database.
        database = FirebaseDatabase.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String path = "userdata/" + mAuth.getUid();  // read from the user account.
        myRef = database.getReference(path);
    }

    private void reterieveData(){
        // TODO: Get the data on a single node.
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                DataStructure ds = dataSnapshot.getValue(DataStructure.class);
                name.setText("Name: "+ ds.getName());
                temperature.setText("Temperature: "+ds.getTemperature());
                humidity.setText("Humidity: " + ds.getHumidity());
                message.setText("Message: " + ds.getMessage());

                // Convert from timestamp to Date and time
                timestamp.setText(convertTimestamp(ds.getTimestamp()));
            }

            private String convertTimestamp(String timestamp){

                long yourSeconds = Long.valueOf(timestamp);
                Date mDate = new Date(yourSeconds * 1000);
                DateFormat df = new SimpleDateFormat("dd MMM yyyy hh:mm:ss a");
                return df.format(mDate);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                DataStructure ds = dataSnapshot.getValue(DataStructure.class);
                name.setText("Name:      "+ ds.getName());
                temperature.setText("Temperature:      "+ds.getTemperature());
                humidity.setText("Humidity:      " + ds.getHumidity());
                message.setText("Message:      " + ds.getMessage());

                // Convert from timestamps to Date and time
                timestamp.setText(convertTimestamp(ds.getTimestamp()));
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // TODO: Get the whole data array on a reference.
                myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<DataStructure> arraylist= new ArrayList<DataStructure>();

                // TODO: Now data is retrieved, needs to process data.
                if (dataSnapshot != null && dataSnapshot.getValue() != null) {

                    // iterate all the items in the dataSnapshot
                    for (DataSnapshot a : dataSnapshot.getChildren()) {
                        DataStructure dataStructure = new DataStructure();
                        dataStructure.setName(a.getValue(DataStructure.class).getName());
                        dataStructure.setTemperature(a.getValue(DataStructure.class).getTemperature());
                        dataStructure.setHumidity(a.getValue(DataStructure.class).getHumidity());
                        dataStructure.setMessage(a.getValue(DataStructure.class).getMessage());
                        dataStructure.setTimestamp(a.getValue(DataStructure.class).getTimestamp());

                        arraylist.add(dataStructure);  // now all the data is in arraylist.
                        Log.d("MapleLeaf", "dataStructure " + dataStructure.getTimestamp());
                    }
                }else
                {
                    Toast.makeText(getApplicationContext(), getString(R.string.data_unavailable), Toast.LENGTH_LONG).show();
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Getting data failed, log a message
                Log.d("MapleLeaf", "Data Loading Canceled/Failed.", databaseError.toException());
            }
        });
    }



}
