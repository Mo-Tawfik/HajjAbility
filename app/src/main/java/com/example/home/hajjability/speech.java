package com.example.home.hajjability;

import android.content.Intent;
import android.graphics.Color;
import android.speech.tts.TextToSpeech;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vikramezhil.droidspeech.DroidSpeech;
import com.vikramezhil.droidspeech.OnDSListener;
import com.vikramezhil.droidspeech.OnDSPermissionsListener;
import com.ybs.countrypicker.CountryPicker;
import com.ybs.countrypicker.CountryPickerListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class speech extends AppCompatActivity implements View.OnClickListener, OnDSListener, OnDSPermissionsListener {
    public final String TAG = "DroidSpeech";
    DroidSpeech droidSpeech;
    Button transcribe_btn;
    List<String> supportedSpeechLanguages;

    TextToSpeech t1;
    EditText ed1;
    Button b1, b2;


    String arabicLetters;
    String englishLetters;
    ArrayList<String> englistLettersArray = new ArrayList<>();

    @Override
    protected void onDestroy() {
        droidSpeech.closeDroidSpeechOperations();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech);

        TextView bar;
        bar = (TextView) findViewById(R.id.textView_toolbar);
        bar.setText("Talk for me");

        new navbar().create((DrawerLayout) this.findViewById(R.id.drawer_layout), speech.this);

        setupDroidSpeech();

        setupTextToSpeech();
    }

    void setupTextToSpeech() {

        ed1 = (EditText) findViewById(R.id.my_editText);
        b1 =(Button)findViewById(R.id.button);
        b2 = (Button)findViewById(R.id.button2);

        b1.setOnClickListener(this);
        b2.setOnClickListener(this);

        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status!=TextToSpeech.ERROR)
                {
                    t1.setLanguage(Locale.US);
                }

            }
        });
    }

    public String toFranko(String ArabicText)
    {
        String EnglishText = null;

        String transWord = "";
        // Change inputted Arabic text to English letters
        arabicLetters = "ءاأبتثجحخدذرزسشصضطظعغفقكلمنهويةئه";
        englishLetters = new StringBuilder("aaaywhnmlkkfgaztdssszrzdahgstbaaa").reverse().toString();


        ///////////////////////////////////////////
        if (ArabicText.isEmpty() == false) {
            englistLettersArray.clear();
            String phase = ArabicText;
            String[] splited = phase.split("\\s+");
            for (String splited1 : splited) {    // loop across this array of strings
                for (int x = 0; x < splited1.length(); x++) {   // for a single string loop through its characters
                    String lett = String.valueOf(splited1.charAt(x));
                    if (arabicLetters.contains(lett)) {
                        int index = arabicLetters.indexOf(lett);    // identify the place of this character
                        String engLet = String.valueOf(englishLetters.charAt(index));
                        englistLettersArray.add(engLet);
                    }
                }
                englistLettersArray.add("");
            }
            for (int x = 0; x < englistLettersArray.size(); x++) {
                String letter = englistLettersArray.get(x).toString();
                if (letter.equals("")) {
                    transWord = transWord + " " + letter;
                } else {
                    transWord = transWord + letter;
                }
            }
        }

        ////////////////////////////////////////////
        return transWord;
    }

    public void onPause()
    {
        super.onPause();
        if (t1 != null) {
            t1.stop();
            t1.shutdown();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_transcribe:
                startTranscribing();
                break;
            case R.id.home:
                Intent int1 = new Intent(this, home.class);
                startActivity(int1);
                break;
            case R.id.settings:
                Intent int2 = new Intent(this, settings.class);
                startActivity(int2);
                break;
            case R.id.logoff:
                Intent int3 = new Intent(this, login.class);
                startActivity(int3);
                break;

            case R.id.button:   // English
            {
                String toSpeak = ed1.getText().toString();
                Toast.makeText(getApplicationContext(), toSpeak, Toast.LENGTH_SHORT).show();
                t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            }
            break;

            case R.id.button2:     // Arabic
            {
                Toast.makeText(getApplicationContext(), "amaaaarrrr", Toast.LENGTH_SHORT).show();
                String toSpeak = ed1.getText().toString();
                String franko= toFranko(toSpeak);
                Toast.makeText(getApplicationContext(), franko, Toast.LENGTH_SHORT).show();
                t1.speak(franko, TextToSpeech.QUEUE_FLUSH, null);
            }
            break;

        }
    }

    void startTranscribing() {
        Log.d(TAG, "Starting to listen");
        final CountryPicker picker = CountryPicker.newInstance("Select Laguage by Country Dialect ");  // dialog title
        picker.setListener(new CountryPickerListener() {
            @Override
            public void onSelectCountry(String name, String code, String dialCode, int flagDrawableResID) {
                Log.d(TAG, "Country selected: " + name + " code: " + code);
                picker.dismiss();
                String lang = new Language(code).toString();
                if(speech.this.supportedSpeechLanguages.contains(lang)) {
                    speech.this.droidSpeech.setPreferredLanguage(lang);
                    droidSpeech.startDroidSpeechRecognition();
                } else {
                    Toast.makeText(speech.this, "This country's language dialect will be supported in the future. \nTry a different country with the same language.", Toast.LENGTH_LONG).show();
                }
            }
        });
        picker.show(getSupportFragmentManager(), "COUNTRY_PICKER");
    }

    void setupDroidSpeech() {
        droidSpeech = new DroidSpeech(this, getFragmentManager());
        droidSpeech.setOnDroidSpeechListener(this);
        droidSpeech.setContinuousSpeechRecognition(false);
        droidSpeech.setShowRecognitionProgressView(true);
        droidSpeech.setOneStepResultVerify(true);
        droidSpeech.setRecognitionProgressMsgColor(Color.WHITE);
        droidSpeech.setOneStepVerifyConfirmTextColor(Color.WHITE);
        droidSpeech.setOneStepVerifyRetryTextColor(Color.WHITE);
        //droidSpeech.setPreferredLanguage("en-US");

        transcribe_btn = findViewById(R.id.start_transcribe);
        transcribe_btn.setOnClickListener(this);



        /*
        droidSpeech.setShowRecognitionProgressView(true);
        int[] colors = new int[] {Color.RED, Color.GREEN, Color.BLUE, Color.CYAN, Color.MAGENTA};
        droidSpeech.setRecognitionProgressViewColors(colors);
        droidSpeech.setRecognitionProgressMsgColor(Color.WHITE);
        */
    }

    @Override
    public void onDroidSpeechRmsChanged(float rmsChangedValue) {
     //   Log.d(TAG, "RMS Changed " +  Float.toString(rmsChangedValue));

    }
    @Override
    public void onDroidSpeechFinalResult(String finalResult) {
        Log.d(TAG, "Final Result: " + finalResult);

    }
    @Override
    public void onDroidSpeechLiveResult(String liveResult) {
        Log.d(TAG, "Live Result: " + liveResult);

    }
    @Override
    public void onDroidSpeechClosedByUser() {
        Log.d(TAG, "Speech Closed By User");

    }
    @Override
    public void onDroidSpeechSupportedLanguages(String currentSpeechLanguage, List<String> supportedSpeechLanguages) {
        Log.i(TAG, "Current speech language = " + currentSpeechLanguage);
        Log.i(TAG, "Supported speech languages = " + supportedSpeechLanguages.toString());

        this.supportedSpeechLanguages = supportedSpeechLanguages;

        if(supportedSpeechLanguages.contains("ar-EG")) {
            // Setting the droid speech preferred language as Egyptian Arabic if found
            droidSpeech.setPreferredLanguage("ar-EG");
        }
    }
    @Override
    public void onDroidSpeechError(String errorMsg)
    {
        // Speech error
        Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();


    }

    @Override
    public void onDroidSpeechAudioPermissionStatus(boolean audioPermissionGiven, String errorMsgIfAny)
    {
        if (audioPermissionGiven) {
            Log.d(TAG, "Permission given");
            droidSpeech.startDroidSpeechRecognition();
        } else {
            Toast.makeText(this, "Please enable voice commands to use this feature", Toast.LENGTH_LONG).show();
            Log.d(TAG, "Permission not given");

        }
    }
}
