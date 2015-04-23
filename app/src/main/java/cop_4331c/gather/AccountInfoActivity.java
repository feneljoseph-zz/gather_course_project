package cop_4331c.gather;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class AccountInfoActivity extends ActionBarActivity {

    private Button save;// = (Button) findViewById(R.id.buttonSaveChanges);
    private EditText firstName; // = (EditText) findViewById(R.id.textFirstName);
    private EditText lastName; // = (EditText) findViewById(R.id.textLastName);
    private EditText userName; // = (EditText) findViewById(R.id.textUsernameLogin);
    private EditText phoneNumber; // = (EditText) findViewById(R.id.textPhoneNumber);
    private EditText password; // = (EditText) findViewById(R.id.textPasswordLogin);
    private EditText verifyPassword; // = (EditText) findViewById(R.id.textPasswordVerify);
    private ParseUser user;

    @Override
     protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_info);
        //Get user info and fill fields with current info
        user = ParseUser.getCurrentUser();
        if (user!=null)
        {
            //fill data fields
            String firstName1 = (String) user.get("firstName");
            firstName = (EditText) findViewById(R.id.textFirstName);
            firstName.setText(firstName1);
            String lastName1 = (String) user.get("lastName");
            lastName = (EditText) findViewById(R.id.textLastName);
            lastName.setText(lastName1);
            String userName1 = user.getUsername();
            userName = (EditText) findViewById(R.id.textUsernameLogin);
            userName.setText(userName1);
            String phoneNum = (String) user.get("phoneNumber");
            phoneNumber = (EditText) findViewById(R.id.textPhoneNumber);
            phoneNumber.setText(phoneNum);

        }
        //Setup onClickListener for updating user info when save changes is pressed
        save = (Button) findViewById(R.id.buttonSaveChanges);
        save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                //Check if field has data, if so update changes
                if (firstName.getText().toString().trim().length() > 0)
                    user.put("firstName", firstName.getText().toString());
                if (lastName.getText().toString().trim().length() > 0)
                    user.put("lastName", lastName.getText().toString());
                if (userName.getText().length() > 0)
                    user.setUsername(userName.getText().toString());
                if (phoneNumber.getText().length() > 0 /*&& isValidPhone(phoneNumber.getText().toString())*/)
                    user.put("phoneNumber", phoneNumber.getText().toString());
                password = (EditText) findViewById(R.id.textPasswordLogin);
                verifyPassword = (EditText) findViewById(R.id.textPasswordVerify);

                if(password.getText().length()>6 && (password.getText().toString().compareTo(verifyPassword.getText().toString()) == 0)) {
                    user.setPassword(password.getText().toString());
                    //save to parse
                    user.saveInBackground();
                    Toast.makeText(getApplicationContext(), "User Info Was Updated", Toast.LENGTH_LONG)
                    .show();
                }
                else if(password.getText().length()==0)
                {
                    user.saveInBackground();
                    Toast.makeText(getApplicationContext(), "User Info Was Updated", Toast.LENGTH_LONG)
                        .show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Make sure your passwords match and are over" +
                            " 6 characters long!",Toast.LENGTH_LONG).show();
                }

                goBack();
            }

        });
    }

    private boolean isValidPhone(String s) {
        String regex = "^\\+(?:[0-9] ?){6,14}[0-9]$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s);

        return matcher.matches();
    }

    public void goBack() {
        Intent goBack = new Intent(this, MainActivity.class);
        startActivity(goBack);
    }

    private boolean isMatching(EditText text1, EditText text2) {
        return text1.getText().toString().equals(text2.getText().toString());
    }

}