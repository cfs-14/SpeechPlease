package com.example.gabav.speechplease;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by gabav on 07-Jun-18.
 */

public class MenuFragment extends Fragment {
    //this fragment will hold the menu.
    //imageView for logo
    ImageView imageViewLogo;
    //Button for words
    Button wordButton;
    //Button for phrases
    Button phrasesButton;
    //background
    AnimationDrawable background;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_menu, container, false);

        //workaround to have background as a gif.
        background = (AnimationDrawable) v.getBackground();
        background.start();

        imageViewLogo = (ImageView) v.findViewById(R.id.imageword);

        wordButton = (Button) v.findViewById(R.id.btnWordsPractice);

        phrasesButton = (Button) v.findViewById(R.id.btnPhrasesPractice);

        //add Listeners here in order to start the corresponding activities.
        wordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(getActivity(), WordsCategoryActivity.class));
            }
        });

        phrasesButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                
            }
        });

        return v;
    }
}
