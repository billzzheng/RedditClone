package com.example.javaredditclone.Adapters;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.javaredditclone.R;

import java.util.ArrayList;
import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostViewHolder> {
    private List<String> mDataset;
    public static class PostViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public TextView textView;
        public PostViewHolder(View v) {
            super(v);
            view = v;
            textView = v.findViewById(R.id.tv_post);
        }
    }

    public PostsAdapter() {
        mDataset = new ArrayList<>();
    }

    public void replaceData(List<String> datas) {
        mDataset.clear();
        for(String data : datas) {
            mDataset.add(data);
        }
        notifyDataSetChanged();
    }

    @Override
    public PostsAdapter.PostViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        View view = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.posts_row, parent, false);
        PostViewHolder vh = new PostViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        holder.textView.setText(mDataset.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
