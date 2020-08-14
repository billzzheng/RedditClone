package com.example.javaredditclone.Models;

import android.os.AsyncTask;

import com.example.javaredditclone.App;
import com.example.javaredditclone.Presenters.PostsPresenter;

import net.dean.jraw.RedditClient;
import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Submission;
import net.dean.jraw.models.SubredditSort;
import net.dean.jraw.pagination.DefaultPaginator;

import java.util.ArrayList;
import java.util.List;

public class PostsModel {
    //update later
    private List<String> mPostsTitles = new ArrayList<>();

    public void loadPosts(String subredditName, PostsPresenter.View view) {
        new GetSubredditPostsTask(subredditName, view).execute(App.getAccountHelper().getReddit());
    }

    public static final class GetSubredditPostsTask extends AsyncTask<RedditClient, Void, List<String>> {
        private final String subredditName;
        private final PostsPresenter.View view;

        GetSubredditPostsTask(String subredditName, PostsPresenter.View view) {
            this.subredditName = subredditName;
            this.view = view;
        }

        @Override
        protected List<String> doInBackground(RedditClient... redditClients) {
            DefaultPaginator.Builder<Submission, SubredditSort> paginatorBuilder = redditClients[0].subreddit(subredditName).posts();
            DefaultPaginator<Submission> paginator = paginatorBuilder.build();

            Listing<Submission> firstPage = paginator.next();
            List<String> titles = new ArrayList();
            for(int i = 0; i < firstPage.size(); i++) {
                titles.add(firstPage.get(i).getTitle());
            }
            return titles;
        }

        @Override
        protected void onPostExecute(List<String> titles) {
            view.updatePosts(titles);
        }
    }
}
