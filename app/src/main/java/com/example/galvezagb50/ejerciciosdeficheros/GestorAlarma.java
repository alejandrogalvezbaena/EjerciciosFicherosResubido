package com.example.galvezagb50.ejerciciosdeficheros;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by galvezagb50.
 */
public class GestorAlarma
{
    private int minutos;
    private String frase;
    private String nombreFichero;
    private String nuevoContacto;
    private Context contexto;
    private static File miFichero;

    public int getMinutos() {
        return minutos;
    }

    public void setMinutos(int minutos) {this.minutos = minutos;}

    public String getFrase() {
        return frase;
    }

    public void setFrase(String frase) {
        this.frase = frase;
    }

    public String getNombreFichero() {return nombreFichero;}

    public String getNuevoContacto() {return nuevoContacto;}

    public Context getContexto() {return contexto;}

    public static File getMiFichero() {return miFichero;}

    public static void setMiFichero(File miFichero) {GestorAlarma.miFichero = miFichero;}

    public GestorAlarma(int m, String f, Context c, String nFile)
    {
        this.minutos=m;
        this.frase=f;
        this.contexto=c;
        this.nombreFichero=nFile;
        this.nuevoContacto="********************************************\nMinutos: "+this.getMinutos()+"\nFrase: "+this.getFrase()+"\n";
    }

    public String CrearAlarma()
    {
        File tarjeta;
        tarjeta = Environment.getExternalStorageDirectory();
        this.setMiFichero(new File(tarjeta.getAbsolutePath(), this.getNombreFichero()));
        EscribirAlarma(this.getMiFichero());
        return MostrarPropiedades(this.getMiFichero());
    }

    public boolean BorrarTodos()
    {
        return this.getMiFichero().delete();
    }

    private void EscribirAlarma(File f)
    {
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        BufferedWriter out = null;
        try {
            fos = new FileOutputStream(f, true);
            osw = new OutputStreamWriter(fos, "UTF-8");
            out = new BufferedWriter(osw);
            out.append(this.getNuevoContacto());
        } catch (IOException e) {
            Log.e("Error de E/S", e.getMessage());
        }
        finally
        {
            try
            {
                if (out != null)
                {
                    out.close();
                }
            } catch (IOException e) {
                Log.e("Error al cerrar", e.getMessage());
            }
        }
    }

    private String MostrarPropiedades (File f) {
        SimpleDateFormat formato = null;
        StringBuffer txt = new StringBuffer();
        try {
            if (f.exists()) {
                txt.append("Nombre: " + f.getName() + '\n');
                txt.append("Ruta: " + f.getAbsolutePath() + '\n');
                txt.append("Tamaño (bytes): " + Long.toString(f.length()) + '\n');
                formato = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss", Locale.getDefault());
                txt.append("Fecha: " + formato.format(new Date(f.lastModified())) + '\n');
            } else
                txt.append("No existe el fichero " + f.getName() + '\n');
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            txt.append(e.getMessage());
        }
        return txt.toString();
    }

    public String LeerInterna() {
        FileInputStream fis = null;
        StringBuilder miCadena = new StringBuilder();
        int n;
        try
        {
            fis = new FileInputStream(getMiFichero());
            while ((n = fis.read()) != -1)
                miCadena.append((char) n);
        } catch (Exception e) {}

        return miCadena.toString();
    }


}
