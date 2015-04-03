package cop_4331c.gather.music;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ajariwinfield on 4/1/15.
 */
public class Song implements Parcelable
{
    private String mSongName;
    private String mAlbumCoverURL;


    public Song() {};
    public String getSongName() {
        return mSongName;
    }

    public void setSongName(String songName) {
        mSongName = songName;
    }

    public String getAlbumCoverURL() {
        return mAlbumCoverURL;
    }

    public void setAlbumCoverURL(String albumCoverURL) {
        mAlbumCoverURL = albumCoverURL;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(mSongName);
//        dest.writeInt(mAlbumCoverURL);
    }

    public Song(Parcel in)
    {
        mSongName = in.readString();
//        mAlbumCoverURL = in.readInt();
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel source) {
            return new Song(source);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };
}
