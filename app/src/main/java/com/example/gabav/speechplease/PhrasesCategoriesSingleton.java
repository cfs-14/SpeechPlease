package com.example.gabav.speechplease;

import android.content.Context;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by gabav on 09-Jun-18.
 */

public class PhrasesCategoriesSingleton
{

    //we have only one instance of this singleton class that will hold the phrasesCategories
    //the single instance of a list of phrasesCategories
    private static PhrasesCategoriesSingleton categoriesSingleton;

    private Set<String> phrasesCategories;

    //this acts as constructor for this class
    public static PhrasesCategoriesSingleton get(Context context)
    {
        if(categoriesSingleton == null)
        {
            categoriesSingleton = new PhrasesCategoriesSingleton(context);
        }
        return categoriesSingleton;
    }

    private PhrasesCategoriesSingleton(Context context)
    {
        phrasesCategories = new LinkedHashSet<>();
        //(!) get the list of phrasesCategories from somewhere?
        //_or hardcode them here? O/w they would eventually be hardcoded.

        phrasesCategories.add("Greetings");
        phrasesCategories.add("Personal");
        phrasesCategories.add("Food");
        phrasesCategories.add("City"); //store bank bathroom
        phrasesCategories.add("House");
        phrasesCategories.add("Politics");
        phrasesCategories.add("Work");
        phrasesCategories.add("Conversational"); //Clouds
        phrasesCategories.add("Random"); //mixed
        phrasesCategories.add("Sex");
        phrasesCategories.add("Pokemon");
        phrasesCategories.add("School");
        phrasesCategories.add("One");
        phrasesCategories.add("Two");
        phrasesCategories.add("Four");
    }

    public Set<String> getPhrasesCategories()
    {
        return phrasesCategories;
    }

    public String getCategoryStr(String category)
    {
        for(String str : phrasesCategories)
        {
            if(str.equalsIgnoreCase(category))
                return str;
        }

        return null;
    }

    public int getCategoryPos(String category)
    {
        for(String str : phrasesCategories)
        {
            if(str.equalsIgnoreCase(category))
                return (new ArrayList<String>(phrasesCategories)).indexOf(str);
        }

        return -1;
    }

    public String getCategoryAtPos(int pos)
    {
        List<String> temp = new ArrayList<String>(phrasesCategories);
        if(0 <= pos && pos < temp.size())
            return temp.get(pos);
        return null;
    }
}
