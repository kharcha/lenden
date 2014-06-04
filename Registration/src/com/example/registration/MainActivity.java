package com.example.registration;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
public class MainActivity extends Activity {

	 	private Button btnLogin;
	    private Button btnLinkToRegister;
	    private EditText inputEmail;
	    private EditText inputPassword;
	    private TextView loginErrorMsg;
	    private CheckBox rememberme;
	    private Boolean ischecked = false;
	    
	    private ProgressDialog dialog;
	    private Context context;
	    private UserFunctions userFunction;
        private JSONObject json;
        private String email;
        private String password;
        
	    // JSON Response node names
	    private static String KEY_SUCCESS = "success";
	    private static String KEY_ERROR = "error";
	    private static String KEY_ERROR_MSG = "error_msg";
	    private static String KEY_UID = "uid";
	    private static String KEY_NAME = "name";
	    private static String KEY_EMAIL = "email";
	    private static String KEY_CREATED_AT = "created_at";
	    
	    
	    
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	    	StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	        StrictMode.setThreadPolicy(policy); 
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_main);
	        context = this;
	 
	        // Importing all assets like buttons, text fields
	        rememberme = (CheckBox)findViewById(R.id.remember);
	        inputEmail = (EditText) findViewById(R.id.loginEmail);
	        inputPassword = (EditText) findViewById(R.id.loginPassword);
	        btnLogin = (Button) findViewById(R.id.btnLogin);
	        btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);
	        loginErrorMsg = (TextView) findViewById(R.id.login_error);
	        SharedPreferences mPreferences = getSharedPreferences("CurrentUser", MODE_PRIVATE);
	        final SharedPreferences.Editor editor = mPreferences.edit();
	        ischecked = mPreferences.getBoolean("savelogin", false);
	        Log.e("msg", ischecked.toString());
	        if(ischecked==true)
	        {
	        	inputEmail.setText(mPreferences.getString("email", null));
	        	inputPassword.setText(mPreferences.getString("password", null));
	        	login();
	        }
	        // Login button Click Event
	        btnLogin.setOnClickListener(new View.OnClickListener() {
	 
	            public void onClick(View view) {
	                if(rememberme.isChecked())
	                {
	                	editor.putString("email", inputEmail.getText().toString());
	                	editor.putString("password", inputPassword.getText().toString());
	                	editor.putBoolean("savelogin", true);
	                	//rememberme.setChecked(true);
	                	editor.commit();
	                }
	                login();
	            }
	        });
	 
	        // Link to Register Screen
	        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {
	 
	            public void onClick(View view) {
	                Intent i = new Intent(getApplicationContext(),
	                        RegisterActivity.class);
	                startActivity(i);
	                finish();
	            }
	        });
	    }
	    
	    public void login()
	    {
	    	email = inputEmail.getText().toString();
            password = inputPassword.getText().toString();
            
            dialog = new ProgressDialog(context);
			dialog.setTitle("Logging in");
			dialog.setMessage("Please wait...");
			dialog.setCancelable(false);
			dialog.setIndeterminate(true);
			dialog.show();
			new LoginTask().execute();

	    	
	    }
	    
	    
	    private class LoginTask extends AsyncTask<Void, Void, Void>{

			@Override
			protected Void doInBackground(Void... params) {
				
				userFunction = new UserFunctions();
	            json = userFunction.loginUser(email, password);
             
				return null;
			}
	    	
			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				
				if (dialog != null)
					dialog.dismiss();
				
				// check for login response
	            try {
	            	if (json == null)
	            		loginErrorMsg.setText("Connection Problem. Please Check your Internet Connection");
	            	
	            	else if (json.getString(KEY_SUCCESS) != null) {
	                    loginErrorMsg.setText("");
	                    String res = json.getString(KEY_SUCCESS);
	                    if(Integer.parseInt(res) == 1){
	                        // user successfully logged in
	                        // Store user details in SQLite Database
	                        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
	                        JSONObject json_user = json.getJSONObject("user");
	                         
	                        // Clear all previous data in database
	                        userFunction.logoutUser(getApplicationContext());
	                        db.addUser(json_user.getString(KEY_NAME), json_user.getString(KEY_EMAIL), json.getString(KEY_UID), json_user.getString(KEY_CREATED_AT));                       
	                         
	                        // Launch Dashboard Screen
	                        Intent dashboard = new Intent(getApplicationContext(), Home.class);
	                         
	                        // Close all views before launching Dashboard
	                        dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	                        dashboard.putExtra("email",email);
	                        startActivity(dashboard);
	                         
	                        // Close Login Screen
	                        finish();
	                    }else{
	                        // Error in login
	                        loginErrorMsg.setText("Incorrect username/password");
	                    }
	                }
	            } catch (JSONException e) {
	                e.printStackTrace();
	            }
                // end Check
				
				
			}
	    }
	}