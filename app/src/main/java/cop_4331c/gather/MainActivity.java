package cop_4331c.gather;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import cop_4331c.gather.ui.HostMusicPlaylistHomeActivity;

public class MainActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startActivity(new Intent(this, HostMusicPlaylistHomeActivity.class));
    }
}
