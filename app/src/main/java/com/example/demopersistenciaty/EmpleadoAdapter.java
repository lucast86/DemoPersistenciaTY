package com.example.demopersistenciaty;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.demopersistenciaty.models.Empleado;

import java.util.List;

public class EmpleadoAdapter extends BaseAdapter {

    List<Empleado> listaEmpleado;

    public EmpleadoAdapter(List<Empleado> listaEmpleado) {
        this.listaEmpleado = listaEmpleado;
    }

    @Override
    public int getCount() {
        return listaEmpleado.size();
    }

    @Override
    public Empleado getItem(int position) {
        return listaEmpleado.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listaEmpleado.get(position).getIdempleado();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;

        if(convertView==null){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_empleado,parent,false);
        }else{
            view = convertView;
        }

        // obtengo item de datos a mostrar
        Empleado item = listaEmpleado.get(position);

        // referencia a controles en la vista
        TextView txtItemListaNombre = view.findViewById(R.id.txtItemListaNombre);
        TextView txtItemListaEmail = view.findViewById(R.id.txtItemListaEmail);
        TextView txtItemListaTelefono = view.findViewById(R.id.txtItemListaTelefono);

        txtItemListaNombre.setText(item.getNombre());
        txtItemListaEmail.setText(item.getEmail());
        txtItemListaTelefono.setText(item.getTelefono());

        return view;
    }
}
