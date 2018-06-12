package com.example.gabav.speechplease;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.Locale;
import java.util.Random;

import android.widget.Button;

import com.microsoft.cognitiveservices.speechrecognition.DataRecognitionClient;
import com.microsoft.cognitiveservices.speechrecognition.ISpeechRecognitionServerEvents;
import com.microsoft.cognitiveservices.speechrecognition.MicrophoneRecognitionClient;
import com.microsoft.cognitiveservices.speechrecognition.RecognitionResult;
import com.microsoft.cognitiveservices.speechrecognition.RecognitionStatus;
import com.microsoft.cognitiveservices.speechrecognition.SpeechRecognitionMode;
import com.microsoft.cognitiveservices.speechrecognition.SpeechRecognitionServiceFactory;


/**
 * Created by gabav on 11-Jun-18.
 */

public class TestActivity extends AppCompatActivity implements ISpeechRecognitionServerEvents
{

    /***************
     *
     */

    int m_waitSeconds = 0;
    DataRecognitionClient dataClient = null;
    MicrophoneRecognitionClient micClient = null;
    FinalResponseStatus isReceivedResponse = FinalResponseStatus.NotReceived;

    Button _buttonSelectMode;

    public enum FinalResponseStatus { NotReceived, OK, Timeout }

    //int for us to identify which intent returned.
    private final static int REQ_CODE_SPEECH_INPUT = 100;

    TextToSpeech myTTS;
    String dynamicUID = "0"; //A unique ID for every utterance (a text that is to be synthesized).

    //the mic button
    private ImageButton _startButton; //(!) changed from btnSpeak
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

    /**
     * Gets the primary subscription key
     */
    public String getPrimaryKey() {
        return this.getString(R.string.primaryKey);
    }

    /**
     * Gets a value indicating whether or not to use the microphone.
     * @return true if [use microphone]; otherwise, false.
     */
    private Boolean getUseMicrophone()
    {
        return true;
    }

    /**
     * Gets the default locale.
     * @return The default locale.
     */
    private String getDefaultLocale() {
        return "en-us";
    }

    /**
     * Gets the Cognitive Service Authentication Uri.
     * @return The Cognitive Service Authentication Uri.  Empty if the global default is to be used.
     */
    private String getAuthenticationUri() {
        return this.getString(R.string.authenticationUri);
    }

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

