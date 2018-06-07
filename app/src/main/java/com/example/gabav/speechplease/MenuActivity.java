package com.example.gabav.speechplease;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

/**
 * Created by gabav on 07-Jun-18.
 */

public class MenuActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_menu);

        //fm will manage the fragment hosted by this activity
        FragmentManager fm = getSupportFragmentManager(); //we are using the
        //get the fragment at this activity's instance of frame layout.
        Fragment fragment = fm.findFragmentById(R.id.fragment__container);
        //We check if there is any fragment there, when onCreate is called.
        if(fragment == null)
        {
            //if there's not, we add one to it.
            fragment = new MenuFragment();
            fm.beginTransaction()
                    .add(R.id.fragment__container, fragment)
                    .commit();
        }

    }

}
