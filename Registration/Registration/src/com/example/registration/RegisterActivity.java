package com.example.registration;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
 
public class RegisterActivity extends Activity {
    
	private Button btnRegister;
    private Button btnLinkToLogin;
    private EditText inputFullName;
    private EditText inputEmail;
    private EditText inputPassword;
    private TextView registerErrorMsg;
    
    private ProgressDialog dialog;
    private Context context;
    private UserFunctions userFunction;
    private JSONObject json;
    private String name;
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        context = this;
        
        // Importing all assets like buttons, text fields
        inputFullName = (EditText) findViewById(R.id.registerName);
        inputEmail = (EditText) findViewById(R.id.registerEmail);
        inputPassword = (EditText) findViewById(R.id.registerPassword);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);
        registerErrorMsg = (TextView) findViewById(R.id.register_error);
         
        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {        
            public void onClick(View view) {
                name = inputFullName.getText().toString();
                email = inputEmail.getText().toString();
                password = inputPassword.getText().toString();
                
                dialog = new ProgressDialog(context);
    			dialog.setTitle("Registering  on server");
    			dialog.setMessage("Please wait...");
    			dialog.setCancelable(false);
    			dialog.setIndeterminate(true);
    			dialog.show();
    			new RegisterTask().execute();
                
            }
        });
 
        // Link to Login Screen
        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {
 
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        MainActivity.class);
                startActivity(i);
                // Close Registration View
                finish();
            }
        });
    }
    
    
    private class RegisterTask extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... params) {
			
			userFunction = new UserFunctions();
            json = userFunction.registerUser(name, email, password);
         
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
            		registerErrorMsg.setText("Connection Problem. Please Check your Internet Connection");
                
            	else if (json.getString(KEY_SUCCESS) != null) {
                    registerErrorMsg.setText("");
                    String res = json.getString(KEY_SUCCESS);
                    if(Integer.parseInt(res) == 1){
                        // user successfully registered
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
                        // Close Registration Screen
                        finish();
                    }else{
                        // Error in registration
                        registerErrorMsg.setText("Error occured in registration");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // end Check
			
			
		}
    }
}