package com.example.galvezagb50.ejerciciosdeficheros;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class ViewContacts extends AppCompatActivity {

    TextView texto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contacts);

        texto=(TextView) findViewById(R.id.txvTexto);

        Bundle bundle =this.getIntent().getExtras();

        texto.setText(bundle.getString("texto"));
    }
}
