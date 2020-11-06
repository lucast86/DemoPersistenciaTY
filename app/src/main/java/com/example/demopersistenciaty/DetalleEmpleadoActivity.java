package com.example.demopersistenciaty;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class DetalleEmpleadoActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnDetalleEmpleadoGrabar, btnDetalleEmpleadoCancelar;
    private EditText edtDetalleEmpleadoNombre, edtDetalleEmpleadoEmail, edtDetalleEmpleadoTelefono,
            edtDetalleEmpleadoDomicilio, edtDetalleEmpleadoPassword;
    private Switch swtDetalleEmpleadoHabilitado, swtDetalleEmpleadoFavorito;

    private DbHelper dbHelper;
    private SQLiteDatabase db;
    private Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_empleado);

        this.ctx = this.getApplicationContext();

        Intent i = getIntent();

        int idEmpleado = i.getIntExtra("ID_EMPLEADO",0);

        getSupportActionBar().setTitle("ITES - Editar Empleado");

        findViewsById();

        //abrimos db en modo escritura
        dbHelper = new DbHelper(this.ctx);
        db = dbHelper.getWritableDatabase();

        if(idEmpleado!=0){
            Toast.makeText(ctx, "seleccionó: "+idEmpleado, Toast.LENGTH_SHORT).show();
            cargarItemSqlite(idEmpleado);
        }
    }

    // referencias a los controles en la vista
    private void findViewsById() {
        // cuadros de texto
        edtDetalleEmpleadoNombre = findViewById(R.id.edtDetalleEmpleadoNombre);
        edtDetalleEmpleadoEmail = findViewById(R.id.edtDetalleEmpleadoEmail);
        edtDetalleEmpleadoDomicilio = findViewById(R.id.edtDetalleEmpleadoDomicilio);
        edtDetalleEmpleadoTelefono = findViewById(R.id.edtDetalleEmpleadoTelefono);
        edtDetalleEmpleadoPassword = findViewById(R.id.edtDetalleEmpleadoPassword);

            // controles switch
            swtDetalleEmpleadoHabilitado = findViewById(R.id.swtDetalleEmpleadoHabilitado);
            swtDetalleEmpleadoFavorito = findViewById(R.id.swtDetalleEmpleadoFavorito);

        // botones
        btnDetalleEmpleadoCancelar = findViewById(R.id.btnDetalleEmpleadoCancelar);
        btnDetalleEmpleadoGrabar = findViewById(R.id.btnDetalleEmpleadoGrabar);

        // clic listeners
        btnDetalleEmpleadoCancelar.setOnClickListener(this);
        btnDetalleEmpleadoGrabar.setOnClickListener(this);
        
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnDetalleEmpleadoCancelar:
                Intent intent = new Intent(DetalleEmpleadoActivity.this, ListaEmpleadoActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btnDetalleEmpleadoGrabar:
                agregarItemSqlite();
                break;
        }
    }
    //-------------------------- hasta ya esta
    private void cargarItemSqlite(int idEmpleado) {
        // obtenemos datos de SQLite
        //String consulta = "SELECT * FROM Empleados WHERE idempleado="+ ID;

        //seleccionamos todos los registros
        Cursor cursor = db.rawQuery("SELECT * FROM Empleados WHERE idempleado=?", new String[]{String.valueOf(idEmpleado)});

        //nos posicionamos al inicio del curso
        if(cursor!=null && cursor.moveToLast()) {

            //iteramos todos los registros del cursor y llenamos array con registros
            edtDetalleEmpleadoNombre.setText(cursor.getString(cursor.getColumnIndex("nombre")));
            edtDetalleEmpleadoEmail.setText(cursor.getString(cursor.getColumnIndex("email")));
            edtDetalleEmpleadoDomicilio.setText(cursor.getString(cursor.getColumnIndex("domicilio")));
            edtDetalleEmpleadoTelefono.setText(cursor.getString(cursor.getColumnIndex("telefono")));
            swtDetalleEmpleadoHabilitado.setChecked(cursor.getInt(cursor.getColumnIndex("habilitado"))==1?true:false);
            swtDetalleEmpleadoFavorito.setChecked(cursor.getInt(cursor.getColumnIndex("favorito"))==1?true:false);
        }else{
            Toast.makeText(ctx, "No hay registros", Toast.LENGTH_SHORT).show();
        }

        db.close();

    }

    //-------------------------- hasta ya esta
    private void editarItemSqlite(){}

    private void agregarItemSqlite(){

        // verificamos que los valores sean validos
        if(validarItems()){

            ContentValues nuevoRegistro = new ContentValues();
            nuevoRegistro.put("telefono", edtDetalleEmpleadoTelefono.getText().toString());
            nuevoRegistro.put("nombre",edtDetalleEmpleadoNombre.getText().toString());
            nuevoRegistro.put("email",edtDetalleEmpleadoEmail.getText().toString());
            nuevoRegistro.put("domicilio",edtDetalleEmpleadoDomicilio.getText().toString());
            nuevoRegistro.put("favorito",swtDetalleEmpleadoFavorito.isChecked()?1:0);
            //nuevoRegistro.put("habilitado",swtDetalleEmpleadoHabilitado.isChecked()?1:0);
            nuevoRegistro.put("habilitado",1);
            nuevoRegistro.put("password",Utils.convertirSHA256(edtDetalleEmpleadoDomicilio.getText().toString()));
            //insertamos registro nuevo
            db.insert("Empleados", null, nuevoRegistro);

            Toast.makeText(ctx, "Registro Grabado OK", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(ctx, "Verifique datos inválidos", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validarItems(){
        // TODO: completar validaciones necesarias pre-grabación del empleado
        boolean valido=true;
        // campo nombre requerido
        if(edtDetalleEmpleadoNombre.getText().toString().isEmpty()){
            valido=false;
            edtDetalleEmpleadoNombre.setError("Debe completar este campo");
        }
        // campo email requerido
        if(edtDetalleEmpleadoEmail.getText().toString().isEmpty()){
            valido=false;
            edtDetalleEmpleadoEmail.setError("Debe completar este campo");
        }
        // campo password requerido
        if(edtDetalleEmpleadoPassword.getText().toString().isEmpty()){
            valido=false;
            edtDetalleEmpleadoPassword.setError("Debe completar este campo");
        }
        return valido;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
