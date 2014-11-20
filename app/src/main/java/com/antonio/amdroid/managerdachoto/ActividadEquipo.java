package com.antonio.amdroid.managerdachoto;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;


public class ActividadEquipo extends Activity {
    private Button bt1, bt2, btok, btNo;
    private Manager jugadores = new Manager();

    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        this.setContentView(R.layout.equipos);

        bt1 = (Button) this.findViewById(R.id.button1);
        bt2 = (Button) this.findViewById(R.id.button2);
        ArrayList<Jugador> js = this.getIntent().getParcelableArrayListExtra("jugadores");
        jugadores.copiaArraylist(js);
        if (!jugadores.isEmpty()) {
            int num = jugadores.size();
            if (num % 2 == 0 && num <= 22 && num >= 6) {
                bt1.setVisibility(View.VISIBLE);
                bt2.setVisibility(View.VISIBLE);
                String eq1 = "";
                String eq2 = "";
                for (int i = 0; i < jugadores.size() - 2; i++) {
                    if (i % 2 == 0) {
                        eq1 += jugadores.get(i).getNombre().toString() + " " + jugadores.get(i).getDorsal() + "\n";
                    } else {
                        eq2 += jugadores.get(i).getNombre().toString() + " " + jugadores.get(i).getDorsal() + "\n";
                    }
                }
                //compensa las medias
                eq1 += jugadores.get(jugadores.size() - 1).getNombre().toString() + " " + jugadores.get(jugadores.size() - 1).getDorsal() + "\n";
                eq2 += jugadores.get(jugadores.size() - 2).getNombre().toString() + " " + jugadores.get(jugadores.size() - 2).getDorsal() + "\n";
                bt1.setBackgroundResource(R.drawable.madrid);
                bt1.setText(eq1);
                bt2.setBackgroundResource(R.drawable.barca);
                bt2.setText(eq2);
            } else {
                tostada("no se pudo hacer equipo");
            }

        }
        btok = (Button) this.findViewById(R.id.buttonOk);
        btNo = (Button) this.findViewById(R.id.buttonNo);
        btok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ArrayList<Jugador> list;
                list = jugadores.pasaArraylist();
                String eq1 = "";
                String eq2 = "";
                int med1 = 0;
                int cont1 = 0;
                int cont2 = 0;
                int med2 = 0;
                for (int i = 0; i < jugadores.size() - 2; i++) {
                    if (i % 2 == 0) {
                        med1 = med1 + jugadores.get(i).getMedia();
                        cont1++;
                        eq1 = eq1 + jugadores.get(i).getNombre() + " ";
                    } else {
                        med2 = med2 + jugadores.get(i).getMedia();
                        cont2++;
                        eq2 = eq2 + jugadores.get(i).getNombre() + " ";
                    }
                }
                med1 = med1 + jugadores.get(jugadores.size() - 1).getMedia();
                eq1 = eq1 + jugadores.get(jugadores.size() - 1).getNombre() + "";
                cont1++;
                med2 = med2 + jugadores.get(jugadores.size() - 2).getMedia();
                eq2 = eq2 + jugadores.get(jugadores.size() - 2).getNombre();
                cont2++;
                med1 = med1 / cont1;
                med2 = med2 / cont2;
                String mensaje = "Partido entre:\n " + eq1 + " media total de " + med1 + "\n y " + eq2 + " media total de " + med2;
                Intent i = new Intent(ActividadEquipo.this, ActividadEquipo.class);
                i.putExtra("text", mensaje);
                i.putParcelableArrayListExtra("jugadores", list);
                setResult(Activity.RESULT_OK, i);
                ActividadEquipo.this.finish();
            }

        });
        btNo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ArrayList<Jugador> js = jugadores.pasaArraylist();
                Intent i = new Intent(ActividadEquipo.this, ActividadEquipo.class);
                i.putParcelableArrayListExtra("jugadores", js);
                setResult(Activity.RESULT_CANCELED, i);
                ActividadEquipo.this.finish();
            }

        });


    }


    public Toast tostada(String t) {
        Toast toast =
                Toast.makeText(getApplicationContext(),
                        t + "", Toast.LENGTH_SHORT);
        toast.show();
        return toast;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ArrayList<Jugador> js = jugadores.pasaArraylist();
        outState.putParcelableArrayList("Jugadores", js);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        ArrayList<Jugador> js = savedInstanceState.getParcelableArrayList("Jugadores");
        jugadores.copiaArraylist(js);
        if (!jugadores.isEmpty()) {
            int num = jugadores.size();
            if (num % 2 == 0 && num <= 22 && num >= 6) {
                bt1.setVisibility(View.VISIBLE);
                bt2.setVisibility(View.VISIBLE);
                String eq1 = "";
                String eq2 = "";
                for (int i = 0; i < jugadores.size() - 2; i++) {
                    if (i % 2 == 0) {
                        eq1 += jugadores.get(i).getNombre().toString() + " " + jugadores.get(i).getDorsal() + "\n";
                    } else {
                        eq2 += jugadores.get(i).getNombre().toString() + " " + jugadores.get(i).getDorsal() + "\n";
                    }
                }
                //compensa las medias
                eq1 += jugadores.get(jugadores.size() - 1).getNombre().toString() + " " + jugadores.get(jugadores.size() - 1).getDorsal() + "\n";
                eq2 += jugadores.get(jugadores.size() - 2).getNombre().toString() + " " + jugadores.get(jugadores.size() - 2).getDorsal() + "\n";
                bt1.setBackgroundResource(R.drawable.madrid);
                bt1.setText(eq1);
                bt2.setBackgroundResource(R.drawable.barca);
                bt2.setText(eq2);
            } else {
                tostada("no se pudo hacer equipo");
            }
        }
    }
}