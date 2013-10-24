/***
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Copyright 2013 Alexander Forselius
 **/
package com.deployd;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

public class Deployd {
	/**
	 * Endpoint for deployd
	 */
	public static String endpoint = null;
	public static String key = null;
	public static String sid = "";
	public static void init(String endpoint, String key) {
		Deployd.key = key;
		Deployd.endpoint = endpoint;
	}
	public static JSONObject delete(String uri) throws ClientProtocolException, IOException, JSONException {
		
		HttpDelete post = new HttpDelete(endpoint + uri);
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(post);
		ByteArrayEntity e = (ByteArrayEntity) response.getEntity();
		InputStream is = e.getContent();
		String data = new Scanner(is).next();
		JSONObject result = new JSONObject(data);
		return result;
	}
	public static JSONObject put(JSONObject input, String uri) throws ClientProtocolException, IOException, JSONException {
		
		HttpPut post = new HttpPut(endpoint + uri);
		HttpClient client = new DefaultHttpClient();
		post.setEntity(new ByteArrayEntity(input.toString().getBytes("UTF8")));
		HttpResponse response = client.execute(post);
		ByteArrayEntity e = (ByteArrayEntity) response.getEntity();
		InputStream is = e.getContent();
		String data = new Scanner(is).next();
		JSONObject result = new JSONObject(data);
		return result;
	}
	public static JSONObject post(JSONObject input, String uri) throws ClientProtocolException, IOException, JSONException {
		
		HttpPost post = new HttpPost(endpoint + uri);
		HttpClient client = new DefaultHttpClient();
		post.setEntity(new ByteArrayEntity(input.toString().getBytes("UTF8")));
		HttpResponse response = client.execute(post);
		ByteArrayEntity e = (ByteArrayEntity) response.getEntity();
		InputStream is = e.getContent();
		String data = new Scanner(is).next();
		JSONObject result = new JSONObject(data);
		return result;
	}
	public static JSONObject get(String uri) throws ClientProtocolException, IOException, JSONException {
		
		HttpGet post = new HttpGet(endpoint + uri);
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(post);
		ByteArrayEntity e = (ByteArrayEntity) response.getEntity();
		InputStream is = e.getContent();
		String data = new Scanner(is).next();
		JSONObject result = new JSONObject(data);
		return result;
	}
	public static void signup(String username, String displayName, String password, final SignupEventHandler signedUp) {
		AsyncTask<JSONObject, JSONObject, JSONObject> query = new AsyncTask<JSONObject, JSONObject, JSONObject>() {

			@Override
			protected JSONObject doInBackground(JSONObject... arg0) {
				// TODO Auto-generated method stub
				
				try {
					JSONObject result = Deployd.post(arg0[0], "/users/signup");
					return result;
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
				
				return null;
			}

			@Override
			protected void onPostExecute(JSONObject result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if(result == null) {
					signedUp.failed();
					return;
				}
				DeploydObject jres = new DeploydObject(result);
				
				signedUp.signupComplete(jres);
			}
			
		};
		try {
			JSONObject pass = new JSONObject();
			pass.put("username", username);
			pass.put("password", password);
			pass.put("displayName", password);
			query.execute(pass);
		} catch (Exception e) {
			
		}
	}
	public static void login(String username, String password, final LoginEventHandler loggedIn) {
		AsyncTask<JSONObject, JSONObject, JSONObject> query = new AsyncTask<JSONObject, JSONObject, JSONObject>() {

			@Override
			protected JSONObject doInBackground(JSONObject... arg0) {
				// TODO Auto-generated method stub
				
				try {
					JSONObject result = Deployd.post(arg0[0], "/users/login");
					return result;
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
				
				return null;
			}

			@Override
			protected void onPostExecute(JSONObject result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if(result == null) {
					loggedIn.failed();
				}
				DeploydObject jres = new DeploydObject(result);
				
				loggedIn.loggedIn(jres);
			}
			
		};
		try {
			JSONObject pass = new JSONObject();
			pass.put("username", username);
			pass.put("password", password);
			query.execute(pass);
		} catch (Exception e) {
			
		}
	}
}
