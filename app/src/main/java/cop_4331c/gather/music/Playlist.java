package cop_4331c.gather.music;


import java.util.List;

import kaaes.spotify.webapi.android.models.PlaylistTrack;
import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by ajariwinfield on 4/1/15.
 */
public class Playlist
{
    private Song[] mSongs;
    private String mPlaylistId;

    //Constructor for testing purposes only
    public Playlist()
    {

        mSongs = new Song[1];
        mSongs[0] = new Song();
        mSongs[0].setSongName("Playlist Loading...");

    }


    public Playlist(List<PlaylistTrack> songs)
    {
        mSongs = new Song[songs.size()];

        for(int i=0; i<songs.size(); i++)
        {
            mSongs[i] = new Song();
            mSongs[i].setSongName(songs.get(i).track.name);
            mSongs[i].setAlbumCoverURL(songs.get(i).track.album.images.get(0).url);
            mSongs[i].setArtist(songs.get(i).track.artists.get(0).name);
        }

    }

//    public Playlist(List<Track> songs)
//    {
//        mSongs = new Song[songs.size()];
//
//        for(int i=0; i<songs.size(); i++)
//        {
//            mSongs[i] = new Song();
//            mSongs[i].setSongName(songs.get(i).track.name);
//            mSongs[i].setAlbumCoverURL(songs.get(i).track.album.images.get(0).url);
//            mSongs[i].setArtist(songs.get(i).track.artists.get(0).name);
//        }
//
//    }


    public String getPlaylistId() {
        return mPlaylistId;
    }

    public void setPlaylistId(String playlistId) {
        mPlaylistId = playlistId;
    }

    public Song[] getSongs() {
        return mSongs;
    }

    public void setSongs(Song[] songs) {
        mSongs = songs;
    }

}
