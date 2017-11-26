package com.example.shefaliupadhyaya.finalpathdetection;

import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.WindowManager;

import com.daimajia.androidanimations.library.Techniques;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

import java.util.Locale;

public class MainActivity extends AwesomeSplash implements TextToSpeech.OnInitListener{

    private TextToSpeech tts;
    @Override
    public void initSplash(ConfigSplash configSplash) {
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        tts = new TextToSpeech(this, this);
        configSplash.setBackgroundColor(R.color.bg_splash);
        configSplash.setAnimCircularRevealDuration(2000);
        configSplash.setRevealFlagX(Flags.REVEAL_RIGHT);
        configSplash.setRevealFlagY(Flags.REVEAL_BOTTOM);

        configSplash.setLogoSplash(R.drawable.shoes);
        configSplash.setAnimLogoSplashDuration(3000);
        configSplash.setAnimLogoSplashTechnique(Techniques.Flash);

        configSplash.setTitleSplash("Path Finder");
        configSplash.setTitleTextColor(R.color.white);
        configSplash.setTitleTextSize(50f);
        configSplash.setAnimTitleDuration(2000);
        configSplash.setAnimTitleTechnique(Techniques.RotateInDownRight);

    }

    @Override
    public void animationsFinished() {
        startActivity(new Intent(MainActivity.this,SecondScreen.class));
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
            }
            else {
                tts.speak("Hello friends! Welcome to your very own path finder! This app will help you find your path.", TextToSpeech.QUEUE_FLUSH, null);
            }

        } else {
            Log.e("TTS", "Initialization Failed!");
        }
    }
}
