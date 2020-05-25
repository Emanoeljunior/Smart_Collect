package com.alvitre.SmartCollect.repository.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.alvitre.SmartCollect.view.TelaMenuUsuario;

import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO extends SQLiteOpenHelper {

    private static final int VERSAO =1;
    private static final String TABELA = "Usuario";

    public UsuarioDAO(Context context){
        super(context, TABELA, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder sb= new StringBuilder();
        sb.append("CREATE TABLE " + TABELA + " ");
        sb.append("(id INTEGER PRIMARY KEY, ");
        sb.append(" usuario TEXT UNIQUE NOT NULL, ");
        sb.append(" email TEXT, ");
        sb.append(" senha TEXT,");
        sb.append(" foto TEXT );");
        db.execSQL(sb.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        StringBuilder sb= new StringBuilder();
        sb.append("DROP TABLE IF EXISTS " + TABELA);
        db.execSQL(sb.toString());
        onCreate(db);
    }

    public void inserir(Usuario usuario){
        ContentValues values = new ContentValues();
        values.put("usuario", usuario.getUsuario());
        values.put("email", usuario.getEmail());
        values.put("senha", usuario.getSenha());
        values.put("foto", usuario.getFoto());

        getWritableDatabase().insert(TABELA, null, values);
    }

    //select
    private static final String[] COLS ={ "usuario", "email", "senha","foto"};

    public List<Usuario> getLista(){
        List<Usuario> usuarios = new ArrayList<>();

        Cursor c = getWritableDatabase().query(TABELA, COLS, null,null,null,null,null);

        while (c.moveToNext()){
            Usuario usuario = new Usuario();
            usuario.setUsuario(c.getString(c.getColumnIndex("usuario")));
            usuario.setEmail(c.getString(c.getColumnIndex("email")));
            usuario.setSenha(c.getString(c.getColumnIndex("senha")));
            usuario.setFoto(c.getString(c.getColumnIndex("foto")));

            usuarios.add(usuario);
        }
        c.close();
        return usuarios;
    }

    public Usuario getUsuarioByUser(String user){
        Cursor c = getWritableDatabase().query(TABELA,COLS,"usuario=?",new String[] {String.valueOf(user)},null,null ,null);
        Usuario usuario = new Usuario();
        if(c.moveToFirst()){
            usuario.setUsuario(c.getString(c.getColumnIndex("usuario")));
            usuario.setEmail(c.getString(c.getColumnIndex("email")));
            usuario.setSenha(c.getString(c.getColumnIndex("senha")));
            usuario.setFoto(c.getString(c.getColumnIndex("foto")));
        }
        c.close();
        return usuario;
    }

}

