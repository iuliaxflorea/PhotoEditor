package com.example.photoeditor;
import androidx.annotation.NonNull;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.dsphotoeditor.sdk.activity.DsPhotoEditorActivity;
import com.dsphotoeditor.sdk.activity.DsPhotoEditorCropActivity;
import com.dsphotoeditor.sdk.utils.DsPhotoEditorConstants;


public class MainActivity extends AppCompatActivity {
    //Initialize variable
    Button btPick;
    ImageView imageView;
    Drawable drawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //conecteaza layout-ul la activitate

        //Assign variable
        btPick = findViewById(R.id.bt_pick); // gaseste butonul din layout si il asigneaza unei noi variabile "btPick"
        imageView = findViewById(R.id.image_view);
        drawable = getResources().getDrawable(R.drawable.image);
        imageView.setImageDrawable(drawable);

        btPick.setOnClickListener(new View.OnClickListener() {   //eveniment ce se declanseaza la apasarea butonului
            @Override
            public void onClick(View v) {
                //Create method - la click pe butom cere permisiunea de a accesa galeria
                checkPermission();
            }
        });
    }

    private void checkPermission(){
        //Initialize permission
        int permission = ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        //Check condition
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
           //when device version is greater than equal to version 10
           //Create method
            pickImage();
        }else {
            //when device version is below version 10
            //check condition
            if (permission != PackageManager.PERMISSION_GRANTED){
                //When permission is NOT granted
                //Request permission
                ActivityCompat.requestPermissions(MainActivity.this,
                       new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
            }else{
                //When permission is granted
                //Call method
                pickImage();

            }
        }
    }
    private void pickImage(){
        //Initialize intent
        Intent intent = new Intent(Intent.ACTION_PICK);
        //set type
        intent.setType("image/*");
        //Star activity for result
        startActivityForResult(intent,100);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
//                                           @NonNull @org.jetbrains.annotations.NotNull
                                           String[] permissions,
                                           //@NonNull @org.jetbrains.annotations.NotNull
                                                       int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //Check condition
        if(requestCode == 100 && grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
            //when permission is granted
            //call method
            pickImage();
        }else{
            //when permission is denied
            //display toast
            Toast.makeText(getApplicationContext(),"Permission Denied.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    //@Nullable @org.jetbrains.annotations.Nullable
                                            Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Check condition
        if(resultCode == RESULT_OK){
            //When result is ok
            //Initialize uri
            Uri uri = data.getData();
            switch (requestCode){
                case 100:
                    //When request code is equal to 100
                    //Initialize intent
                    Intent intent = new Intent(MainActivity.this,
                            DsPhotoEditorActivity.class);
                    //Set data
                    intent.setData(uri);
                    //Set output directory name
                    intent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY,
                            "Images");
                    //Set toolbar color
                    intent.putExtra(DsPhotoEditorConstants.DS_TOOL_BAR_BACKGROUND_COLOR,
                            Color.parseColor("#FFBB86FC"));
                    //Set background color
                    intent.putExtra(DsPhotoEditorConstants.DS_MAIN_BACKGROUND_COLOR,
                            Color.parseColor("#FFFFFF"));
                    //Hide tools
                    //Selectez functiile pe care nu doresc sa le afisez
                    intent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_TOOLS_TO_HIDE,
                            new int[]{
                            DsPhotoEditorActivity.TOOL_PIXELATE, DsPhotoEditorActivity.TOOL_CROP,
                            DsPhotoEditorActivity.TOOL_FRAME, DsPhotoEditorActivity.TOOL_DRAW,
                            DsPhotoEditorActivity.TOOL_STICKER, DsPhotoEditorActivity.TOOL_TEXT,
                            DsPhotoEditorActivity.TOOL_ROUND, DsPhotoEditorActivity.TOOL_ORIENTATION,
                            DsPhotoEditorActivity.TOOL_SHARPNESS});
                    //Start activity for result
                    startActivityForResult(intent,101);
                    break;
                case 101:
                    //When request is equal to 100
                    //Set image on image view
                    imageView.setImageURI(uri);
                    //Display toast
                    Toast.makeText(getApplicationContext(),"Photo saved",Toast.LENGTH_SHORT).show();
                    break;

            }

        }
    }
}