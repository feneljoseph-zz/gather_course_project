package cop_4331c.gather.music;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ajariwinfield on 4/1/15.
 */
public class Song implements Parcelable
{
    private String mSongName;
    private int mSongId;


    public Song() {};
    public String getSongName() {
        return mSongName;
    }

    public void setSongName(String songName) {
        mSongName = songName;
    }

    public int getSongId() {
        return mSongId;
    }

    public void setSongId(int songId) {
        mSongId = songId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(mSongName);
//        dest.writeInt(mSongId);
    }

    public Song(Parcel in)
    {
        mSongName = in.readString();
//        mSongId = in.readInt();
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
