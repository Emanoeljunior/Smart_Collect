package com.alvitre.SmartCollect.repository.model;

import java.io.Serializable;

public class Bin implements Serializable {

    public Long getId(){ return id; }

    public void setId(Long id){ this.id=id; }

    public String getRef() { return ref; }

    public void setRef(String email) { this.ref = ref; }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public String getConecction() {
        return conecction;
    }

    public void setConecction(String conecction){
        this.conecction = conecction;
    }


    private Long id = null;
    private String ref;
    private float volume;
    private String conecction;


}
