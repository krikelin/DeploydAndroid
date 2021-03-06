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

public class DeploydQuery {
	private String resource = null;
	public String getResource() {
		return resource;
	}
	public DeploydQuery(String resource) {
		this.resource = resource;
	}

	public static DeploydQuery getQuery(String resource) {
		DeploydQuery object = new DeploydQuery(resource);
		return object;
	}
}
