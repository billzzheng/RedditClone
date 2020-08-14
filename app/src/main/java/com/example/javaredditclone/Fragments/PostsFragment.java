package com.example.javaredditclone.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.javaredditclone.Adapters.PostsAdapter;
import com.example.javaredditclone.Presenters.PostsPresenter;
import com.example.javaredditclone.R;

import java.util.List;

public class PostsFragment extends Fragment implements PostsPresenter.View {
    private RecyclerView recyclerView;
    private PostsAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Button mPostsButton;
    private EditText mPostsSubredditText;
    private PostsPresenter mPresenter;
    private Toolbar mToolbar;


    public PostsFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_posts, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.rv_posts_list_fragment_posts);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new PostsAdapter();
        recyclerView.setAdapter(mAdapter);

        mPostsButton = (Button) v.findViewById(R.id.btn_load_posts_fragment_posts);
        mPostsSubredditText = (EditText) v.findViewById(R.id.et_subreddit_fragment_posts);

        mPresenter = new PostsPresenter(this);

        mPostsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String subredditName = mPostsSubredditText.getText().toString();
                mPresenter.updatePostForSubreddit(subredditName);
            }
        });

        //toolbar
        mToolbar = (Toolbar) v.findViewById(R.id.toolbar_fragment_posts);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);

        return v;
    }

    @Override
    public void updatePosts(List<String> newPosts) {
        mAdapter.replaceData(newPosts);
    }
}