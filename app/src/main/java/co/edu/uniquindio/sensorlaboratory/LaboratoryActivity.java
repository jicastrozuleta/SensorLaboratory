package co.edu.uniquindio.sensorlaboratory;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import co.edu.uniquindio.dataobject.TestObject;
import co.edu.uniquindio.utilidades.CustomAlert;
import co.edu.uniquindio.utilidades.TextMaker;
import co.edu.uniquindio.utilidades.Utilities;

public class LaboratoryActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * Time for doubleclick event
     */
    private long mLastClickTime = 0;

    /**
     * Constante para pasar de nano segundos a segundos.
     */
    private static final float NANOS_TO_SECONDS = 1.0f / 1000000000.0f;


    /**
     * Identificador de la prueba que se ejecuta.
     */
    private String idTest;

    /**
     * tiempo de captura de datos de sensor inicial.
     */
    private float timestamp = 0f;

    /**
     * SEGUNDOS Antes de iniciar la prueba.
     */
    private static final int SECONDS = 16;


    /**
     * Intervalo de conteto, uno en uno.
     */
    private static final int INTERVAL = 1;

    /**
     * base para iniciar el conteo del cronometro
     */
    private static final long BASE = 1000 * SECONDS;

    /**
     * Tag describe activity
     */
    private final String TAG = getClass().toString();

    /**
     * TAG para acceso de datos extras nombre de sensor
     */
    public static final String NAME_SENSOR = "nameSensor";

    /**
     * TAG para acceso de datos extras tipo de sensor
     */
    public static final String TYPE_SENSOR = "typeSensor";

    /**
     * TAG para acceso de datos extras posicion de sensor
     */
    public static final String POSITION = "position";

    /**
     * TAG para acceso de imei
     */
    public static final String IMEI = "imei";

    /**
     * TAG para acceso de datos del dispositivo
     */
    public static final String DEVICE_MODEL = "devicemodel";

    /**
     * TAG para fecha de inicio de la prueba
     */
    public static final String CURRENT_DATE = "fechaactual";

    /**
     * TAG para hora de inicio de la prueba
     */
    public static final String CURRENT_TIME = "horaactual";

    /**
     * TAG para lista de pruebas
     */
    public static final String TEST_LIST = "testlist";

    /**
     * TAG para usuario logeado
     */
    public static final String USER = "usuario";

    /**
     * TAG para nombre dle archivo de datos.
     */
    public static final String FILE_NAME = "nombrearchivo";

    /**
     * Nombre del sensor que se esta usando para la prueba de laboratorio actual.
     */
    private String nameSensor;

    /**
     * Tipo de sensor en SensorManager id.
     */
    private int typeSensor;

    /**
     * posicion en la lista de sensores.
     */
    private int position;


    /**
     * imei
     */
    private String imei;

    /**
     * informacion del dispositivo.
     */
    private String device_model;


    /**
     * informacion de la fecha actual.
     */
    private String currentDate;

    /**
     * informacion de la hora actual.
     */
    private String currentTime;

    /**
     * usuario logeado
     */
    private String user;

    /**
     * Referencia para el administrador de sensores.
     */
    private SensorManager sensorManager;


    /**
     * Administrador de gps.
     */
    private LocationManager locationManager;

    /**
     * Referencia al sensor seleccionado.
     */
    private Sensor sensor;


    /**
     * referencia a la coordenada capturada en la prueba.
     */
    protected Location coordenada;

    /**
     * Contador timer para control de inicio de las pruebas.
     */
    private CounterTask counterTask;

    /**
     * lista que contiene las pruebas que se realizan.
     */
    private ArrayList<TestObject> testList;

    /**
     * nomnbre del archivo de datos.
     */
    private String fileName;





    @Override
    protected void onCreate (Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laboratory);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*Instanciar la lista de objetos de prueba*/
        testList = new ArrayList<>();

        /*Cargar informacion necesaria de sensor seleccionado*/
        getExtras();

        /*iniciar el sensor seleccionado*/
        iniciarSensor();

        /*Instanciar fab button para controlar el 'stop' de la prueba.*/
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabButtonWork);
        fab.setOnClickListener(fabListener);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string
                .navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        drawer.openDrawer(GravityCompat.START);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        String title = this.nameSensor + " :\n" + this.sensor.getVendor() + " " + this.sensor.getName();
        ((TextView) navigationView.getHeaderView(0).findViewById(R.id.textViewNavHeaderNameSensor)).setText(title);
        navigationView.setNavigationItemSelectedListener(this);
    }




    /**
     * extraer datos extras desde {@link MainActivity}
     */
    private void getExtras () {

        try {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                /*Cargar nombre de sensor*/
                if (extras.containsKey(NAME_SENSOR))
                    this.nameSensor = extras.getString(NAME_SENSOR);
                else
                    throw new Exception("");

                /*Cargar tipo de sensor*/
                if (extras.containsKey(TYPE_SENSOR))
                    this.typeSensor = extras.getInt(TYPE_SENSOR);
                else
                    throw new Exception("");

                /*Cargar posicion en lista de sensores*/
                if (extras.containsKey(POSITION))
                    this.position = extras.getInt(POSITION);
                else
                    throw new Exception("");

                /*cargar el imei del dispositivo*/
                if (extras.containsKey(IMEI))
                    this.imei = extras.getString(IMEI);
                else
                    throw new Exception("");

                /*cargar el imei del dispositivo*/
                if (extras.containsKey(DEVICE_MODEL))
                    this.device_model = extras.getString(DEVICE_MODEL);
                else
                    throw new Exception("");

                /*cargar fecha inicial */
                if (extras.containsKey(CURRENT_DATE))
                    this.currentDate = extras.getString(CURRENT_DATE);
                else
                    throw new Exception("");

                /*cargar hora inicial*/
                if (extras.containsKey(CURRENT_TIME))
                    this.currentTime = extras.getString(CURRENT_TIME);
                else
                    throw new Exception("");

                /*cargar la informacion del usuario logeado*/
                if (extras.containsKey(USER))
                    this.user = extras.getString(USER);
                else
                    this.user = "invitado";

                 /*cargar nombre del archivo de datos*/
                if (extras.containsKey(FILE_NAME))
                    this.fileName = extras.getString(FILE_NAME);
                else
                    throw new Exception("");

            } else {
                throw new Exception("");
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error cargando datos de sensor. Intente nuevamente.", Toast.LENGTH_SHORT).show();
            this.finish();
        }
    }




    @Override
    protected void onSaveInstanceState (Bundle outState) {

        outState.putString(NAME_SENSOR, this.nameSensor);
        outState.putInt(TYPE_SENSOR, this.typeSensor);
        outState.putInt(POSITION, this.position);
        outState.putString(IMEI, this.imei);
        outState.putString(DEVICE_MODEL, this.device_model);
        outState.putString(CURRENT_DATE, this.currentDate);
        outState.putString(CURRENT_TIME, this.currentTime);
        outState.putString(USER, this.user);
        outState.putParcelableArrayList(TEST_LIST, this.testList);
        outState.putString(FILE_NAME, this.fileName);
        super.onSaveInstanceState(outState);
    }



    @Override
    protected void onRestoreInstanceState (Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);
        /*Si el estado ha sido guardado intentar restaurar la informacion de la actividad*/
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(NAME_SENSOR)) {
                this.nameSensor = savedInstanceState.getString(NAME_SENSOR);
            }
            if (savedInstanceState.containsKey(TYPE_SENSOR)) {
                this.typeSensor = savedInstanceState.getInt(TYPE_SENSOR);
            }
            if (savedInstanceState.containsKey(POSITION)) {
                this.position = savedInstanceState.getInt(POSITION);
            }
            if (savedInstanceState.containsKey(IMEI)) {
                this.imei = savedInstanceState.getString(IMEI);
            }
            if (savedInstanceState.containsKey(DEVICE_MODEL)) {
                this.device_model = savedInstanceState.getString(DEVICE_MODEL);
            }
            if (savedInstanceState.containsKey(CURRENT_DATE)) {
                this.currentDate = savedInstanceState.getString(CURRENT_DATE);
            }
            if (savedInstanceState.containsKey(CURRENT_TIME)) {
                this.currentTime = savedInstanceState.getString(CURRENT_TIME);
            }
            if (savedInstanceState.containsKey(USER)) {
                this.user = savedInstanceState.getString(USER);
            }
            if (savedInstanceState.containsKey(TEST_LIST)) {
                this.testList = savedInstanceState.getParcelableArrayList(TEST_LIST);
            }
            if (savedInstanceState.containsKey(FILE_NAME)) {
                this.fileName = savedInstanceState.getString(FILE_NAME);
            }
        }
    }



    @Override
    public void onBackPressed () {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }



    @Override
    protected void onResume () {

        /*iniciar el sensor seleccionado*/
        iniciarSensor();
        /*Iniciar el servicio de GPS para captura de la coordenada*/
        iniciarGPS();
        super.onResume();
    }



    /**
     * iniciar el administrador de sensores
     */
    private void iniciarSensor () {

        if (this.sensorManager == null) {
        /*Instanciar el administrador de sensores */
            this.sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        }
        /*Sensor diferente a orientation*/
        if (typeSensor != 0) {
            if (this.sensor == null) {
                 /*Instanciar el sensor */
                this.sensor = sensorManager.getDefaultSensor(typeSensor);
            }
        }
    }



    @Override
    protected void onDestroy () {

        /*Si la app se cierra se detienen la deteccion de coordenadas.*/
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
        super.onDestroy();
    }



    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.laboratory, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_exit) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected (@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_guardar_prueba:
                guardarPruebaGenerada();
                break;

            case R.id.nav_nueva_prueba:
                break;

            case R.id.nav_share:
                compartirArchivo();
                break;

            case R.id.nav_usb:
                saveFileDownloadFolder();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    /**
     * compartir archivo por los medios disponibles para tratar con archivos de texto.
     *
     */
    private void compartirArchivo () {
        File gpxfile = new File(Utilities.directorioApp(), fileName);
        if(gpxfile.exists()){
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(gpxfile));
            startActivity(intent);
        }
        else {
            CustomAlert.onCreateErrorDialog("No se ha guardado el archivo de datos.",this).show();
        }
    }



    /**
     * Guardar el archivo con los datos obtenidos en la carpeta de descargas download
     */
    private void saveFileDownloadFolder () {
        if(fileName!=null){
            File dest = TextMaker.moveToDownloadFolder(fileName, this);
            if(dest.exists()){
                Toast.makeText(this,"USB OK! - Archivo generado en Download", Toast.LENGTH_LONG).show();
            }
        }
    }




    /**
     * metodo que crea el archivo de texto con formato csv.
     * con la informacion generada del sensor probado.
     */
    private void guardarPruebaGenerada () {

        /*Intentar crear el archivo de texto con los datos obtenidos del sensor.*/
        try {
            if(this.testList != null && !testList.isEmpty() && fileName != null){
                TextMaker.generateTextFile(this.testList, fileName);
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                Toast.makeText(this, "Archivo generado correctamente", Toast.LENGTH_LONG).show();
                this.testList.clear();
            }
            else {
                throw new Exception("No hay datos para generar archivo de registros.");
            }
        }
        catch (Exception e){
            CustomAlert.onCreateErrorDialog(e.getMessage(),this).show();
        }
    }



    /**
     * Inciar la captura de la coordenada con el GPS del dispositivo.
     */
    private void iniciarGPS () {
        /* Instanciar referencia para locaclizacion con gps.*/
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        /*Registrar el listener para capturar los eventos de captura de coordenada*/
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            try {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    //Conservar la coordenada de cache ofrecida por la red, mientras se captura una coordenada mas precisa..
                    setCoordenada(locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));
                } else {
                    mostrarGpsApagado();
                }
            } catch (Exception e) {
                CustomAlert.onCreateErrorDialog(e.getMessage(), this).show();
            }
        } else {
            Toast.makeText(this, TAG + " no tiene permisos para usar Localizacion.", Toast.LENGTH_SHORT).show();
            this.finish();
        }
    }



    /**
     * alert para informar al usuario de gps apagado
     */
    private void mostrarGpsApagado () {

        this.findViewById(R.id.imageViewGps).setBackground(getResources().getDrawable(R.mipmap.ic_gps_fail, null));
    }



    /**
     * Listener para captura de coordenada.
     */
    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged (Location location) {

            setCoordenada(location);
        }



        @Override
        public void onStatusChanged (String s, int i, Bundle bundle) {

        }



        @Override
        public void onProviderEnabled (String s) {

            if (ActivityCompat.checkSelfPermission(LaboratoryActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            //Conservar la coordenada de cache ofrecida por la red, mientras se captura una coordenada mas precisa..
            setCoordenada(locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));
        }



        @Override
        public void onProviderDisabled (String s) {

            mostrarGpsApagado();
        }
    };


    /**
     * conservar coordenada capturada.
     * @param coordenada coordenada capturada por lo medios de localizacion disponibles.
     */
    public void setCoordenada (final Location coordenada) {

        /*Conservar la coordenada actual, si es nula*/
        if (this.coordenada == null && coordenada != null)
            this.coordenada = coordenada;

        /*
         * Si la precision es mayor o igual al 80% no se requiere mas captura de coordenadas
         */
        if (coordenada == null) {
            this.findViewById(R.id.imageViewGps).setBackground(getResources().getDrawable(R.mipmap.ic_gps_fail, null));
        } else if (coordenada.getAccuracy() >= 80) {
            locationManager.removeUpdates(locationListener);
            this.findViewById(R.id.imageViewGps).setBackground(getResources().getDrawable(R.mipmap.ic_gps_succes, null));
            this.coordenada = coordenada;
        } else {
            this.findViewById(R.id.imageViewGps).setBackground(getResources().getDrawable(R.mipmap.ic_gps_captured, null));
            /*Conservar la coordenada con mejor precision*/
            if (this.coordenada.getAccuracy() < coordenada.getAccuracy())
                this.coordenada = coordenada;
        }
    }



    /**
     *
     * @param view view button event
     */
    public void onClickIniciarPrueba(View view) {

        /*Controlar el evento de doble-click*/
        if (doubleClick()) {
            return;
        }

        /*Iniciar el contador*/
        TextView textViewCounter = (TextView) this.findViewById(R.id.textViewiniciar);
        textViewCounter.setEnabled(false);
        counterTask = new CounterTask(BASE, INTERVAL, textViewCounter, testListener);
        counterTask.start();
    }



    /**
     * Listener del fab Button para detener la prueba actual.
     */
    private View.OnClickListener fabListener = new View.OnClickListener() {

        @Override
        public void onClick (View view) {
            /*Si el contador esta iniciado, detener la prueba actual.*/
            if (counterTask != null) {
                counterTask.cancel();
                Snackbar.make(view, R.string.detener_prueba, Snackbar.LENGTH_LONG).show();
                TextView textView = (TextView) LaboratoryActivity.this.findViewById(R.id.textViewiniciar);
                textView.setText(R.string.iniciar_prueba);
                textView.setEnabled(true);

                /*terminar el registro de captura de datos del sensor.*/
                if(sensorManager != null) {
                    sensorManager.unregisterListener(sensorEventListener);
                    timestamp = 0f;
                }
            }
        }
    };


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
     * Interface para iniciar la captura de datos del sensor.
     */
    private ITest testListener = new ITest() {
        @Override
        public void test () {
            registerCaptureSensor();
        }
    };



    /**
     * se inicia la captura de datos del sensor seleccionado.
     */
    private void registerCaptureSensor () {
        /*Generar el id de la prueba que se va a iniciar*/
        this.idTest = Utilities.generarID();
        iniciarSensor();
        this.sensorManager.registerListener(sensorEventListener, this.sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }



    /**
     * listener de captura de eventos de sensor.
     */
    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged (SensorEvent sensorEvent) {
            saveSensorEvent(sensorEvent);
        }



        @Override
        public void onAccuracyChanged (Sensor sensor, int i) {

        }
    };



    /**
     * guardar la informacion
     * @param sensorEvent datos ofrecidos por el sensor.
     */
    private void saveSensorEvent (final SensorEvent sensorEvent) {

        // inicializar el diferencial de tiempo (en segundos) a cero.
        float dT = 0;

        // validar si es el primer dato que se registra.
        if(timestamp != 0){
            /*Diferencial de tiempo entre captura de datos. conversion a segundos*/
            dT = (sensorEvent.timestamp - timestamp) * NANOS_TO_SECONDS;
        }
        /*Conservar la informacion de captura del sensor*/
        testList.add( new TestObject(
                this.idTest,
                this.user,
                this.imei,
                this.device_model,
                this.currentDate,
                this.currentTime,
                Utilities.horaActual(),
                String.valueOf((this.coordenada != null)? this.coordenada.getAltitude() : 0.0),
                String.valueOf((this.coordenada != null)? this.coordenada.getLongitude(): 0.0),
                String.valueOf((this.coordenada != null)? this.coordenada.getLatitude() : 0.0),
                String.valueOf(dT),
                String.valueOf(sensorEvent.timestamp),
                String.valueOf(sensorEvent.values[0]),
                String.valueOf(sensorEvent.values[1]),
                String.valueOf(sensorEvent.values[2]),
                String.valueOf(sensorEvent.accuracy),
                sensorEvent.sensor.getStringType(),
                sensorEvent.sensor.getVendor(),
                sensorEvent.sensor.getName()
        ));

        /*Actualizar el tiempo de captura del ultimo evento*/
        timestamp = sensorEvent.timestamp;
    }
}
