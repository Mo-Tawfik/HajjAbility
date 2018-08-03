package com.example.home.hajjability;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class home extends AppCompatActivity implements View.OnClickListener {

    EditText ed;
    TextView tv;
    private static final int REQUEST_CODE = 1234;
    Button speak;
    TextToSpeech t1;
    SpeechListener s1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        s1 = new SpeechListener(this);

        TextView bar;
        bar = (TextView) findViewById(R.id.textView_toolbar);
        bar.setText("Home");
        new navbar().create((DrawerLayout) this.findViewById(R.id.drawer_layout), home.this);
        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                /*if(i != TextToSpeech.ERROR)
                {
                    t1.setLanguage(Locale.getDefault());

                }
                else
                {
                    Log.d("gazarERROR", "error");
                }*/
                if(i == TextToSpeech.SUCCESS)
                {
                    t1.setLanguage(Locale.getDefault());
                  //   Log.d("gazar", "Setting listener");
                    //        t1.setOnUtteranceProgressListener(s1);

                    t1.speak("For ordering a wheelchair, say wheelchair. " +
                            "For requesting a volunteer, say help." +
                            "For contacting with a sheikh, say fatwa", TextToSpeech.QUEUE_FLUSH, null, "instructions");
                }

            }
        });
        Log.d("gazar", "Setting listener");
        t1.setOnUtteranceProgressListener(s1);

        //t1.speak("For ordering a wheelchair, say wheelchair. ", TextToSpeech.QUEUE_FLUSH, null);
      //  startVoiceRecognitionActivity();
    }

    private void voiceThingy()
    {

        // Disable button if no recognition service is present
        PackageManager pm = getPackageManager();
        List< ResolveInfo > activities = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() == 0)
        {
            speak.setEnabled(false);
            speak.setText("Recognizer not present");
        }
        ed.addTextChangedListener(new TextWatcher()
        {@
                Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {
            // TODO Auto-generated method stub
        }@
                Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {
            // TODO Auto-generated method stub
        }@
                Override
        public void afterTextChanged(Editable s)
        {
            // TODO Auto-generated method stub
            speak.setEnabled(true);
        }
        });
    }
    /**
     * Fire an intent to start the voice recognition activity.
     */
    void startVoiceRecognitionActivity()
    {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Voice searching...");
        startActivityForResult(intent, REQUEST_CODE);

    }
    /**
     * Handle the results from the voice recognition activity.
     */
    @
            Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK)
        {
            // Populate the wordsList with the String values the recognition engine thought it heard
            final ArrayList< String > matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (!matches.isEmpty())
            {
                String Query = matches.get(0);
                //ed.setText(Query);
                Log.v("Ummrah", Query);
                voiceAssist(Query);
                //speak.setEnabled(false);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void voiceAssist(String query) {
        if(query.contains(getString(R.string.chair_command)) || query.contains(getString(R.string.wheel_command)))
        {
            Intent int1 = new Intent(this, chair.class);
            startActivity(int1);
        }
        Log.d("Ummrah", "Command is help");
        Log.d("UmmrahHelp", String.valueOf(query.indexOf(getString(R.string.help_command))));

        if(query.contains(getString(R.string.help_command)) || query.contains(getString(R.string.volunteer_command)))
        {
            Log.d("Ummrah", "Handling help command");
            Intent int1 = new Intent(this, volunteer.class);
            startActivity(int1);
        }
    }

    public void onPause()
    {
        if(t1 != null)
        {
            t1.stop();
            t1.shutdown();
        }
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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

        }
    }

}
