package cop_4331c.gather.music;

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
        mSongs = new Song[20];
        for(int i=0; i<20; i++)
        {
            mSongs[i] = new Song();
            mSongs[i].setSongName("This is some song");
        }



    }

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
