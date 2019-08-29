package com.example.newsreader;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsreader.model.Article;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoriesRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int mCategory;

    // constants for view holder type
    private final int TYPE_NEWS = 0;
    private final int TYPE_LOADING = 1;

    private ArrayList<Article> mData;

    // Item and button item listener
    private final OnCategoryItemClick mListener;
    private final OnSaveButtonItemClick mSaveButtonListener;

    public CategoriesRecyclerAdapter(ArrayList<Article> data, OnCategoryItemClick click,
                                     OnSaveButtonItemClick buttonClick, int category) {
        mData = data;
        mListener = click;
        mSaveButtonListener = buttonClick;
        mCategory = category;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mData.size() - 1 && mCategory != 5) {
            return TYPE_LOADING;
        } else {
            return TYPE_NEWS;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_NEWS) {
            View v = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.item_list_category, parent, false);
            return new NormalViewHolder(v);
        } else {
            View loadingView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(loadingView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder != null) {
            if (holder.getItemViewType() == 0) {
                ((NormalViewHolder) holder).mHeadLine.setText(mData.get(position).getHeadline());
                ((NormalViewHolder) holder).mSection.setText(mData.get(position).getSectionName());

                if (mCategory == 5) {
                    ((NormalViewHolder) holder).saveButton.setText(R.string.delete_button);
                }

                // on Save button pressed
                ((NormalViewHolder) holder).saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mSaveButtonListener.onButtonClick(mData.get(position), position);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class NormalViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.category_headline)
        TextView mHeadLine;

        @BindView(R.id.category_section)
        TextView mSection;

        @BindView(R.id.button_item_save)
        Button saveButton;

        NormalViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClick(mData.get(getAdapterPosition()));
                }
            });
        }
    }

    class LoadingViewHolder extends RecyclerView.ViewHolder {
        LoadingViewHolder(View view) {
            super(view);
        }
    }

    public interface OnCategoryItemClick {
        void onItemClick(Article position);
    }

    public interface OnSaveButtonItemClick {
        void onButtonClick(Article position, int itemPosition);
    }

    public void deleteItem(int position){
        mData.remove(position);
        notifyDataSetChanged();
    }

    public void addItems(ArrayList<Article> items, int position) {
        if (mData != null){
            if (position == 5) {
                mData.clear();
            }
            mData.addAll(items);
            notifyDataSetChanged();
        }
    }
}
