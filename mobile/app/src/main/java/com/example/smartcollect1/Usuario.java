package com.example.smartcollect1;

import org.json.JSONException;
import org.json.JSONObject;

public class Usuario {

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    private String nome;
    private String email;
    private String senha;


    public JSONObject toJson() {
        JSONObject usuario = new JSONObject();

        try {
            usuario.put("nome", getNome());
            usuario.put("email", getEmail());
            usuario.put("senha", getSenha());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return usuario;
    }

}
