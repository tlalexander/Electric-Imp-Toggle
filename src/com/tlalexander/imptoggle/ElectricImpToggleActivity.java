/*
 * Copyright 2012 Taylor Alexander
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tlalexander.imptoggle;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.tlalexander.imptoggle.R;


/**
 * A simple app to toggle on and off a thing connected to your impee. See README.md for more details
 * 
 * 
 * @author Taylor Alexander
 */



public class ElectricImpToggleActivity extends Activity {
	
	
	private Button buttonOn;
	private Button buttonOff;
	private Button buttonScan;
	private String address;
	
	private static final String PREFS_NAME = "PreferenceFile";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        address = settings.getString("address", null);
        
        buttonOn = (Button) findViewById(R.id.buttonOn);
        buttonOn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	   new postDataTask().execute("1");
            }
        });
        
        buttonOff = (Button) findViewById(R.id.buttonOff);
        buttonOff.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	   new postDataTask().execute("0");
     
            }
        });
        
        buttonScan = (Button) findViewById(R.id.buttonScan);
        buttonScan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	
            	   IntentIntegrator integrator = new IntentIntegrator(ElectricImpToggleActivity.this);
            	   integrator.initiateScan(IntentIntegrator.QR_CODE_TYPES);
            	   
            }
        });
        
        if(address==null)
        {
        	buttonOn.setVisibility(View.GONE);
        	buttonOff.setVisibility(View.GONE);
        
        }
        
        
    }
    
    
    
    class postDataTask extends AsyncTask<String, Void, Void> {

        private Exception exception;

        protected Void doInBackground(String... parameter) {
            try {
               
            	  // Perform action on click
            	HttpClient client = new DefaultHttpClient();
            	HttpPost post = new HttpPost(address);
            	
            	List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            	pairs.add(new BasicNameValuePair("value",parameter[0]));
            	//pairs.add(new BasicNameValuePair("key2", "value2"));
            	try {
					post.setEntity(new UrlEncodedFormEntity(pairs));
					client.execute(post);
					
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	
            } catch (Exception e) {
                this.exception = e;
            
                return null;
            }
			return null;
        }

        protected void onPostExecute() {
            // TODO: check this.exception 
            // TODO: do something with the feed
        }
     

  
    
    
}
    
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    	  IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
    	  if (scanResult != null) {
    	    // handle scan result
    		  
    		buttonOn.setVisibility(View.VISIBLE);
          	buttonOff.setVisibility(View.VISIBLE);
          	
          	address=scanResult.getContents();
          	
          	 // We need an Editor object to make preference changes.
	        // All objects are from android.context.Context
	        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	        SharedPreferences.Editor editor = settings.edit();  

	        editor.putString("address", address);

	        // Commit the edits!
	        editor.commit();
    		  
    		  
    	  }else
    	  {
    	  // else continue with any other code you need in the method
    	  Toast.makeText(getApplicationContext(), R.string.noQRCode, 1000).show();
    	  }
    	 
    	}
    
    
    
}
    
   