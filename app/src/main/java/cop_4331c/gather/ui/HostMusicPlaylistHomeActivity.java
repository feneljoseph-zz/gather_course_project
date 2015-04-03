package cop_4331c.gather.ui;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import java.util.Iterator;
import java.util.List;
import butterknife.ButterKnife;
import butterknife.InjectView;
import cop_4331c.gather.R;
import cop_4331c.gather.adapter.PlaylistAdapter;
import cop_4331c.gather.music.Playlist;
import cop_4331c.gather.music.User;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.PlaylistTrack;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class HostMusicPlaylistHomeActivity extends ActionBarActivity {

    private PlaylistAdapter adapter = null;

    // Spotify auth info
    private static final String CLIENT_ID = "e565d7c739914340abfae5dec7e525a6";
    private static final String REDIRECT_URI = "gather-app-login://callback";
    private static final int REQUEST_CODE = 1337;

    SpotifyApi api = new SpotifyApi();
    SpotifyService spotify;

    private User mUser = new User();

    @InjectView(R.id.recyclerView) RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_music_playlist_home);

        //Connect Recycler view to activity
        ButterKnife.inject(this);
        Intent intent = getIntent();
        mUser.setUserType("HOST");


        //TESTING
        mUser.setPlaylists(new Playlist());

        //Authenticate API and fill out the rest of the user information as well as playlist
        authenticateSpotifyAPI();



        //Create new adapter and set it.
        adapter = new PlaylistAdapter(this, mUser.getPlaylists().getSongs());
        mRecyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
    }


    //This method is called to begin the spotify authentication process
    public void authenticateSpotifyAPI()
    {
        AuthenticationRequest.Builder builder =
                new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);

        builder.setScopes(new String[]{"streaming"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);

    }



    //This method defines what happens after our authentication request receives a response
    //If successful store the user's access token
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);

            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    // Handle successful response
                    // Store Host's access token
                    api.setAccessToken(response.getAccessToken());
                    mUser.setAccessToken(response.getAccessToken());

                    spotify = api.getService();

                    findGatherPlaylist();
//                    fillPlaylist();
                    break;

                // Auth flow returned an error
                case ERROR:
                    // Handle error response
                    break;

                // Most likely auth flow was cancelled
                default:
                    // Handle other cases
            }
        }
    }


    public void fillPlaylist()
    {
        spotify.getPlaylist(mUser.getUserID(), mUser.getPlaylists().getPlaylistId(), new Callback<kaaes.spotify.webapi.android.models.Playlist>()
        {

            //If a playlist is successfully returned parse it's track info
            @Override
            public void success(final kaaes.spotify.webapi.android.models.Playlist playlist, Response response)
            {
                //Create array of Playlist tracks from api response and set as user's playlist
                List<PlaylistTrack> songs = playlist.tracks.items;
                mUser.setPlaylists(new Playlist(songs));

                //Refresh view
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run() {
                       adapter.refreshWithNewData(mUser.getPlaylists().getSongs());
                    }
                });
            }

            @Override
            public void failure(RetrofitError error)
            {
                //Inform user that playlist could not be found.
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        displayError();
                    }
                });

            }

        });
    }

    public void findGatherPlaylist()
    {
        //Gather host's id
        spotify.getMe(new Callback<kaaes.spotify.webapi.android.models.User>() {
            @Override
            public void success(final kaaes.spotify.webapi.android.models.User user, Response response)
            {
                String playlistId;
                mUser.setUserID(user.id);

                //Once you have host id use it to find his/her gather playlist
                spotify.getPlaylists(mUser.getUserID(), new Callback<Pager<kaaes.spotify.webapi.android.models.Playlist>>() {
                    @Override
                    public void success(Pager<kaaes.spotify.webapi.android.models.Playlist> playlistPager, Response response)
                    {
                        List<kaaes.spotify.webapi.android.models.Playlist> list = playlistPager.items;

                        for(kaaes.spotify.webapi.android.models.Playlist p: list)
                        {
                            //Wait until we find gather playlist id and then fill the playlist
                             if(p.name.equals("gather"))
                            {
                                mUser.getPlaylists().setPlaylistId(p.id);
                                fillPlaylist();
                                Log.d("FOUND IT", p.id);
                                return;
                            }

                        }

                    }

                    @Override
                    public void failure(RetrofitError error)
                    {

                    }
                });
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });


    }

    public void displayError()
    {
        Toast.makeText(this, "Could not find playlist.", Toast.LENGTH_LONG).show();
    }

}
