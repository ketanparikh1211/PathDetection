package com.example.shefaliupadhyaya.finalpathdetection;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import android.speech.tts.TextToSpeech;

import java.util.ArrayList;
import java.util.Locale;

import static com.example.shefaliupadhyaya.finalpathdetection.SecondScreen.dest;
import static com.example.shefaliupadhyaya.finalpathdetection.SecondScreen.src;

public class StepCounter extends AppCompatActivity implements SensorEventListener, TextToSpeech.OnInitListener {

    SensorManager sensorManager;
    private TextToSpeech tts;
    TextView info,steps,finalsteps,dir;
    boolean running = false;
    private int counterSteps = 0;
    GraphNode graphNode;
    MapperClass mapperClass;
    ArrayList<StepDir> information;
    int i=0,flag=0;
    int counter = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step_counter);
        tts = new TextToSpeech(this, this);
        mapperClass = new MapperClass();
        graphNode = new GraphNode(src,dest);
        information=graphNode.getInfo();
        info = (TextView)findViewById(R.id.info);
        steps = (TextView)findViewById(R.id.steps);
        finalsteps = (TextView)findViewById(R.id.finalsteps);
        dir = (TextView)findViewById(R.id.dir);
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        running = true;
        Sensor count = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(count!=null) {
            sensorManager.registerListener(this, count, SensorManager.SENSOR_DELAY_UI);
        }
        else Toast.makeText(this, "Sensor not found!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        running = false;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        try{
        StepDir sd=information.get(i);

        if(running) {

                if (counterSteps < 1) {
                    counterSteps = (int) sensorEvent.values[0];
                }
                int noOfSteps = (int) sensorEvent.values[0] - counterSteps;
                steps.setText(String.valueOf(noOfSteps));

                dir.setText(String.valueOf(sd.dir));
                if(noOfSteps == sd.step-3 && flag==0){
                    StepDir sd1=information.get(i+1);
                    int dir1=sd1.dir;
                    String direction = mapperClass.DirectionInString(dir1);
                    if(dir1==sd.dir){
                        tts.speak("Continue walk towards " + direction, TextToSpeech.QUEUE_FLUSH, null);
                    }
                    else {
                        tts.speak("After 3 steps, take " + direction, TextToSpeech.QUEUE_FLUSH, null);
                     }
                     if(i==information.size()-2)
                         flag=1;
                }
                if (noOfSteps == sd.step) {
                    if(i==(information.size()-1)){
                        counter += noOfSteps;
                        counterSteps += noOfSteps;
                        noOfSteps = 0;
                        steps.setText(String.valueOf(noOfSteps));
                        finalsteps.setText(String.valueOf(counter));
                        tts.speak("Destination is reached",TextToSpeech.QUEUE_FLUSH, null);
                        Thread.sleep(1500);
                        finishAffinity();
                    }
                    else {
                        counter += noOfSteps;
                        counterSteps += noOfSteps;
                        noOfSteps = 0;
                        sd = information.get(++i);
                        steps.setText(String.valueOf(noOfSteps));
                        finalsteps.setText(String.valueOf(counter));
                        dir.setText(String.valueOf(sd.dir));
                    }
                }
            }

        }
        catch (Exception e)
        {
            tts.speak("Something is wrong",TextToSpeech.QUEUE_FLUSH, null);
            finishAffinity();
            //Toast.makeText(this, "Reached destination", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        running = false;
        if(!running)
        {
            steps.setText(String.valueOf(0));
            //sensorManager.unregisterListener(this);
        }
        finishAffinity();
    }

    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onInit(int i) {
        if (i == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
                StepDir sd=information.get(0);
                int direc = sd.dir;
                String direction = mapperClass.DirectionInString(direc);
                tts.speak("Head " + direction, TextToSpeech.QUEUE_FLUSH, null);
            }

        } else {
            Log.e("TTS", "Initialization Failed!");
        }
    }
}
