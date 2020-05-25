package com.alvitre.SmartCollect.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.alvitre.SmartCollect.R;
import com.alvitre.SmartCollect.repository.model.Usuario;
import com.alvitre.SmartCollect.repository.model.UsuarioDAO;

import java.io.File;

public class TelaMenuUsuario extends AppCompatActivity {

    private Button sair;
    private ImageView ivmFoto;
    private String user, photoFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_menu);
        ivmFoto = (ImageView) findViewById(R.id.ivLogin);
        sair = (Button) findViewById(R.id.btSair);

        SharedPreferences sp = getSharedPreferences("login", Context.MODE_PRIVATE);
        user = sp.getString("user","not found");
       // Toast.makeText(getApplicationContext(),"Está logado como - " + user,
         //       Toast.LENGTH_SHORT).show();

        UsuarioDAO usuarioDAO = new UsuarioDAO(TelaMenuUsuario.this);
        Usuario usuario = usuarioDAO.getUsuarioByUser(user);

        photoFileName = getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString()+ File.separator + usuario.getFoto();
        Bitmap bitmap = BitmapFactory.decodeFile(photoFileName);
        Log.d("Foto recuperar",photoFileName);
        showImage();

        sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    public void showImage( ){
        int targetW = ivmFoto.getWidth();
        int targetH = ivmFoto.getHeight();

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoFileName,bmOptions);


        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = 4;

        Bitmap bitmap = BitmapFactory.decodeFile(photoFileName, bmOptions);
        ivmFoto.setImageBitmap(bitmap);
    }

    public void telaCadLixeira(View view){
        Intent it = new Intent(TelaMenuUsuario.this, TelaCadLixeira.class);
        startActivity(it);
    }

    public void telaNavegar(View view){
        Intent it = new Intent(TelaMenuUsuario.this, TelaNavegacao.class);
        startActivity(it);
    }

    public void telaMonitorar(View view){
        Intent it = new Intent(TelaMenuUsuario.this, TelaMonitorar.class);
        startActivity(it);

    }
    public void telaGerenciarUsuario(View view){
        Intent it = new Intent(TelaMenuUsuario.this, TelaGerenciarUsuario.class);
        startActivity(it);

    }

}
