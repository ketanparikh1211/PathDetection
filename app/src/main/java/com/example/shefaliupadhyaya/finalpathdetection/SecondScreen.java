package com.example.shefaliupadhyaya.finalpathdetection;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ebanx.swipebtn.OnStateChangeListener;
import com.ebanx.swipebtn.SwipeButton;

import java.util.ArrayList;
import java.util.Locale;

public class SecondScreen extends AppCompatActivity implements TextToSpeech.OnInitListener{

    private TextView txtSpeechInput, textView;
    private ImageButton btnSpeak1;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private TextToSpeech tts;
    private static int flag1 = 0, flag2 = 0;
    public static String source, destination;
    SwipeButton swipe;
    MapperClass mapperClass;
    public static int src = -1, dest = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_screen);

        txtSpeechInput = (TextView) findViewById(R.id.txtSpeechInput);
        btnSpeak1 = (ImageButton) findViewById(R.id.btnSpeak1);
        swipe = (SwipeButton)findViewById(R.id.swipe);
        swipe.setEnabled(false);
        textView = (TextView)findViewById(R.id.text);
        textView.setText("Tap on Mic");
        tts = new TextToSpeech(this, this);
        mapperClass = new MapperClass();

        btnSpeak1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });
        swipe.setOnStateChangeListener(new OnStateChangeListener() {
            @Override
            public void onStateChange(boolean active) {
                if(flag2==1) {
                    flag1 = flag2 = 0;
                  Intent i = new Intent(getApplicationContext(), StepCounter.class);
                    startActivity(i);
                }
            }
        });
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txtSpeechInput.setText(result.get(0));
                    if(flag1==0)
                    {   source = result.get(0);
                        flag1=1;
                        src = mapperClass.NodeInInteger(source);
                        if(src==-1) {
                            flag1=0;
                            tts.speak("Please re-enter your origin", TextToSpeech.QUEUE_FLUSH, null);
                        }
                    }
                    else if(flag1==1) {
                        destination = result.get(0);
                        dest = mapperClass.NodeInInteger(destination);
                        flag1=0;
                        if(dest==-1) {
                            flag1=1;
                            tts.speak("Please re-enter your destination", TextToSpeech.QUEUE_FLUSH, null);
                        }
                        else flag2=1;
                    }
                    if(flag2==1)
                        tts.speak("Now swipe right at the bottom of your screen to know your route. The app will start counting your steps as soon as you start walking.", TextToSpeech.QUEUE_FLUSH, null);
                        swipe.setEnabled(true);
                }
                break;
            }
        }
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
                tts.speak("Tap on the microphone at the center each time to provide your origin and destination", TextToSpeech.QUEUE_FLUSH, null);
                btnSpeak1.setEnabled(true);
            }

        } else {
            Log.e("TTS", "Initialization Failed!");
        }
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}
