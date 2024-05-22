package com.example.mrhealth;


import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mrhealth.Models.Persona;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class paginainicio extends AppCompatActivity {

    private List<Persona> listPerson = new ArrayList<Persona>();
    ArrayAdapter<Persona> arrayAdapter;

    EditText nomP, altP, pesoP, fechaP;
    ListView listV_Personas;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    Persona personaseleccionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paginainicio);

        nomP = findViewById(R.id.txt_nombrePersona);
        altP = findViewById(R.id.txt_alturaPersona);
        pesoP = findViewById(R.id.txt_pesoPersona);
        fechaP = findViewById(R.id.txt_fechaPersona);

        listV_Personas = findViewById(R.id.lv_datosPersonas);

        inializadorFirebase();
        listarDatos();

        listV_Personas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                personaseleccionada = (Persona) parent.getItemAtPosition(position);
                nomP.setText(personaseleccionada.getNombre());
                altP.setText(personaseleccionada.getAltura());
                pesoP.setText(personaseleccionada.getPeso());
                fechaP.setText(personaseleccionada.getFecha());
            }
        });

    }

    private void listarDatos() {
        databaseReference.child("Persona").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listPerson.clear();
                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()){
                    Persona p = objSnapshot.getValue(Persona.class);
                    listPerson.add(p);

                    arrayAdapter = new ArrayAdapter<Persona>(paginainicio.this, android.R.layout.simple_list_item_1,listPerson);
                    listV_Personas.setAdapter(arrayAdapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void inializadorFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    //SE AGREGA ACTIONBAR
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }
    //ACTIONBAR
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        String nombre = nomP.getText().toString();
        String altura = altP.getText().toString();
        String peso = pesoP.getText().toString();
        String fecha = fechaP.getText().toString();
        

        int id = item.getItemId();

        if (id == R.id.icon_add){
            if (nombre.equals("")||altura.equals("")||peso.equals("")||fecha.equals("")){

                validacion();
            }else {
                Persona p = new Persona();
                p.setUid(UUID.randomUUID().toString());
                p.setNombre(nombre);
                p.setAltura(altura);
                p.setPeso(peso);
                p.setFecha(fecha);
                databaseReference.child("Persona").child(p.getUid()).setValue(p);
                Toast.makeText(this, "Agregado con exito!", Toast.LENGTH_SHORT).show();
                limpiarcajas();

            }
            return true;


        } else if (id == R.id.icon_save) {
            Persona p = new Persona();
            p.setUid(personaseleccionada.getUid());
            p.setNombre(nomP.getText().toString().trim());
            p.setAltura(altP.getText().toString().trim());
            p.setPeso(pesoP.getText().toString());
            p.setFecha(fechaP.getText().toString());
            databaseReference.child("Persona").child(p.getUid()).setValue(p);
            Toast.makeText(this, "Guardado con exito!", Toast.LENGTH_SHORT).show();
            limpiarcajas();
            return true;


        } else if (id == R.id.icon_delate) {
            Persona p = new Persona();
            p.setUid(personaseleccionada.getUid());
            databaseReference.child("Persona").child(p.getUid()).removeValue();
            Toast.makeText(this, "Eliminado con exito!", Toast.LENGTH_SHORT).show();
            limpiarcajas();
            return true;
        }
        return true;
    }

    private void limpiarcajas() {
        nomP.setText("");
        altP.setText("");
        pesoP.setText("");
        fechaP.setText("");
    }

    private void validacion() {
        String nombre = nomP.getText().toString();
        String altura = altP.getText().toString();
        String peso = pesoP.getText().toString();
        String fecha = fechaP.getText().toString();

        if (nombre.equals("")){
            nomP.setError("Requiered");
        } else if (altura.equals("")) {
            altP.setError("Requiered");
        } else if (peso.equals("")) {
            pesoP.setError("Requiered");
        } else if (fecha.equals("")) {
            fechaP.setError("Requiered");
        }
    }
}