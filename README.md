AsyncHttpUrlConnection
======================

This is the Simple Example of use HttpUrlConnection for Android

use this way


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
			
			
