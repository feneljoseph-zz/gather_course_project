package cop_4331c.gather;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;

public class WelcomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

    public void onBackPressed() {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
    }

    public void signIn(View view) {
        Intent signIn = new Intent(this, SignInActivity.class);
        startActivity(signIn);
    }

    public void signUp(View view) {
        Intent signUp = new Intent(this,SignUpActivity.class);
        startActivity(signUp);
    }
}
