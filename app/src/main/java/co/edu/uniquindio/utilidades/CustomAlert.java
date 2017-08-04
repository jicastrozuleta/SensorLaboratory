package co.edu.uniquindio.utilidades;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import co.edu.uniquindio.sensorlaboratory.R;
/**
 * Created by JICZ on 28/07/2017.
 */

public class CustomAlert {


    /**
     * Permite crear un dialog para control de errores de la app.
     * @param message mensaje de error
     * @param context contexto enq ue se ejecuta el dialog.
     * @return dialog a mostrar.
     */
    public static Dialog onCreateErrorDialog (String message, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Error");
        builder.setIconAttribute(android.R.attr.alertDialogIcon);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick (DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        return builder.create();
    }
}
