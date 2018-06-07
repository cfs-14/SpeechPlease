package com.example.gabav.speechplease;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.speech.SpeechRecognizer;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;

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
import java.util.Random;

/*
    TO DO:
    +Make an SQLite w/ the strings of words so that they may be fetched using their id
        and a random int. O/w will have to see how to have the pool of viable words to pick from.
        One way is to have a marker in the string names in the res/values/strings.xml so we search
        for (all/some) of them in the beginning, and add them to the pool, also keeping a list of
        the used ones.
    +In the case that they say two or more words, yet they said the word, then the toast should
        say remind them that they should only say the single word and to try again.

    +Add animations to it.
        +

    +   Experiment w/ voices & other languages
        Check if words are one syllable to make them slower. Or can check the length of time it takes
        to play, somehow, perhaps beforehand when new word loaded?, and set the speechRate.

        setPitch(float pitch)
        Sets the speech pitch for the TextToSpeech engine.

        int	setSpeechRate(float speechRate)
        Sets the speech rate.

        int	setVoice(Voice voice)
        Sets the text-to-speech voice.

 */

public class MainActivity extends AppCompatActivity
{

    //int for us to identify which intent returned.
    private final static int REQ_CODE_SPEECH_INPUT = 100;

    TextToSpeech myTTS;
    String dynamicUID = "0"; //A unique ID for every utterance (a text that is to be synthesized).

    //the mic button
    private ImageButton btnSpeak;
    private ImageButton rightButton;
    //ToDo: animate, must go up Y, then shake slowly left to right twice, then stop and come back /
    //down, like a hand-waving motion.
    private ImageButton speakerButton;

    //the image that represents the word.
    private ImageView image4Word;
    //the current word for the user to speak.
    private TextView currWordView;

    private String currWordStr;
    //word retrieved by the user
    private String wordSpoken;
    //The string w/ the score for the word.
    private String confScoresStr;
    //the array that holds the viable strings
    private String[] wordsPool;

    private int randomInt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        try
        {
            getSupportActionBar().hide();
        }
        catch(NullPointerException n)
        {
            //(H);
        }
        setContentView(R.layout.activity_main);


        //getSupportActionBar().show();
        myTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status)
            {
                if(status == TextToSpeech.SUCCESS)
                    if(myTTS != null)
                    {
                        int result = myTTS.setLanguage(Locale.getDefault());
                        //expect it to work, simply for this project
                        if(result == TextToSpeech.LANG_COUNTRY_VAR_AVAILABLE )
                            myTTS.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                                //(!) Has more useful methods to override, and write to file capabilities.
                                //(!) To do something with utterances, use UtteranceID.
                                @Override
                                public void onStart(String s) //called before audio is played
                                {
                                    ;
                                }

                                @Override
                                public void onDone(String s) //called when audio is done playing
                                {
                                    ;
                                }

                                @Override
                                public void onError(String s)
                                {
                                    Toast.makeText(MainActivity.this, "Error in TextToSpeech!", Toast.LENGTH_SHORT);
                                }
                            });
                    }
            }//onInit
        });//new Text to speech

        myTTS.setSpeechRate(0.9f);

        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);
        rightButton = (ImageButton) findViewById(R.id.right_button);
        speakerButton = (ImageButton) findViewById(R.id.speaker_button);

        image4Word = (ImageView) findViewById(R.id.imageword);
        currWordView = (TextView) findViewById(R.id.word);

        //get the string arrays & sets up the pool of words to choose from
        wordsPool = getResources().getStringArray(R.array.animal_words);
        updateWordsAndViews();

        //(T)(To do): create a list of words/images already used so they don't appear again.

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
        speakerButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                //increment the utteranceID <String>
                dynamicUID = String.valueOf((Integer.parseInt(dynamicUID)+1));
                //bundle not needed to be saved for reuse all the time
                Bundle params = new Bundle();
                //Bundle to give to speak() to assign the Utterance ID to
                params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, dynamicUID);
                //speakWords(currWordView, TextToSpeech.ADD, dynamicUID);
                myTTS.speak(currWordStr, TextToSpeech.QUEUE_FLUSH, params, dynamicUID);
            }
        });
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateWordsAndViews();
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
                     correctOrIncorrect(currWordStr.compareTo(wordSpoken));
                }
                break;
        }
    }//onActivityResult


    public String getConfidenceScoresAsString(float[] confScores)
    {
        String temp = String.valueOf(confScores[0]);
        if(temp.length() >= 5)
            return String.valueOf(confScores[0]).substring(2,5);
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

    /*
    private void speakWords(View v)
    {


    }//speak_word
    */

    private boolean updateWordsAndViews()
    {
        randomInt = new Random().nextInt(wordsPool.length);

        //gets a random word from the pool & sets the textview
        currWordStr = wordsPool[randomInt].toUpperCase();
        currWordView.setText(currWordStr);

        //(!) get max length of utterance. currWordView.setText("Max Utterance Length: "+String.valueOf(TextToSpeech.getMaxSpeechInputLength());
        //sets the imageview
        if(currWordStr.contentEquals("DRAGON"))
        {
            image4Word.setImageResource(getResources().getIdentifier(currWordStr.toLowerCase(),
                    "raw", getPackageName()));
        }
        else
        {
            image4Word.setImageResource(getResources().getIdentifier(currWordStr.toLowerCase(),
                    "drawable", getPackageName()));
        }
        //(T) look for errors here?
        return true;
    }
}
