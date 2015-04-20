package cop_4331c.gather;

import android.app.Application;
import android.os.Bundle;

import com.parse.Parse;

public class GatherActivity extends Application {

        protected void onCreate(Bundle savedInstanceState) {
            // Enable Local Datastore.
            Parse.enableLocalDatastore(this);

            Parse.initialize(this, "r0AWTV2rHQu1LKLuvghS5dxgw32hKeBWDnVmyxNQ", "THis9813mCk50ooetnDlY9wIkAcYDkBn10IE5u2a");
        }
}

