package com.example.gabav.speechplease;

import android.content.Context;
import android.content.res.Resources;

import java.util.UUID;

/**
 * Created by CFS on 07-Jun-18.
 * This class is the (business) model for WordInfo.
 * Will encapsulate all the info that surrounds a word:
 * String of word
 * Bitmap of word
 * Syllables of word in order to set speed of utterance.
 * Points scored on word?
 * Difficulty?
 * Category?
 *
 */

class WordInfo {
    private UUID mId;
    private String strWord;
    //resource identifiers
    //private int resStrWord;
    private int resImWord;
    //will not store the bitmap here, since stored in phone storage.
    //_will instead store resId of it.

    //if we want to set the entire word again, we call the constructor again.
    public WordInfo(String strWord, Context context)
    {
        this.strWord = strWord;
        //image view supports setting image as Bitmap, Drawable, resId, and even URI.
        //will use resID for now.
        resImWord = context.getResources().getIdentifier(strWord.toLowerCase(),
                "raw", context.getPackageName());
        mId = UUID.randomUUID();
    }

    public String getStringWord()
    {
        return strWord;
    }
    public int getResImWord()
    {
        return resImWord;
    }

    public UUID getId()
    {
        return mId;
    }

}
