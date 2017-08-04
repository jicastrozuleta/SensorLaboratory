package co.edu.uniquindio.utilidades;

import android.os.Environment;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import co.edu.uniquindio.sensorlaboratory.R;

/**
 * Created by JICZ on 27/07/2017.
 */

public class Utilities {

    public static final String APP_NAME = "SensorLaboratory";


    /**
     * Identificador de sensor acelerometro
     */
    public static final String ACELEROMETRO = "Acelerometro";

    /**
     * Identificador de sensor Campo Magnetico
     */
    public static final String CAMPO_MAGNETICO = "Campo Magnetico";

    /**
     * Identificador de sensor Giroscopio
     */
    public static final String GIROSCOPIO = "Giroscopio";

    /**
     * Identificador de sensor Orientacion
     */
    public static final String ORIENTACION = "Orientacion";

    /**
     * Identificador de sensor Proximidad
     */
    public static final String PROXIMIDAD = "Proximidad";

    /**
     * Identificador de sensor Rotacion
     */
    public static final String ROTACION = "Rotacion";

    /**
     * Identificador de sensor Temperatura
     */
    public static final String TEMPERATURA = "Temperatura";

    /**
     * lista de sensores soportados por la app.
     */
    public static final String[] ARRAY_SENSORS = new String[]{
            Utilities.ACELEROMETRO,
            Utilities.CAMPO_MAGNETICO,
            Utilities.GIROSCOPIO,
            Utilities.ORIENTACION,
            Utilities.PROXIMIDAD,
            Utilities.ROTACION,
            Utilities.TEMPERATURA
    };



    /**
     * obtener la fecha actual del dispositivo.
     * @return Cadena formada por la fecha actual.
     */
    public static String fechaActual(){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd", new Locale("es","CO"));
        df.setTimeZone(TimeZone.getTimeZone("America/Bogota"));
        return df.format(Calendar.getInstance().getTime());
    }


    /**
     * obtener la hora actual del dispositivo.
     * @return Cadena formada por la hora actual.
     */
    public static String horaActual(){
        DateFormat df = new SimpleDateFormat("HH:mm:ss.SSS", new Locale("es","CO"));
        df.setTimeZone(TimeZone.getTimeZone("America/Bogota"));
        return df.format(Calendar.getInstance().getTime());
    }



    /**
     * genera un identificador a partir de la feche de ejecucion de la prueba.
     * @return
     */
    public static String generarID () {
        DateFormat df = new SimpleDateFormat("yyMMddHHmmssSSS", new Locale("es","CO"));
        df.setTimeZone(TimeZone.getTimeZone("America/Bogota"));
        return df.format(Calendar.getInstance().getTime());
    }



    /**
     * crear el directorio de almacenamiento de archivos
     * @return file
     */
    public static File directorioApp(){
        File root = Environment.getExternalStorageDirectory();
        File dirApp = new File(root.getPath() + "/" + APP_NAME);
        if (!dirApp.isDirectory()) {
            dirApp.mkdirs();
        }
        return dirApp;
    }
}
