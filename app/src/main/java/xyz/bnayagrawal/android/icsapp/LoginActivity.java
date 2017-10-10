package xyz.bnayagrawal.android.icsapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.transition.TransitionManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import xyz.bnayagrawal.android.icsapp.internet.NetworkOperation;
import xyz.bnayagrawal.android.icsapp.internet.iNetCallback;

public class LoginActivity extends AppCompatActivity implements iNetCallback {

    // UI references.
    private AutoCompleteTextView mUsernameView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    // Network Operation
    private NetworkOperation networkOperation;
    private String result;
    private boolean ongoing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mUsernameView = (AutoCompleteTextView) findViewById(R.id.act_login_email);
        mPasswordView = (EditText) findViewById(R.id.act_login_password);

        //On enter key move the focus to set password
        mUsernameView.setOnEditorActionListener(
                new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                        if (id == EditorInfo.IME_ACTION_NEXT)
                            mPasswordView.requestFocus();
                        return false;
                    }
                }
        );

        //If the user presses enter key in the keypad, login attempt will be performed
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_ACTION_DONE) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.act_login_sign_in);

        //performs login process when clicked
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.act_login_form);
        mProgressView = findViewById(R.id.act_login_progress);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();

        // If the cancel flag is set true login attempt wont be made.
        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            String jsonData = "{" + "\"username\":\"" + username + "\"" + ", " + "\"password\":" + "\"" + password + "\"" + " }";
            networkOperation = new NetworkOperation(this);
            networkOperation.beginOperation(getString(R.string.url_login), jsonData);
        }
    }

    //Shows the progress UI and hides the login form.
    private void showProgress(final boolean show) {
        //My custom transition
        TransitionManager.beginDelayedTransition((LinearLayout) mProgressView);
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        TransitionManager.beginDelayedTransition((CardView) mLoginFormView);
        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    //DEALING WITH THE SERVER
    @Override
    public void netUpdateResult(String result) {
        if (result != null) {
            if (result.equals("HTTP error code: 403")) {
                Toast.makeText(this, "Incorrect username or password", Toast.LENGTH_SHORT).show();
            } else {
                //parse json data
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString("status");
                    if (status.equals("error")) {
                        Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    } else {
                        HashMap<String, String> hmUserData = getParsedUserData(jsonObject.getJSONObject("data"));
                        if (hmUserData == null) {
                            Toast.makeText(getApplicationContext(), "Something went wrong :(", Toast.LENGTH_LONG).show();
                        } else {
                            //update shared preferences
                            SharedPreferences sharedPref = getSharedPreferences(getString(R.string.SP_USER_FILE), Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();

                            editor.putInt("USER_ID", Integer.parseInt(hmUserData.get("id")));
                            editor.putString("USER_FIRST_NAME", hmUserData.get("firstName"));
                            editor.putString("USER_LAST_NAME", hmUserData.get("lastName"));
                            editor.putString("USER_USERNAME", hmUserData.get("username"));
                            editor.putString("USER_EMAIL", hmUserData.get("email"));
                            editor.putString("USER_JWT_TOKEN", hmUserData.get("jwtToken"));
                            editor.apply();

                            //start the activity
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    }
                } catch (JSONException je) {
                    Toast.makeText(getApplicationContext(), "Error parsing data! Contact the developer.", Toast.LENGTH_LONG).show();
                }
            }
        } else {
            Toast.makeText(this, "error occur!", Toast.LENGTH_SHORT).show();
        }
        showProgress(false);
    }

    @Override
    public NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo();
    }

    @Override
    public void netTaskComplete() {
        ongoing = false;
        if (networkOperation != null) {
            networkOperation.cancelOperation();
        }
    }

    @Override
    public void netOnProgressUpdate(int progressCode, int percentComplete) {
        //not required in this context
    }

    //will return a hashmap containing user details and jwt token
    private HashMap<String, String> getParsedUserData(JSONObject jsonDataObject) {
        HashMap<String, String> hm = new HashMap<>();

        try {
            String jwtToken = jsonDataObject.getString("jwt");
            JSONObject userJsonObject = jsonDataObject.getJSONObject("user");
            //get user details from json object and write to hashmap
            hm.put("id", String.valueOf(userJsonObject.getInt("id")));
            hm.put("firstName", userJsonObject.getString("firstName"));
            hm.put("lastName", userJsonObject.getString("lastName"));
            hm.put("username", userJsonObject.getString("username"));
            hm.put("email", userJsonObject.getString("email"));
            hm.put("jwtToken", jwtToken);
        } catch (JSONException je) {
            hm = null;
        }

        return hm;
    }
}

