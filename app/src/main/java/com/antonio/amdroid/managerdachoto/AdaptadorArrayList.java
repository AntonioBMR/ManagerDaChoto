package com.antonio.amdroid.managerdachoto;

/**
 * Created by Antonio on 06/10/2014.
 */

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;


public class AdaptadorArrayList extends ArrayAdapter<Jugador>{
    private Context contexto;
    private ArrayList<Jugador> lista;
    private int recurso;
    private static LayoutInflater i;
    private ViewHolder vh;

    static class ViewHolder{
        public TextView tvt,tvt1, tvt2,tvt3,tv,tv1,tv2,tv3;
        public ImageView iv;
        public MenuItem item;
        public int posicion;
    }
    public AdaptadorArrayList(Context context, int resource, ArrayList<Jugador> objects) {
        super(context, resource, objects);
        this.contexto=context;
        this.lista=objects;
        this.recurso=resource;
        this.i = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Log.v("LOG",""+lista.size());
        if(convertView == null){
            convertView = i.inflate(recurso, null);
            vh = new ViewHolder();
            vh.item=(MenuItem)convertView.findViewById(R.id.hazte);
            vh.tv3=(TextView)convertView.findViewById(R.id.textView3);
            vh.tv2=(TextView)convertView.findViewById(R.id.textView2);
            vh.tv1=(TextView)convertView.findViewById(R.id.textView1);
            vh.tv=(TextView)convertView.findViewById(R.id.textView);
            vh.tvt=(TextView)convertView.findViewById(R.id.tvTexto);
            vh.tvt1=(TextView)convertView.findViewById(R.id.tvTexto1);
            vh.tvt2=(TextView)convertView.findViewById(R.id.tvTexto2);
            vh.tvt3=(TextView)convertView.findViewById(R.id.tvTexto3);
            vh.iv=(ImageView)convertView.findViewById(R.id.ivImagen);
            convertView.setTag(vh);
        }else{
            //comentario
            vh=(ViewHolder)convertView.getTag();
        }
        vh.posicion=position;
        vh.tvt.setText(lista.get(position).getMedia()+"");
        vh.tvt1.setText(lista.get(position).getDorsal()+"");
        vh.tvt2.setText(lista.get(position).getNombre().toString());
        vh.tvt3.setText(lista.get(position).getTelefono().toString());
        vh.iv.setImageResource(R.drawable.persona);
        vh.iv.setTag(position);
        return convertView;
    }
}