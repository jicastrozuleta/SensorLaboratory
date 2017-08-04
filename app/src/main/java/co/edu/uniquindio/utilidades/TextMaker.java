package co.edu.uniquindio.utilidades;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import co.edu.uniquindio.dataobject.TestObject;
/**
 * Clase que permite crear un archivo de texto con la informacion de las pruebas generadas.
 * Created by JICZ on 1/08/2017.
 */

public class TextMaker {

    /**
     * generar el archivo de texto plano en el directorio de la app.
     *
     * @return true si el archivo es generado de forma correcta, false en caso contrario.
     */
    public static void generateTextFile (final ArrayList<TestObject> testList, final String fileName) throws IOException {

        File gpxfile = new File(Utilities.directorioApp(), fileName);

        final String horaFinal = Utilities.horaActual();
        String line = "";
        // cargar archivo en buffer
        BufferedWriter buf = new BufferedWriter(new FileWriter(gpxfile, true));

        // cargar los datos en el archivo.
        for (TestObject test : testList) {
            line += test.getIdTest() + ";";
            line += test.getUsuario() + ";";
            line += test.getImei() + ";";
            line += test.getRefrerenciaDispositivo() + ";";
            line += test.getFechaPrueba() + ";";
            line += test.getHoraInicioPrueba() + ";";
            line += horaFinal + ";";
            line += test.getAltitud() + ";";
            line += test.getLongitud() + ";";
            line += test.getLatitud() + ";";
            line += test.getDt() + ";";
            line += test.getUnixTimeStamp() + ";";
            line += test.getData1() + ";";
            line += test.getData2() + ";";
            line += test.getData3() + ";";
            line += test.getData4() + ";";
            line += test.getSensor() + ";";
            line += test.getFabricante() + ";";
            line += test.getReferenciaSensor() + "\r\n";

            //agregar las lineas
            buf.append(line);
            buf.newLine();
            buf.flush();
            //limpiar la linea
            line = "";
        }

        // cerrar el buffer
        buf.close();
    }



    /**
     * Metodo para mover el archivo de texto generado de los datos, en una carpeta
     * con acceso publico para que pueda ser obtenida por medio usb.
     * @param fileName nombre del archivo
     * @param context contexto en que se ejecuta la accion
     * @return el archivo generado.
     */
    public static File moveToDownloadFolder (String fileName, Context context){
        File gpxfile = new File(Utilities.directorioApp(), fileName);
        File downloadFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File dirDownloadApp = new File(downloadFolder.getPath() + "/" + Utilities.APP_NAME);
        if (!dirDownloadApp.isDirectory()) {
            dirDownloadApp.mkdirs();
        }

        //dar permisos al la carpeta para que pueda ser modificada desde usb.
        dirDownloadApp.setWritable(true);
        dirDownloadApp.setExecutable(true);

        File dest = new File(dirDownloadApp.getAbsolutePath() + "/" + fileName);
        gpxfile.renameTo(dest);
        MediaScannerConnection.scanFile(context.getApplicationContext(), new String[] {dest.getAbsolutePath()}, null, null);
        return dest;
    }
}
