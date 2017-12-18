package com.example.galvezagb50.ejerciciosdeficheros;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChooseActivity extends AppCompatActivity implements View.OnClickListener {

    Button ejercicio1, ejercicio2, ejercicio3, ejercicio4, ejercicio5, ejercicio6, ejercicio7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        ejercicio1=(Button)findViewById(R.id.btnEjercicio1);
        ejercicio2=(Button)findViewById(R.id.btnEjercicio2);
        ejercicio3=(Button)findViewById(R.id.btnEjercicio3);
        ejercicio4=(Button)findViewById(R.id.btnEjercicio4);
        ejercicio5=(Button)findViewById(R.id.btnEjercicio5);
        ejercicio6=(Button)findViewById(R.id.btnEjercicio6);
        ejercicio7=(Button)findViewById(R.id.btnEjercicio7);
        ejercicio1.setOnClickListener(this);
        ejercicio2.setOnClickListener(this);
        ejercicio3.setOnClickListener(this);
        ejercicio4.setOnClickListener(this);
        ejercicio5.setOnClickListener(this);
        ejercicio6.setOnClickListener(this);
        ejercicio7.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        Intent i;
        if (view==ejercicio1)
        {
            i=new Intent(this, ContactsActivity.class);
            startActivity(i);
        }
        if (view==ejercicio2)
        {
            i=new Intent(this, AlarmActivity.class);
            startActivity(i);
        }
        if (view==ejercicio3)
        {
            i=new Intent(this, DatesActivity.class);
            startActivity(i);
        }

        if (view==ejercicio4)
        {
            i=new Intent(this, ShowWeb.class);
            startActivity(i);
        }
        if (view==ejercicio5)
        {
            i=new Intent(this, DescargarImagenes.class);
            startActivity(i);
        }
        if (view==ejercicio6)
        {
            i=new Intent(this, ConversorActivity.class);
            startActivity(i);
        }
        if (view==ejercicio7)
        {
            i=new Intent(this, SubidaActivity.class);
            startActivity(i);
        }

    }
}
