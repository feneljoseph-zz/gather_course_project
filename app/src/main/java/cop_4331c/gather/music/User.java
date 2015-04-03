package cop_4331c.gather.music;

/**
 * Created by ajariwinfield on 4/2/15.
 */
public class User
{
    private String userType;
    private String userID;
    private String accessToken = "";
    private Playlist mPlaylists;

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

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
