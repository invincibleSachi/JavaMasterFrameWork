package com.inspire.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.ws.rs.core.MultivaluedMap;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.inspire.abstestbase.MasterLogger;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import net.minidev.json.JSONArray;

/**
 * @author sachi
 *
 */
public class JsonUtils {
	public static Gson gson = new Gson();
	public static Logger log = MasterLogger.getInstance();

	public String Object2Json(Object o) {
		String json = gson.toJson(o);
		return json;
	}

	public static String readJson(String jsonFile) {
		String json = FileIOUtility.readFileAsString(jsonFile);
		return json;

	}

	public MultivaluedMap Json2queryParms(String json) {
		Map<String, Object> retMap = new Gson().fromJson(json, new TypeToken<HashMap<String, Object>>() {
		}.getType());
		MultivaluedMap queryparms = new MultivaluedMapImpl();
		queryparms.putAll(retMap);
		return queryparms;
	}

	public static Map json2Map(JSONObject json) {

		Gson gson = new Gson();
		Map<Object, Object> map = new HashMap<Object, Object>();
		map = (Map) gson.fromJson(json.toString(), map.getClass());
		return map;
	}

	public static Map readJsonFromFile(String fileName) {
		return parseJson(FileIOUtility.readFileAsString(fileName));
	}

	public static Map parseJson(String jsonStr) {
		// String jsonStr = "{ \"author\": \"Steve Jin\", \"title\" : \"vSphere
		// SDK\", \"obj\" : {\"objint\" : {}} }";
		/*
		 * Object obj = gson.fromJson(jsonStr, Object.class);
		 *
		 * log.info("obj type: " + obj.getClass().toString()); //
		 * com.google.gson.internal.LinkedTreeMap log.info("obj: " + obj);
		 */

		Map<String, String> m = gson.fromJson(jsonStr, Map.class);
		return m;
	}

	public static String getJsonProperty(String jsonFile, String property) {
		return (String) parseJson(readJson(jsonFile)).get(property);
	}

	public static String getJsonFromPojo(Object obj) {
		Gson gson = new GsonBuilder().create();
		return gson.toJson(obj);
	}

	public static String getJsonFromMap(Map m) {
		Gson gsonObj = new Gson();
		String jsonStr = gsonObj.toJson(m);
		return jsonStr;
	}

	public static String setJsonArray(String jsonStr, String key, JSONArray jarr) {
		JSONObject json = new JSONObject(jsonStr);
		json.put(key, jarr);
		return json.toString();
	}

	public static String getJsonStrfromJsonPath(String jsonStr, String jsonPath) {
		DocumentContext jsonContext = JsonPath.parse(jsonStr);
		String jsonpathCreatorName = jsonContext.read(jsonPath);
		return jsonpathCreatorName;
	}

	public static JSONArray getJsonArrayfromJsonPath(String jsonStr, String jsonPath) {
		DocumentContext jsonContext = JsonPath.parse(jsonStr);
		JSONArray jsonarr = jsonContext.read(jsonPath);
		return jsonarr;
	}

	public static boolean compareJson(JsonElement json1, JsonElement json2) {
		boolean isEqual = true;
		// Check whether both jsonElement are not null
		if (json1 != null && json2 != null) {

			// Check whether both jsonElement are objects
			if (json1.isJsonObject() && json2.isJsonObject()) {
				Set<Entry<String, JsonElement>> ens1 = ((JsonObject) json1).entrySet();
				Set<Entry<String, JsonElement>> ens2 = ((JsonObject) json2).entrySet();
				JsonObject json2obj = (JsonObject) json2;
				if (ens1 != null && ens2 != null && (ens2.size() == ens1.size())) {
					// Iterate JSON Elements with Key values
					for (Entry<String, JsonElement> en : ens1) {
						isEqual = isEqual && compareJson(en.getValue(), json2obj.get(en.getKey()));
					}
				} else {
					return false;
				}
			}

			// Check whether both jsonElement are arrays
			else if (json1.isJsonArray() && json2.isJsonArray()) {
				JsonArray jarr1 = json1.getAsJsonArray();
				JsonArray jarr2 = json2.getAsJsonArray();
				if (jarr1.size() != jarr2.size()) {
					return false;
				} else {
					int i = 0;
					// Iterate JSON Array to JSON Elements
					for (JsonElement je : jarr1) {
						isEqual = isEqual && compareJson(je, jarr2.get(i));
						i++;
					}
				}
			}

			// Check whether both jsonElement are null
			else if (json1.isJsonNull() && json2.isJsonNull()) {
				return true;
			}

			// Check whether both jsonElement are primitives
			else if (json1.isJsonPrimitive() && json2.isJsonPrimitive()) {
				if (json1.equals(json2)) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else if (json1 == null && json2 == null) {
			return true;
		} else {
			return false;
		}
		return isEqual;
	}

}
