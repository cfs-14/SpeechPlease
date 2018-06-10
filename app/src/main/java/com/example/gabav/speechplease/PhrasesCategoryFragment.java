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
 * Created by gabav on 09-Jun-18.
 * This fragment is home to the RecyclerView that will display the categories or buttons that
 * represent categories. The fragment will display the horizontal buttons as a vertical list.
 * Clicking on the buttons will open the corresponding practice page.
 */

public class PhrasesCategoryFragment extends Fragment
{
    private RecyclerView mRecyclerView;
    private CategoryAdapter mAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_category_list, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.category_recycler_view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();
        return view;
    }

    private void updateUI()
    {
        PhrasesCategoriesSingleton instanceCategories = PhrasesCategoriesSingleton.get(getActivity());
        Set<String> setCategories = instanceCategories.getPhrasesCategories();

        mAdapter = new PhrasesCategoryFragment.CategoryAdapter(setCategories);
        mRecyclerView.setAdapter(mAdapter);
    }

    class PhraseCategoryHolder extends RecyclerView.ViewHolder
    {
        private Button mCategoryButton;

        public PhraseCategoryHolder(LayoutInflater inflater, ViewGroup parent)
        {
            super(inflater.inflate(R.layout.list_item_category, parent, false));

            mCategoryButton = (Button) itemView.findViewById(R.id.category_button);
        }

        public void bind(String category)
        {
            mCategoryButton.setText(category);
        }
    }

    private class CategoryAdapter extends RecyclerView.Adapter<PhraseCategoryHolder>
    {
        private Set<String> mCategories;
        private List<String> listCategories;

        public CategoryAdapter(Set<String> categories) {
            mCategories = categories;
            listCategories = new ArrayList<>(mCategories);
            Log.d("ConsAdptr", "\n>Made an Adapter");
            Log.v("ConsAdptr", "\n>Made an Adapter");
        }

        @Override
        public PhraseCategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new PhraseCategoryHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(PhraseCategoryHolder holder, int position)
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
