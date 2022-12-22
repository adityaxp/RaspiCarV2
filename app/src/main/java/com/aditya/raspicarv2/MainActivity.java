package com.aditya.raspicarv2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<MenuItemData> menuItemDataArrayList;
    private MenuItemsAdapter menuItemsAdapter;
    private MenuItemData menuItemData;
    private RecyclerView menuItemRecyclerView;
    private DatabaseReference databaseReference;
    private TextView carStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        toolbar =  findViewById(R.id.raspiToolBar);
        setSupportActionBar(toolbar);
        setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        menuItemDataArrayList = new ArrayList<MenuItemData>();
        menuItemRecyclerView = (RecyclerView) findViewById(R.id.menuItemRecyclerView);
        carStateTextView = (TextView) findViewById(R.id.carStateTextView);
        layoutManager = new LinearLayoutManager(this);
        menuItemData = new MenuItemData("Camera Stream", R.drawable.camera);
        menuItemDataArrayList.add(menuItemData);

        menuItemData = new MenuItemData("Manual Control Over Internet", R.drawable.fingercontrol);
        menuItemDataArrayList.add(menuItemData);

        menuItemData = new MenuItemData("Guided Control Over Internet", R.drawable.signpost);
        menuItemDataArrayList.add(menuItemData);

        menuItemData = new MenuItemData("Realtime Logs and Sensor Data", R.drawable.logfile);
        menuItemDataArrayList.add(menuItemData);

        menuItemsAdapter =  new MenuItemsAdapter(this, menuItemDataArrayList);

        menuItemRecyclerView.setLayoutManager(layoutManager);
        menuItemRecyclerView.setAdapter(menuItemsAdapter);

        getCarState();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void getCarState() {


        databaseReference = FirebaseDatabase.getInstance("https://raspicarv2-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("RaspiCarV2State");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.i("Status", snapshot.getValue().toString());
                if(snapshot.getValue().toString().equals("offline")){
                    carStateTextView.setTextColor(Color.rgb(255,0,0));
                }else{
                    carStateTextView.setTextColor(Color.rgb(0,255,0));
                }
                carStateTextView.setText(carStateTextView.getText() + snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this,error.toString() , Toast.LENGTH_SHORT).show();

            }
        });



    }
}
