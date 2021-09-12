package com.pms.cloudinaryandroidupdate;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Upload ###";

    private static int IMAGE_REQ=1;
    private Uri imagePath;
    Map config = new HashMap();

    private ImageView imageView;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView=findViewById(R.id.imageView);
        button=findViewById(R.id.button);

        initCongif();





        /*
        * 1. user want to click the imageview
        * 2. select the image/pdg/video file gallery
        * 3. show the preview of the image in imageview
        * 4. click the upload button to upload image to the cloudinary
        *
        * */

        imageView.setOnClickListener(v -> {
            /*
            * 1.1. ask the user to give the media permission
            * 1.2. moving to the gallery
            *
            * */

            //1.1
            requestPermission();
            Log.d(TAG, ": "+"request permission");
        });

        button.setOnClickListener(v -> {

            Log.d(TAG, ": "+" button clicked");

            MediaManager.get().upload(imagePath).callback(new UploadCallback() {
                @Override
                public void onStart(String requestId) {
                    Log.d(TAG, "onStart: "+"started");
                }

                @Override
                public void onProgress(String requestId, long bytes, long totalBytes) {
                    Log.d(TAG, "onStart: "+"uploading");
                }

                @Override
                public void onSuccess(String requestId, Map resultData) {
                    Log.d(TAG, "onStart: "+"usuccess");
                }

                @Override
                public void onError(String requestId, ErrorInfo error) {
                    Log.d(TAG, "onStart: "+error);
                }

                @Override
                public void onReschedule(String requestId, ErrorInfo error) {
                    Log.d(TAG, "onStart: "+error);
                }
            }).dispatch();
        });



    }

    private void initCongif() {

        config.put("cloud_name", "dyp8ikyqu");
        config.put("api_key","141177131558868");
        config.put("api_secret","yi-CT6D5awMVZWGQ1IZUmXzweOY");
//        config.put("secure", true);
        MediaManager.init(this, config);
    }

    private void requestPermission() {
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
        == PackageManager.PERMISSION_GRANTED)
        {
            selectImage();
        }else
        {
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE
            },IMAGE_REQ);
        }

    }

    /*
    * sele the image from the gallery
    * */
    private void selectImage() {
        Intent intent=new Intent();
        intent.setType("image/*");// if you want to you can use pdf/gif/video
        intent.setAction(Intent.ACTION_GET_CONTENT);
        someActivityResultLauncher.launch(intent);

    }



    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                       imagePath=data.getData();
                        Picasso.get().load(imagePath).into(imageView);

                    }
                }
            });



}