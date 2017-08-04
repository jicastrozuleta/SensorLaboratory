package co.edu.uniquindio.dataobject;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Objeto que representa el paquete de datos que sera persistido.
 * Created by JICZ on 1/08/2017.
 */

public class TestObject implements Parcelable {


    /**
     * identificador de las pruebas que se ejecutan
     */
    private String idTest;

    /**
     * Usuario logeado que genera la prueba.
     */
    private String usuario;

    /**
     * imei del telefono que realiza la prueba
     */
    private String imei;

    /**
     * referencia del dispositivo que realiza la prueba.
     */
    private String refrerenciaDispositivo;

    /**
     * fecha en que se realiza la prueba.
     */
    private String fechaPrueba;

    /**
     * hora en que se inicia la prueba.
     */
    private String horaInicioPrueba;

    /**
     * hora en que finaliza la prueba.
     */
    private String horaFinPrueba;

    /**
     * localizacion altitud
     */
    private String altitud;

    /**
     * localizacion longitud
     */
    private String longitud;

    /**
     * localizacion latitud
     */
    private String latitud;

    /**
     * diferencial de tiempo en segundos entre muestras.
     */
    private String dT;

    /**
     * tiempo unix en que se captura el dato de sensor.
     */
    private String unixTimeStamp;

    /**
     * dato 1 del sensor (o eje X)
     */
    private String data1;

    /**
     * dato 2 del sensor (o eje Y)
     */
    private String data2;

    /**
     * dato 3 del sensor (o eje Z)
     */
    private String data3;

    /**
     * dato 4 del sensor (o dato adicional)
     */
    private String data4;

    /**
     * sensor usado para la prueba.
     */
    private String sensor;

    /**
     * fabricante del sensor
     */
    private String fabricante;

    /**
     * referencia del sensor
     */
    private String referenciaSensor;



    /**
     * Constructor de objeto test
     * @param usuario usuario
     * @param imei imei
     * @param refrerenciaDispositivo referencia del movil
     * @param fechaPrueba fecha de la prueba
     * @param horaInicioPrueba hora de inicio de la prueba
     * @param horaFinPrueba hora de finalizacion de la prueba
     * @param altitud altitud de localizacion
     * @param longitud longitud de localizacion
     * @param latitud latitud de localizacion
     * @param unixTimeStamp tiempo unix POSIX
     * @param data1 dato 1 del sensor
     * @param data2 dato 2 del sensor
     * @param data3 dato 3 del sensor
     * @param data4 dato 4 del sensor
     * @param sensor sensor usado para la prueba
     * @param fabricante fabricante del sensor
     * @param referenciaSensor referencia del sensor.
     */
    public TestObject (@NonNull String idTest,
                       @NonNull String usuario,
                       @NonNull String imei,
                       @NonNull String refrerenciaDispositivo,
                       @NonNull String fechaPrueba,
                       @NonNull String horaInicioPrueba,
                       @NonNull String horaFinPrueba,
                       @NonNull String altitud,
                       @NonNull String longitud,
                       @NonNull String latitud,
                       @NonNull String dT,
                       @NonNull String unixTimeStamp,
                       @NonNull String data1,
                       @NonNull String data2,
                       @NonNull String data3,
                       @NonNull String data4,
                       @NonNull String sensor,
                       @NonNull String fabricante,
                       @NonNull String referenciaSensor
    ) {
        this.idTest = idTest;
        this.usuario = usuario;
        this.imei = imei;
        this.refrerenciaDispositivo = refrerenciaDispositivo;
        this.fechaPrueba = fechaPrueba;
        this.horaInicioPrueba = horaInicioPrueba;
        this.horaFinPrueba = horaFinPrueba;
        this.altitud = altitud;
        this.longitud = longitud;
        this.latitud = latitud;
        this.dT = dT;
        this.unixTimeStamp = unixTimeStamp;
        this.data1 = data1;
        this.data2 = data2;
        this.data3 = data3;
        this.data4 = data4;
        this.sensor = sensor;
        this.fabricante = fabricante;
        this.referenciaSensor = referenciaSensor;
    }


    public String getIdTest () {

        return idTest;
    }



    public void setIdTest (String idTest) {

        this.idTest = idTest;
    }



    public String getUsuario () {

        return usuario;
    }



    public void setUsuario (String usuario) {

        this.usuario = usuario;
    }



    public String getImei () {

        return imei;
    }



    public void setImei (String imei) {

        this.imei = imei;
    }



    public String getRefrerenciaDispositivo () {

        return refrerenciaDispositivo;
    }



