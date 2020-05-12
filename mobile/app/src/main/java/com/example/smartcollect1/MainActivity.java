package com.example.smartcollect1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;



public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void telaLogin(View view){

       //  db =

        //SQLiteDatabase banco = this.getReadableDatabase();

        /*SQLiteDatabase db = this.getReadableDatabase();
Cursor c = db.rawQuery("SELECT column1,column2,column3 FROM table ", null);
if (c.moveToFirst()){
    do {
        // Passing values
        String column1 = c.getString(0);
        String column2 = c.getString(1);
        String column3 = c.getString(2);
        // Do something Here with values
    } while(c.moveToNext());
}
c.close();
db.close();*/



        //if adm -> tela inicio
        //else -> tela mapa
        Intent it = new Intent(MainActivity.this, TelaInicio.class);
        startActivity(it);
    }

    public void telaCadastro1(View view){
        Intent it = new Intent(MainActivity.this, TelaCadastro.class);
        startActivity(it);
    }

}
