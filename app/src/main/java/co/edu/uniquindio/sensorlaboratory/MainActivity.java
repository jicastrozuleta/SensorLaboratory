package co.edu.uniquindio.sensorlaboratory;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import co.edu.uniquindio.utilidades.CustomAlert;
import co.edu.uniquindio.utilidades.Utilities;


public class MainActivity extends AppCompatActivity {

    /**
     * Tag describe activity
     */
    private final String TAG = getClass().toString();

    /**
     * Constante para identificar extras en intent de laboratorio
     */
    private final String SENSOR = "sensor";

    /**
     * Constante para indentificar extras tipo de sensor
     */
    private final String TYPE_SENSOR = "typsensor";

    /**
     * Peticion para actividad de laboratorio
     */
    private static final int REQUEST_LABORATORY = 0x0A;

    /**
     * Time for doubleclick event
     */
    private long mLastClickTime = 0;


    /**
     * administrador de sensores disponibles en el dispositivo.
     */
    private SensorManager mSensorManager;

    /**
     * Lista que contiene la lista de sensores disponibles en el dispositivo actual.
     */
    private ArrayList<String> listaSensoresDisponibles;


    /**
     * Nombre del archivo generado para guardar datos de las pruebas.
     */
    private static String fileName;



    @Override
    protected void onCreate (Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        /*Mostrar el mensaje de bienvenida en la ventana principal*/
        mostrarMensajeCardView();

        /*Instanciar boton flotante y listener para evento de click*/
        FloatingActionButton floatButtonExperimentar = (FloatingActionButton) this.findViewById(R.id.floatingActionButton);
        floatButtonExperimentar.setOnClickListener(listenerFloatButton);
    }



    @Override
    protected void onResume () {
        fileName = getImei() + ".txt";
        super.onResume();
    }



    /**
     * Definicion de listener para capturar evento click del boton flotante.
     */
    private View.OnClickListener listenerFloatButton = new View.OnClickListener() {
        @Override
        public void onClick (View view) {
            /*controlar evento dobleclick*/
            if (doubleClick()) {
                return;
            }

            /*Iniciar captura de click*/
            switch (view.getId()) {
                case R.id.floatingActionButton:
                    onClickFloatButtonExperimentar();
                    break;
            }
        }
    };



    /**
     * Evento floatButton experimentar
     */
    private void onClickFloatButtonExperimentar () {

        try {
            /*Instanciar el administrador de sensores */
            this.mSensorManager = (SensorManager) getSystemService(this.SENSOR_SERVICE);

            /*Mostrar la lista de sensores para elegir con cual realizar la prueba*/
            elegirSensorParaExperimetar();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }



    /**
     * Muestra un dialog para que el usuario elija uno de los sensores
     * disponibles para realizar las pruebas de laboratorio.
     */
    private void elegirSensorParaExperimetar () {

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom));
        builder.setCancelable(false);
        builder.setTitle(R.string.elegir_sensor);
        builder.setItems(Utilities.ARRAY_SENSORS, new DialogInterface.OnClickListener() {
            @Override
            public void onClick (DialogInterface dialogInterface, int position) {

                dialogInterface.dismiss();
                validarDisponinilidadDelSensor(position);
            }
        });
        builder.setNegativeButton("Salir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick (DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
        /*Define el color del texto de los botones del <Dialog> para que sea acorde al <Theme> actual*/
        ((Button) alert.getButton(DialogInterface.BUTTON_NEGATIVE)).setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
    }



