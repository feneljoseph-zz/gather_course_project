package cop_4331c.gather;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import cop_4331c.gather.util.MessageService;


public class MainActivity extends ActionBarActivity implements ActionBar.TabListener {

    SectionsPagerAdapter mSectionsPagerAdapter;

    ViewPager mViewPager;

    private TextView Name;
    private TextView userName;
    private TextView phoneNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));

        }

        String currentUserId = ParseUser.getCurrentUser().getObjectId();
        final ArrayList<String> names = new ArrayList<String>();

        final ArrayList<String> events = new ArrayList<String>();
        ParseUser currentUser = ParseUser.getCurrentUser();

        ParseQuery<ParseUser> query = ParseUser.getQuery();

        //don't include yourself
        query.whereNotEqualTo("objectId", currentUserId);
        query.findInBackground(new FindCallback<ParseUser>()
        {
            public void done(List<ParseUser> userList, com.parse.ParseException e)
            {
            if (e == null) {
                for (int i=0; i<userList.size(); i++) {
                    names.add(userList.get(i).getUsername().toString());
                }
                ListView usersListView = (ListView)findViewById(R.id.sinchUserLayout);
                ArrayAdapter<String> namesArrayAdapter =
                        new ArrayAdapter<String>(getApplicationContext(),
                                R.layout.user_list_item, names);
                usersListView.setAdapter(namesArrayAdapter);
                usersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> a, View v, int i, long l)
                    {
                        openConversation(names, i);
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(),
                        "Error loading user list",
                        Toast.LENGTH_LONG).show();
            }
            }
        });

        ParseQuery<ParseObject> eventQuery = new ParseQuery("Event");
        eventQuery.whereEqualTo("Attendees", currentUserId);
        eventQuery.findInBackground(new FindCallback<ParseObject>()
        {
            @Override
            public void done(List eventList, com.parse.ParseException e)
            {
                if(e == null) {
                    for (int i=0; i<eventList.size(); i++) {
                        ParseObject temp = (ParseObject) eventList.get(i);
                        events.add(temp.get("name").toString());
                    }
                    ListView eventsListView = (ListView)findViewById(R.id.EventLayout);
                    ArrayAdapter<String> eventsArrayAdapter =
                            new ArrayAdapter<String>(getApplicationContext(),
                                    R.layout.user_list_item, events);
                    eventsListView.setAdapter(eventsArrayAdapter);
                    eventsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> a, View v, int i, long l)
                        {
                            openConversation(events, i);
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Error loading event list",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        final Intent serviceIntent = new Intent(getApplicationContext(), MessageService.class);
        startService(serviceIntent);
    }

    public void openConversation(ArrayList<String> names, int pos) {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username", names.get(pos));
        query.findInBackground(new FindCallback<ParseUser>() {

            public void done(List<ParseUser> user, com.parse.ParseException e)
            {
                if (e == null) {
                    //start the messaging activity
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Error finding that user",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private int backButtonCount = 0;
    public void onBackPressed() {
        if(backButtonCount >= 1)
        {
            backButtonCount = 0;
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "Press the back button once again to close the application.", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Depending on item selected, perform different action
        switch (item.getItemId()) {
            case R.id.main_search:
                return true;
            case R.id.main_account_info:
                return true;
            case R.id.main_sign_out:
                ParseUser.getCurrentUser().logOut();
                startActivity(new Intent(MainActivity.this, WelcomeActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    public void onDestroy() {
        stopService(new Intent(this, MessageService.class));
        super.onDestroy();
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch(position) {
                case 0:
                    return new EventFragment();
                case 1:
                    return new ExchangesFragment();
                case 2:
                    return new ProfileFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }

    public static class EventFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static EventFragment newInstance(int sectionNumber) {
            EventFragment fragment = new EventFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_event, container, false);
            return rootView;
        }
    }

    public static class ExchangesFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static ExchangesFragment newInstance(int sectionNumber) {
            ExchangesFragment fragment = new ExchangesFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_exchanges, container, false);

            return rootView;
        }
    }

    public class ProfileFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        private ProfileFragment newInstance(int sectionNumber) {
            ProfileFragment fragment = new ProfileFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            ParseUser user = null;

            try {
                user = ParseUser.getCurrentUser();
            } catch (Exception e) {
                System.out.println(e);
            }

            if (user != null) {
                Name = (TextView) findViewById(R.id.textNameProfile);

                //fillTextView(R.id.textNameProfile, (String) user.get("firstName").toString());

                //userName = (TextView) findViewById(R.id.textUsernameProfile);
                //userName.setText(user.getUsername().toString());

                //phoneNumber = (TextView) findViewById(R.id.textPhoneNumberProfile);
                //phoneNumber.setText((CharSequence) user.get("phoneNumber"));
            }

            View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
            return rootView;
        }
    }

    public void fillTextView (int tViewID, String text) {
        TextView temp = (TextView) findViewById(tViewID);
        temp.setText(text);
    }

    public static class EventAdapter extends ArrayAdapter<ParseObject> {

        private static class ViewHolder {
            private TextView itemView;
        }

        public EventAdapter(Context context, int textViewResourceId, ArrayList<ParseObject> items) {
            super(context, textViewResourceId, items);
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = LayoutInflater.from(this.getContext())
                        .inflate(R.layout.listview_association, parent, false);

                ViewHolder viewHolder = new ViewHolder();
                viewHolder.itemView = (TextView) convertView.findViewById(R.id.ItemView);

                convertView.setTag(viewHolder);
            } else {
                ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            }

            EventAdapter item = getItem(position);
            if (item!= null) {
                // My layout has only one TextView
                // do whatever you want with your string and long
                viewHolder.itemView.setText(String.format("%s %d", item.reason, item.long_val));
            }

            return view;
        }
    }

}
