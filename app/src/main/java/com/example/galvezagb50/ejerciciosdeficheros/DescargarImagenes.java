package com.example.galvezagb50.ejerciciosdeficheros;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import java.nio.charset.Charset;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class DescargarImagenes extends AppCompatActivity {

    Button btDownload, btUp,btDown;
    ImageView ivImage;
    EditText etUrl;
    TextView tvContador;
    ArrayList<String> urls;
    int urlActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descargar_imagenes);

        etUrl = (EditText) findViewById(R.id.etUrl5);
        btDownload = (Button) findViewById(R.id.btDownload5);
        ivImage = (ImageView) findViewById(R.id.ivImage5);
        btUp = (Button) findViewById(R.id.btUp5);
        btDown = (Button) findViewById(R.id.btDown5);
        tvContador = (TextView) findViewById(R.id.tvContador5);

        btDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = etUrl.getText().toString();
                if (!TextUtils.isEmpty(url)){
                    loadUrls(url);
                }
            }
        });

        btUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeImageUp();
            }
        });
        btDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeImageDown();
            }
        });
    }

    private void loadUrls(String url) {
        final ProgressDialog dialog = new ProgressDialog(this);
        RestClient.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                dialog.setCancelable(false);
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.setMessage("Conectando...");
                dialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                urls = new ArrayList<String>();
                CharSequence seq2 = new String(responseBody, Charset.forName("UTF-8"));
                String pattern = "(http(s?):/)(/[^/]+)+" + ".(?:jpg|gif|png)";
                boolean matches;
                for (String q : seq2.toString().split("\n| ")) {
                    matches = q.matches(pattern);
                    if (matches)
                        urls.add(q);
                }
                if (urls != null && urls.size() > 0){
                    urlActual = 0;
                    setImage();
                    btUp.setEnabled(true);
                    btDown.setEnabled(true);
                    Toast.makeText(DescargarImagenes.this, "Se ha descargado correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    btUp.setEnabled(false);
                    btDown.setEnabled(false);
                    ivImage.setImageDrawable(null);
                    tvContador.setText("");
                    Toast.makeText(DescargarImagenes.this, "Se ha descargado correctamente, pero no había ninguna imagen válida", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                btUp.setEnabled(false);
                btDown.setEnabled(false);
                ivImage.setImageDrawable(null);
                tvContador.setText("");
                Toast.makeText(DescargarImagenes.this, "Error:" + error.getMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }


    private void changeImageDown() {
        if (urlActual == 0)
            urlActual = urls.size() - 1;
        else
            urlActual --;
        setImage();
    }

    private void changeImageUp() {
        if (urlActual == urls.size() - 1)
            urlActual = 0;
        else
            urlActual ++;
        setImage();
    }

    private void setImage() {
        Picasso.with(getApplicationContext())
                .load(urls.get(urlActual))
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(ivImage);
        tvContador.setText("Imagen " + (urlActual + 1) + " de " + urls.size());
    }
}
