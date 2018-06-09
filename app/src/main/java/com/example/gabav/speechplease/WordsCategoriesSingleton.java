package com.example.gabav.speechplease;

import android.content.Context;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by gabav on 08-Jun-18.
 */

public class WordsCategoriesSingleton
{
    //we have only one instance of this singleton class that will hold the wordCategories
    //the single instance of a list of wordCategories
    private static WordsCategoriesSingleton categoriesSingleton;

    private Set<String> wordCategories;

    //this acts as constructor for this class
    public static WordsCategoriesSingleton get(Context context)
    {
        if(categoriesSingleton == null)
        {
            categoriesSingleton = new WordsCategoriesSingleton(context);
        }
        return categoriesSingleton;
    }

    private WordsCategoriesSingleton(Context context)
    {
        wordCategories = new LinkedHashSet<>();
        //(!) get the list of wordCategories from somewhere?
        //_or hardcode them here? O/w they would eventually be hardcoded.


        wordCategories.add("Animals");
        wordCategories.add("House");
        wordCategories.add("Foods");
        wordCategories.add("City"); //store bank bathroom
        wordCategories.add("Celebrities");
        wordCategories.add("Clothing");
        wordCategories.add("Descriptors");
        wordCategories.add("Things"); //Clouds
        wordCategories.add("Random"); //mixed
        wordCategories.add("Sex");
        wordCategories.add("Pokemon");
        wordCategories.add("Carlos");

    }

    public Set<String> getWordCategories()
    {
        return wordCategories;
    }

    public String getCategoryStr(String category)
    {
        for(String str : wordCategories)
        {
            if(str.equalsIgnoreCase(category))
                return str;
        }

        return null;
    }

    public int getCategoryPos(String category)
    {
        for(String str : wordCategories)
        {
            if(str.equalsIgnoreCase(category))
                return (new ArrayList<String>(wordCategories)).indexOf(str);
        }

        return -1;
    }

    public String getCategoryAtPos(int pos)
    {
        List<String> temp = new ArrayList<String>(wordCategories);
        if(0 <= pos && pos < temp.size())
            return temp.get(pos);
        return null;
    }
}
