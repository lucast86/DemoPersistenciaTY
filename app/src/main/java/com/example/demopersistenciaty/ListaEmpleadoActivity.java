package com.example.demopersistenciaty;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.demopersistenciaty.models.Empleado;

import java.util.ArrayList;
import java.util.List;

public class ListaEmpleadoActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private EmpleadoAdapter adapter;
    private List<Empleado> listaEmpleado;
    private ListView listViewEmpleados;
    //SqLite
    private Context ctx;
    private DbHelper dbHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_empleado);

        this.ctx = getApplicationContext();

        getSupportActionBar().setTitle("ITES - Lista de Empleados");

        listaEmpleado = new ArrayList<Empleado>();

        // cargar lista
        // cargarListaDummy();
        cargarListaDesdeSqlite();

        findViewsById();
    }

    private void cargarListaDesdeSqlite() {
        // obtenemos datos de SQLite
        //abrimos db en modo escritura
        dbHelper = new DbHelper(this.ctx);
        db = dbHelper.getReadableDatabase();

        // limpiamos lista
        listaEmpleado.clear();

        //seleccionamos todos los registros
        Cursor cursor = db.rawQuery("SELECT * FROM Empleados WHERE habilitado=1",null);

        //nos posicionamos al inicio del curso
        if(cursor.moveToFirst()) {
            //iteramos todos los registros del cursor y llenamos array con registros



                while (cursor.isAfterLast() == false) {

                    Empleado item = new Empleado();
                    //recorremos hasta llegar al ultimo registro
                    item.setIdempleado(cursor.getInt(cursor.getColumnIndex("idempleado")));
                    item.setNombre(cursor.getString(cursor.getColumnIndex("nombre")));
                    item.setEmail(cursor.getString(cursor.getColumnIndex("email")));
                    item.setTelefono(cursor.getString(cursor.getColumnIndex("telefono")));

                    listaEmpleado.add(item);

                    // corremos a siguiente posición del curso
                    cursor.moveToNext();
                }


        }else{
            Toast.makeText(ctx, "No hay registros", Toast.LENGTH_SHORT).show();
        }
        // cerramos conexion SQLite
        db.close();
    }

    private void cargarListaDummy(){

        // vaciamos lista por las dudas
        listaEmpleado.clear();

        // empleado 1
        Empleado e = new Empleado();
        e.setIdempleado(1);
        e.setNombre("Luis Garcia");
        e.setDomicilio("Belgrano 665");
        e.setEmail("luis.garcia@correo.com.ar");
        e.setTelefono("0233-445566");
        e.setHabilitado(1);
        listaEmpleado.add(e);

        e = null;

        // empleado 2
        e = new Empleado();
        e.setIdempleado(1);
        e.setNombre("Berta Dominguez");
        e.setDomicilio("Las Heras 556");
        e.setEmail("berta.dominguez@correo.com.ar");
        e.setTelefono("0233-214568");
        e.setHabilitado(1);
        listaEmpleado.add(e);

        e = null;

        // empleado 3
        e = new Empleado();
        e.setIdempleado(1);
        e.setNombre("Roberto Perez");
        e.setDomicilio("Congreso 771");
        e.setEmail("roberto.perez@correo.com.ar");
        e.setTelefono("0233-445097");
        e.setHabilitado(1);
        listaEmpleado.add(e);

    }

    private void findViewsById() {
        listViewEmpleados = findViewById(R.id.listViewEmpleados);
        adapter = new EmpleadoAdapter(listaEmpleado);
        listViewEmpleados.setAdapter(adapter);

        listViewEmpleados.setOnItemClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_basico,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.action_empleado_agregar:
                Intent intent = new Intent(ListaEmpleadoActivity.this,DetalleEmpleadoActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_empleado_actualizar:
                cargarListaDesdeSqlite();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        int ID = (int)adapter.getItemId(position);

        Intent i = new Intent(ListaEmpleadoActivity.this, DetalleEmpleadoActivity.class);
        i.putExtra("ID_EMPLEADO", ID);
        startActivity(i);
    }


}
