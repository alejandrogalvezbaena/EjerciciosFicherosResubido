package com.example.galvezagb50.ejerciciosdeficheros;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class ConversorActivity extends AppCompatActivity implements View.OnClickListener {

    Button boton;
    EditText euros, dolares;
    RadioButton radioEuros, radioDolares;
    double cambio=0;

    String fileRoute = "http://alumno.mobi/~alumno/superior/galvez/cambio.txt";
    String fileName = "cambio.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversor);

        euros=(EditText) findViewById(R.id.text_euros);
        dolares=(EditText) findViewById((R.id.text_dolares));
        radioEuros=(RadioButton) findViewById(R.id.radio_euros);
        radioDolares=(RadioButton) findViewById((R.id.radio_dolares));
        boton=(Button) findViewById(R.id.boton_convertir);
        boton.setOnClickListener(this);

        if (isNetworkAvailable()) {
            try {
                new Download().execute();

            } catch (Exception e) {
                e.getMessage();
            }
        } else {
            Toast.makeText(this, "No hay Internet", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        try
        {
            cambio = Double.parseDouble(Utilities.readExternal(fileName, "UTF-8").replace("\n", ""));

            if (view==boton)
            if ((radioEuros.isChecked()) && !(euros.getText().toString().equals("."))) {
                dolares.setText(convertirADolares(euros.getText().toString()));
            }
            else if(!(dolares.getText().toString().equals("."))){
                euros.setText(convertirAEuros(dolares.getText().toString()));

            }
        }
        catch (Exception e)
        {
            Toast.makeText(this,"Asegúrese de tener internet y el archivo cambio.txt en la direccion correcta",Toast.LENGTH_SHORT).show();
        }

    }

    public String convertirADolares(String cantidad) {
        double valor = Double.parseDouble(cantidad) / cambio;
        return Double.toString(Math.round(valor*100d)/100d);
        //return String.valueOf(valor);
    }

    public String convertirAEuros(String cantidad) {
        double valor = Double.parseDouble(cantidad) * cambio;
        //return String.format("%.2f", valor);

        return Double.toString(Math.round(valor*100d)/100d);
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    class Download extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                URL url = new URL(fileRoute);
                URLConnection conexion = url.openConnection();
                conexion.connect();
                int lenghtOfFile = conexion.getContentLength();
                InputStream is = url.openStream();
                File file = new File(Environment.getExternalStorageDirectory() + "/" + fileName);
                if (!file.exists())
                    file.createNewFile();

                FileOutputStream fos = new FileOutputStream(Environment.getExternalStorageDirectory() + "/"+fileName);
                byte data[] = new byte[1024];
                long total = 0;
                int count = 0;
                while ((count = is.read(data)) != -1) {
                    total += count;
                    int progress_temp = (int) total * 100 / lenghtOfFile;
                    fos.write(data, 0, count);
                }
                Handler handler =  new Handler(ConversorActivity.this.getMainLooper());
                handler.post( new Runnable(){
                    public void run(){
                        Toast.makeText(ConversorActivity.this,"Archivo cambio.txt actualizado correctamente", Toast.LENGTH_SHORT).show();
                    }
                });
                is.close();
                fos.close();
            } catch (final Exception e) {
                Handler handler =  new Handler(ConversorActivity.this.getMainLooper());
                handler.post( new Runnable(){
                    public void run(){
                        Toast.makeText(ConversorActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
            return null;
        }
    }

    static class Utilities{

        public static String readExternal(String fileName, String codification){
            String result = "";
            if (canReadExternal()){
                File file, externalCardName;
                externalCardName = Environment.getExternalStorageDirectory();
                file = new File(externalCardName.getAbsolutePath(), fileName);
                result = read(file, codification);
            }
            return result;
        }

        public static boolean canReadExternal(){
            boolean canRead = false;
            String state = Environment.getExternalStorageState();
            if (state.equals(Environment.MEDIA_MOUNTED_READ_ONLY)
                    || state.equals(Environment.MEDIA_MOUNTED))
                canRead = true;
            return canRead;
        }

        private static String read(File file, String codification){
            FileInputStream fileInputStream = null;
            InputStreamReader inputStreamReader = null;
            BufferedReader bufferedReader = null;
            StringBuilder result = new StringBuilder();
            int n;
            try {
                fileInputStream = new FileInputStream(file);
                inputStreamReader = new InputStreamReader(fileInputStream, codification);
                bufferedReader = new BufferedReader(inputStreamReader);
                while ((n = bufferedReader.read()) != -1)
                    result.append((char) n);
            } catch (IOException e) {
                Log.e("Error", e.getMessage());
            } finally {
                try {
                    if (bufferedReader != null) {
                        bufferedReader.close();
                    }
                } catch (IOException e) {
                    Log.e("Error al cerrar", e.getMessage());
                }
            }
            return result.toString();
        }
    }
}
