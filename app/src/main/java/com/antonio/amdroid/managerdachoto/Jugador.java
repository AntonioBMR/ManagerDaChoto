package com.antonio.amdroid.managerdachoto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Antonio on 06/10/2014.
 */

public class Jugador implements Parcelable,Comparable<Jugador> {
    private String nombre, telefono;
    private int media, dorsal;
    public static final Parcelable.Creator<Jugador> CREATOR =
            new Parcelable.Creator<Jugador>() {
                @Override
                public Jugador createFromParcel(Parcel source) {
                    return new Jugador(source);
                }

                @Override
                public Jugador[] newArray(int size) {
                    return new Jugador[size];
                }
            };

    public Jugador(int d,String nom, String tlf, int m) {
        this.dorsal = d;
        this.nombre = nom;
        this.telefono = tlf;
        this.media = m;
    }
    public Jugador(Parcel prc) {

        this.dorsal = prc.readInt();
        this.nombre = prc.readString();
        this.telefono = prc.readString();
        this.media = prc.readInt();
    }

    public Jugador() {

    }

    public int getDorsal() {
        return this.dorsal;
    }

    public void setDorsal(int dorsal) {
        this.dorsal = dorsal;
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
        if (media > 0 && media <= 10) {
            this.media = media;
            return true;
        }
        return false;
    }


    @Override
    public int compareTo(Jugador another) {
        if (this.dorsal > another.dorsal) {
            return 1;
        } else if (this.dorsal < another.dorsal) {
            return -1;
        }
        return 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(dorsal);
        dest.writeString(nombre);
        dest.writeString(telefono);
        dest.writeInt(media);
    }

    @Override
    public String toString() {
        return "Jugador{" +
                "media=" + media +
                ", dorsal=" + dorsal +
                ", telefono='" + telefono + '\'' +
                ", nombre='" + nombre + '\'' +
                '}';
    }
    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null) return false;
        if(!(obj instanceof Jugador)) return false;

        Jugador other = (Jugador) obj;

        if(dorsal == 0){
            if(other.dorsal != 0) return false;
        }
        else return dorsal==(other.dorsal);

        return true;
    }
    @Override
    public int hashCode() {
        return dorsal;
    }
}


