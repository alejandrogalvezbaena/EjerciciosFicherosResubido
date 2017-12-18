package com.example.galvezagb50.ejerciciosdeficheros;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by galvezagb50.
 */

public class GestorPersona {
    private String nombre;
    private String telefono;
    private String email;
    private String nombreFichero;
    private String nuevoContacto;
    private Context contexto;
    private static File miFichero;
    private static final String PATTERN_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";


    public String getNombre() {
        return nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getEmail() {
        return email;
    }

    public Context getContexto() {
        return contexto;
    }

    public String getNombreFichero() {
        return nombreFichero;
    }

    public String getNuevoContacto() {
        return nuevoContacto;
    }

    public static File getMiFichero() {
        return GestorPersona.miFichero;
    }

    public void setMiFichero(File miFichero) {
        this.miFichero = miFichero;
    }

    public GestorPersona(String n, String t, String e, Context c, String nFile)
    {
        this.nombre=n;
        this.telefono=t;
        this.email=e;
        this.contexto=c;
        this.nombreFichero=nFile;
        this.nuevoContacto="********************************************\nNombre: "+this.getNombre()+"\nTelefono: "+this.getTelefono()+"\nEmail: "+this.getEmail()+"\n";

    }

    public String CrearPersona()
    {
        this.setMiFichero(new File(this.getContexto().getFilesDir(), this.getNombreFichero()));
        EscribirPersona(this.getMiFichero());
        return MostrarPropiedades(this.getMiFichero());
    }

    public static boolean ComprobarEmail(String e)
    {
        Pattern pattern = Pattern.compile(PATTERN_EMAIL);
        Matcher matcher = pattern.matcher(e);
        return matcher.matches();
    }

    public boolean BorrarTodos()
    {
        return this.getMiFichero().delete();
    }

    private void EscribirPersona(File f)
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


