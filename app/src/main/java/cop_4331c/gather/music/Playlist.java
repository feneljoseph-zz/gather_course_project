package cop_4331c.gather.music;


import java.util.LinkedList;
import java.util.List;

import kaaes.spotify.webapi.android.models.PlaylistTrack;
import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by ajariwinfield on 4/1/15.
 */
public class Playlist
{
    private String mPlaylistId;
    private LinkedList<Song> mSongs2 = new LinkedList<Song>();


    public Playlist()
    {
        Song s = new Song();
        s.setSongName("Playlist Loading...");
        mSongs2.add(s);
    }

    // TODO LL IMPLEMENTATION
    public Playlist(List<PlaylistTrack> songs)
    {
       for(int i=0; i<songs.size(); i++)
        {
            Song song = new Song();

            song.setSongName(songs.get(i).track.name);
            song.setAlbumCoverURL(songs.get(i).track.album.images.get(0).url);
            song.setArtist(songs.get(i).track.artists.get(0).name);
            song.setURI(songs.get(i).track.uri);

            mSongs2.add(song);
        }

    }


    public String getPlaylistId() {
        return mPlaylistId;
    }

    public void setPlaylistId(String playlistId) {
        mPlaylistId = playlistId;
    }

    public LinkedList<Song> getSongs() {
        return mSongs2;
    }


    public void setSongs(LinkedList<Song> songs) {
        mSongs2 = songs;
    }

}