        //check if api key placed
        if (getString(R.string.primaryKey).startsWith("Please")) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.add_subscription_key_tip_title))
                    .setMessage(getString(R.string.add_subscription_key_tip))
                    .setCancelable(false)
                    .show();
        }

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
                                    Toast.makeText(/*com.example.gabav.speechplease.MainActivity.this*/ TestActivity.this, "Error in TextToSpeech!", Toast.LENGTH_SHORT).show();
                                }
                            });
                    }
            }//onInit
        });//new Text to speech

        myTTS.setSpeechRate(0.9f);

        _startButton = (ImageButton) findViewById(R.id.btnSpeak);
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
        /*
        _startButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //promptSpeechInput(v);
            }
        });

        */

        // setup the buttons
        final TestActivity This = this;
        //Clicking the start button will begin the service
        this._startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                This.StartButton_Click(arg0);
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


    /**
     * Handles the Click event of the _startButton control.
     */
    private void StartButton_Click(View arg0)
    {
        this._startButton.setEnabled(false);

        this.m_waitSeconds = 5;//this.getMode() == SpeechRecognitionMode.ShortPhrase ? 20 : 200;

        //make a toast that the speech recognition is starting.
        //this.LogRecognitionStart();

        if (this.getUseMicrophone()) {
            if (this.micClient == null) { //if it's the first time using mic mode

                //speechRecSerFactory gives config.
                this.micClient = SpeechRecognitionServiceFactory.createMicrophoneClient(
                        this,
                        SpeechRecognitionMode.ShortPhrase,
                        this.getDefaultLocale(),
                        this,
                        this.getPrimaryKey());

                this.micClient.setAuthenticationUri(this.getAuthenticationUri());
            }

            this.micClient.startMicAndRecognition();
        }
    }

    /**
     * Logs the recognition start.
     */
    private void LogRecognitionStart() {
        String recoSource;
        recoSource = "microphone";
        //(!) Make a toast here that voice recognition started.
        Toast.makeText(this, "speech recognition using " + recoSource + " in " + this.getDefaultLocale() + " language --", Toast.LENGTH_SHORT).show();
    }

    public void onFinalResponseReceived(final RecognitionResult response) {
        //is longDication Mode && the status is the end of dictation by user (silence) OR timed out.
        boolean isFinalDicationMessage = (response.RecognitionStatus == RecognitionStatus.EndOfDictation ||
                        response.RecognitionStatus == RecognitionStatus.DictationEndSilenceTimeout);

        //if micClient is not null (we used the mic) && is microphone mode (??) && ( ShortPhrase Mode || finished dictating) //meaning mic is done.
        /*
        if (null != this.micClient && this.getUseMicrophone() && ((this.getMode() == SpeechRecognitionMode.ShortPhrase) || isFinalDicationMessage)) {
            // we got the final result, so it we can end the mic reco.  No need to do this
            // for dataReco, since we already called endAudio() on it as soon as we were done
            // sending all the data.
            this.micClient.endMicAndRecognition();
        }
        */

        //Long Phrase Mode:
        // if (isFinalDicationMessage) {
        // this._startButton.setEnabled(true);
        // this.isReceivedResponse = FinalResponseStatus.OK;
        // }
        if(isFinalDicationMessage) {

            this.micClient.endMicAndRecognition();

            //Short Phrase Mode:
            Handler handler = new Handler();
            for (int i = 0; i < response.Results.length; i++) {
                Toast.makeText(this, "[" + i + "]" + " Confidence=" + response.Results[i].Confidence +
                        " Text=\"" + response.Results[i].DisplayText + "\"", Toast.LENGTH_SHORT).show();

                handler.postDelayed(new Runnable() {
                    public void run() {
                        // yourMethod();
                    }
                }, 1000);
            }

        }
        else
            Toast.makeText(this, "Didn't end recording!", Toast.LENGTH_SHORT).show();
        // this.WriteLine();
        // }

        //Toast.makeToast("I'm "+(response.confidence[0]*100)+"% sure that you said: "+response.Results[0]);
    }

    // public void onPartialResponseReceived(final String response) {
    // this.WriteLine("--- Partial result received by onPartialResponseReceived() ---");
    // this.WriteLine(response);
    // this.WriteLine();
    // }

    public void onError(final int errorCode, final String response)
    {
        Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
        this._startButton.setEnabled(true);
        // this.WriteLine("--- Error received by onError() ---");
        // this.WriteLine("Error code: " + SpeechClientStatus.fromInt(errorCode) + " " + errorCode);
        // this.WriteLine("Error text: " + response);
        // this.WriteLine();
    }

    /**
     * Called when the microphone status has changed.
     * @param recording The current recording state
     */
    public void onAudioEvent(boolean recording) {
        Toast.makeText(this, "--- Microphone status change received by onAudioEvent() ---", Toast.LENGTH_SHORT).show();

        Toast.makeText(this, "********* Microphone status: " + recording + " *********", Toast.LENGTH_LONG).show();
        if (recording)
        {
            Toast.makeText(this,"Please start speaking.", Toast.LENGTH_SHORT).show();
        }

        //if no longer recording, end the mic, and re-enable button.
        if (!recording) {
            this.micClient.endMicAndRecognition();
            this._startButton.setEnabled(true);
        }
    }




    /*
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
    */

    /*
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

    */

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

    //Necessary for Interface.
    public void onPartialResponseReceived(final String response) {
        //do nothing.
        Toast.makeText(this, "Partial Result!: "+response, Toast.LENGTH_LONG).show();
    }

    //Necessary for Interface
    public void onIntentReceived(final String payload) {
        Toast.makeText(this, "Intent!: "+payload, Toast.LENGTH_LONG).show();
    }
    /****************
     *
     */
}
