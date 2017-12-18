package com.example.galvezagb50.ejerciciosdeficheros;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class ShowWeb extends AppCompatActivity {

    EditText etUrl, etFile;
    Button btConnect, btSave;
    WebView wvResult;
    TextView tvResult;
    RadioGroup rgOption;
    long inicio, fin;
    TareaAsincrona tareaAsincrona;
    String textWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_web);

        etUrl = (EditText) findViewById(R.id.direccion);
        etFile=(EditText)findViewById(R.id.edtNombreFile);
        btConnect = (Button) findViewById(R.id.conectar);
        btSave=(Button)findViewById(R.id.btnGuardar);
        wvResult = (WebView) findViewById(R.id.web);
        tvResult = (TextView) findViewById( R.id.resultado);
        rgOption = (RadioGroup) findViewById(R.id.radiogroup);
        textWeb="";

        btConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = etUrl.getText().toString();
                if (isNetworkAvailable()) {
                    if (!TextUtils.isEmpty(url)) {
                        switch (rgOption.getCheckedRadioButtonId()) {
                            case R.id.radioJava:
                                tareaAsincrona = new TareaAsincrona(ShowWeb.this);
                                tareaAsincrona.execute(url);
                                tvResult.setText("Conectando...");
                                break;
                            case R.id.radioAAHC:
                                AAHC(url);
                                break;
                            case R.id.radioVolley:
                                makeRequest(etUrl.getText().toString());
                                break;
                        }
                    } else {
                        Toast.makeText(ShowWeb.this, "No has escrito ninguna ruta", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ShowWeb.this, "No hay conexión de internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (disponibleEscritura())
                {
                    if (textWeb != "") {
                        escribirExterna(etFile.getText().toString()+".txt", textWeb,true, "UTF-8");
                        Toast.makeText(ShowWeb.this, "Archivo "+etFile.getText().toString()+".txt creado con exito en memoria externa", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(ShowWeb.this, "Carga una pagina para poder guardarla en "+etFile.getText().toString()+".txt", Toast.LENGTH_SHORT).show();

                    }
                }
                else {
                    Toast.makeText(ShowWeb.this, "No se ha podido escribir en memoria externa", Toast.LENGTH_SHORT).show();

                }
            }
        });

        mRequestQueue = MySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    private void AAHC(String url) {
        final long inicio;
        tvResult.setText("Conectando...");
        final long[] fin = new long[1];
        final ProgressDialog progreso = new ProgressDialog(ShowWeb.this);
        progreso.setCancelable(false);
        inicio = System.currentTimeMillis();
        RestClient.get(url, new TextHttpResponseHandler() {
            @Override
            public void onStart() {
                progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progreso.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    public void onCancel(DialogInterface dialog) {
                        RestClient.cancelRequests(getApplicationContext(), true);
                    }
                });
                progreso.show();
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                fin[0] = System.currentTimeMillis();
                progreso.dismiss();
                wvResult.loadDataWithBaseURL(null, "fallo: "+responseString, "text/html", "UTF-8", null);
                tvResult.setText("Duración: " + String.valueOf(fin[0] - inicio) + " milisegundos");
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                fin[0] = System.currentTimeMillis();
                progreso.dismiss();
                wvResult.loadDataWithBaseURL(null, responseString, "text/html", "UTF-8", null);
                tvResult.setText("Duración: " + String.valueOf(fin[0] - inicio) + " milisegundos");
                textWeb=responseString;
            }
        });

    }

    public static Resultado conectarJava(String texto) {
        URL url;
        HttpURLConnection urlConnection = null;
        int respuesta;
        Resultado resultado = new Resultado();
        try {
            url = new URL(texto);
            urlConnection = (HttpURLConnection) url.openConnection();
            respuesta = urlConnection.getResponseCode();
            if (respuesta == HttpURLConnection.HTTP_OK) {
                resultado.setCodigo(true);
                resultado.setContenido(leer(urlConnection.getInputStream()));
            } else {
                resultado.setCodigo(false);
                resultado.setMensaje("Error en el acceso a la web: " + String.valueOf(respuesta));
            }
        } catch (IOException e) {
            resultado.setCodigo(false);
            resultado.setMensaje("Excepción: " + e.getMessage());
        } finally {
            try {
                if (urlConnection != null)
                    urlConnection.disconnect();
            } catch (Exception e) {
                resultado.setCodigo(false);
                resultado.setMensaje("Excepción: " + e.getMessage());
            }
            return resultado;
        }
    }

    private static String leer(InputStream entrada) throws IOException{
        BufferedReader in;
        String linea;
        StringBuilder miCadena = new StringBuilder();
        in = new BufferedReader(new InputStreamReader(entrada), 32000);
        while ((linea = in.readLine()) != null)
            miCadena.append(linea);
        in.close();
        return miCadena.toString();
    }

    public class TareaAsincrona extends AsyncTask<String, Integer, Resultado> {
        private ProgressDialog progreso;
        private Context context;

        public TareaAsincrona(Context context) {
            this.context = context;
        }

        protected void onPreExecute() {
            progreso = new ProgressDialog(context);
            progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progreso.setMessage("Conectando . . .");
            progreso.setCancelable(true);
            progreso.setOnCancelListener(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface dialog) {
                    TareaAsincrona.this.cancel(true);
                }
            });
            progreso.show();
        }

        protected Resultado doInBackground(String... cadena) {
            Resultado resultado;
            inicio = System.currentTimeMillis();
            int i = 1;
            try {
                // operaciones en el hilo secundario
                publishProgress(i++);
                resultado = conectarJava(cadena[0]);
            } catch (Exception e) {
                resultado = new Resultado();
                resultado.setCodigo(false);
                resultado.setContenido(e.getMessage());
                //cancel(true);
            }
            return resultado;
        }

        protected void onProgressUpdate(Integer... progress) {
            progreso.setMessage("Conectando . . .");
        }

        protected void onPostExecute(Resultado result) {
            progreso.dismiss();
            fin = System.currentTimeMillis();
            // mostrar el resultado
            if (result.isCodigo())
                wvResult.loadDataWithBaseURL(null, result.getContenido(), "text/html", "UTF-8", null);
            else
                wvResult.loadDataWithBaseURL(null, result.getMensaje(), "text/html", "UTF-8", null);
            textWeb=result.getContenido();
            tvResult.setText("Duración: " + String.valueOf(fin - inicio) + " milisegundos");

        }

        protected void onCancelled() {
            progreso.dismiss();
            fin = System.currentTimeMillis();
            // mostrar cancelación
            wvResult.loadDataWithBaseURL(null, "Cancerado", "text/html", "UTF-8", null);
            tvResult.setText("Duración: " + String.valueOf(fin - inicio) + " milisegundos");
        }
    }

    RequestQueue mRequestQueue;

    public static String TAG = "Tag";


    public void makeRequest(String url) {
        final long inicio = System.currentTimeMillis();
        tvResult.setText("Conectando...");
        final long[] fin = new long[1];
        final String enlace = url;
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        wvResult.loadDataWithBaseURL(enlace, response, "text/html", "utf-8", null);
                        fin[0] = System.currentTimeMillis();
                        tvResult.setText("Duración: " + String.valueOf(fin[0] - inicio) + " milisegundos");
                        textWeb=response;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String message = "";
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            message = "Timeout Error " + error.getMessage();
                        } else if (error instanceof AuthFailureError) {
                            message = "AuthFailure Error " + error.getMessage();
                        } else if (error instanceof ServerError) {
                            message = "Server Error " + error.getMessage();
                        } else if (error instanceof NetworkError) {
                            message = "Network Error " + error.getMessage();
                        } else if (error instanceof ParseError) {
                            message = "Parse Error " + error.getMessage();
                        }
                        wvResult.loadDataWithBaseURL(null, message, "text/html", "utf-8", null);
                        fin[0] = System.currentTimeMillis();
                        tvResult.setText("Duración: " + String.valueOf(fin[0] - inicio) + " milisegundos");
                    }
                });
        stringRequest.setTag(TAG);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 1, 1));
        mRequestQueue.add(stringRequest);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(TAG);
        }
    }

    private boolean disponibleEscritura(){
        boolean escritura = false;
        String estado = Environment.getExternalStorageState();
        if (estado.equals(Environment.MEDIA_MOUNTED))
            escritura = true;
        return escritura;
    }

    private boolean escribirExterna(String fichero, String cadena, Boolean anadir, String codigo) {
        File miFichero, tarjeta;
        tarjeta = Environment.getExternalStorageDirectory();
        miFichero = new File(tarjeta.getAbsolutePath(), fichero);
        if (miFichero.exists())
        {
            miFichero.delete();
        }
        return escribir(miFichero, cadena, anadir, codigo);
    }

    private boolean escribir(File fichero, String cadena, Boolean anadir, String codigo) {
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        BufferedWriter out = null;
        boolean correcto = false;
        try {
            fos = new FileOutputStream(fichero, anadir);
            osw = new OutputStreamWriter(fos, codigo);
            out = new BufferedWriter(osw);
            out.write(cadena);
        } catch (IOException e) {
            Log.e("Error de E/S", e.getMessage());
        } finally {
            try {
                if (out != null) {
                    out.close();
                    correcto = true;
                }
            } catch (IOException e) {
                Log.e("Error al cerrar", e.getMessage());
            }
        }
        return correcto;
    }
}
