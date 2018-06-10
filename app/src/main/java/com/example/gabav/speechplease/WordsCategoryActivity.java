package com.example.gabav.speechplease;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by gabav on 08-Jun-18.
 * This class
 */

public class WordsCategoryActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //Remove title bar
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        try
        {
            getSupportActionBar().hide();
        }
        catch(NullPointerException n)
        {
            //(H);
        }
        //Remove notification bar
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        // WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //set content view AFTER ABOVE sequence (to avoid crash)
        setContentView(R.layout.category_list_host);

        /*
            Try to set margins programmatically.
            FrameLayout fl = (FrameLayout) findViewById(R.id.fragment__container);
            fl.getLayoutParams()
         */

        //fm will manage the fragment hosted by this activity
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager(); //we are using the
        //get the fragment at this activity's instance of frame layout.
        Log.d("WrdCatAct", ">>>ActivityLaunched!");
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        //We check if there is any fragment there, when onCreate is called.
        if(fragment == null)
        {
            //if there's not, we add one to it.
            fragment = new WordsCategoryFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }
}
