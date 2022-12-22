package com.aditya.raspicarv2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GuidedControlActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Toolbar toolbar;
    private Spinner commandSpinner;
    private String commandText;
    private ListView commandList;
    private Button enterButton, startButton, stopButton, forcedStopButton;
    private ArrayList<String> commands;
    private ArrayList<CustomItems> customItemsArrayList;
    private int width = 150;
    private ArrayAdapter<String> arrayAdapter;
    private Switch aSwitch;
    private WebView webView;
    private CardView cardView;
    private DatabaseReference databaseReference;
    private String command = "";
    private String currentState = "";
    private String carState = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guided_control);
        toolbar =  findViewById(R.id.guidedControlOverInternetToolBar);
        setSupportActionBar(toolbar);
        setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        commandList = (ListView) findViewById(R.id.commandList);
        commands = new ArrayList<>();

        arrayAdapter = new ArrayAdapter<String>(GuidedControlActivity.this, android.R.layout.simple_list_item_1, commands);
        commandList.setAdapter(arrayAdapter);


        commandSpinner = (Spinner) findViewById(R.id.commandSpinner);
        customItemsArrayList = commandList();
        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(this, customItemsArrayList);


        if(commandSpinner != null){
            commandSpinner.setAdapter(customSpinnerAdapter);
            commandSpinner.setOnItemSelectedListener(GuidedControlActivity.this);
        }

        aSwitch = (Switch) findViewById(R.id.streamSwitch);
        webView = (WebView) findViewById(R.id.cameraStreamWebView);
        startButton = (Button) findViewById(R.id.startButton);
        stopButton = (Button) findViewById(R.id.forcedStopButton);
        forcedStopButton = (Button) findViewById(R.id.forcedStopButton);
        cardView = (CardView) findViewById(R.id.webStreamCardView);

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    webView.setVisibility(View.VISIBLE);
                    cardView.setVisibility(View.VISIBLE);

                    String cameraStreamIP = "http://192.168.1.3:8000/stream.mjpg";

                    if(!cameraStreamIP.equals("")) {
                        webView.setWebViewClient(new WebViewClient());
                        WebSettings webSettings = webView.getSettings();
                        //webView.setInitialScale(200);
                        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
                        webSettings.setBuiltInZoomControls(true);
                        webSettings.setDisplayZoomControls(false);
                        webSettings.getDisplayZoomControls();
                        webSettings.setJavaScriptEnabled(true);
                        webView.loadUrl(cameraStreamIP);
                    }
                }else {
                    webView.setVisibility(View.INVISIBLE);
                    cardView.setVisibility(View.INVISIBLE);
                }

            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultCarState();
            }
        });



        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(commands.size() < 2){
                    Toast.makeText(GuidedControlActivity.this, "Not Enough commands", Toast.LENGTH_SHORT).show();
                }else {
                    getCarState();
                    for (int i = 0; i < commands.size(); i++){
                        command = commands.get(i);
                        new CountDownTimer(30000, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                if(command.equals("Accelerate")){
                                    currentState = "W";
                                }else if(command.equals("Left")){
                                    currentState = "L";
                                }
                                else if(command.equals("Right")){
                                    currentState = "R";
                                }
                                else if(command.equals("Reverse")){
                                    currentState = "RV";
                                }
                                else if(command.equals("Spin")){
                                    currentState = "SP";
                                }else{
                                    defaultCarState();
                                }
                                setCarMovementState(currentState);
                            }

                            @Override
                            public void onFinish() {
                            }
                        }.start();

                    }
                }
            }
        });

        forcedStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCarState();
                FirebaseDatabase.getInstance("https://raspicarv2-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("RaspiCarV2MovementState").setValue("FS").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.i("Status: ", "Value Changed Successfully");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public ArrayList<CustomItems> commandList(){
        customItemsArrayList = new ArrayList<CustomItems>();
        customItemsArrayList.add(new CustomItems("Accelerate", R.drawable.ic_arrow_upward));
        customItemsArrayList.add(new CustomItems("Left", R.drawable.ic_arrow_left));
        customItemsArrayList.add(new CustomItems("Right", R.drawable.ic_arrow_right));
        customItemsArrayList.add(new CustomItems("Reverse", R.drawable.ic_arrow_reverse));
        customItemsArrayList.add(new CustomItems("Spin", R.drawable.ic_spin));


        return customItemsArrayList;

    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        try {
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear);
            width = linearLayout.getWidth();
        }catch (Exception e){
            e.printStackTrace();
        }

        commandSpinner.setDropDownWidth(width);
        CustomItems customItems = (CustomItems) parent.getSelectedItem();
        commandText = customItems.getSpinnerText();
        
        enterButton = (Button) findViewById(R.id.enterButton);


        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(commands.size() < 4){
                    commands.add(commandText);
                    arrayAdapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(GuidedControlActivity.this, "Command limit exceeded!", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public void getCarState(){
        databaseReference = FirebaseDatabase.getInstance("https://raspicarv2-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("RaspiCarV2MovementState");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.i("Status", snapshot.getValue().toString());
                carState = snapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(GuidedControlActivity.this, error.toString() , Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void setCarMovementState(String state){

        FirebaseDatabase.getInstance("https://raspicarv2-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("RaspiCarV2MovementState").setValue(state).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.i("Status: ", "Value Changed Successfully");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }



    public void defaultCarState(){
        FirebaseDatabase.getInstance("https://raspicarv2-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("RaspiCarV2MovementState").setValue("Still").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.i("Status: ", "Value Changed Successfully");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}