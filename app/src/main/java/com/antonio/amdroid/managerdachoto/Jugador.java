package com.antonio.amdroid.managerdachoto;

/**
 * Created by Antonio on 06/10/2014.
 */

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.widget.ImageView;

import java.io.Serializable;
import java.util.ArrayList;


public class Jugador {
    private String nombre, telefono;
    private int media;
    private boolean vistaF;
    public Jugador(){
        this.vistaF=true;
    }
    public Jugador(String nom, String tlf, int m){
        this.nombre=nom;
        this.telefono=tlf;
        this.media=m;
        this.vistaF=true;
    }
    public boolean getEquip(){
        return vistaF;
    }
    public void setEquip(Boolean b){
        vistaF=b;
    }
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    public int getMedia() {
        return media;
    }

    public boolean setMedia(int media) {
        if(media>0 && media<=10){
            this.media = media;
            return true;
        }
        return false;
    }


}
