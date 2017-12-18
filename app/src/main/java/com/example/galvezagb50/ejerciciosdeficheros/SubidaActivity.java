package com.example.galvezagb50.ejerciciosdeficheros;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.File;
import java.io.FileNotFoundException;

import cz.msebera.android.httpclient.Header;

public class SubidaActivity extends AppCompatActivity {

    EditText edtRuta;
    Button btnSubir, btnSeleccionar;

    private static final int ABRIRFICHERO_REQUEST_CODE = 1;
    public final static String WEB = "http://alumno.mobi/~alumno/superior/galvez/upload.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subida);

        edtRuta=(EditText)findViewById(R.id.edtRuta);
        btnSubir=(Button)findViewById(R.id.btnSubir);
        btnSeleccionar=(Button)findViewById(R.id.btnSeleccionar);

        btnSubir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subida();
            }
        });

        btnSeleccionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("file/*");
                if (intent.resolveActivity(getPackageManager()) != null)
                    startActivityForResult(intent, ABRIRFICHERO_REQUEST_CODE);
                else
                    Toast.makeText(SubidaActivity.this, "No hay aplicación para manejar ficheros", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (requestCode == ABRIRFICHERO_REQUEST_CODE)
            if (resultCode == RESULT_OK) {
                String ruta = data.getData().getPath();
                edtRuta.setText(ruta);
            }
            else
                Toast.makeText(this, "Error: " + resultCode, Toast.LENGTH_SHORT).show();
    }

    private void subida() {
        String fichero = edtRuta.getText().toString();
        final ProgressDialog progreso = new ProgressDialog(SubidaActivity.this);
        File myFile;
        Boolean existe = true;
        myFile = new File(edtRuta.getText().toString());
        RequestParams params = new RequestParams();
        try {
            params.put("fileToUpload", myFile);
        } catch (FileNotFoundException e) {
            existe = false;
            Toast.makeText(this, "Error en el fichero: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        if (existe)
            RestClient.post(WEB, params, new TextHttpResponseHandler() {
                @Override
                public void onStart() {
                    progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progreso.setMessage("Conectando . . .");
                    progreso.setOnCancelListener(new DialogInterface.OnCancelListener(){
                        public void onCancel(DialogInterface dialog){
                            RestClient.cancelRequests(getApplicationContext(), true);
                        }
                    });
                    progreso.show();
                }
                @Override
                public void onSuccess(int statusCode, Header[] headers, String response) {
                    progreso.dismiss();
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, String response, Throwable t) {
                    progreso.dismiss();
                }
            });
    }
}
