package cop_4331c.gather;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class new_features_list extends ActionBarActivity {
    private String TargetEventID;
    private ParseObject TargetEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_features_list);

        Intent intent = getIntent();
        TargetEventID = intent.getStringExtra("TargetObjectID");

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Event");
        query.getInBackground(TargetEventID, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    TextView title = (TextView) findViewById(R.id.txtEventTitle);

                    try {title.setText(object.get("name").toString());}
                    catch (Exception ex) {title.setText("No event name set");}

                } else {
                    ProgressDialog dlg = new ProgressDialog(new_features_list.this);
                    dlg.setMessage("Could not get event");
                    dlg.show();
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_features_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void PublishActivity(View view) {
        Intent launch = new Intent(this, PublishEventActivity.class);
        launch.putExtra("TargetObjectID", TargetEventID);
        startActivity(launch);
        finish();
    }

    public void nameEventActivity(View view) {
        Intent nameEventActivity = new Intent(this, name_event.class);
        nameEventActivity.putExtra("TargetObjectID", TargetEventID);
        startActivity(nameEventActivity);
        finish();
    }

    public void onBackPressed() {
        startActivity(new Intent(new_features_list.this, MainActivity.class));
    }
}
