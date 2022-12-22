package com.aditya.raspicarv2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
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

public class ManualControl extends AppCompatActivity {

    private Toolbar toolbar;
    private Switch aSwitch, bSwitch;
    private WebView webView;
    private CardView cardView;
    private Button upwardButton, reverseButton, leftButton, rightButton, spinButton, forceStopButton, stopButton, setAngleButton, honkButton;
    private DatabaseReference databaseReference;
    private EditText logsEditText, servoAngleEditText;
    private String carState = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_control);

        toolbar =  findViewById(R.id.manualControlToolBar);
        setSupportActionBar(toolbar);
        setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        aSwitch = (Switch) findViewById(R.id.streamSwitch);
        bSwitch = (Switch) findViewById(R.id.logsSwitch);
        webView = (WebView) findViewById(R.id.cameraStreamWebView);
        cardView = (CardView) findViewById(R.id.webStreamCardView);
        upwardButton = (Button) findViewById(R.id.upwardButton);
        reverseButton = (Button) findViewById(R.id.reverseButton);
        leftButton = (Button) findViewById(R.id.leftButton);
        rightButton = (Button) findViewById(R.id.rightButton);
        stopButton = (Button) findViewById(R.id.stopButton);
        spinButton = (Button) findViewById(R.id.spinButton);
        logsEditText = (EditText) findViewById(R.id.logsEditText);
        forceStopButton = (Button) findViewById(R.id.forceStopButton);
        servoAngleEditText = (EditText) findViewById(R.id.servoAngleEditText);
        setAngleButton = (Button) findViewById(R.id.setAngleButton);
        honkButton = (Button) findViewById(R.id.honkButton);



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

        bSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    logsEditText.setVisibility(View.VISIBLE);
                }else{
                    logsEditText.setVisibility(View.INVISIBLE);
                    getLogsAndSensorData();
                }
            }
        });

        setAngleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance("https://raspicarv2-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("RaspiCarV2ServoAngle").setValue(servoAngleEditText.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
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

        upwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCarState();
                getLogsAndSensorData();
                if(carState.equals("Still")){
                    FirebaseDatabase.getInstance("https://raspicarv2-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("RaspiCarV2MovementState").setValue("W").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.i("Status: ", "Value Changed Successfully");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                }else{
                    //defaultCarState();
                    Toast.makeText(ManualControl.this, "Error: Car Moving Stopping Car Now!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        reverseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCarState();
                getLogsAndSensorData();
                if(carState.equals("Still")){
                    FirebaseDatabase.getInstance("https://raspicarv2-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("RaspiCarV2MovementState").setValue("RV").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.i("Status: ", "Value Changed Successfully");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                }else{
                    //defaultCarState();
                    Toast.makeText(ManualControl.this, "Error: Car Moving Stopping Car Now!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCarState();
                getLogsAndSensorData();
                if(carState.equals("Still")){
                    FirebaseDatabase.getInstance("https://raspicarv2-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("RaspiCarV2MovementState").setValue("L").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.i("Status: ", "Value Changed Successfully");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                }else{
                    //defaultCarState();
                    Toast.makeText(ManualControl.this, "Error: Car Moving Stopping Car Now!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCarState();
                getLogsAndSensorData();
                if(carState.equals("Still")){
                    FirebaseDatabase.getInstance("https://raspicarv2-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("RaspiCarV2MovementState").setValue("R").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.i("Status: ", "Value Changed Successfully");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                }else{
                    //defaultCarState();
                    Toast.makeText(ManualControl.this, "Error: Car Moving Stopping Car Now!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultCarState();
                getLogsAndSensorData();
            }
        });

        spinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCarState();
                getLogsAndSensorData();
                if(carState.equals("Still")){
                    FirebaseDatabase.getInstance("https://raspicarv2-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("RaspiCarV2MovementState").setValue("SP").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.i("Status: ", "Value Changed Successfully");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                }else{
                    //defaultCarState();
                    Toast.makeText(ManualControl.this, "Error: Car Moving Stopping Car Now!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        honkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance("https://raspicarv2-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("RaspiCarV2BuzzerState").setValue(1).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
            }
        });

        forceStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCarState();
                getLogsAndSensorData();
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
                Toast.makeText(ManualControl.this,error.toString() , Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void getLogsAndSensorData(){
        databaseReference = FirebaseDatabase.getInstance("https://raspicarv2-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("RaspiCarV2LogsAndSensorData");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                logsEditText.setText("> "+logsEditText.getText().toString() + "\n" + snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ManualControl.this,error.toString() , Toast.LENGTH_SHORT).show();

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