package com.antonio.amdroid.managerdachoto;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class Main extends Activity implements AdapterView.OnItemLongClickListener {
    private ArrayList<Jugador> datos = new ArrayList();
    private AdaptadorArrayList ad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        datos.add(new Jugador("Antonio", "667867249", 10));
        datos.add(new Jugador("Javier", "665444444", 9));
        datos.add(new Jugador("Roberto", "667867249", 7));
        datos.add(new Jugador("Ismael", "667867249", 7));
        datos.add(new Jugador("Miguel", "667867249", 6));
        datos.add(new Jugador("Bili", "667867249", 4));
        datos.add(new Jugador("Paco", "667867249", 1));
        datos.add(new Jugador("Diego", "667867249", 8));
        datos.add(new Jugador("Manuel", "667867249", 4));
        datos.add(new Jugador("Joaquin", "667867243",4));
        ad = new AdaptadorArrayList(this, R.layout.lista_detalle, datos);
        ListView lv = (ListView) findViewById(R.id.lvLista);
        lv.setAdapter(ad);
        lv.setOnItemLongClickListener(this);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.contextual, menu);
    }
    public void setEquipo(Boolean b){
        for(int i=0;i<datos.size();i++){
            Jugador  j=new Jugador();
            j=datos.get(i);
            j.setEquip(b);
            this.datos.set(i,j);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.anadir) {
            agregar();
            setEquipo(true);
            return true;
        }
        if (id == R.id.hazte) {
            if(hazte()){
                setEquipo(false);
                return true;
            }
            tostada("Estais impares no podeis jugar" );
            return false;
        }
        return super.onOptionsItemSelected(item);
    }
    public boolean hazte() {
        if(datos.size()%2==0){
             ordenarInsercion(datos);
            setEquipo(false);
            AdaptadorArrayList ad = new AdaptadorArrayList(Main.this, R.layout.lista_detalle, datos);
            ListView lv = (ListView) findViewById(R.id.lvLista);
            lv.setAdapter(ad);
            setEquipo(true);
            return true;
        }
        return false;
        }

    public boolean borrar(final int pos){
        setEquipo(true);
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(Main.this);
        dialogo1.setTitle("Importante");
        dialogo1.setMessage("¿ Desea borrar el jugador seleccionado ?");
        dialogo1.setCancelable(false);
        dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                datos.remove(pos);
                ad.notifyDataSetChanged();
                AdaptadorArrayList ad = new AdaptadorArrayList(Main.this, R.layout.lista_detalle, datos);
                ListView lv = (ListView) findViewById(R.id.lvLista);
                lv.setAdapter(ad);
                tostada("Jugador borrado");
            }
        });
        dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                finish();
            }
        });
        dialogo1.show();
        return true;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        setEquipo(true);
        String[] opc = new String[]{"Borrar", "Modificar"};
        final int posicion = position;
        AlertDialog opciones = new AlertDialog.Builder(
                Main.this)
                .setTitle("Opciones")
                .setItems(opc,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int selected) {
                                if (selected == 0) {
                                    borrar(posicion);
                                } else if (selected == 1) {
                                    editar(position);
                                }
                            }
                        }).create();
        opciones.show();
        return true;
    }

    public boolean agregar() {
        setEquipo(true);
        //cargamos vista
        LayoutInflater inflater = LayoutInflater.from(this);
        final View vista = inflater.inflate(R.layout.dialogo_alta, null);
        final EditText  et,et1, et2;
        et = (EditText) vista.findViewById(R.id.etA);
        et1 = (EditText) vista.findViewById(R.id.et1A);
        et2 = (EditText) vista.findViewById(R.id.et2A);
        et.setHint("Introduzca media");
        et1.setHint("Introduzca nombre");
        et2.setHint("Introduzca teléfono");
        et.setFilters(new InputFilter[]{new InputFilterMinMax("1", "10")});
        //dialogo
        final AlertDialog d = new AlertDialog.Builder(this)
                .setView(vista)
                .setTitle("Añadir jugador")
                .setPositiveButton(android.R.string.ok, null) //Set to null. We override the onclick
                .setNegativeButton(android.R.string.cancel, null)
                .create();
        d.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button b = d.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        //filtros
                        if(esTelefono(et2.getText().toString())&&isNumeric(et.getText().toString())&&
                                et1.getText().toString().length() > 0 && et2.getText().toString().length()> 0
                                &&et.getText().toString().length() > 0 && et.getText().toString().length()<=2
                                && et2.getText().length() < 10) {
                            int med = 0;
                            Jugador j = new Jugador();
                            try {
                                med = Integer.parseInt(et.getText().toString());
                            } catch (Exception e) {
                            }
                            j.setMedia(med);
                            j.setNombre(et1.getText().toString());
                            j.setTelefono(et2.getText().toString());
//                      añadimos jugados y mostramos
                            datos.add( j);
                            ad.notifyDataSetChanged();
                            AdaptadorArrayList ad = new AdaptadorArrayList(Main.this, R.layout.lista_detalle, datos);
                            ListView lv = (ListView) findViewById(R.id.lvLista);
                            lv.setAdapter(ad);
                            tostada("Jugador añadido");
                            d.dismiss();
                        }
                        // Filtramos que nos este vacios
                        if(et1.getText().toString().length() == 0 ){
                            tostada("¡Introduzca nombre!");
                        }
                        if(et2.getText().toString().length() == 0 ){
                            tostada("¡Introduzca teléfono!");
                        }
                        if(et.getText().toString().length() == 0 ){
                            tostada("¡Introduzca media!");
                        }
                        if(!esTelefono(et2.getText().toString())){
                            tostada("¡Teléfono incorrecto!");
                        }
                    }
                });
            }
        });
        d.show();
        AdaptadorArrayList ad = new AdaptadorArrayList(Main.this, R.layout.lista_detalle, datos);
        ListView lv = (ListView) findViewById(R.id.lvLista);
        lv.setAdapter(ad);
        return true;
    }

    private boolean editar(final int index) {
        setEquipo(true);
        //cargamos jugador
        Jugador g=new Jugador();
        g=datos.get(index);
        //cargamos vista
        LayoutInflater inflater = LayoutInflater.from(this);
        final View vista = inflater.inflate(R.layout.dialogo_alta, null);
        final EditText  et,et1, et2;
        String hmed,hnom,htel;
        hmed=g.getMedia()+"";
        hnom=g.getNombre();
        htel=g.getTelefono();
        et = (EditText) vista.findViewById(R.id.etA);
        et1 = (EditText) vista.findViewById(R.id.et1A);
        et2 = (EditText) vista.findViewById(R.id.et2A);
        et.setText(hmed);
        et1.setText(hnom);
        et2.setText(htel);
        et.setFilters(new InputFilter[]{new InputFilterMinMax("1", "10")});
        //dialogo
        final AlertDialog d = new AlertDialog.Builder(this)
                .setView(vista)
                .setTitle("Modificar jugador")
                .setPositiveButton(android.R.string.ok, null) //Set to null. We override the onclick
                .setNegativeButton(android.R.string.cancel, null)
                .create();
        d.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button b = d.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        //filtros
                        if(esTelefono(et2.getText().toString())&&isNumeric(et.getText().toString())&&
                                et1.getText().toString().length() > 0 && et2.getText().toString().length()> 0
                                &&et.getText().toString().length() > 0 && et.getText().toString().length()<=2
                                && et2.getText().length() < 10) {
                            int med = 0;
                            Jugador j = new Jugador();
                            try {
                                med = Integer.parseInt(et.getText().toString());
                            } catch (Exception e) {
                            }
                            j.setMedia(med);
                            j.setNombre(et1.getText().toString());
                            j.setTelefono(et2.getText().toString());
//                      modificamos jugados y mostramos
                            datos.set(index, j);
                            ad.notifyDataSetChanged();
                            AdaptadorArrayList ad = new AdaptadorArrayList(Main.this, R.layout.lista_detalle, datos);
                            ListView lv = (ListView) findViewById(R.id.lvLista);
                            lv.setAdapter(ad);
                            tostada("Elemento modificado");
                            d.dismiss();
                        } // Filtramos que nos este vacios
                        if(et1.getText().toString().length() == 0 ){
                            tostada("¡Introduzca nombre!");
                        }
                        if(et2.getText().toString().length() == 0 ){
                            tostada("¡Introduzca teléfono!");
                        }
                        if(et.getText().toString().length() == 0 ){
                            tostada("¡Introduzca media!");
                        }
                        if(!esTelefono(et2.getText().toString())){
                            tostada("¡Teléfono incorrecto!");
                        }
                    }
                });
            }
        });
        d.show();
        AdaptadorArrayList ad = new AdaptadorArrayList(Main.this, R.layout.lista_detalle, datos);
        ListView lv = (ListView) findViewById(R.id.lvLista);
        lv.setAdapter(ad);
        return true;
    }

    public Toast tostada(String t) {
        Toast toast =
                Toast.makeText(getApplicationContext(),
                        t + "", Toast.LENGTH_SHORT);
         toast.show();
        return toast;
    }
    private static boolean isNumeric(String cadena){
        try {
            Integer.parseInt(cadena);
            return true;
        } catch (NumberFormatException nfe){
            return false;
        }
    }
    private static boolean esTelefono(String cadena){
        try {
            int num=Integer.parseInt(cadena);
            if(num<600000000||(num>799999999&&num<900000000)||num>999999999)
                return false;

            return true;
        } catch (NumberFormatException nfe){
            return false;
        }
    }
    public void ordenarInsercion(ArrayList<Jugador>l){
        Jugador aux;
        for (int i = 1; i < l.size(); i++) {
            aux =l.get(i);
            for (int j = i-1; j >=0 && l.get(j).getMedia()>aux.getMedia(); j--) {
                l.set((j + 1), l.get(j));
                l.set((j),aux);
            }
        }
    }

    }






