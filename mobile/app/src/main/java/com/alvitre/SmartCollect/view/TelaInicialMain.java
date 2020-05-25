package com.alvitre.SmartCollect.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.alvitre.SmartCollect.repository.model.Usuario;
import com.alvitre.SmartCollect.repository.model.UsuarioDAO;
import com.alvitre.SmartCollect.repository.remote.HttpClient;
import com.alvitre.SmartCollect.R;


public class TelaInicialMain extends AppCompatActivity {

    private Button submit;
    private EditText password, user;
    private ImageView ivFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user = (EditText) findViewById(R.id.edUser);
        password = (EditText) findViewById(R.id.edSenha);
        submit = (Button) findViewById(R.id.btLogin);



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(password.getText().toString().isEmpty() || user.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Preencha usuario e senha",Toast.LENGTH_SHORT).show();
                }else{

                    UsuarioDAO usuarioDAO = new UsuarioDAO(TelaInicialMain.this);
                    if((password.getText().toString()).equals((usuarioDAO.getUsuarioByUser(user.getText().toString())).getSenha())){

                        SharedPreferences sp = getSharedPreferences("login", Context.MODE_PRIVATE);
                        SharedPreferences.Editor ed = sp.edit();
                        ed.putString("user",user.getText().toString());
                        ed.apply();
                        telaLogin();
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Usuario ou senha incorretos",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void telaLogin(){
        Intent it = new Intent(TelaInicialMain.this, TelaMenuUsuario.class);
        startActivity(it);
    }

    public void telaCadastro(View view){
        Intent it = new Intent(TelaInicialMain.this, TelaCadUser.class);
        startActivity(it);
    }
    private void sincronizar(){
        HttpClient client = new HttpClient(this);
        client.getJsonList(this);
    }



}
