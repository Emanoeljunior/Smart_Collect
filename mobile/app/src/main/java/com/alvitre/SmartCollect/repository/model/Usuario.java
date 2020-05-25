package com.alvitre.SmartCollect.repository.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Usuario implements Serializable {

    public Long getId(){ return id; }

    public void setId(Long id){ this.id=id; }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
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

    public String  getFoto(){return foto;}

    public void setFoto(String foto){this.foto = foto;}

    private Long id = null;
    private String usuario;
    private String email;
    private String senha;
    private String foto;


    public JSONObject toJson() {
        JSONObject usuario = new JSONObject();

        try {
            usuario.put("nome", getUsuario());
            usuario.put("email", getEmail());
            usuario.put("senha", getSenha());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return usuario;
    }

}
