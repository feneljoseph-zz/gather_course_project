package cop_4331c.gather.ui;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cop_4331c.gather.R;
import cop_4331c.gather.adapter.EditAdapter;
import cop_4331c.gather.adapter.HomeAdapter;
import cop_4331c.gather.music.Host;
import cop_4331c.gather.music.Playlist;
import kaaes.spotify.webapi.android.SpotifyService;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class EditPlaylistsActivity extends ActionBarActivity {

    //Use data already available to playlist home activity
    public static EditAdapter adapter = null;
    public static Host mHost = HostMusicPlaylistHomeActivity.mHost;

    public static SpotifyService spotify = HostMusicPlaylistHomeActivity.spotify;

    @InjectView(R.id.editRecyclerView) RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_playlists);

        //Connect Recycler view to activity
        ButterKnife.inject(this);


        //Fill with empty playlist until data is done downloading
        //Create new adapter and set it.
        adapter = new EditAdapter(this, mHost.getPlaylists().getSongs());
        mRecyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

    }


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

        HostMusicPlaylistHomeActivity.mHost.setPlaylists(mHost.getPlaylists());
    }
}
