package cop_4331c.gather.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.squareup.picasso.Picasso;

import java.util.LinkedList;

import cop_4331c.gather.R;
import cop_4331c.gather.music.Host;
import cop_4331c.gather.music.Song;
import cop_4331c.gather.ui.HostMusicPlaylistHomeActivity;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.SnapshotId;
import kaaes.spotify.webapi.android.models.TrackToRemove;
import kaaes.spotify.webapi.android.models.TracksToRemove;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by ajariwinfield on 4/21/15.
 */
public class EditAdapter extends RecyclerView.Adapter<EditAdapter.PlaylistViewHolder> {

    private LinkedList<Song> mSongs2;
    private Context mContext;

    // TODO LL implementation
    public EditAdapter(Context context, LinkedList<Song> songs)
    {
        mSongs2 = songs;
        mContext = context;
    }

    @Override
    public EditAdapter.PlaylistViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.edit_song_item, parent, false);

        PlaylistViewHolder viewHolder = new PlaylistViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(EditAdapter.PlaylistViewHolder holder, final int position)
    {
        holder.bindSong(mSongs2.get(position));

        final Host host = HostMusicPlaylistHomeActivity.mHost;
        final SpotifyService spotify = HostMusicPlaylistHomeActivity.spotify;



        holder.mUpButton.setOnClickListener(new View.OnClickListener() {

            ImmutableMap<String, Object> body = ImmutableMap.of("range_start", (Object)position, "insert_before", (Object)(position -1));

            @Override
            public void onClick(View v) {

                if (position == 0)
                    return;



                spotify.reorderPlaylistTracks(host.getUserID(), host.getPlaylistId(), body, new Callback<SnapshotId>() {
                    @Override
                    public void success(SnapshotId snapshotId, Response response) {

                        //Change playlist model to reflect the changes
                        mSongs2.add(position - 1, mSongs2.remove(position));


                        ((Activity) mContext).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                notifyDataSetChanged();
                            }
                        });
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });


            }
        });

        holder.mDownButton.setOnClickListener(new View.OnClickListener() {

            ImmutableMap<String, Object> body = ImmutableMap.of("range_start", (Object)position, "insert_before", (Object)(position + 2));

            @Override
            public void onClick(View v) {

                //If already last item do nothing
                if (position == mSongs2.size() - 1)
                    return;



                spotify.reorderPlaylistTracks(host.getUserID(), host.getPlaylistId(), body, new Callback<SnapshotId>() {
                    @Override
                    public void success(SnapshotId snapshotId, Response response) {

                        //Change playlist model to reflect the changes
                        mSongs2.add(position + 1, mSongs2.remove(position));


                        ((Activity) mContext).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                notifyDataSetChanged();
                            }
                        });
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });


            }
        });

        holder.mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TracksToRemove tracks = new TracksToRemove();

                final TrackToRemove track = new TrackToRemove();

                track.uri = mSongs2.get(position).getURI();

                ImmutableList<TrackToRemove> list = ImmutableList.of(track);

                tracks.tracks = list;


                spotify.removeTracksFromPlaylist(host.getUserID(), host.getPlaylistId(), tracks, new Callback<SnapshotId>() {
                    @Override
                    public void success(SnapshotId snapshotId, Response response) {


                        final Song song = mSongs2.remove(position);

                        ((Activity) mContext).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                notifyDataSetChanged();
                                Toast.makeText(mContext, song.getSongName() + " removed from playlist", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        ((Activity) mContext).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, mSongs2.get(position).getSongName() + " was not removed from playlist", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSongs2.size();
    }

    //TODO LL IMPLEMENTATION
    public void refreshWithNewData(LinkedList<Song> songs)
    {
        mSongs2 = songs;
        notifyDataSetChanged();
    }

    public void refresh()
    {
        notifyDataSetChanged();
    }


    public class PlaylistViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView mAlbumCover;
        public TextView mSongInfoLabel;

        public ImageButton mUpButton;
        public ImageButton mDownButton;
        public ImageButton mDeleteButton;


        public PlaylistViewHolder(View itemView) {
            super(itemView);

            mAlbumCover = (ImageView) itemView.findViewById(R.id.albumCoverImage);
            mSongInfoLabel = (TextView) itemView.findViewById(R.id.songInfoLabel);
            mUpButton = (ImageButton) itemView.findViewById(R.id.upButton);
            mDownButton = (ImageButton) itemView.findViewById(R.id.downButton);
            mDeleteButton = (ImageButton) itemView.findViewById(R.id.deleteSongButton);

        }


        public void bindSong(Song song)
        {
            Picasso.with(mContext).load(song.getAlbumCoverURL()).into(mAlbumCover);
            mSongInfoLabel.setText( song.getArtist() + " - " + song.getSongName());
        }
    }
}
