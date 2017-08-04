/*
* Codigo basado en https://github.com/googlesamples/android-AccelerometerPlay.
*
* observaciones de licencia:
*
*
* Copyright (C) 2010 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
* */


package co.edu.uniquindio.sensorlaboratory;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class AcelerometerActivity extends AppCompatActivity {


    /**
     * View para mostrar datos de eje x
     */
    private TextView mTextViewEjeX;

    /**
     * View para mostrar datos de eje y
     */
    private TextView mTextViewEjeY;

    /**
     * View para mostrar datos de eje z.
     */
    private TextView mTextViewEjeZ;

    /**
     * Administrador de sensores.
     * Permite acceder y hacer uso de los sensores disponibles en dispositivo.
     */
    private SensorManager mSensorManager;

    /**
     * referencia al sensor acelerometro del dispositivo.
     */
    private Sensor mAcelerometer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acelerometer);

        /*Instancias views.*/
        mTextViewEjeX = (TextView) this.findViewById(R.id.textViewEjeX);
        mTextViewEjeY = (TextView) this.findViewById(R.id.textViewEjeY);
        mTextViewEjeZ = (TextView) this.findViewById(R.id.textViewEjeZ);

        /*Obtener la instancia de SensorManager*/
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        /*Obtener la instancia del sensor de tipo acelerometro*/
        mAcelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }



    @Override
    protected void onResume() {
        /*Iniciar la captura de datos generada por el sensor*/
        mSensorManager.registerListener(acelerometerListener, mAcelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        super.onResume();
    }



    /**
     * referencia al listener de captura de eventos del sensor
     */
    private SensorEventListener acelerometerListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            /*Si el evento generado no es del acelerometro, se descarta el dato*/
            if (sensorEvent.sensor.getType() != Sensor.TYPE_ACCELEROMETER)
                return;

            mTextViewEjeX.setText(String.valueOf(sensorEvent.values[0]));
            mTextViewEjeY.setText(String.valueOf(sensorEvent.values[1]));
            mTextViewEjeZ.setText(String.valueOf(sensorEvent.values[2]));
        }



        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };
}
