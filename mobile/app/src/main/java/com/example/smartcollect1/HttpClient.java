package com.example.smartcollect1;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HttpClient {
    private Context context;
    private Usuario usuario = null;
    private String url = "https://smartcollect-1234.ue.r.appspot.com/api/dataUsuario/";

    public HttpClient(Context context) {
        this.context = context;
    }

    public HttpClient(Context context, Usuario usuario) {
        this.context = context;
        this.usuario = usuario;
    }

    public void postJson(JSONObject jsonBody) {
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("RQ", "Retornou do request");
                try {
                    if (response.getString("result").equals("0")) {
                        Log.d("RQ", "JSON post erro!");
                    } else {
                        if (usuario != null) {
                            UsuarioDAO dao = new UsuarioDAO(context);
                            dao.inserir(usuario);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("RQ", error.toString());
            }
        });
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(jsObjRequest);
    }

    public void getJsonList(final MainActivity main){
        JsonArrayRequest jsObjRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonKeyValue = response.getJSONObject(i);
                        Usuario usuario = new Usuario();
                        usuario.setNome(jsonKeyValue.getString("nome"));
                        usuario.setEmail(jsonKeyValue.getString("email"));
                        usuario.setSenha(jsonKeyValue.getString("senha"));
                        UsuarioDAO dao = new UsuarioDAO(context);
                        /* if(dao.getUsuario(usuario.getNome()) != null){
                            dao.alterar(usuario)
                        }else{ */
                        dao.inserir(usuario);
                        //}
                        dao.close();
                    }
                   // main.carregaLista();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Erro ao conectar no servidor", Toast.LENGTH_LONG);
            }
        });
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(jsObjRequest);
    }
}
