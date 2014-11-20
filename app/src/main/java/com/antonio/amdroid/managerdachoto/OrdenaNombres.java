package com.antonio.amdroid.managerdachoto;

import java.util.Comparator;

/**
 * Created by Antonio on 18/11/2014.
 */

public class OrdenaNombres implements Comparator<Jugador> {

    @Override
    public int compare(Jugador j1, Jugador j2) {
        if(j1.getNombre().compareTo(j2.getNombre())>0)
            return 1;
        if(j1.getNombre().compareTo(j2.getNombre())<0)
            return -1;
        return 0;
    }
}
