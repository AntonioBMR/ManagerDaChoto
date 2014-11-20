package com.antonio.amdroid.managerdachoto;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Antonio on 18/11/2014.
 */
public class Manager  {
    private ArrayList<Jugador> jugadores;
    public Manager (){
        jugadores=new ArrayList<Jugador>();
    }
    public Manager (ArrayList<Jugador> aj){
        jugadores=aj;
    }
    public int size(){
        return jugadores.size();
    }
    public void set(int i,Jugador j){
        jugadores.set(i,j);
    }
    public void copiaArraylist(ArrayList<Jugador> aj){
        this.jugadores=aj;
    }
    public ArrayList<Jugador> pasaArraylist(){
        ArrayList<Jugador> ju=new ArrayList<Jugador>();
        for(int i=0;i<jugadores.size();i++){
            ju.add(jugadores.get(i));
        }
        return ju;
    }

    public Jugador get(int i){
        Jugador j=jugadores.get(i);
        return j;
    }
    public void remove(int i){
        jugadores.remove(i);
    }
    public void add(Jugador j){
        jugadores.add(j);
    }
    public boolean existeDorsal(int d){
        for(int i=0;i<jugadores.size();i++){
            if(jugadores.get(i).getDorsal()==d){
                return true;
            }
        }
        return false;
    }
    public boolean isEmpty(){
        if(jugadores.isEmpty())
            return true;
        return false;
    }

    public int comparaDorsal(int i, Jugador j1){
        return jugadores.get(i).compareTo(j1);
    }
    public void ordenaDorsales(){
        Collections.sort(jugadores);
    }
    public void ordenaMedias(){
        Collections.sort(jugadores, new OrdenaMedias());
    }
    public void ordenaNombres(){
        Collections.sort(jugadores,new OrdenaNombres());
    }
    public void ordenaTelefonos(){
        Collections.sort(jugadores,new OrdenaTelefonos());
    }
}
