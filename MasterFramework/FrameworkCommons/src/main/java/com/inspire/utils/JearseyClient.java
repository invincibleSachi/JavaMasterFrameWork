package com.inspire.utils;

import java.io.File;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.log4j.Logger;

import com.inspire.abstestbase.MasterLogger;
import com.inspire.constants.UtilConstants;
import com.sun.jersey.api.client.AsyncWebResource;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.MultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;


/**
 * @author sachi
 *
 */
public class JearseyClient {
	static ClientConfig config = new DefaultClientConfig();
	static Client client = Client.create(config);
	static String uri;
	static JsonUtils jsonUtil = new JsonUtils();
	public static Logger log = MasterLogger.getInstance();

	//
	/**
	 * HTTP GET Request using following parameters this method assumes response as
	 * JSON
	 * 
	 * @param uri
	 * @param key
	 * @param value
	 * @return ClientResponse
	 */
	public static ClientResponse getUsingKeyValuePair(String uri, String key, String value) {

		WebResource webResource = client.resource(uri).queryParam(key, value);
		// checkout for valid accept media types
		// https://jersey.java.net/nonav/apidocs/1.4/jersey/javax/ws/rs/core/MediaType.html
		// http://www.programcreek.com/java-api-examples/index.php?api=com.sun.jersey.api.client.config.DefaultClientConfig

		ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);
		log.info("response code" + response.getStatus());
		return response;
	}

	/**
	 * HTTP Get Request using following params
	 * 
	 * @param uri
	 * @param json
	 * @return ClientResponse
	 */
	public static ClientResponse getUsingJson(String uri, String json) {
		MultivaluedMap queryparm = jsonUtil.Json2queryParms(json);
		WebResource webResource = client.resource(uri).queryParams(queryparm);
		// checkout for valid accept media types
		// https://jersey.java.net/nonav/apidocs/1.4/jersey/javax/ws/rs/core/MediaType.html

		ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);
		log.info("response code" + response.getStatus());
		return response;
	}

	/**
	 * HTTP Get request using just URI
	 * 
	 * @param uri
	 * @return ClientResponse
	 */
	public static ClientResponse get(String uri) {
		WebResource webResource = client.resource(uri);
		// checkout for valid accept media types
		// https://jersey.java.net/nonav/apidocs/1.4/jersey/javax/ws/rs/core/MediaType.html

		ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);
		log.info("response code" + response.getStatus());
		return response;
	}

	/**
	 * HTTP GET Request using given uri and headers
	 * 
	 * @param uri
	 * @param headers
	 * @return ClientResponse
	 */
	public static ClientResponse getWithHeaders(String uri, Map<String, String> headers) {
		WebResource webResource = client.resource(uri);
		Builder builder = webResource.accept("application/json");
		for (String key : headers.keySet()) {
			// log.info("headerKey: Value " + key + ": " + headers.get(key));
			builder = builder.header(key, headers.get(key));
		}
		ClientResponse response = builder.get(ClientResponse.class);

		log.info("response code" + response.getStatus());
		return response;
	}

	/**
	 * HTTP Get Request using given uri and query params <br>
	 * checkout for valid accept media types<br>
	 * https://jersey.java.net/nonav/apidocs/1.4/jersey/javax/ws/rs/core/MediaType.html<br>
	 * 
	 * @param uri
	 * @param queryParam
	 * @return ClientResponse
	 */
	public static ClientResponse getUsingQueryParm(String uri, MultivaluedMap<String, String> queryParam) {
		WebResource webResource = client.resource(uri);
		return webResource.queryParams(queryParam).accept(MediaType.TEXT_PLAIN).get(ClientResponse.class);
	}

	/**
	 * HTTP get request using uri and request json
	 * 
	 * @param uri2
	 * @param requestJson
	 * @return ClientResponse
	 */
	public static ClientResponse post(String uri2, String requestJson) {

		// MultivaluedMap queryparm = jsonUtil.Json2queryParms(requestJson);
		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, null, null);
		} catch (NoSuchAlgorithmException | KeyManagementException e) {
			log.info(e.getMessage());
		}
		System.setProperty("https.protocols", UtilConstants.TLS_VERSION);
		WebResource webResource = client.resource(uri2);
		ClientResponse response = webResource.accept("application/json").type("application/json")
				.post(ClientResponse.class, requestJson);

		log.info("response code " + response.getStatus());
		/*
		 * if ("response code" +response.getStatus() != 200) { throw new
		 * RuntimeException("Failed : HTTP error code : " + "response code"
		 * +response.getStatus()); }
		 */

		return response;

	}

	/**
	 * HTTP Post Async
	 * 
	 * @param uri2
	 * @param requestJson
	 */
	public static void postAsync(String uri2, String requestJson) {

		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, null, null);
		} catch (NoSuchAlgorithmException | KeyManagementException e) {
			log.info(e.getMessage());
		}
		System.setProperty("https.protocols", UtilConstants.TLS_VERSION);
		AsyncWebResource webResource = client.asyncResource(uri2);
		webResource.accept("application/json").type("application/json").post(ClientResponse.class, requestJson);
	}

	/**
	 * Post Async with uri, request json and headers
	 * 
	 * @param uri
	 * @param requestJson
	 * @param headers
	 */
	public static void postAsync(String uri, String requestJson, Map<String, String> headers) {

		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, null, null);
		} catch (NoSuchAlgorithmException | KeyManagementException e) {
			log.info(e.getMessage());
		}
		System.setProperty("https.protocols", UtilConstants.TLS_VERSION);
		AsyncWebResource webResource = client.asyncResource(uri);
		com.sun.jersey.api.client.AsyncWebResource.Builder builder = webResource.accept("application/json");
		for (String key : headers.keySet()) {
			builder = builder.header(key, headers.get(key));
		}
		builder.type("application/json").post(ClientResponse.class, requestJson);
	}

	/**
	 * HTTP Post with uri request json and headers
	 * 
	 * @param uri
	 * @param requestJson
	 * @param headers
	 * @return ClientResponse
	 */
	public static ClientResponse postWithHeaders(String uri, String requestJson, Map<String, String> headers) {

		// MultivaluedMap queryparm = jsonUtil.Json2queryParms(requestJson); try
		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, null, null);
		} catch (NoSuchAlgorithmException | KeyManagementException e) {
			log.info(e.getMessage());
		}
		WebResource webResource = client.resource(uri);
		Builder builder = webResource.accept("application/json");
		if (headers != null) {
			for (String key : headers.keySet()) {
				builder = builder.header(key, headers.get(key));
			}
		}
		ClientResponse response = builder.type("application/json").post(ClientResponse.class, requestJson);
		log.info("response code " + response.getStatus());

		return response;

	}

	/**
	 * Http Post Uri with headers
	 * 
	 * @param uri
	 * @param headers
	 * @return ClientResponse
	 */
	public static ClientResponse postUrlWithHeaders(String uri, Map<String, String> headers) {

		// MultivaluedMap queryparm = jsonUtil.Json2queryParms(requestJson);
		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, null, null);
		} catch (NoSuchAlgorithmException | KeyManagementException e) {
			log.info(e.getMessage());
		}
		System.setProperty("https.protocols", UtilConstants.TLS_VERSION);
		WebResource webResource = client.resource(uri);
		Builder builder = webResource.accept("application/json");
		if (headers != null) {
			for (String key : headers.keySet()) {
				builder = builder.header(key, headers.get(key));
			}
		}
		ClientResponse response = builder.accept("application/json").type("application/json")
				.post(ClientResponse.class);
		log.info("response code " + response.getStatus());
		/*
		 * if ("response code" +response.getStatus() != 200) { throw new
		 * RuntimeException("Failed : HTTP error code : " + "response code"
		 * +response.getStatus()); }
		 */

		return response;

	}
	/*
	 * public static ClientResponse postWithAuthNHeader(String uri, String
	 * requestJson, String authKey, String authVal, String headerKey, String
	 * headerValue) {
	 * 
	 * // MultivaluedMap queryparm = jsonUtil.Json2queryParms(requestJson); try {
	 * SSLContext sc = SSLContext.getInstance("TLS"); sc.init(null, null, null); }
	 * catch (NoSuchAlgorithmException | KeyManagementException e) {
	 * log.info(e.getMessage()); } System.setProperty("https.protocols",
	 * UtilConstants.TLS_VERSION); WebResource webResource = client.resource(uri);
	 * ClientResponse response =
	 * webResource.accept("application/json").type("application/json")
	 * .header(headerKey, headerValue).header(authKey,
	 * authVal).post(ClientResponse.class, requestJson); log.info("response code"
	 * +response.getStatus());
	 * 
	 * if ("response code" +response.getStatus() != 200) { throw new
	 * RuntimeException("Failed : HTTP error code : " + "response code"
	 * +response.getStatus()); }
	 * 
	 * 
	 * return response;
	 * 
	 * }
	 */

	/*
	 * public static ClientResponse postWithheader(String uri, String requestJson,
	 * String headerKey, String headerValue) {
	 * 
	 * // MultivaluedMap queryparm = jsonUtil.Json2queryParms(requestJson); try {
	 * SSLContext sc = SSLContext.getInstance("TLS"); sc.init(null, null, null); }
	 * catch (NoSuchAlgorithmException | KeyManagementException e) {
	 * log.info(e.getMessage()); } System.setProperty("https.protocols",
	 * UtilConstants.TLS_VERSION); WebResource webResource = client.resource(uri);
	 * ClientResponse response =
	 * webResource.accept("application/json").type("application/json")
	 * .header(headerKey, headerValue).post(ClientResponse.class, requestJson);
	 * log.info("response code" +response.getStatus());
	 * 
	 * if ("response code" +response.getStatus() != 200) { throw new
	 * RuntimeException("Failed : HTTP error code : " + "response code"
	 * +response.getStatus()); }
	 * 
	 * 
	 * return response;
	 * 
	 * }
	 */
	/**
	 * Post HTTP file with request json and headers
	 * 
	 * @param uri
	 * @param requestJson
	 * @param file
	 * @param headers
	 * @return
	 */
	public static ClientResponse postFileWithheader(String uri, String requestJson, String file,
			Map<String, String> headers) {

		// MultivaluedMap queryparm = jsonUtil.Json2queryParms(requestJson);
		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, null, null);
		} catch (NoSuchAlgorithmException | KeyManagementException e) {
			log.info(e.getMessage());
		}
		System.setProperty("https.protocols", UtilConstants.TLS_VERSION);
		WebResource webResource = client.resource(uri);
		WebResource.Builder webBuilder = webResource.getRequestBuilder();
		if (headers != null) {
			for (String key : headers.keySet()) {
				webBuilder = webBuilder.header(key, headers.get(key));
			}
		}

		FormDataMultiPart multiPart = new FormDataMultiPart();
		if (file != null) {
			File file2upload = new File(file);
			multiPart.bodyPart(new FileDataBodyPart("file", file2upload, MediaType.MULTIPART_FORM_DATA_TYPE));
		}

		multiPart.bodyPart(new FormDataBodyPart("data", requestJson, MediaType.APPLICATION_JSON_TYPE));

		ClientResponse response = webBuilder.type(MediaType.MULTIPART_FORM_DATA_TYPE).post(ClientResponse.class,
				multiPart);
		log.info("response code " + response.getStatus());
		return response;

	}

	/**
	 * HTTP Post Video with videofile headers and request json
	 * 
	 * @param uri
	 * @param requestJson
	 * @param videoFile
	 * @param headers
	 * @return ClientResponse
	 */
	public static ClientResponse postUploadVideo(String uri, String requestJson, String videoFile,
			Map<String, String> headers) {

		WebResource webResource = client.resource(uri);
		Builder builder = webResource.accept("application/json");
		if (headers != null) {
			for (String key : headers.keySet()) {
				builder = webResource.header(key, headers.get(key));
			}
		}
		File fileToUpload = new File(videoFile);
		FileDataBodyPart fileDataBodyPart = new FileDataBodyPart("file", fileToUpload,
				MediaType.APPLICATION_OCTET_STREAM_TYPE);
		MultiPart multiPart = new FormDataMultiPart().field(requestJson, MediaType.APPLICATION_JSON)
				.field(fileToUpload.getName(), MediaType.APPLICATION_OCTET_STREAM).bodyPart(fileDataBodyPart);
		multiPart.setMediaType(MediaType.MULTIPART_FORM_DATA_TYPE);

		return builder.type(MediaType.MULTIPART_FORM_DATA_TYPE).post(ClientResponse.class, multiPart);
	}

	/**
	 * HTTP Post with request json and headers
	 * 
	 * @param uri
	 * @param requestJson
	 * @param file
	 * @param headers
	 * @return ClientResponse
	 */
	public static ClientResponse postUploadFile(String uri, String requestJson, String file,
			Map<String, String> headers) {

		log.info(uri);
		log.info(file);
		Client client = Client.create();
		WebResource webResource = client.resource(uri);
		Builder builder = webResource.accept("application/json");
		if (headers != null) {
			for (String key : headers.keySet()) {
				builder = webResource.header(key, headers.get(key));
			}
		}
		FormDataMultiPart multiPart = new FormDataMultiPart();
		File file2upload = new File(file);
		multiPart.bodyPart(new FileDataBodyPart("file", file2upload));
		multiPart.bodyPart(new FormDataBodyPart("data", requestJson, MediaType.APPLICATION_JSON_TYPE));
		return builder.type(MediaType.MULTIPART_FORM_DATA_TYPE).post(ClientResponse.class, multiPart);
	}

	/**
	 * 
	 * @param uri
	 * @param formdata
	 * @param headers
	 * @return ClientResponse
	 */
	public static ClientResponse postUploadFormData(String uri, HashMap<String, String> formdata,
			Map<String, String> headers) {
		log.info(uri);
		WebResource webResource = client.resource(uri);
		Builder builder = webResource.accept(MediaType.APPLICATION_JSON);
		if (headers != null) {
			for (String key : headers.keySet()) {
				builder = builder.header(key, headers.get(key));
			}
		}
		FormDataMultiPart multiPart = new FormDataMultiPart();
		if (formdata != null) {
			for (String key : formdata.keySet()) {
				multiPart.bodyPart(
						new FormDataBodyPart(key, formdata.get(key), MediaType.APPLICATION_FORM_URLENCODED_TYPE));
			}
		} else {
			return null;
		}
		return builder.type(MediaType.MULTIPART_FORM_DATA_TYPE).post(ClientResponse.class, multiPart);

	}

	/**
	 * HTTP Put with requestJson and headers
	 * 
	 * @param requestJson
	 * @param headers
	 *            Map
	 * @param uri
	 * @return ClientResponse
	 */
	public static ClientResponse put(String requestJson, Map<String, String> headers, String uri) {
		WebResource webResource = client.resource(uri);
		Builder builder = webResource.accept("application/json");
		for (String key : headers.keySet()) {
			// log.info("headerKey: Value " + key + ": " + headers.get(key));
			builder = builder.header(key, headers.get(key));
		}
		ClientResponse response = builder.put(ClientResponse.class);
		log.info("response code " + response.getStatus());

		return response;

	}

	/**
	 * HTTP Delete request with uri and headers
	 * 
	 * @param uri
	 * @param headers
	 *            Map
	 * @return ClientResponse
	 */
	public static ClientResponse delete(String uri, Map<String, String> headers) {
		WebResource webResource = client.resource(uri);
		Builder builder = webResource.accept("application/json");
		for (String key : headers.keySet()) {
			log.info("headerKey: Value " + key + ": " + headers.get(key));
			builder = builder.header(key, headers.get(key));
		}
		ClientResponse response = builder.delete(ClientResponse.class);
		log.info("response code" + response.getStatus());
		return response;

	}

	/**
	 * HTTP put uri requestJson with headers
	 * 
	 * @param uri
	 * @param requestJson
	 * @param headers
	 * @return ClientResponse
	 */
	public static ClientResponse putWithHeaders(String uri, String requestJson, Map<String, String> headers) {
		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, null, null);
		} catch (NoSuchAlgorithmException | KeyManagementException e) {
			log.info(e.getMessage());
		}
		WebResource webResource = client.resource(uri);
		Builder builder = webResource.accept("application/json");
		if (headers != null) {
			for (String key : headers.keySet()) {
				builder = builder.header(key, headers.get(key));
			}
		}
		ClientResponse response = builder.type("application/json").put(ClientResponse.class, requestJson);
		log.info("response code " + response.getStatus());

		return response;

	}
	
	public static ClientResponse putUrlWithHeaders(String uri, Map<String, String> headers) {
		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, null, null);
		} catch (NoSuchAlgorithmException | KeyManagementException e) {
			log.info(e.getMessage());
		}
		WebResource webResource = client.resource(uri);
		Builder builder = webResource.accept("application/json");
		if (headers != null) {
			for (String key : headers.keySet()) {
				builder = builder.header(key, headers.get(key));
			}
		}
		ClientResponse response = builder.type("application/json").put(ClientResponse.class);
		log.info("response code " + response.getStatus());

		return response;

	}
}
