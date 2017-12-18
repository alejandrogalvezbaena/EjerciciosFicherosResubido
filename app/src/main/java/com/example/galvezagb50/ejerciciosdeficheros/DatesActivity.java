package com.example.galvezagb50.ejerciciosdeficheros;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.IllegalFormatCodePointException;
import java.util.List;
import java.util.Locale;

public class DatesActivity extends AppCompatActivity{

    EditText edt_diaIni, edt_mesIni, edt_anioIni, edt_diaFin, edt_mesFin, edt_anioFin;
    Button btnGuardarFechas;
    TextView txvDiaLectivo, txvDescripcion;
    String diaIni, mesIni, anioIni, diaFin, mesFin, anioFin, fechaIni, fechaFin, diasLectivos;
    Integer diaActual, mesActual, anioActual;
    ScrollView scroll;

    ArrayList<String> listaLectivos;
    Calendar fechaActual = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dates);

        txvDiaLectivo=(TextView)findViewById(R.id.txvDiaLectivo);
        txvDescripcion=(TextView)findViewById(R.id.txvDescripcion);
        edt_diaIni=(EditText)findViewById(R.id.edtDiaIni);
        edt_mesIni=(EditText)findViewById(R.id.edtMesIni);
        edt_anioIni=(EditText)findViewById(R.id.edtAnioIni);
        edt_diaFin=(EditText)findViewById(R.id.edtDiaFin);
        edt_mesFin=(EditText)findViewById(R.id.edtMesFin);
        edt_anioFin=(EditText)findViewById(R.id.edtAnioFin);
        btnGuardarFechas=(Button)findViewById(R.id.btnGuardarFecha);
        scroll=(ScrollView)findViewById(R.id.scroll);

        diaActual=fechaActual.get(Calendar.DAY_OF_MONTH);
        mesActual=fechaActual.get(Calendar.MONTH)+1;
        anioActual=fechaActual.get(Calendar.YEAR);


        btnGuardarFechas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    diasLectivos="";
                    listaLectivos=new ArrayList<String>();
                    if ((CompruebaParametros() & (CompruebaFechas()))) {
                        fechaIni=diaIni+"-"+mesIni+"-"+anioIni;
                        fechaFin=diaFin+"-"+mesFin+"-"+anioFin;
                        if (RecorrerFechas(Integer.valueOf(anioIni),Integer.valueOf(mesIni),Integer.valueOf(diaIni),Integer.valueOf(anioFin),Integer.valueOf(mesFin),Integer.valueOf(diaFin)))
                        {
                            for (int i = 0; i < listaLectivos.size(); i++)
                            {
                                diasLectivos+=listaLectivos.get(i)+"\n"; //String con todos los dias lectivos entre las dos fechas dadas
                            }
                            if (disponibleEscritura())
                            {
                                escribirExterna("diasLectivos.txt", diasLectivos,true, "UTF-8");
                                Toast.makeText(DatesActivity.this, "Archivo diasLectivos.txt creado con exito en memoria externa", Toast.LENGTH_SHORT).show();
                                txvDescripcion.setText("\nDIAS LECTIVOS ENTRE LAS DOS FECHAS:\n\n"+diasLectivos);
                                scroll.setVisibility(View.VISIBLE);
                                if (DiaFiesta(mesActual, diaActual))
                                {
                                    txvDiaLectivo.setText("HOY NO ES DIA LECTIVO");
                                }
                                else
                                {
                                    txvDiaLectivo.setText("HOY ES DIA LECTIVO");
                                }
                            }
                            else
                            {
                                Toast.makeText(DatesActivity.this, "No se puede escribir en memoria externa", Toast.LENGTH_SHORT).show();

                            }

                        }
                        else
                        {
                            Toast.makeText(DatesActivity.this, "La fecha inicial no puede ser mayor que la fecha final", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(DatesActivity.this, "Debe introducir fechas correctas", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (NumberFormatException e)
                {
                    Toast.makeText(DatesActivity.this, "Debe rellenar todos los campos", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e)
                {
                    Toast.makeText(DatesActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private boolean CompruebaParametros(){
        diaIni=edt_diaIni.getText().toString();
        mesIni=edt_mesIni.getText().toString();
        anioIni=edt_anioIni.getText().toString();
        diaFin=edt_diaFin.getText().toString();
        mesFin=edt_mesFin.getText().toString();
        anioFin=edt_anioFin.getText().toString();

        return  true;
    }

    private boolean CompruebaFechas(){
        boolean correcto;

        if ((Integer.valueOf(diaIni)<=0)|(Integer.valueOf(diaFin)<=0)|(Integer.valueOf(diaIni)>31)|(Integer.valueOf(diaFin)>31))
        {
            return correcto=false;
        }
        if ((Integer.valueOf(mesIni)<=0)|(Integer.valueOf(mesFin)<=0)|(Integer.valueOf(mesIni)>12)|(Integer.valueOf(mesFin)>12))
        {
            return correcto=false;
        }
        if ((Integer.valueOf(anioIni)<2017)|(Integer.valueOf(anioFin)<2017)|(Integer.valueOf(anioIni)>2018)|(Integer.valueOf(anioFin)>2018))
        {
            return correcto=false;
        }
        if ((Integer.valueOf(anioIni)==2017))
        {
            if ((Integer.valueOf(mesIni)<9))
            {
                return  correcto=false;
            }
            if ((Integer.valueOf(mesIni)==9))
            {
                if ((Integer.valueOf(diaIni)<15))
                {
                    return  correcto=false;
                }
            }
        }
        if ((Integer.valueOf(anioFin)==2017))
        {
            if ((Integer.valueOf(mesFin)<9))
            {
                return  correcto=false;
            }
            if ((Integer.valueOf(mesFin)==9))
            {
                if ((Integer.valueOf(diaFin)<15))
                {
                    return  correcto=false;
                }
            }
        }
        if ((Integer.valueOf(anioIni)==2018))
        {
            if ((Integer.valueOf(mesIni)>6))
            {
                return  correcto=false;
            }
            if ((Integer.valueOf(mesIni)==6))
            {
                if ((Integer.valueOf(diaIni)>22))
                {
                    return  correcto=false;
                }
            }
        }
        if ((Integer.valueOf(anioFin)==2018))
        {
            if ((Integer.valueOf(mesFin)>6))
            {
                return  correcto=false;
            }
            if ((Integer.valueOf(mesFin)==6))
            {
                if ((Integer.valueOf(diaFin)>22))
                {
                    return  correcto=false;
                }
            }
        }

        return  correcto=true;
    }

    private boolean DiaFiesta(int mes, int dia){
        boolean fiesta;

        if (mes==9)
        {
            if ((dia==16)|(dia==17)|(dia==23)|(dia==24)|(dia==30)|(dia==31))
            {
                return fiesta=true;
            }
        }
        if (mes==10)
        {
            if ((dia==1)|(dia==7)|(dia==8)|(dia==12)|(dia==14)|(dia==15)|(dia==21)|(dia==22)|(dia==28)|(dia==29))
            {
                return fiesta=true;
            }
        }
        if (mes==11)
        {
            if ((dia==1)|(dia==4)|(dia==5)|(dia==11)|(dia==12)|(dia==18)|(dia==19)|(dia==25)|(dia==26)|(dia==31))
            {
                return fiesta=true;
            }
        }
        if (mes==12)
        {
            if ((dia==2)|(dia==3)|(dia==6)|(dia==7)|(dia==8)|(dia==9)|(dia==10)|(dia==16)|(dia==17)|(dia==23)|(dia==24)|(dia==25)|(dia==26)|(dia==27)|(dia==28)|(dia==29)|(dia==30)|(dia==31))
            {
                return fiesta=true;
            }
        }
        if (mes==1)
        {
            if ((dia==1)|(dia==2)|(dia==3)|(dia==4)|(dia==5)|(dia==6)|(dia==7)|(dia==13)|(dia==14)|(dia==20)|(dia==21)|(dia==27)|(dia==28))
            {
                return fiesta=true;
            }
        }
        if (mes==2)
        {
            if ((dia==3)|(dia==4)|(dia==10)|(dia==11)|(dia==17)|(dia==18)|(dia==24)|(dia==25)|(dia==26)|(dia==27)|(dia==28)|(dia==29)|(dia==30)|(dia==31))
            {
                return fiesta=true;
            }
        }
        if (mes==3)
        {
            if ((dia==1)|(dia==2)|(dia==3)|(dia==4)|(dia==10)|(dia==11)|(dia==17)|(dia==18)|(dia==24)|(dia==25)|(dia==26)|(dia==27)|(dia==28)|(dia==29)|(dia==30)|(dia==31))
            {
                return fiesta=true;
            }
        }
        if (mes==4)
        {
            if ((dia==1)|(dia==7)|(dia==8)|(dia==14)|(dia==15)|(dia==21)|(dia==22)|(dia==28)|(dia==29)|(dia==31))
            {
                return fiesta=true;
            }
        }
        if (mes==5)
        {
            if ((dia==1)|(dia==5)|(dia==6)|(dia==12)|(dia==13)|(dia==19)|(dia==20)|(dia==26)|(dia==27))
            {
                return fiesta=true;
            }
        }
        if (mes==6)
        {
            if ((dia==2)|(dia==3)|(dia==9)|(dia==10)|(dia==16)|(dia==17))
            {
                return fiesta=true;
            }
        }

        return fiesta=false;
    }

    private boolean RecorrerFechas(int anioIni, int mesIni, int diaIni, int anioFin, int mesFin, int diaFin){
        boolean correcto;

        if (anioIni>anioFin)
        {
            return  correcto=false;
        }
        if (anioIni==anioFin)
        {
            if (mesIni>mesFin)
            {
                return  correcto=false;
            }
        }
        if (mesIni==mesFin)
        {
            if (diaIni>diaFin)
            {
                return correcto=false;
            }
        }

            for (int i = anioIni; i <= anioFin ; i++) {
                for (int j = mesIni; j <= 12; j++) {
                    for (int k = diaIni; k <= 31 ; k++) {
                        if (!DiaFiesta(j,k))
                        {
                            listaLectivos.add(k+"-"+j+"-"+i);
                        }
                        if ((k==diaFin)&(j==mesFin)&(i==anioFin))
                        {
                            return  correcto=true;
                        }
                    }
                    diaIni=1;
                }
                mesIni=1;
            }

        return correcto=true;
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

