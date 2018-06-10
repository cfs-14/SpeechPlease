package com.example.gabav.speechplease;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by gabav on 08-Jun-18.
 */

public class WordsCategoryFragment extends Fragment
{
    //will get the view on onCreateView to add top margins to the fragment? no.
    private RecyclerView mCategoryRecyclerView;
    private CategoryAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_category_list, container, false);
        mCategoryRecyclerView = (RecyclerView) view.findViewById(R.id.category_recycler_view);

        mCategoryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();
        return view;
    }

    private void updateUI()
    {
        WordsCategoriesSingleton instanceCategories = WordsCategoriesSingleton.get(getActivity());
        Set<String> setCategories = instanceCategories.getWordCategories();

        mAdapter = new CategoryAdapter(setCategories);
        mCategoryRecyclerView.setAdapter(mAdapter);
    }

    private class CategoryHolder extends RecyclerView.ViewHolder
    {
        private Button mCategoryButton;

        public CategoryHolder(LayoutInflater inflater, ViewGroup parent)
        {
            super(inflater.inflate(R.layout.list_item_category, parent, false));

            mCategoryButton = (Button) itemView.findViewById(R.id.category_button);

        }

        public void bind(String category)
        {
            mCategoryButton.setText(category);
        }
    }

    private class CategoryAdapter extends RecyclerView.Adapter<CategoryHolder> {
        private Set<String> mCategories;
        private List<String> listCategories;

        public CategoryAdapter(Set<String> categories) {
            mCategories = categories;
            listCategories = new ArrayList<>(mCategories);
            Log.d("ConsAdptr", "\n>Made an Adapter");
            Log.v("ConsAdptr", "\n>Made an Adapter");
        }

        @Override
        public CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new CategoryHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(CategoryHolder holder, int position)
        {
            holder.bind(listCategories.get(position));
        }
        @Override
        public int getItemCount()
        {
            return mCategories.size();
        }
    }
}
