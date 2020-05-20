package com.example.smartcollect1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.MapView;

public class TelaNavegacao extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_navegacao);

        MapView mapView = (MapView) findViewById(R.id.mapView);

    }


}
