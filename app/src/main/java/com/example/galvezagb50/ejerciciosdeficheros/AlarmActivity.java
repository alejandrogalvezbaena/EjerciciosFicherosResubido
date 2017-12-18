package com.example.galvezagb50.ejerciciosdeficheros;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AlarmActivity extends AppCompatActivity implements OnClickListener{

    Button crearAlarma, iniciarAlarmas, borrarAlarmas;
    EditText edtMinutos, edtFrase;
    TextView propiedades;
    private int numAlarm=0;
    GestorAlarma[] listaAlarmas;
    String minutos;
    String frase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        edtMinutos = (EditText) findViewById(R.id.edtMinutos);
        edtFrase = (EditText) findViewById(R.id.edtFrase);
        propiedades = (TextView) findViewById(R.id.txvPropiedadesAlarmas);
        crearAlarma = (Button) findViewById(R.id.btnCrearAlarma);
        crearAlarma.setOnClickListener(this);
        iniciarAlarmas = (Button) findViewById(R.id.btnIniciarAlarmas);
        iniciarAlarmas.setOnClickListener(this);
        borrarAlarmas = (Button) findViewById(R.id.btnBorrarAlarmas);
        borrarAlarmas.setOnClickListener(this);

        listaAlarmas=new GestorAlarma[5];
    }

        public void onClick(View view) {
        if (view==crearAlarma)
        {
            try
            {
                minutos = edtMinutos.getText().toString();
                frase=edtFrase.getText().toString();

                if ((minutos.equals(""))|(frase.equals("")))
                {
                    throw new ArrayIndexOutOfBoundsException();
                }
                else
                {
                    listaAlarmas[numAlarm] = new GestorAlarma(Integer.parseInt(minutos), frase, getApplicationContext(), "alarmas.txt");
                    propiedades.setText(listaAlarmas[numAlarm].CrearAlarma());
                    numAlarm++;
                    Toast.makeText(this, "Alarma creada correctamente", Toast.LENGTH_SHORT).show();
                    edtMinutos.setText("");
                    edtFrase.setText("");
                    edtMinutos.requestFocus();
                }
            }
            catch (ArrayIndexOutOfBoundsException e)
            {
                Toast.makeText(this, "No puedes crear mas alarmas. Error o ausencia de parámetros.", Toast.LENGTH_SHORT).show();
            }

        }
        if (view==iniciarAlarmas)
        {
            if(listaAlarmas[0]!=null)
            {
                Intent i = new Intent(this, ShowAlarms.class);
                Bundle bundle = new Bundle();
                if (listaAlarmas[0].LeerInterna().equals(""))
                {
                    Toast.makeText(this, "Crea una alarma para poder mostrarla", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    bundle.putString("texto", listaAlarmas[0].LeerInterna());
                    bundle.putInt("numeroAlarmas",numAlarm);
                    for (int j = 0; j <numAlarm; j++) {
                        bundle.putInt("tiempo"+j, listaAlarmas[j].getMinutos());
                        bundle.putString("frase"+j, listaAlarmas[j].getFrase());
                    }
                }

                i.putExtras(bundle);
                startActivity(i);
            }
            else
            {
                Toast.makeText(this, "Crea una alarma para poder mostrarla", Toast.LENGTH_SHORT).show();
            }

        }
        if (view==borrarAlarmas)
        {
            if(listaAlarmas[0]!=null)
            {
                if (listaAlarmas[0].BorrarTodos())
                {
                    propiedades.setText("");
                    listaAlarmas=new GestorAlarma[5];
                    numAlarm=0;
                    Toast.makeText(this, "Alarmas borradas correctamente", Toast.LENGTH_SHORT).show();
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