    public void setRefrerenciaDispositivo (String refrerenciaDispositivo) {

        this.refrerenciaDispositivo = refrerenciaDispositivo;
    }



    public String getFechaPrueba () {

        return fechaPrueba;
    }



    public void setFechaPrueba (String fechaPrueba) {

        this.fechaPrueba = fechaPrueba;
    }



    public String getHoraInicioPrueba () {

        return horaInicioPrueba;
    }



    public void setHoraInicioPrueba (String horaInicioPrueba) {

        this.horaInicioPrueba = horaInicioPrueba;
    }



    public String getHoraFinPrueba () {

        return horaFinPrueba;
    }



    public void setHoraFinPrueba (String horaFinPrueba) {

        this.horaFinPrueba = horaFinPrueba;
    }



    public String getAltitud () {

        return altitud;
    }





    public void setAltitud (String altitud) {

        this.altitud = altitud;
    }



    public String getLongitud () {

        return longitud;
    }

    public void setDt (String dT) {

        this.dT = dT;
    }


    public String getDt  () {

        return dT;
    }



    public void setLongitud (String longitud) {

        this.longitud = longitud;
    }



    public String getLatitud () {

        return latitud;
    }



    public void setLatitud (String latitud) {

        this.latitud = latitud;
    }



    public String getUnixTimeStamp () {

        return unixTimeStamp;
    }



    public void setUnixTimeStamp (String unixTimeStamp) {

        this.unixTimeStamp = unixTimeStamp;
    }



    public String getData1 () {

        return data1;
    }



    public void setData1 (String data1) {

        this.data1 = data1;
    }



    public String getData2 () {

        return data2;
    }



    public void setData2 (String data2) {

        this.data2 = data2;
    }



    public String getData3 () {

        return data3;
    }



    public void setData3 (String data3) {

        this.data3 = data3;
    }



    public String getData4 () {

        return data4;
    }



    public void setData4 (String data4) {

        this.data4 = data4;
    }



    public String getSensor () {

        return sensor;
    }



    public void setSensor (String sensor) {

        this.sensor = sensor;
    }



    public String getFabricante () {

        return fabricante;
    }



    public void setFabricante (String fabricante) {

        this.fabricante = fabricante;
    }



    public String getReferenciaSensor () {

        return referenciaSensor;
    }



    public void setReferenciaSensor (String referenciaSensor) {

        this.referenciaSensor = referenciaSensor;
    }



    @Override
    public int describeContents () {

        return 0;
    }



    @Override
    public void writeToParcel (Parcel parcel, int i) {
        parcel.writeString(this.idTest);
        parcel.writeString(this.usuario);
        parcel.writeString(this.imei);
        parcel.writeString(this.refrerenciaDispositivo);
        parcel.writeString(this.fechaPrueba);
        parcel.writeString(this.horaInicioPrueba);
        parcel.writeString(this.horaFinPrueba);
        parcel.writeString(this.altitud);
        parcel.writeString(this.longitud);
        parcel.writeString(this.latitud);
        parcel.writeString(this.dT);
        parcel.writeString(this.unixTimeStamp);
        parcel.writeString(this.data1);
        parcel.writeString(this.data2);
        parcel.writeString(this.data3);
        parcel.writeString(this.data4);
        parcel.writeString(this.sensor);
        parcel.writeString(this.fabricante);
        parcel.writeString(this.referenciaSensor);
    }



    /**
     * constructor estatico para crear el objeto parcelable.
     */
    public static final Parcelable.Creator<TestObject> CREATOR = new Parcelable.Creator<TestObject>() {
        public TestObject createFromParcel (Parcel in) {

            return new TestObject(in);
        }



        public TestObject[] newArray (int size) {

            return new TestObject[size];
        }
    };



    /**
     * Crear el objeto desde el Parcelable.
     * @param in
     */
    private TestObject(Parcel in) {
        this.idTest = in.readString();
        this.usuario = in.readString();
        this.imei = in.readString();
        this.refrerenciaDispositivo = in.readString();
        this.fechaPrueba = in.readString();
        this.horaInicioPrueba = in.readString();
        this.horaFinPrueba = in.readString();
        this.altitud = in.readString();
        this.longitud = in.readString();
        this.latitud = in.readString();
        this.dT = in.readString();
        this.unixTimeStamp = in.readString();
        this.data1 = in.readString();
        this.data2 = in.readString();
        this.data3 = in.readString();
        this.data4 = in.readString();
        this.sensor = in.readString();
        this.fabricante = in.readString();
        this.referenciaSensor = in.readString();
    }
}
