package cop_4331c.gather.music;

/**
 * Created by ajariwinfield on 4/2/15.
 */
public class Host
{
    private String userID;
    private String accessToken = "";
    private Playlist mPlaylists;
    private String mPlaylistId;

    public String getPlaylistId() {
        return mPlaylistId;
    }

    public void setPlaylistId(String playlistId) {
        mPlaylistId = playlistId;
    }

    public Playlist getPlaylists() {
        return mPlaylists;
    }

    public void setPlaylists(Playlist playlist) {
        mPlaylists = playlist;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
