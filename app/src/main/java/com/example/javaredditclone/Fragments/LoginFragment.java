package com.example.javaredditclone.Fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.javaredditclone.App;
import com.example.javaredditclone.R;

import net.dean.jraw.oauth.OAuthException;
import net.dean.jraw.oauth.StatefulAuthHelper;

import java.lang.ref.WeakReference;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    public interface OnLoginListener {
        public void onLogin();
    }

    public LoginFragment() {
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
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        Button loginButton = (Button)v.findViewById(R.id.btn_login_fragment_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });
        return v;
    }

    public void userLogin() {
        final WebView webView = getView().findViewById(R.id.webview_fragment_login);
        webView.clearCache(true);
        webView.clearHistory();

        // Stolen from https://github.com/ccrama/Slide/blob/a2184269/app/src/main/java/me/ccrama/redditslide/Activities/Login.java#L92
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            CookieManager.getInstance().removeAllCookies(null);
            CookieManager.getInstance().flush();
        } else {
            CookieSyncManager cookieSyncMngr = CookieSyncManager.createInstance(getContext());
            cookieSyncMngr.startSync();
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookie();
            cookieManager.removeSessionCookie();
            cookieSyncMngr.stopSync();
            cookieSyncMngr.sync();
        }

        // Get a StatefulAuthHelper instance to manage interactive authentication
        final StatefulAuthHelper helper = App.getAccountHelper().switchToNewUser();

        // Watch for pages loading
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (helper.isFinalRedirectUrl(url)) {
                    // No need to continue loading, we've already got all the required information
                    webView.stopLoading();
                    webView.setVisibility(View.GONE);

                    // Try to authenticate the user
                    new AuthenticateTask(LoginFragment.this, getContext(), helper).execute(url);
                }
            }
        });

        // Generate an authentication URL
        boolean requestRefreshToken = true;
        boolean useMobileSite = true;
        String[] scopes = new String[]{ "read", "identity" };
        String authUrl = helper.getAuthorizationUrl(requestRefreshToken, useMobileSite, scopes);

        // Finally, show the authorization URL to the user
        webView.loadUrl(authUrl);
    }

    private static final class AuthenticateTask extends AsyncTask<String, Void, Boolean> {
        private final WeakReference<LoginFragment> fragment;
        private final WeakReference<Context> context;
        private final StatefulAuthHelper helper;

        AuthenticateTask(LoginFragment fragment, Context context, StatefulAuthHelper helper) {
            this.fragment = new WeakReference<>(fragment);
            this.context = new WeakReference<>(context);
            this.helper = helper;
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            try {
                helper.onUserChallenge(urls[0]);
                return true;
            } catch (OAuthException e) {
                // Report failure if an OAuthException occurs
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            OnLoginListener listener = (OnLoginListener)context.get();
            listener.onLogin();
        }
    }

    /*private static final class GetSubredditPostsTask extends AsyncTask<RedditClient, Void, Listing<Submission>> {
        @Override
        protected Listing<Submission> doInBackground(RedditClient... redditClients) {
            DefaultPaginator.Builder<Submission, SubredditSort> paginatorBuilder = redditClients[0].subreddit("playboicarti").posts();
            DefaultPaginator<Submission> paginator = paginatorBuilder.build();

            Listing<Submission> firstPage = paginator.next();
            return firstPage;
        }

        @Override
        protected void onPostExecute(Listing<Submission> posts) {
            for(int i = 0; i < posts.size(); i++) {
                String title = posts.get(i).getTitle();
                Log.d("billz", title);
            }
        }
    }*/
}