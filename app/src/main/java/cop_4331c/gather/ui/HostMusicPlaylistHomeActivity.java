package cop_4331c.gather.ui;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cop_4331c.gather.R;
import cop_4331c.gather.adapter.PlaylistAdapter;
import cop_4331c.gather.music.Playlist;

public class HostMusicPlaylistHomeActivity extends ActionBarActivity {

    private Playlist mPlaylists;

    @InjectView(R.id.recyclerView) RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_music_playlist_home);

        ButterKnife.inject(this);
        Intent intent = getIntent();

        //TESTING PURPOSES ONLY
        mPlaylists = new Playlist();


        //Create new adapter and set it
        PlaylistAdapter adapter = new PlaylistAdapter(this, mPlaylists.getSongs());
        mRecyclerView.setAdapter(adapter);


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);
    }



}
