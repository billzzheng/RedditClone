package com.example.javaredditclone.Presenters;

import com.example.javaredditclone.Models.PostsModel;

import java.util.List;

public class PostsPresenter {

    private PostsModel postsModel;
    private View view;

    public PostsPresenter(View view) {
        this.postsModel = new PostsModel();
        this.view = view;
    }

    public void updatePostForSubreddit(String subreddit) {
        postsModel.loadPosts(subreddit, view);
    }

    public interface View {
        void updatePosts(List<String> newPosts);
    }
}
