package com.example.smartcollect1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.renderscript.RenderScript;

import androidx.annotation.Nullable;

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
        sb.append("(nome TEXT UNIQUE NOT NULL, ");
        sb.append(" email TEXT, ");
        sb.append(" senha TEXT);");
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
        values.put("nome", usuario.getNome());
        values.put("email", usuario.getEmail());
        values.put("senha", usuario.getSenha());

        getWritableDatabase().insert(TABELA, null, values);
    }


    //select
    private static final String[] COLS ={ "nome", "email", "senha"};

    public List<Usuario> getLista(){
        List<Usuario> usuarios = new ArrayList<>();

        Cursor c = getWritableDatabase().query(TABELA, COLS, null,null,null,null,null);

        while (c.moveToNext()){
            Usuario usuario = new Usuario();
            usuario.setNome(c.getString(0));
            usuario.setEmail(c.getString(1));
            usuario.setSenha(c.getString(2));

            usuarios.add(usuario);
        }
        c.close();
        return usuarios;
    }

    public Usuario getUsuario(String nome){
        Cursor c = getWritableDatabase().query(TABELA,COLS,"nome=?",new String[] {String.valueOf(nome)}, null,null,null);

       // if (c != null) {
       //     c.moveToFirst();
       //     if (c.getInt (0) == 0) {
        //        return 0;  //vazia
       //     } else {
        //        return 1; //possui info
         //   }
       // }

        Usuario usuario = new Usuario();
        usuario.setNome(c.getString(0));
        usuario.setEmail(c.getString(1));
        usuario.setSenha(c.getString(2));

        c.close();

        return usuario;
    }

}

