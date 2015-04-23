package cop_4331c.gather.ui;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.LinkedList;

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
    private LinkedList<Song> mSongs = new LinkedList<Song>();
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
        Song song = new Song();
        song.setSongName("Search for songs here");
        song.setArtist("... ");
        mSongs.add(song);




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

                int j = tracksPager.tracks.items.size();
                int k = 15;


                if( j< 15)
                    k = j;

                for(int i=0; i<k; i++)
                {
                    Song song = new Song();
                    String url = "";

                    try
                    {
                        url = tracksPager.tracks.items.get(i).album.images.get(0).url;
                    }
                    catch(Exception e)
                    {
                        url = "http://i.imgur.com/XIXcV5Y.jpg";
                    }

                    Log.d("URL IS!!! : ", url);



                    song.setAlbumCoverURL(url);

                    song.setArtist(tracksPager.tracks.items.get(i).artists.get(0).name);
                    song.setSongName(tracksPager.tracks.items.get(i).name);
                    song.setId(tracksPager.tracks.items.get(i).id);

                    mSongs.add(song);
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
        mSongs.clear();
        String songName = mSearchEditText.getText().toString();
        searchForSong(songName);
    }

    @OnClick (R.id.homeTab)
    public void startHomeActivity(View view)
    {
        Intent intent = new Intent(this, HostMusicPlaylistHomeActivity.class);
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
