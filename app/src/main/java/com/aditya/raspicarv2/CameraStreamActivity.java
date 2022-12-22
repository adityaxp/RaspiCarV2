package com.aditya.raspicarv2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

public class CameraStreamActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private WebView webView;
    private String cameraStreamIP;
    private Button takeSnapShotButton;
    private int STORAGE_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_stream);

        toolbar =  findViewById(R.id.streamToolBar);
        setSupportActionBar(toolbar);
        setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        webView = (WebView) findViewById(R.id.cameraStreamWebView);
        takeSnapShotButton = (Button) findViewById(R.id.takeSnapshotButton);
        cameraStreamIP = "http://IP_ADDRESS/stream.mjpg";

        if(!cameraStreamIP.equals("")){
            webView.setWebViewClient(new WebViewClient());
            WebSettings webSettings = webView.getSettings();
            //webView.setInitialScale(200);
            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
            webSettings.setBuiltInZoomControls(true);
            webSettings.setDisplayZoomControls(false);
            webSettings.getDisplayZoomControls();
            webSettings.setJavaScriptEnabled(true);
            webView.loadUrl(cameraStreamIP);

            takeSnapShotButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bitmap bitmap = getSnapshot();
                    if(ContextCompat.checkSelfPermission(CameraStreamActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED  && ContextCompat.checkSelfPermission(CameraStreamActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        saveLiveStreamSnapshot(bitmap);
                    }else {
                        requestStoragePermission();
                    }

                }
            });

        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public Bitmap getSnapshot(){
        Bitmap bitmap = Bitmap.createBitmap(webView.getWidth(), webView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        webView.draw(canvas);
        return bitmap;
    }

    private void requestStoragePermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)  && ContextCompat.checkSelfPermission(CameraStreamActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
        }else {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    public void saveLiveStreamSnapshot(Bitmap bitmap){
        FileOutputStream outputStream = null;
        File file = Environment.getExternalStorageDirectory();
        File dir = new File(file.getAbsolutePath() + "/RaspiCarV2");
        dir.mkdirs();

        String filename = String.format("%d.png",System.currentTimeMillis());
        File outFile = new File(dir,filename);
        try{
            outputStream = new FileOutputStream(outFile);
        }catch (Exception e){
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
        try{
            outputStream.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            outputStream.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Toast.makeText(this, "Snapshot Saved File Name: " + filename , Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Storage permission granted by user", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(CameraStreamActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
