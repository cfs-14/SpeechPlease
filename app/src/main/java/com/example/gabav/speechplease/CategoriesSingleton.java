package com.example.gabav.speechplease;

import android.content.Context;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by gabav on 08-Jun-18.
 */

public class CategoriesSingleton
{
    //we have only one instance of this singleton class that will hold the categories
    //the single instance of a list of categories
    private static CategoriesSingleton categoriesSingleton;

    private Set<String> categories;

    //this acts as constructor for this class
    public static CategoriesSingleton get(Context context)
    {
        if(categoriesSingleton == null)
        {
            categoriesSingleton = new CategoriesSingleton(context);
        }
        return categoriesSingleton;
    }

    private CategoriesSingleton(Context context)
    {
        categories = new LinkedHashSet<>();
        //(!) get the list of categories from somewhere?
        //_or hardcode them here? O/w they would eventually be hardcoded.


        categories.add("Animals");
        categories.add("House");
        categories.add("Foods");
        categories.add("City"); //store bank bathroom
        categories.add("Celebrities");
        categories.add("Clothing");
        categories.add("Descriptors");
        categories.add("Things"); //Clouds
        categories.add("Random"); //mixed

    }

    public Set<String> getCategories()
    {
        return categories;
    }

    public String getCategoryStr(String category)
    {
        for(String str : categories)
        {
            if(str.equalsIgnoreCase(category))
                return str;
        }

        return null;
    }

    public int getCategoryPos(String category)
    {
        for(String str : categories)
        {
            if(str.equalsIgnoreCase(category))
                return (new ArrayList<String>(categories)).indexOf(str);
        }

        return -1;
    }

    public String getCategoryAtPos(int pos)
    {
        List<String> temp = new ArrayList<String>(categories);
        if(0 <= pos && pos < temp.size())
            return temp.get(pos);
        return null;
    }
}
