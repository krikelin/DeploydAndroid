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
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

public class DeploydObject extends JSONObject {
	private String resource;
	public HashMap<String, DeploydObject> junctions = new HashMap<String, DeploydObject>();
	public DeploydObject(String resource) {
		this.resource = resource;
	}
	public String getObjectId() {
		try {
			return this.getString("id");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			return null;
			
		}
	}
	@Override
	public JSONObject put(String key, Object val) throws JSONException {
		if(val instanceof DeploydObject) {
			String id = ((DeploydObject) val).getString("id");
			if(id == null) {
				id = ""; // TODO: Assign custom ID
			}
			this.junctions.put(id, (DeploydObject)val);
			return (JSONObject)val;
		} else {
			return super.put(key, val);		
		}
	}
	public void deleteInBackground() {
		AsyncTask<JSONObject, JSONObject, JSONObject> del = new AsyncTask<JSONObject, JSONObject, JSONObject> () {

			@Override
			protected JSONObject doInBackground(JSONObject... params) {
				// TODO Auto-generated method stub
				try {
					JSONObject result = Deployd.delete("/" + resource + "/" + DeploydObject.this.getObjectId());
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
			}
		};
	//	del.execute(null);
	}
	public DeploydObject(JSONObject obj) {
		String k;
		while((k = (String)obj.keys().next()) != NULL) {
			try {
				this.put(k, obj.get(k));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void saveInBackground() {
		saveInBackground(null);
	}
	public void saveInBackground(final ObjectCreateEventHandler onCreated) { 
		AsyncTask<JSONObject, JSONObject, JSONObject> save = new AsyncTask<JSONObject, JSONObject, JSONObject> () {

			@Override
			protected JSONObject doInBackground(JSONObject... params) {
				// TODO Auto-generated method stub
				try {
					JSONObject result = null;
					if(DeploydObject.this.has("id")) {
						result = Deployd.put(params[0], "/" + resource + "/" + DeploydObject.this.getString("id"));
						
						// TODO: add code for handling junctions
						
					} else {
						result = Deployd.post(params[0], "/" + resource);
					}
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
				if(result != null) {
					if(onCreated != null) {
						try {
							DeploydObject.this.put("id", result.getString("id"));
							
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						onCreated.objectCreated(DeploydObject.this);
					}
				}
				super.onPostExecute(result);
			}
		};
		save.execute(this);
	}
}
