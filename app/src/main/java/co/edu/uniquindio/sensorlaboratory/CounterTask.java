package co.edu.uniquindio.sensorlaboratory;

import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.widget.TextView;

/**
 * Created by JICZ on 31/07/2017.
 */

public class CounterTask extends CountDownTimer{


    /**
     * Textview para mostrar el conteo de tiempo
     */
    private TextView textView;

    /**
     * interface para iniciar la captura de datos del sensor
     */
    private ITest listener;

    public CounterTask (long millisInFuture, long countDownInterval, TextView textView, ITest testListener) {
        super(millisInFuture, countDownInterval);
        this.textView = textView;
        this.listener = testListener;
    }



    @Override
    public void onTick (long millisUntilFinished) {
        String v = String.format("%02d", millisUntilFinished/60000);
        int va = (int)( (millisUntilFinished%60000)/1000);
        textView.setText(String.format("%02d",va));
    }



    @Override
    public void onFinish () {
        textView.setText("!");
        listener.test();
    }
}
