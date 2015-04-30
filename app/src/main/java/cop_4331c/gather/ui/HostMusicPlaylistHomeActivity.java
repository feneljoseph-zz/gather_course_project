package cop_4331c.gather.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import java.util.ListIterator;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cop_4331c.gather.R;
import cop_4331c.gather.adapter.HomeAdapter;
import cop_4331c.gather.music.Host;
import cop_4331c.gather.music.Playlist;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class HostMusicPlaylistHomeActivity extends ActionBarActivity {

    public static HomeAdapter adapter = null;

    // Spotify auth info
    private static final String CLIENT_ID = "e565d7c739914340abfae5dec7e525a6";
    private static final String REDIRECT_URI = "gather-app-login://callback";
    private static final int AUTH_REQUEST_CODE = 1337;


    SpotifyApi api = new SpotifyApi();
    public static SpotifyService spotify;
    private boolean isInit = false;

    public static Host mHost = new Host();

    @InjectView(R.id.recyclerView) RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_music_playlist_home);

        //Connect Recycler view to activity
        ButterKnife.inject(this);


        //Fill with empty playlist until data is done downloading
        mHost.setPlaylists(new Playlist());
        new initApiTask().execute();
        new downloadPlaylistTask().execute();

        //Create new adapter and set it.
        adapter = new HomeAdapter(this, mHost.getPlaylists().getSongs());
        mRecyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
    }

    //Authenticates api with user info
    private class initApiTask extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected void onPreExecute()
        {
            authenticateSpotifyAPI();
        }

        @Override
        protected Void doInBackground(Void... params)
        {

            //Wait until authentication completes
            while(mHost.getAccessToken() == "")
            {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


            spotify = api.getService();


            //Set User ID
            mHost.setUserID(spotify.getMe().id);


            //Find gather playlist id
            ListIterator<kaaes.spotify.webapi.android.models.Playlist> itr = spotify.getPlaylists(mHost.getUserID()).items.listIterator();

            while (itr.hasNext())
            {
                kaaes.spotify.webapi.android.models.Playlist p = itr.next();

                if(p.name.equals("gather"))
                {
                      mHost.setPlaylistId(p.id);
                }
            }
            return null;
        }


    }


    //Use to initially download playlist
    private class downloadPlaylistTask extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params)
        {

            //Wait until authentication completes
            while(mHost.getAccessToken() == "")
            {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            //TODO LL IMPLEMENTATION
            mHost.setPlaylists(new Playlist(spotify.getPlaylist(mHost.getUserID(), mHost.getPlaylistId()).tracks.items));
            isInit = true;
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
                //Refresh view
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run() {
                       adapter.refreshWithNewData(mHost.getPlaylists().getSongs());
                    }
                });
        }


    }


    //TODO LL Implementation
    public void refreshPlaylist()
    {

        //Download new playlist
        spotify.getPlaylist(mHost.getUserID(), mHost.getPlaylistId(), new Callback<kaaes.spotify.webapi.android.models.Playlist>() {
            @Override
            public void success(kaaes.spotify.webapi.android.models.Playlist playlist, Response response) {
                mHost.setPlaylists(new Playlist(playlist.tracks.items));

                //Refresh view
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.refreshWithNewData(mHost.getPlaylists().getSongs());
                    }
                });
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

    }



    //This method is called to begin the spotify authentication process
    public void authenticateSpotifyAPI()
    {
        AuthenticationRequest.Builder builder =
                new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);

        builder.setScopes(new String[]{"playlist-modify-public","streaming"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, AUTH_REQUEST_CODE, request);

    }

    //This method defines what happens after our authentication request receives a response
    //If successful store the user's access token
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == AUTH_REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);

            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    // Handle successful response
                    // Store Host's access token
                    api.setAccessToken(response.getAccessToken());
                    mHost.setAccessToken(response.getAccessToken());
                    break;

                // Auth flow returned an error
                case ERROR:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(HostMusicPlaylistHomeActivity.this, "Could not authenticate.", Toast.LENGTH_LONG).show();
                        }
                    });
                    break;

                // Most likely auth flow was cancelled
                default:
                    // Handle other cases
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(isInit)
            refreshPlaylist();
    }

    @OnClick (R.id.searchTab)
    public void startSearchActivity(View view)
    {
        Intent intent = new Intent(this, SearchForSongActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);

    }

    @OnClick (R.id.editPlaylistButton)
    public void startEditPlaylistsActivity(View view)
    {
        Intent intent = new Intent(this, EditPlaylistsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);

    }
}
