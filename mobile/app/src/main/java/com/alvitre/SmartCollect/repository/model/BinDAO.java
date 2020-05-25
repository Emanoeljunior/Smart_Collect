package com.alvitre.SmartCollect.repository.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import java.util.ArrayList;
import java.util.List;

public class BinDAO extends SQLiteOpenHelper {

    private static final int VERSAO =1;
    private static final String TABELA = "Bins";

    public BinDAO(Context context){
        super(context, TABELA, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder sb= new StringBuilder();
        sb.append("CREATE TABLE " + TABELA + " ");
        sb.append("(id INTEGER PRIMARY KEY, ");
        sb.append(" ref TEXT UNIQUE NOT NULL, ");
        sb.append(" volume TEXT, ");
        sb.append(" connection TEXT);");
        db.execSQL(sb.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        StringBuilder sb= new StringBuilder();
        sb.append("DROP TABLE IF EXISTS " + TABELA);
        db.execSQL(sb.toString());
        onCreate(db);
    }

    public void inserir(Bin bin){
        ContentValues values = new ContentValues();
        values.put("ref", bin.getRef() );
        values.put("volume", bin.getVolume());
        values.put("connection", bin.getConecction());


        getWritableDatabase().insert(TABELA, null, values);
    }

    //select
    private static final String[] COLS ={ "ref", "volume", "connection"};

    public List<Bin> getLista(){
        List<Bin> bins = new ArrayList<>();

        Cursor c = getWritableDatabase().query(TABELA, COLS, null,null,null,null,null);

        while (c.moveToNext()){
           Bin bin = new Bin();
            bin.setRef(c.getString(c.getColumnIndex("ref")));
            bin.setVolume(c.getFloat(c.getColumnIndex("volume")));
            bin.setConecction(c.getString(c.getColumnIndex("connection")));


            bins.add(bin);
        }
        c.close();
        return bins;
    }

    public Bin getBinByRef(String ref){
        Cursor c = getWritableDatabase().query(TABELA,COLS,"ref=?",new String[] {String.valueOf(ref)},null,null ,null);
        Bin bin = new Bin();
        if(c.moveToFirst()){
            bin.setRef(c.getString(c.getColumnIndex("ref")));
            bin.setVolume(c.getFloat(c.getColumnIndex("volume")));
            bin.setConecction(c.getString(c.getColumnIndex("conneciton")));
        }
        c.close();
        return bin;
    }

}

