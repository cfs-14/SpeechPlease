package com.example.gabav.speechplease;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by gabav on 09-Jun-18.
 */

public class PhrasesCategoryActivity extends AppCompatActivity
{
    //In this activity we host the fragment recyclerView

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //remove actionBar.
        try
        {
            getSupportActionBar().hide();
        }
        catch(NullPointerException n)
        {
            //(H);
        }

        setContentView(R.layout.category_words_fragment);

        //set up the fragmentManager.
        FragmentManager fragmentManager = getSupportFragmentManager();

        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_containero);

        if(fragment == null)
        {
            fragment = new PhrasesCategoryFragment();

            fragmentManager.beginTransaction().add(R.id.fragment_containero, fragment)
                    .commit();
        }
    }
}
