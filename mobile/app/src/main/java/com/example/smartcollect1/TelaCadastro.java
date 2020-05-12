package com.example.smartcollect1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.view.View;

import static android.provider.MediaStore.*;


public class TelaCadastro extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;



    private ImageView ivFoto;
    private com.google.android.material.floatingactionbutton.FloatingActionButton tirafoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastro);
        ivFoto = (ImageView) findViewById(R.id.ivFoto);
        tirafoto = (com.google.android.material.floatingactionbutton.FloatingActionButton) findViewById(R.id.ftTirafoto);
        tirafoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
    }

    protected void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ivFoto.setImageBitmap(imageBitmap);
        }
    }



}
