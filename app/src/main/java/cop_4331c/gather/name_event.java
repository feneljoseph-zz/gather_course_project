package cop_4331c.gather;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Random;


public class name_event extends ActionBarActivity {
    private ParseObject targetEvent;
    private String TargetEventID;
    private String preview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_event);

        Spinner dropdown = (Spinner)findViewById(R.id.event_type);
        String[] items = new String[]{"Corporate", "Birthday", "Dance", "Recreational"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        dropdown.setAdapter(adapter);

        Intent intent = getIntent();
        TargetEventID = intent.getStringExtra("TargetObjectID");

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Event");
        query.getInBackground(TargetEventID, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    targetEvent = object;
                    TextView title = (TextView) findViewById(R.id.txtEventTitle);

                    try {title.setText(object.get("name").toString());}
                    catch (Exception ex) {title.setText("No event name set");}

                } else {
                    ProgressDialog dlg = new ProgressDialog(name_event.this);
                    dlg.setMessage("Could not get event");
                    dlg.show();
                }
            }
        });
    }

    private static String[] recreational = new String[]{"Club Hell Yea", "Drinko de Mayo", "Food Frolic", "Goon Boon", "I Wood Knot No"};
    private static String[] corporate = new String[]{"Investor Meeting","Celebration of Success", "Department Gathering", "Commitment to Excellence", "Exceeding the Vision"};
    private static String[] birthday = new String[]{"Birthday Bash", "Birthapalooza", "Wackytacky Birthday Bashy", "Forty Five Kids, Hot Dogs and Spaghetti", "21st Century Old Fest"};
    private static String[] dance = new String[]{"Dance Contagion","Dance Fusion","Dance Vibrations","Dancellennium", "Boogaloo", "Boogiethon"};

    public void previewUserName(View view) {
        EditText userName = (EditText) findViewById(R.id.txtEventTitle);
        String preview = userName.getText().toString();

        TextView previewName = (TextView) findViewById(R.id.name_preview);
        previewName.setText(preview);
    }

    public void previewRandomName(View view) {
        String selected;
        String randName = "";

        Random rand = new Random();
        EditText creatorName = (EditText) findViewById(R.id.creator_name);
        Spinner eventType = (Spinner)findViewById(R.id.event_type);
        TextView previewName = (TextView) findViewById(R.id.name_preview);

        int n = rand.nextInt(5);
        selected = eventType.getSelectedItem().toString();

        switch (selected) {
            case "Recreational": randName = recreational[n];
                break;
            case "Birthday": randName = birthday[n];
                break;
            case "Corporate": randName = corporate[n];
                break;
            case "Dance": randName = dance[n];
        }

        preview = creatorName.getText().toString() + "'s " + randName;
        previewName.setText(preview);
    }

    // Currently not functional
    public void saveChanges(View view) {
        try {targetEvent.put("name", preview);}
        catch (Exception ex) {}

        try {targetEvent.save();}
        catch (ParseException e) { Toast.makeText(name_event.this, "Failed to save event", Toast.LENGTH_SHORT).show(); }

        final ProgressDialog load = new ProgressDialog(name_event.this);
        load.setTitle("Please wait");
        load.setMessage("Updating name event");
        load.show();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                load.dismiss();
                Intent goBack = new Intent(name_event.this, new_features_list.class );
                goBack.putExtra("TargetObjectID", TargetEventID);
                startActivity(goBack);
            }
        }, 3000);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_name_event, menu);
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
