package cop_4331c.gather;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class SendInvitesActivity extends ActionBarActivity {

    private ParseObject TargetEvent;
    private String TargetEventID;
    private EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_invites);

        Intent intent = getIntent();
        TargetEventID = intent.getStringExtra("TargetObjectID");

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Event");
        query.getInBackground(TargetEventID, new GetCallback<ParseObject>() {
            public void done(final ParseObject object, ParseException e) {
                if (e == null) {

                    email = (EditText) findViewById(R.id.recipientEmail);
                    TargetEvent = object;

                } else {
                    ProgressDialog dlg = new ProgressDialog(SendInvitesActivity.this);
                    dlg.setMessage("Could not get event");
                    dlg.show();
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_send_invites, menu);
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

    public void sendInvite(View view) {
        final ArrayList<String> users = new ArrayList<String>();
        String recipientEmail = email.getText().toString();

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("email", recipientEmail);
        query.findInBackground(new FindCallback<ParseUser>()
        {
            public void done(List<ParseUser> userList, com.parse.ParseException e)
            {
                if (e == null) {

                    //TargetEvent.addUnique("Attendees", userList.get(0).getObjectId());
                    //TargetEvent.add("NewField",userList.get(0).getObjectId());
                    Toast.makeText(SendInvitesActivity.this,"Invite sent", Toast.LENGTH_SHORT);

                    Intent intent = new Intent(SendInvitesActivity.this, new_features_list.class);
                    intent.putExtra("TargetObjectID", TargetEventID);
                    startActivity(intent);

                } else {
                    ProgressDialog error = new ProgressDialog(SendInvitesActivity.this);
                    error.setMessage("Could not get recipient " + email.getText().toString());
                    error.show();
                }
            }
        });

/*
        ParseQuery<ParseUser> getRecipient = ParseUser.getQuery();
        getRecipient.whereEqualTo("username", recipientEmail);
        getRecipient.getInBackground(new GetCallback<ParseUser>() {
            public void done(ParseUser user, ParseException e) {
                if (e == null) {

                    TargetEvent.add("Attendees", user.getObjectId().toString());
                    Toast.makeText(SendInvitesActivity.this,"Invite sent", Toast.LENGTH_SHORT);

                    Intent intent = new Intent(SendInvitesActivity.this, new_features_list.class);
                    intent.putExtra("TargetObjectID", TargetEventID);
                    startActivity(intent);

                } else {

                    ProgressDialog error = new ProgressDialog(SendInvitesActivity.this);
                    error.setMessage("Could not get recipient " + email.getText().toString());
                    error.show();

                }
            }
        });
*/


    }

}
