package com.example.smartcollect1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class TelaNavegacao extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_navegacao);
    }

    public void voltarBT(View view){
        super.onBackPressed();
    }
}
