package com.example.smartcollect1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class TelaInicio extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicio);
    }


    public void telaCadLixeira(View view){
        Intent it = new Intent(TelaInicio.this, TelaCadLixeira.class);
        startActivity(it);
    }

    public void telaNavegar(View view){
        Intent it = new Intent(TelaInicio.this, TelaNavegacao.class);
        startActivity(it);
    }

}
