package com.antonio.amdroid.managerdachoto;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Xml;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

/*puntos que me queda:
--poner textos en strings.xml

-La clase tiene que implementear serializable y comparable, y debe sobreescribir el metodo equals/hascode

*/
public class Main extends Activity implements AdapterView.OnItemLongClickListener {
    private static final int REQUEST_TEXT = 0;
    private static final int ACTIVIDAD_SEGUNDA=1;
    private Manager jugadores= new Manager();
    private AdaptadorArrayList ad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ArrayList<Jugador> datos=new ArrayList<Jugador>();
        datos=leer();
        jugadores.copiaArraylist(datos);
        ad = new AdaptadorArrayList(this, R.layout.lista_detalle, datos);
        ListView lv = (ListView) findViewById(R.id.lvLista);
        lv.setAdapter(ad);
        lv.setOnItemLongClickListener(this);
        TextView tv=(TextView)findViewById(R.id.tvListaEquipos);
        tv.setVisibility(View.INVISIBLE);
        tv.setHeight(0);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.contextual, menu);
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
            return true;
        }
        if (id == R.id.ordenarM) {
            jugadores.ordenaMedias();
            ArrayList<Jugador> datos=jugadores.pasaArraylist();
            ad.notifyDataSetChanged();
            ad = new AdaptadorArrayList(Main.this, R.layout.lista_detalle, datos);
            ListView lv = (ListView) findViewById(R.id.lvLista);
            lv.setAdapter(ad);
                return true;

        }
        if (id == R.id.ordenarN) {
            jugadores.ordenaNombres();
            ArrayList<Jugador> datos=jugadores.pasaArraylist();
            ad.notifyDataSetChanged();
            ad = new AdaptadorArrayList(Main.this, R.layout.lista_detalle, datos);
            ListView lv = (ListView) findViewById(R.id.lvLista);
            lv.setAdapter(ad);
            return true;

        }
        if (id == R.id.hazte) {
            if(jugadores.size()>=6&&jugadores.size()<=22&&jugadores.size()%2==0){
                jugadores.ordenaMedias();
                ArrayList<Jugador>js=jugadores.pasaArraylist();
                Intent  i = new Intent(Main.this, ActividadEquipo.class);
                i.putParcelableArrayListExtra("jugadores", js);
                startActivityForResult(i, ACTIVIDAD_SEGUNDA);

            }else {
                tostada("Estais impares no podeis jugar");
            }
      }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
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

    public boolean borrar(final int pos){
        TextView tv=(TextView)findViewById(R.id.tvListaEquipos);
        tv.setVisibility(View.INVISIBLE);
        tv.setHeight(0);
        tv.setText("");
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(Main.this);
        dialogo1.setTitle("Importante");
        dialogo1.setMessage("¿ Desea borrar el jugador seleccionado ?");
        dialogo1.setCancelable(false);
        dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                jugadores.remove(pos);
                ArrayList<Jugador> datos=jugadores.pasaArraylist();
                ad.notifyDataSetChanged();
                ad = new AdaptadorArrayList(Main.this, R.layout.lista_detalle, datos);
                ListView lv = (ListView) findViewById(R.id.lvLista);
                lv.setAdapter(ad);
                escribir();
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

    private boolean agregar() {
        TextView tv=(TextView)findViewById(R.id.tvListaEquipos);
        tv.setVisibility(View.INVISIBLE);
        tv.setHeight(0);
        tv.setText("");
        //cargamos vista
        LayoutInflater inflater = LayoutInflater.from(this);
        final View vista = inflater.inflate(R.layout.dialogo_alta, null);
        final EditText  et,et1, et2,et3;
        et = (EditText) vista.findViewById(R.id.etA);
        et1 = (EditText) vista.findViewById(R.id.et1A);
        et2 = (EditText) vista.findViewById(R.id.et2A);
        et3 = (EditText) vista.findViewById(R.id.et3A);
        et.setHint("Introduzca media");
        et1.setHint("Introduzca dorsal");
        et2.setHint("Introduzca nombre");
        et3.setHint("Introduzca teléfono");
        et.setFilters(new InputFilter[]{new InputFilterMinMax("1", "10")});
        et1.setFilters(new InputFilter[]{new InputFilterMinMax("1", "99")});
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
                        int num=0;
                        try{
                            num=Integer.parseInt(et1.getText().toString());
                        }catch(Exception e){}
                        if(jugadores.existeDorsal(num)||num==0){
                            tostada("¡Ese dorsal existe, elija otro!");
                        }else{
                        }
                        if(esTelefono(et3.getText().toString())&&isNumeric(et3.getText().toString())&&
                                et1.getText().toString().length() > 0 && et1.getText().toString().length()<=2
                                &&et.getText().toString().length() > 0 && et.getText().toString().length()<=2
                                && et3.getText().length() < 10&& et2.getText().length()>0&&num!=0&&!jugadores.existeDorsal(num)) {
                            int med = 0;

                            Jugador j = new Jugador();
                            try {
                                med = Integer.parseInt(et.getText().toString());
                            } catch (Exception e) {
                            }

                            try {
                                  num = Integer.parseInt(et1.getText().toString());
                                } catch (Exception e) {
                            }
                            if(jugadores.existeDorsal(num)){
                                tostada("¡Ese dorsal existe, elija otro!");
                            }
                            j.setMedia(med);
                            j.setDorsal(num);
                            j.setNombre(et2.getText().toString());
                            j.setTelefono(et3.getText().toString());
//                      añadimos jugados y mostramos
                            jugadores.add(j);
                            ArrayList<Jugador> datos=jugadores.pasaArraylist();
                            ad.notifyDataSetChanged();
                            ad = new AdaptadorArrayList(Main.this, R.layout.lista_detalle, datos);
                            ListView lv = (ListView) findViewById(R.id.lvLista);
                            lv.setAdapter(ad);
                            tostada("El jugador "+j.getNombre()+" ha sido añadido");
                            escribir();
                            d.dismiss();
                        }
                            // Filtramos que nos este vacios
                        if(et2.getText().toString().length() == 0 ){
                            tostada("¡Introduzca nombre!");
                        }
                        if(et3.getText().toString().length() == 0 ){
                            tostada("¡Introduzca teléfono!");
                        }
                        if(et.getText().toString().length() == 0 ){
                            tostada("¡Introduzca media!");
                        }
                        if(et1.getText().toString().length() == 0 ){
                            tostada("¡Introduzca dorsal!");
                        }


                        if(!esTelefono(et3.getText().toString())){
                          tostada("¡Teléfono incorrecto!");
                        }
                    }
                });
            }
        });
        d.show();
        ArrayList<Jugador> datos=jugadores.pasaArraylist();
         ad = new AdaptadorArrayList(Main.this, R.layout.lista_detalle, datos);
        ListView lv = (ListView) findViewById(R.id.lvLista);
        lv.setAdapter(ad);
        return true;
        }

    private boolean editar(final int index) {
        TextView tv=(TextView)findViewById(R.id.tvListaEquipos);
        tv.setVisibility(View.INVISIBLE);
        tv.setHeight(0);
        tv.setText("");
        //cargamos jugador
        Jugador g=new Jugador();
        g=jugadores.get(index);
        //cargamos vista
        LayoutInflater inflater = LayoutInflater.from(this);
        final View vista = inflater.inflate(R.layout.dialogo_alta, null);
        final EditText  et,et1, et2,et3;
        String hmed,hnom,htel,hdor;
        hmed=g.getMedia()+"";
        hdor=g.getDorsal()+"";
        hnom=g.getNombre();
        htel=g.getTelefono();
        et = (EditText) vista.findViewById(R.id.etA);
        et1 = (EditText) vista.findViewById(R.id.et1A);
        et2 = (EditText) vista.findViewById(R.id.et2A);
        et3 = (EditText) vista.findViewById(R.id.et3A);

        et.setText(hmed);
        et1.setText(hdor);
        et2.setText(hnom);
        et3.setText(htel);
        et.setFilters(new InputFilter[]{new InputFilterMinMax("1", "10")});
        et1.setFilters(new InputFilter[]{new InputFilterMinMax("1", "99")});

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
                        int num=0;
                        try{
                            num=Integer.parseInt(et1.getText().toString());
                        }catch(Exception e){}
                        if(jugadores.existeDorsal(num)){
                            tostada("¡Ese dorsal existe, elija otro!");
                        }else{
                        }
                        if(esTelefono(et3.getText().toString())&&isNumeric(et3.getText().toString())&&
                                et1.getText().toString().length() > 0 && et1.getText().toString().length()<=2
                                &&et.getText().toString().length() > 0 && et.getText().toString().length()<=2
                                && et3.getText().length() < 10&& et2.getText().length()>0&&num!=0&&!jugadores.existeDorsal(num)) {
                            int med = 0;
                            Jugador j = new Jugador();
                            try {
                                med = Integer.parseInt(et.getText().toString());
                            } catch (Exception e) {
                            }
                            try {
                                num = Integer.parseInt(et1.getText().toString());
                            } catch (Exception e) {
                            }
                            if(!jugadores.existeDorsal(num)){
                                tostada("¡Ese dorsal existe, elija otro!");
                            }
                            j.setMedia(med);
                            j.setDorsal(num);
                            j.setNombre(et2.getText().toString());
                            j.setTelefono(et3.getText().toString());
//                      modificamos jugados y mostramos
                            jugadores.set(index, j);
                            ArrayList<Jugador> datos=jugadores.pasaArraylist();
                            ad.notifyDataSetChanged();
                            ad = new AdaptadorArrayList(Main.this, R.layout.lista_detalle, datos);
                            ListView lv = (ListView) findViewById(R.id.lvLista);
                            lv.setAdapter(ad);
                            escribir();
                            tostada("Elemento modificado");
                            d.dismiss();
                        } // Filtramos que nos este vacios
                        if(et2.getText().toString().length() == 0 ){
                            tostada("¡Introduzca nombre!");
                        }
                        if(et3.getText().toString().length() == 0 ){
                            tostada("¡Introduzca teléfono!");
                        }
                        if(et.getText().toString().length() == 0 ){
                            tostada("¡Introduzca media!");
                        }if(et1.getText().toString().length() == 0 ){
                            tostada("¡Introduzca dorsal!");
                        }

                        if(!esTelefono(et3.getText().toString())){
                            tostada("¡Teléfono incorrecto!");
                        }
                    }
                });
            }
        });
        d.show();
        ArrayList<Jugador> datos=jugadores.pasaArraylist();
        ad = new AdaptadorArrayList(Main.this, R.layout.lista_detalle, datos);
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

    public void escribir()  {

        try {
            File f = new File(getExternalFilesDir(null), "EquiposFutbol.xml");
            FileOutputStream fosxml = new FileOutputStream(f);
            XmlSerializer docxml = Xml.newSerializer();
            docxml.setOutput(fosxml, "UTF-8");
            docxml.startDocument(null, Boolean.valueOf(true));
            docxml.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            docxml.startTag(null, "Jugadores");
            ///ciclo q recorre el array y lo escribe en l xml
            for (int i=0;i<jugadores.size();i++) {

                docxml.startTag(null, "jugador");
                docxml.startTag(null, "nombre");
                docxml.text(jugadores.get(i).getNombre().toString());
                docxml.endTag(null, "nombre");
                docxml.startTag(null, "telefono");
                docxml.text(jugadores.get(i).getTelefono().toString());
                docxml.endTag(null, "telefono");
                docxml.startTag(null, "media");
                docxml.text(jugadores.get(i).getMedia() + "");
                docxml.endTag(null, "media");
                docxml.startTag(null, "dorsal");
                docxml.text(jugadores.get(i).getDorsal() + "");
                docxml.endTag(null, "dorsal");
                docxml.endTag(null, "jugador");
            }
            docxml.endTag(null, "Jugadores");
            docxml.endDocument();
            docxml.flush();
            fosxml.close();

        }catch (Exception e){}
    }

    public ArrayList<Jugador> leer(){
        ArrayList<Jugador> lista=new ArrayList<Jugador>();
        try {
            XmlPullParser lectorxml = Xml.newPullParser();
            lectorxml.setInput(new FileInputStream(new File(getExternalFilesDir(null), "EquiposFutbol.xml")), "utf-8");
            int evento = lectorxml.getEventType();
            Jugador j=new Jugador();
            while(evento != XmlPullParser.END_DOCUMENT){
                if(evento == XmlPullParser.START_TAG) {
                    String etiqueta = lectorxml.getName();
                    if (etiqueta.compareTo("jugador") == 0) {
                    }
                    if (etiqueta.compareTo("nombre") == 0) {
                            j.setNombre(lectorxml.nextText());
                    }
                    if (etiqueta.compareTo("telefono") == 0) {
                            j.setTelefono(lectorxml.nextText());
                    }
                    if (etiqueta.compareTo("media") == 0) {
                            try {
                                int m = Integer.parseInt(lectorxml.nextText());
                                j.setMedia(m);
                            } catch (Exception e) {
                                tostada("error en la lectura de la media de un jugador");
                            }
                    }
                    if (etiqueta.compareTo("dorsal") == 0) {
                            try {
                                int d = Integer.parseInt(lectorxml.nextText());
                                j.setDorsal(d);

                            } catch (Exception e) {
                                tostada("error en la lectura del dorsal de un jugador");
                            }
                    }

                }
                if (evento == XmlPullParser.END_TAG) {
                    String etiqueta = lectorxml.getName();
                    if (etiqueta.compareTo("jugador") == 0) {
                        lista.add(j);
                        j=new Jugador();
                    }
                }
                evento = lectorxml.next();
            }
        }catch(Exception e){}
        return lista;
    }
    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        TextView tv=(TextView)findViewById(R.id.tvListaEquipos);
        ArrayList<Jugador>js=jugadores.pasaArraylist();
        outState.putParcelableArrayList("Jugadores", js);
        outState.putString("text",tv.getText().toString());
}
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        String m=savedInstanceState.getString("text");
        ArrayList<Jugador>js= savedInstanceState.getParcelableArrayList("Jugadores");
        TextView tv=(TextView)findViewById(R.id.tvListaEquipos);
        if(!m.isEmpty()){
            tv.setVisibility(View.VISIBLE);
            tv.setHeight(80);
            tv.setText(m);

        }
        jugadores.copiaArraylist(js);
        ad = new AdaptadorArrayList(this, R.layout.lista_detalle, js);
        ListView lv = (ListView) findViewById(R.id.lvLista);
        lv.setAdapter(ad);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ( resultCode == Activity.RESULT_OK ){
           ArrayList <Jugador>js=new ArrayList<Jugador>();
           js=data.getParcelableArrayListExtra("jugadores");
            //// recibe jugadores del partido
            jugadores.copiaArraylist(js);
            ad.notifyDataSetChanged();
            ad = new AdaptadorArrayList(Main.this, R.layout.lista_detalle, js);
            ListView lv = (ListView) findViewById(R.id.lvLista);
            lv.setAdapter(ad);
            TextView tv=(TextView)findViewById(R.id.tvListaEquipos);
            tv.setVisibility(View.VISIBLE);
            tv.setHeight(80);
            tv.setText(data.getStringExtra("text"));
            }

    }

}






