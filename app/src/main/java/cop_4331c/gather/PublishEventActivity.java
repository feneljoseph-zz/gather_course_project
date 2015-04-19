package cop_4331c.gather;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AppEventsLogger;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.widget.FacebookDialog;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.PlusShare;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.UUID;

import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by Wesna on 3/20/2015.
 */
public class PublishEventActivity extends Activity{
    ImageButton facebookShareButton;
    ImageButton twitterShareButton;
    ImageButton googlePlusShareButton;
    ImageButton instagramShareButton;

    public static final String FACEBOOK_APP_ID = "928197940546370";

    private UiLifecycleHelper uiHelper;

    private Session.StatusCallback callback = new Session.StatusCallback() {

        @Override
        public void call(Session session, SessionState state,
                         Exception exception) {
            // TODO Auto-generated method stub

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        uiHelper.onActivityResult(requestCode, resultCode, data, new FacebookDialog.Callback() {
            @Override
            public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
                Log.e("Activity", String.format("Error: %s", error.toString()));
            }

            @Override
            public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
                Log.i("Activity", "Success!");
            }
        });
    }

    //Share icon mady by Agata Kuczminska from Flat Icon
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Sets whatever happens in this class to the 'activity_main' view
        setContentView(R.layout.activitiy_publish_event);

        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!YOU WERE HERE!!!!!!!!!!!!!!!!!!!!!!!!!
        //Setting up Google API Client
       /* GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_PROFILE, Plus.SCOPE_PLUS_) //This is currently called PLUS_ME
                .build();*/

        //This is for facebook
        uiHelper = new UiLifecycleHelper(this, callback);
        //uiHelper.onCreate(savedInstanceState);

        //Facebook Stuff
        facebookShareButton = (ImageButton) findViewById(R.id.facebook_share_button);
        final Facebook facebook = new Facebook(FACEBOOK_APP_ID);
        facebookShareButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Making a toast to pop up for a successful Facebook post.
                final Context context = getApplicationContext();
                final CharSequence successText = "Facebook timeline updated";
                final int duration = Toast.LENGTH_SHORT;
                final Toast successToast = Toast.makeText(context, successText, duration);

                //Bundle 'params' is for the link box that we be in the Facebook update.
                Bundle params = new Bundle();
                params.putString("name", "New Gather Event!");
                params.putString("caption", "[user_name] posted a new event on Gather!");
                params.putString("description", "User generated event description.");
                params.putString("link", "http://www.ucf.edu/");
                params.putString("picture", "http://i1066.photobucket.com/albums/u408/Tizzbin/Gather/ic_launcher_zpsxewzpyaj.png");

                facebook.dialog(PublishEventActivity.this, "feed", params, new Facebook.DialogListener() {
                    @Override
                    public void onComplete(Bundle bundle) {
                        successToast.show();
                    }

                    @Override
                    public void onFacebookError(FacebookError facebookError) {

                    }

                    @Override
                    public void onError(DialogError dialogError) {

                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }
        });

        //Twitter Stuff
        twitterShareButton = (ImageButton) findViewById(R.id.twitter_share_button);
        twitterShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;

                //Setting up a toast to display if twitter has an error updating status
                final CharSequence failText = "Twitter be having dem problems.";
                final Toast failToast = Toast.makeText(context, failText, duration);

                //Setting up a toast to display if twitter successfully updates status
                final CharSequence successText = "Twitter status updated.";
                final Toast successToast = Toast.makeText(context, successText, duration);


                final String ACCESS_TOKEN = "390460572-ScZPBHtfa36KZ2wezaCTJ6Ne2USNM6E108INY5MR";
                final String ACCESS_TOKEN_SECRET = "MdN8LDgq6izsPHehLXTjDFwuYvSAcJDqasAG8qhwltjgG";
                final String CONSUMER_KEY = "OBBhgIxMJB2a9aqqoYqqZYphm";
                final String CONSUMER_SECRET = "gqKwoFyg3PlYEJHIo2GLzoOSoGjBFxtnLrI09DX0htxihQjDkU";

                new Thread(new Runnable() {
                    public void run() {
                        boolean success = true;

                        ConfigurationBuilder cb = new ConfigurationBuilder();
                        cb.setDebugEnabled(true)
                                .setOAuthConsumerKey(CONSUMER_KEY)
                                .setOAuthConsumerSecret(CONSUMER_SECRET)
                                .setOAuthAccessToken(ACCESS_TOKEN)
                                .setOAuthAccessTokenSecret(ACCESS_TOKEN_SECRET);
                        TwitterFactory tf = new TwitterFactory(cb.build());
                        Twitter twitter = tf.getInstance();
                        String randomFakeURL = UUID.randomUUID().toString();
                        String message = "THIS IS A TEST. [user made event description] tinyURL:[" + randomFakeURL + "]";

                        try {
                            StatusUpdate status = new StatusUpdate(message);
                            twitter.updateStatus(status);
                        } catch (TwitterException e) {
                            success = false;
                            failToast.show();
                            e.printStackTrace();
                        } finally {
                            if(success)
                                successToast.show();
                        }
                    }
                }).start();
            }
        });

        //Google+ Stuff
        googlePlusShareButton = (ImageButton) findViewById(R.id.google_plus_share_button);
        googlePlusShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch the Google+ share dialog with attribution to your app.
                Intent shareIntent = new PlusShare.Builder(PublishEventActivity.this)
                        .setType("text/plain")
                        .setText("New Event on Gather!")
                        .setContentUrl(Uri.parse("http://ucf.edu"))
                        .getIntent();

                startActivityForResult(shareIntent, 0);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_publish_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
