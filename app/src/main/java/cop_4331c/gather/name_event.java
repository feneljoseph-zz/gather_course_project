package cop_4331c.gather;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Random;


public class name_event extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_event);

        Spinner dropdown = (Spinner)findViewById(R.id.event_type);
        String[] items = new String[]{"Corporate", "Birthday", "Dance", "Recreational"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        dropdown.setAdapter(adapter);
    }

    private static String[] recreational = new String[]{"Club Hell Yea", "Drinko de Mayo", "Food Frolic", "Goon Boon", "I Wood Knot No"};
    private static String[] corporate = new String[]{"Investor Meeting","Celebration of Success", "Department Gathering", "Commitment to Excellence", "Exceeding the Vision"};
    private static String[] birthday = new String[]{"Birthday Bash", "Birthapalooza", "Wackytacky Birthday Bashy", "Forty Five Kids, Hot Dogs and Spaghetti", "21st Century Old Fest"};
    private static String[] dance = new String[]{"Dance Contagion","Dance Fusion","Dance Vibrations","Dancellennium", "Boogaloo", "Boogiethon"};

    public void previewUserName(View view) {
        EditText userName = (EditText) findViewById(R.id.user_event_name);
        String preview = userName.getText().toString();

        TextView previewName = (TextView) findViewById(R.id.name_preview);
        previewName.setText(preview);
    }

    public void previewRandomName(View view) {
        String preview;
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
