package cop_4331c.gather.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.collect.ImmutableMap;
import com.squareup.picasso.Picasso;

import java.util.LinkedList;

import cop_4331c.gather.R;
import cop_4331c.gather.music.Host;
import cop_4331c.gather.music.Song;
import cop_4331c.gather.ui.HostMusicPlaylistHomeActivity;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.PlaylistTrack;
import kaaes.spotify.webapi.android.models.Track;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by ajariwinfield on 4/12/15.
 */
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder>
{
    private LinkedList<Song> mSongs2;
    private Context mContext;


    //TODO LL implementation
    public SearchAdapter(Context context, LinkedList<Song> songs)
    {
        mSongs2 = songs;
        mContext = context;
    }

    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.song_list_item, parent, false);

        SearchViewHolder viewHolder = new SearchViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SearchViewHolder holder, int position) {
        holder.bindSong(mSongs2.get(position));
    }

    @Override
    public int getItemCount() {
        return mSongs2.size();
    }


    public class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public ImageView mAlbumCover;
        public TextView mSongInfoLabel;


        public SearchViewHolder(View itemView) {
            super(itemView);

            mAlbumCover = (ImageView) itemView.findViewById(R.id.albumCoverImage);
            mSongInfoLabel = (TextView) itemView.findViewById(R.id.songInfoLabel);

            itemView.setOnClickListener(this);

        }


        public void bindSong(Song song)
        {
            Picasso.with(mContext).load(song.getAlbumCoverURL()).into(mAlbumCover);
            mSongInfoLabel.setText( song.getArtist() + " - " + song.getSongName());
        }

        @Override
        public void onClick(View v)
        {
            final Host host = HostMusicPlaylistHomeActivity.mHost;
            final SpotifyService spotify = HostMusicPlaylistHomeActivity.spotify;

            spotify.getTrack(mSongs2.get(getPosition()).getId(), new Callback<Track>() {
                @Override
                public void success(final Track track, Response response)
                {
                    ImmutableMap<String, Object> queries = ImmutableMap.of("uris", (Object)track.uri);
                    ImmutableMap<String, Object> body = ImmutableMap.of("uris", (Object)track.uri);


                    spotify.addTracksToPlaylist(host.getUserID(), host.getPlaylistId(), queries, body, new Callback<Pager<PlaylistTrack>>() {
                        @Override
                        public void success(Pager<PlaylistTrack> playlistTrackPager, Response response)
                        {
                            ((Activity) mContext).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(mContext, track.name + " added to playlist", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void failure(RetrofitError error)
                        {
                            ((Activity) mContext).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(mContext, track.name + " could not be added to playlist", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }



                @Override
                public void failure(RetrofitError error) {

                }
            });
            mSongs2.remove(getPosition());
            notifyDataSetChanged();
        }
    }

    public void refreshWithNewData(LinkedList<Song> songs)
    {
        mSongs2 = songs;
        notifyDataSetChanged();
    }

}
