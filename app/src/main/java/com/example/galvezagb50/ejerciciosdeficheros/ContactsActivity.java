package com.example.galvezagb50.ejerciciosdeficheros;

import android.content.Intent;
import android.graphics.ImageFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ContactsActivity extends AppCompatActivity implements View.OnClickListener {

    EditText nombre, telefono, email;
    Button crearContacto, verContacto, borrarTodos;
    TextView propiedades;
    String nuevoNombre, nuevoTelefono, nuevoEmail;
    GestorPersona miPersona=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        nombre=(EditText)findViewById(R.id.edtName);
        telefono=(EditText)findViewById(R.id.edtNumber);
        email=(EditText)findViewById(R.id.edtEmail);
        propiedades=(TextView)findViewById(R.id.txvPropiedades);
        crearContacto=(Button)findViewById(R.id.btnCrearContacto);
        crearContacto.setOnClickListener(this);
        verContacto=(Button)findViewById(R.id.btnVerContactos);
        verContacto.setOnClickListener(this);
        borrarTodos=(Button)findViewById(R.id.btnBorrarContactos);
        borrarTodos.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view==crearContacto)
        {
            try
            {
                nuevoNombre=nombre.getText().toString();
                nuevoTelefono=telefono.getText().toString();
                nuevoEmail=email.getText().toString();
                if ((nuevoNombre.equals(""))|(nuevoTelefono.equals(""))|(nuevoEmail.equals("")))
                {
                    throw new Exception();
                }
                else
                {
                    miPersona=new GestorPersona(nuevoNombre, nuevoTelefono, nuevoEmail, getApplicationContext(), "Contactos.txt");
                    if(!GestorPersona.ComprobarEmail(nuevoEmail)){throw  new Exception();}
                    propiedades.setText(miPersona.CrearPersona());
                    Toast.makeText(this, "Contacto creado correctamente", Toast.LENGTH_SHORT).show();
                    nombre.setText("");
                    telefono.setText("");
                    email.setText("");
                    nombre.requestFocus();
                }
            }
            catch (Exception e)
            {
                Toast.makeText(this, "No se pudo crear el contacto. Error o ausencia de parámetros.", Toast.LENGTH_SHORT).show();
            }
        }
        if (view==verContacto)
        {
            if(miPersona!=null)
            {
                Intent i = new Intent(this, ViewContacts.class);
                Bundle bundle = new Bundle();
                if (miPersona.LeerInterna().equals(""))
                {
                    bundle.putString("texto", "No existen contactos");
                }
                else
                {
                    bundle.putString("texto", miPersona.LeerInterna());
                }

                i.putExtras(bundle);
                startActivity(i);
            }
            else
            {
                Toast.makeText(this, "Crea un contacto para poder mostrarlo", Toast.LENGTH_SHORT).show();
            }

        }
        if (view==borrarTodos)
        {
            if(miPersona!=null)
            {
                if (miPersona.BorrarTodos())
                {
                    propiedades.setText("");
                    Toast.makeText(this, "Contactos borrados correctamente", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(this, "No ha sido posible el borrado", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(this, "El fichero no existe, no hay nada que borrar", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
