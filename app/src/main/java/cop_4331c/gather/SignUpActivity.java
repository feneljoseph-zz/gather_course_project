package cop_4331c.gather;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import cop_4331c.gather.util.MessageService;

public class SignUpActivity extends ActionBarActivity {
    private EditText firstnameView;
    private EditText lastnameView;
    private EditText usernameView;
    private EditText phonenumberView;
    private EditText passwordView;
    private EditText passwordAgainView;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Set up the signup form.
        firstnameView = (EditText) findViewById(R.id.textFirstName);
        lastnameView = (EditText) findViewById(R.id.textLastName);
        usernameView = (EditText) findViewById(R.id.textUsernameLogin);
        phonenumberView = (EditText) findViewById(R.id.textPhoneNumber);
        passwordView = (EditText) findViewById(R.id.textPasswordLogin);
        passwordAgainView = (EditText) findViewById(R.id.textPasswordVerify);

        // Set up the submit button click handler
        findViewById(R.id.buttonSignUp).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                // Validate the sign up data
                boolean validationError = false;
                StringBuilder validationErrorMessage =
                        new StringBuilder("Please ");
                if (isEmpty(firstnameView) || isEmpty(lastnameView)) {
                    validationError = true;
                    validationErrorMessage.append("enter your first & last name");
                }
                if (isEmpty(usernameView) && !isEmailValid(usernameView) ) {
                    if (validationError) {
                        validationErrorMessage.append(", and ");
                    }
                    validationError = true;
                    validationErrorMessage.append("enter a valid username");
                }
                if (isEmpty(passwordView)) {
                    if (validationError) {
                        validationErrorMessage.append(", and ");
                    }
                    validationError = true;
                    validationErrorMessage.append("enter a password");
                }
                if (!isMatching(passwordView, passwordAgainView)) {
                    if (validationError) {
                        validationErrorMessage.append(", and ");
                    }
                    validationError = true;
                    validationErrorMessage.append("enter the same password twice");
                }
                validationErrorMessage.append(".");

                // If there is a validation error, display the error
                if (validationError) {
                    Toast.makeText(SignUpActivity.this, validationErrorMessage.toString(), Toast.LENGTH_LONG)
                            .show();
                    return;
                }

                // Set up a progress dialog
                final ProgressDialog dlg = new ProgressDialog(SignUpActivity.this);
                dlg.setTitle("Please wait.");
                dlg.setMessage("Signing up.  Please wait.");
                dlg.show();

                // Set up a new Parse user
                ParseUser user = new ParseUser();
                user.setUsername(usernameView.getText().toString());
                user.setPassword(passwordView.getText().toString());

                user.put("firstName", firstnameView.getText().toString());
                user.put("lastName", lastnameView.getText().toString());
                user.put("email", usernameView.getText().toString());

                if ( isNumberValid(phonenumberView) && !isEmpty(phonenumberView) ) {
                    user.put("phoneNumber", phonenumberView.getText().toString());
                }

                // Call the Parse signup method
                user.signUpInBackground(new SignUpCallback() {

                    @Override
                    public void done(ParseException e) {
                        dlg.dismiss();
                        if (e != null) {
                            // Show the error message
                            Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        } else {
                            // Start an intent for the dispatch activity
                            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                            final Intent serviceIntent = new Intent(getApplicationContext(), MessageService.class);
                            startService(serviceIntent);
                            startActivity(intent);
                        }
                    }
                });
            }
        });
    }

    private boolean isEmpty(EditText text) {
        return !(text.getText().toString().trim().length() > 0);
    }

    public static boolean isEmailValid(EditText text) {
        String temp = text.getText().toString();

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(temp);

        return matcher.matches();
    }

    public static boolean isNumberValid(EditText text) {
        String temp = text.getText().toString();

        String regex = "^\\+(?:[0-9] ?){6,14}[0-9]$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(temp);

        return matcher.matches();
    }

    private boolean isMatching(EditText text1, EditText text2) {
        return text1.getText().toString().equals(text2.getText().toString());
    }
}