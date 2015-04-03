package cop_4331c.gather;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.content.Intent;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import java.util.Iterator;
import java.util.List;

import cop_4331c.gather.music.Playlist;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Album;
import kaaes.spotify.webapi.android.models.AlbumsPager;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.PlaylistTrack;
import kaaes.spotify.webapi.android.models.Tracks;
import kaaes.spotify.webapi.android.models.User;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class WelcomeActivity extends Activity {

    // Spotify auth info
    private static final String CLIENT_ID = "e565d7c739914340abfae5dec7e525a6";
    private static final String REDIRECT_URI = "gather-app-login://callback";
    private static final int REQUEST_CODE = 1337;
    private String accessToken = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

//        spotifyAuth();


//        pullPlaylistInfo();
    }

    public void spotifyAuth()
    {
        AuthenticationRequest.Builder builder =
                new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);

        builder.setScopes(new String[]{"streaming"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);

            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    // Handle successful response
                    accessToken = response.getAccessToken();
                    Log.d("Succes Auth", accessToken);
                    accessToken = response.getAccessToken();
                    pullPlaylistInfo();
                    break;

                // Auth flow returned an error
                case ERROR:
                    // Handle error response
                    break;

                // Most likely auth flow was cancelled
                default:
                    // Handle other cases
            }
        }
    }

    public void signIn(View view) {
        Intent signIn = new Intent(this, SignInActivity.class);
        startActivity(signIn);
    }

    public void signUp(View view) {
        Intent signUp = new Intent(this,SignUpActivity.class);
        startActivity(signUp);
    }

    public void pullPlaylistInfo()
    {

        SpotifyApi api = new SpotifyApi();

        // Most (but not all) of the Spotify Web API endpoints require authorisation.
        // If you know you'll only use the ones that don't require authorisation you can skip this step
        api.setAccessToken(accessToken);

        final SpotifyService spotify = api.getService();

        spotify.getPlaylist("1210007921", "3VEbfCG6kMd5UOkjqfz9RR", new Callback<kaaes.spotify.webapi.android.models.Playlist>() {
            @Override
            public void success(kaaes.spotify.webapi.android.models.Playlist playlist, Response response)
            {
                Log.d("playlist success", playlist.name);

               Iterator<PlaylistTrack> itr = playlist.tracks.items.iterator() ;

               while(itr.hasNext())
               {
                   PlaylistTrack track = itr.next();

                   Log.d("playlist track", track.track.name);
               }

               spotify.getMe(new Callback<User>() {
                   @Override
                   public void success(User user, Response response) {
                       Log.d("User ID", user.id);
                   }

                   @Override
                   public void failure(RetrofitError error) {

                   }
               });

            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("Playlist failure", error.toString());
            }
        });


    }
}
