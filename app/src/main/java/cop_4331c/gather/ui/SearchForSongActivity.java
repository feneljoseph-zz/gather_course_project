package cop_4331c.gather.ui;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cop_4331c.gather.R;
import cop_4331c.gather.adapter.SearchAdapter;
import cop_4331c.gather.music.Song;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.TracksPager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SearchForSongActivity extends ActionBarActivity {
    private SearchAdapter adapter = null;
    private Song[] mSongs;
    private SpotifyService spotify;

    @InjectView(R.id.recyclerViewSearch) RecyclerView mRecyclerView;
    @InjectView(R.id.searchButton) ImageView mSearchButton;
    @InjectView(R.id.searchSongEditText) EditText mSearchEditText;
    @InjectView(R.id.homeTab) Button mHomeTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_for_song);

        //Connect Recycler view to activity
        ButterKnife.inject(this);


        //TODO Come up with better empty view

        //Init with 1 result
        mSongs = new Song[1];
        mSongs[0] = new Song();
        mSongs[0].setSongName("Search for songs here");
        mSongs[0].setArtist("... ");




        //Create new adapter and set it.
        adapter = new SearchAdapter(this, mSongs);
        mRecyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        spotify = HostMusicPlaylistHomeActivity.spotify;

    }

    public void searchForSong(String songName)
    {
        spotify.searchTracks(songName, new Callback<TracksPager>() {
            @Override
            public void success(TracksPager tracksPager, Response response) {

                mSongs = new Song[12];

                for(int i=0; i<12; i++)
                {
                    mSongs[i] = new Song();
                    mSongs[i].setAlbumCoverURL(tracksPager.tracks.items.get(i).album.images.get(0).url);
                    mSongs[i].setArtist(tracksPager.tracks.items.get(i).artists.get(0).name);
                    mSongs[i].setSongName(tracksPager.tracks.items.get(i).name);
                    mSongs[i].setId(tracksPager.tracks.items.get(i).id);
                }

                //Refresh view
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        adapter.refreshWithNewData(mSongs);
                    }
                });
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }



    @OnClick (R.id.searchButton)
    public void startSearchActivity(View view)
    {
        String songName = mSearchEditText.getText().toString();
        searchForSong(songName);
    }

    @OnClick (R.id.homeTab)
    public void startHomeActivity(View view)
    {

        finish();
    }

    @OnClick (R.id.editPlaylistButton)
    public void startEditPlaylistsActivity(View view)
    {
        startActivity(new Intent(this, EditPlaylistsActivity.class));
    }

}
