package com.example.galvezagb50.ejerciciosdeficheros;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ShowAlarms extends AppCompatActivity{

    TextView texto, tiempo, numAlarm;
    GestorAlarma[] alarmas;
    int numAlarmas;
    CountDownTimer contador;
    int minutos,segundos, cont=0;
    boolean entrar=true;
    String alarmasQuedan="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_alarms);

        texto=(TextView) findViewById(R.id.txvAlarmasRestantes);
        tiempo=(TextView)findViewById(R.id.txvTiempo);
        numAlarm=(TextView)findViewById(R.id.txvNumeroAlarma);

        numAlarmas=getIntent().getExtras().getInt("numeroAlarmas");
        alarmas=new GestorAlarma[numAlarmas];

        for (int j = 0; j < alarmas.length ; j++) {
            alarmas[j]=new GestorAlarma(getIntent().getExtras().getInt("tiempo"+j), getIntent().getExtras().getString("frase"+j), this.getApplicationContext(), "nombre.txt" );
        }

        EmpezarAlarma();

    }

    public void EmpezarAlarma()
    {

        if ((cont<alarmas.length)&&(entrar))
        {
            boolean guarda=true;
            for (int i = cont; i < alarmas.length; i++) {
                if (guarda)
                {
                    alarmasQuedan+=alarmas[i].getMinutos()+", "+alarmas[i].getFrase()+"  (ejecutandose...)"+"\n\n";
                    guarda=false;
                }
                else {
                    alarmasQuedan += alarmas[i].getMinutos() + ", " + alarmas[i].getFrase() + "\n\n";
                }
            }

            texto.setText(alarmasQuedan);

            entrar=false;
            minutos=alarmas[cont].getMinutos();
            segundos=0;
            numAlarm.setText("Texto de Alarma: "+alarmas[cont].getFrase());

            contador = new CountDownTimer(((minutos * 60) + segundos) * 1000, 1000) {
                @Override
                public void onTick(long l)
                {
                    if (segundos <= 0) {
                        segundos = 59;
                        minutos--;
                    } else {
                        segundos--;
                    }
                    tiempo.setText(String.format("%02d", minutos) + " : " + String.format("%02d", segundos));
                }

                @Override
                public void onFinish()
                {
                    segundos = 0;
                    tiempo.setText(String.format("%02d", minutos) + " : " + String.format("%02d", segundos));
                    cont++;
                    entrar=true;
                    alarmasQuedan="";
                    EmpezarAlarma();

                }
            };
            contador.start();
        }
        else
        {
            texto.setText("");
            Toast.makeText(this, "Se Terminaron las Alarmas!", Toast.LENGTH_SHORT).show();

        }
    }



}