    /**
     * Permite validar la disponibilidad del hardware de sensor en el dispositivo.
     * @param position
     */
    private void validarDisponinilidadDelSensor (int position) {

        boolean isSensorAvailable = false;
        int typeSensor = Integer.MIN_VALUE;
        switch (Utilities.ARRAY_SENSORS[position]) {
            case Utilities.ACELEROMETRO:
                isSensorAvailable = checkHardwareSensor(Sensor.TYPE_ACCELEROMETER);
                typeSensor = Sensor.TYPE_ACCELEROMETER;
                break;

            case Utilities.CAMPO_MAGNETICO:
                isSensorAvailable = checkHardwareSensor(Sensor.TYPE_MAGNETIC_FIELD);
                typeSensor = Sensor.TYPE_MAGNETIC_FIELD;
                break;

            case Utilities.GIROSCOPIO:
                isSensorAvailable = checkHardwareSensor(Sensor.TYPE_GYROSCOPE);
                typeSensor = Sensor.TYPE_GYROSCOPE;
                break;

            case Utilities.ORIENTACION:
                /*El sensor de orientacion esta disponible a traves de software, no requiere hardaware
                * y no es necesaria su validacion.*/
                isSensorAvailable = true;
                typeSensor = 0;
                break;

            case Utilities.PROXIMIDAD:
                isSensorAvailable = checkHardwareSensor(Sensor.TYPE_PROXIMITY);
                typeSensor = Sensor.TYPE_PROXIMITY;
                break;

            case Utilities.ROTACION:
                isSensorAvailable = checkHardwareSensor(Sensor.TYPE_ROTATION_VECTOR);
                typeSensor = Sensor.TYPE_ROTATION_VECTOR;
                break;

            case Utilities.TEMPERATURA:
                isSensorAvailable = checkHardwareSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
                typeSensor = Sensor.TYPE_AMBIENT_TEMPERATURE;
                break;
        }

        /*Iniciar la prueba.*/
        iniciarLaboratorio(Utilities.ARRAY_SENSORS[position], typeSensor, isSensorAvailable, position);
    }



    /**
     * Permite iniciar la prueba de laboratorio al sensor elegido por el usuario.
     * Si el sensor no esta disponible, se informa al usuario del error ocurrido.
     * @param nameSensor
     * @param typeSensor
     * @param isSensorAvailable
     */
    private void iniciarLaboratorio (String nameSensor, int typeSensor, boolean isSensorAvailable, int position) {

        try {
            if (isSensorAvailable) {
                Intent intent = new Intent(this, LaboratoryActivity.class);
                intent.putExtra(LaboratoryActivity.NAME_SENSOR,nameSensor);
                intent.putExtra(LaboratoryActivity.TYPE_SENSOR,typeSensor);
                intent.putExtra(LaboratoryActivity.POSITION,position);
                intent.putExtra(LaboratoryActivity.IMEI,getImei());
                intent.putExtra(LaboratoryActivity.DEVICE_MODEL, Build.MANUFACTURER + " - " + Build.MODEL);
                intent.putExtra(LaboratoryActivity.CURRENT_DATE, Utilities.fechaActual());
                intent.putExtra(LaboratoryActivity.CURRENT_TIME, Utilities.horaActual());
                //intent.putExtra(LaboratoryActivity.USER, "usuario");
                intent.putExtra(LaboratoryActivity.FILE_NAME, getFileName());
                startActivityForResult(intent, REQUEST_LABORATORY);
                return;
            }
            throw new Exception("El dispositivo no cuenta con hardware para realizar prueba de " + nameSensor);
        } catch (Exception e) {
            CustomAlert.onCreateErrorDialog(e.getMessage(), MainActivity.this).show();
        }
    }



    /**
     * Metodo que comprueba la disponibilidad de un sensor en el dispositivo.
     * @param typeSensor
     *
     * @return true si existe el sensor el dispositivo y puede ser usado, false en caso contrario.
     */
    private boolean checkHardwareSensor (int typeSensor) {

        return mSensorManager.getDefaultSensor(typeSensor) != null;
    }



    /**
     * Permite mostrar el mensaje de bienvenida en la pantalla principal de la app.
     */
    private void mostrarMensajeCardView () {

        WebView webViewText = (WebView) this.findViewById(R.id.webViewCardViewInit);
        webViewText.loadData(getString(R.string.textoCarviewInicial), "text/html; charset=utf-8", "utf-8");
    }



    /**
     * Evento de login
     * @param view boton que dispara el evento
     */
    public void onClickButtonLogin (View view) {

        if (!doubleClick()) {
            try {
                Toast.makeText(this, "Realizando login..", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }



    /**
     * Control de eventos dobleclick.
     * @return true cuando es evento dobleclick, false caso contrario.
     */
    public boolean doubleClick () {

        if (SystemClock.elapsedRealtime() - mLastClickTime < 2000) {
            return true;
        } else {
            mLastClickTime = SystemClock.elapsedRealtime();
            return false;
        }
    }



    /**
     * Obtener id del movil.
     * @return imei o id android del movil que ejecuta la app
     */
    private String getImei(){
        String identifier = null;
        TelephonyManager tm = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm != null)
            identifier = tm.getDeviceId();
        if (identifier == null || identifier .length() == 0)
            identifier = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        return identifier;
    }



    /**
     * nombre del archivo de datos
     * @return nombre del archivo de datos.
     */
    public static String getFileName () {
        return fileName;
    }
}
