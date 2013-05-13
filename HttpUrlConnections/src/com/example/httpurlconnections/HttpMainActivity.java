package com.example.httpurlconnections;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;

import com.example.httpurlconnection.network.AsyncHttpParams;
import com.example.httpurlconnection.network.AsyncHttpResponse;
import com.example.httpurlconnection.network.AsyncHttpURLConnection;

public class HttpMainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_http_main);
		
            
		HttpUrlAsynctask mtask =   new HttpUrlAsynctask();
		mtask.execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.http_main, menu);
		return true;
	}

	

	public class HttpUrlAsynctask extends AsyncTask<String, Void, String>
	{

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			// Simple HTTPUrlConnection GET Request
			AsyncHttpURLConnection mGETClient = new AsyncHttpURLConnection();

			AsyncHttpResponse mGETRes = mGETClient.get(" Your http Url ");
			
			
			// With Params HTTPUrlConnection GET Request
			AsyncHttpURLConnection mmGETClientWithParams = new AsyncHttpURLConnection();
			
			AsyncHttpParams mGETParams = new AsyncHttpParams();
			mGETParams.put("key", "value");
		
			AsyncHttpResponse mGETResWithParams = 
					mmGETClientWithParams.get(" Your http Url ",mGETParams);
			
			
			
			// Simple HTTPUrlConnection POST Request
			AsyncHttpURLConnection mPOSTClient = new AsyncHttpURLConnection();
		
			AsyncHttpResponse mPOSTClientRes = 
					mPOSTClient.post(" Your http Url ");
			
						
			// With Params HTTPUrlConnection POST Request
			AsyncHttpURLConnection mPOSTClientWithParams = new AsyncHttpURLConnection();
					
			AsyncHttpParams mPOSTParams = new AsyncHttpParams();
			mPOSTParams.put("key", "value");
		
			
			AsyncHttpResponse mPOSTClientWithParamsRes = 
					mPOSTClientWithParams.post(" Your http Url ",mPOSTParams);
			
			
			
			return mGETRes.responseBody;
		}
		
	}
}
