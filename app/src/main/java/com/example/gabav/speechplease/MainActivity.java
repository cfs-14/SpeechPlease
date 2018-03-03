package com.example.gabav.speechplease;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.speech.SpeechRecognizer;
import android.speech.RecognizerIntent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
{

    //int for us to identify which intent returned.
    private final static int REQ_CODE_SPEECH_INPUT = 100;

    //the mic button
    private ImageButton btnSpeak;
    //the image that represents the word.
    private ImageView image4Word;
    //the current word for the user to speak.
    private TextView currWordView;

    private String currWord;
    //word retrieved by the user
    private String wordSpoken;
    //The string w/ the score for the word.
    private String confScoresStr;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);


        //getSupportActionBar().show();

        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);
        image4Word = (ImageView) findViewById(R.id.imageword);
        currWordView = (TextView) findViewById(R.id.word);

        currWord = getString(R.string.str_giraffe).toUpperCase();
        currWordView.setText(currWord);
        image4Word.setImageResource(R.drawable.giraffe);

        //checks the permissions, asks for one if not yet granted.
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},0);//requestcode to id the intent to call it. //chose 0 bc to not needed.
        }

        //button click for speech
        btnSpeak.setOnClickListener(new View.OnClickListener()
        {
              @Override
            public void onClick(View v)
              {
                  promptSpeechInput(v);
              }
        });
    }//onCreate

    private void promptSpeechInput(View view)
    {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speech_prompt));
        intent.putExtra(RecognizerIntent.EXTRA_PREFER_OFFLINE, true);

        //check if speech supported or not.
        if(intent.resolveActivity(getPackageManager()) != null)
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        else
            Toast.makeText(this, getString(R.string.speech_not_supported), Toast.LENGTH_SHORT).show();

    }//promptSpeechInput

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data)
    {
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode)
        {
            case REQ_CODE_SPEECH_INPUT:
                if(resultCode == RESULT_OK && data != null)
                {
                    //get the string of words said by person
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    //check if the string(s) are there
                    confScoresStr = getConfidenceScoresAsString(data.getFloatArrayExtra(RecognizerIntent.EXTRA_CONFIDENCE_SCORES));

                   if(!result.isEmpty()) {
                       wordSpoken = extractFirstWord_Format(result.get(0));
                       //Toast.makeText(this, "Correct! " + wordSpoken + ": "+confScoresStr+"%", Toast.LENGTH_SHORT).show();
                   }
                    //(A)

                    //(A)
                     correctOrIncorrect(currWord.compareTo(wordSpoken));
                }
                break;
        }
    }//onActivityResult


    public String getConfidenceScoresAsString(float[] confScores)
    {
        String temp = String.valueOf(confScores[0]);
        if(temp.length() >= 4)
            return String.valueOf(confScores[0]).substring(2,4);
        return temp;
    }//
    public String extractFirstWord_Format(String str)
    {
        str.toUpperCase();
        return Array.get(str.split("\\s"), 0).toString().toUpperCase();
    }//
    public void correctOrIncorrect(int i)
    {
        switch (i)
        {
            case 0: //correct
            {
                Toast.makeText(this, "Correct! " + wordSpoken + ": "+confScoresStr+"%.", Toast.LENGTH_LONG).show();
                break;
            }
            default:
            {
                Toast.makeText(this, "Incorrect! You said "+wordSpoken+".", Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }//correctOrIncorrect
}
