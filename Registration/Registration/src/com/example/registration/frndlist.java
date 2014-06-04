package com.example.registration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
 
import com.example.registration.UserFunctions;
 
public class frndlist extends Activity {
    
    Button btnLogout;
    
    static JSONObject jObj1 = null;
    static JSONObject jObjN = null;
    static JSONObject jObjE = null;
    JSONParser jsonp ;
    ListView listView ;
    String[] valueN;
    String[] valueE;
    Iterator<String> iter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frndlist);
        
        String emailid =getIntent().getExtras().getString("email");
        listView = (ListView) findViewById(R.id.list);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("email",emailid));
        nameValuePairs.add(new BasicNameValuePair("tag","frnd"));
        jsonp = new JSONParser();
        jObj1= jsonp.getJSONFromUrl("http://kejri.netii.net/q.php", nameValuePairs);
        
        
			try {
				jObjN = jObj1.getJSONObject("name");
				jObjE = jObj1.getJSONObject("email");
				iter = jObjN.keys();
				valueE = new String[jObjE.length()];
				valueN = new String[jObjE.length()];
				
				for (int i = 0; i < valueE.length; i++) 
				{
					valueE[i] = jObjE.getString(Integer.toString(i+1));
					valueN[i] = jObjN.getString(Integer.toString(i+1));
					 Log.e("valuear", valueN[i]);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			 Log.e("valueE", jObjE.toString());
			 Log.e("valueN", jObjN.toString());
			 Log.e("valuear",Integer.toString( valueN.length));
     
			 ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		              android.R.layout.simple_list_item_1, android.R.id.text1, valueN);
			 Log.e("valueadapt", adapter.getItem(1).toString());
            listView.setAdapter(adapter);
        
        
        
    }
}