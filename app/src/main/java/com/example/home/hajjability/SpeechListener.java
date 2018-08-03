package com.example.home.hajjability;

import android.speech.tts.UtteranceProgressListener;
import android.util.Log;

public class SpeechListener extends UtteranceProgressListener {
    home act;
    SpeechListener(home act) {
        this.act = act;
    }
    @Override
    public void onStart(String utteranceId) {
        Log.d("gazar", "Speech is starting");
    }
    @Override
    public void onError(String utteranceId) {
        Log.d("gazar", "Error in speech");

    }
    @Override
    public void onDone(String utteranceId) {
        Log.d("gazar", "Speech is done");
        act.startVoiceRecognitionActivity();
    }
}
