package com.aditya.raspicarv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class SplashScreen extends AppCompatActivity {

    private ImageView raspiLogoImageView;
    private int lastPosition = -1;
    private Intent mainActivityIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

//        mainActivityIntent = new Intent(SplashScreen.this, MainActivity.class);
//        startActivity(mainActivityIntent);
//        raspiLogoImageView = (ImageView) findViewById(R.id.raspiLogoImageView);
//        setAnimation(raspiLogoImageView, 1);

         //Dumb Code
        new CountDownTimer(2500, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                mainActivityIntent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(mainActivityIntent);
                finish();
            }
        }.start();



    }

    public void setAnimation(View view, int position){
        if(position > lastPosition){
            ScaleAnimation scaleAnimation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            scaleAnimation.setDuration(1500);
            view.startAnimation(scaleAnimation);
            lastPosition = position;
        }

    }
}