package com.example.javaredditclone.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.javaredditclone.App;
import com.example.javaredditclone.R;

import net.dean.jraw.RedditClient;
import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Submission;
import net.dean.jraw.models.SubredditSort;
import net.dean.jraw.pagination.DefaultPaginator;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostsFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Button mPostsButton;
    private EditText mPostsSubredditText;
    private List<String> mPostsTitles = new ArrayList<>();

    public PostsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_posts, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.rv_posts_list_fragment_posts);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(mPostsTitles);
        recyclerView.setAdapter(mAdapter);

        mPostsButton = (Button) v.findViewById(R.id.btn_load_posts_fragment_posts);
        mPostsSubredditText = (EditText) v.findViewById(R.id.et_subreddit_fragment_posts);

        mPostsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String subredditName = mPostsSubredditText.getText().toString();
                loadPosts(subredditName);
            }
        });
        return v;
    }

    private void loadPosts(String subredditName) {
        new GetSubredditPostsTask(subredditName, this).execute(App.getAccountHelper().getReddit());
    }

    private void updatePosts(List<String> newPosts) {
        for(int i = 0; i < newPosts.size(); i++) {
            Log.d("billz", newPosts.get(i));
        }
        mPostsTitles.clear();
        mPostsTitles.addAll(newPosts);
        mAdapter.notifyDataSetChanged();
    }

    private static final class GetSubredditPostsTask extends AsyncTask<RedditClient, Void, Listing<Submission>> {
        private final String subredditName;
        private final WeakReference<PostsFragment> postsFragment;

        GetSubredditPostsTask(String subredditName, PostsFragment postsFragment) {
            this.subredditName = subredditName;
            this.postsFragment = new WeakReference<>(postsFragment);
        }

        @Override
        protected Listing<Submission> doInBackground(RedditClient... redditClients) {
            DefaultPaginator.Builder<Submission, SubredditSort> paginatorBuilder = redditClients[0].subreddit(subredditName).posts();
            DefaultPaginator<Submission> paginator = paginatorBuilder.build();

            Listing<Submission> firstPage = paginator.next();
            return firstPage;
        }

        @Override
        protected void onPostExecute(Listing<Submission> posts) {
            List<String> titles = new ArrayList();
            for(int i = 0; i < posts.size(); i++) {
                String title = posts.get(i).getTitle();
                titles.add(title);
            }
            postsFragment.get().updatePosts(titles);
        }
    }
}

class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<String> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View view;
        public TextView textView;
        public MyViewHolder(View v) {
            super(v);
            view = v;
            textView = v.findViewById(R.id.tv_post);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(List<String> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View view = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.posts_row, parent, false);
        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textView.setText(mDataset.get(position));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}