package com.example.gabav.speechplease;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Set;

/**
 * Created by gabav on 08-Jun-18.
 */

public class WordsCategoryFragment extends Fragment
{
    //will get the view on onCreateView to add top margins to the fragment? no.
    private RecyclerView mCategoryRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_category_list, container, false);
        mCategoryRecyclerView = (RecyclerView) view.findViewById(R.id.category_words_recycler_view);

        mCategoryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //updateUI();
        return view;
    }

    private class CategoryHolder extends RecyclerView.ViewHolder
    {
        public CategoryHolder(LayoutInflater inflater, ViewGroup parent)
        {
            super(inflater.inflate(R.layout.list_item_category, parent, false));
        }
    }

    private class CategoryAdapter extends RecyclerView.Adapter<CategoryHolder> {
        private Set<String> mCategories;

        public CategoryAdapter(Set<String> categories) {
            mCategories = categories;
        }

        @Override
        public CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new CategoryHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(CategoryHolder holder, int position)
        {

        }
        @Override
        public int getItemCount()
        {
            return mCategories.size();
        }
    }
}
