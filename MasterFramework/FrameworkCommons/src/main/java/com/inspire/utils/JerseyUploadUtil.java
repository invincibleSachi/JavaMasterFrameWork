package com.inspire.utils;

import java.io.File;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.inspire.abstestbase.MasterLogger;
import com.inspire.constants.UtilConstants;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;

/**
 * @author sachi
 *
 */
public class JerseyUploadUtil {
	public static Logger log = MasterLogger.getInstance();

	/**
	 * HTTP Post upload file
	 * 
	 * @param uri
	 * @param requestJson
	 * @param file
	 * @param headers
	 * @return ClientResponse
	 */
	public static ClientResponse postUploadFile(String uri, String requestJson, String[] file,
			Map<String, String> headers) {

		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, null, null);
		} catch (NoSuchAlgorithmException | KeyManagementException e) {
			log.info(e.getMessage());
		}
		System.setProperty("https.protocols", UtilConstants.TLS_VERSION);
		Client client = Client.create();
		WebResource webResource = client.resource(uri);
		Builder builder = webResource.accept("application/json");
		for (String key : headers.keySet()) {
			builder = builder.header(key, headers.get(key));
		}
		FormDataMultiPart multiPart = new FormDataMultiPart();
		File file2upload = null;
		for (String f : file) {
			file2upload = new File(f);
			multiPart.bodyPart(new FileDataBodyPart("file", file2upload, MediaType.MULTIPART_FORM_DATA_TYPE));
		}
		multiPart.bodyPart(new FormDataBodyPart("data", requestJson, MediaType.APPLICATION_JSON_TYPE));
		return builder.type(MediaType.MULTIPART_FORM_DATA_TYPE).post(ClientResponse.class, multiPart);
	}

	/**
	 * HTTP Post upload File with request json and file
	 * 
	 * @param uri
	 * @param requestJson
	 * @param file
	 * @param headers
	 * @return ClientResponse
	 */
	public static ClientResponse postUploadFile(String uri, String requestJson, String file,
			Map<String, String> headers) {
		log.info("uploading file: "+file);
		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, null, null);
		} catch (NoSuchAlgorithmException | KeyManagementException e) {
			log.info(e.getMessage());
		}
		System.setProperty("https.protocols", UtilConstants.TLS_VERSION);
		Client client = Client.create();
		WebResource webResource = client.resource(uri);
		Builder builder = webResource.accept("application/json");
		if(headers!=null) {
			for (String key : headers.keySet()) {
				builder = builder.header(key, headers.get(key));
			}
		}
		
		FormDataMultiPart multiPart = new FormDataMultiPart();
		File file2upload = new File(file);
		multiPart.bodyPart(new FileDataBodyPart("file", file2upload, MediaType.MULTIPART_FORM_DATA_TYPE));
		multiPart.bodyPart(new FormDataBodyPart("data", requestJson, MediaType.APPLICATION_JSON_TYPE));
		return builder.type(MediaType.MULTIPART_FORM_DATA_TYPE).post(ClientResponse.class, multiPart);
	}

	/*
	 * public static ClientResponse postUploadFileWithHeader(String uri, String
	 * requestJson, String file, String headerKey, String headerVal) {
	 * 
	 * try { SSLContext sc = SSLContext.getInstance("TLS"); sc.init(null, null,
	 * null); } catch (NoSuchAlgorithmException | KeyManagementException e) {
	 * e.printStackTrace(); } System.setProperty("https.protocols",
	 * UtilConstants.TLS_VERSION); Client client = Client.create(); WebResource
	 * webResource = client.resource(uri); FormDataMultiPart multiPart = new
	 * FormDataMultiPart(); File file2upload = new File(file);
	 * multiPart.bodyPart(new FileDataBodyPart("file", file2upload,
	 * MediaType.MULTIPART_FORM_DATA_TYPE)); multiPart.bodyPart(new
	 * FormDataBodyPart("data", requestJson, MediaType.APPLICATION_JSON_TYPE));
	 * return webResource.type(MediaType.MULTIPART_FORM_DATA_TYPE).header(headerKey,
	 * headerVal) .post(ClientResponse.class, multiPart); }
	 */
	/**
	 * HTTP post upload file with headers and filename parameter
	 * 
	 * @param uri
	 * @param requestJson
	 * @param fileName
	 * @param headers
	 * @return ClientResponse
	 */
	public static ClientResponse postUploadFileNameWithHeader(String uri, String requestJson, String fileName,
			Map<String, String> headers) {

		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, null, null);
		} catch (NoSuchAlgorithmException | KeyManagementException e) {
			log.info(e.getMessage());
		}
		System.setProperty("https.protocols", UtilConstants.TLS_VERSION);
		Client client = Client.create();
		WebResource webResource = client.resource(uri);
		Builder builder = webResource.accept("application/json");
		if(headers!=null) {
			for (String key : headers.keySet()) {
				builder = builder.header(key, headers.get(key));
			}
		}
		
		FormDataMultiPart multiPart = new FormDataMultiPart();
		multiPart.bodyPart(new FormDataBodyPart("fileName", fileName, MediaType.MULTIPART_FORM_DATA_TYPE));
		multiPart.bodyPart(new FormDataBodyPart("data", requestJson, MediaType.APPLICATION_JSON_TYPE));
		return builder.type(MediaType.MULTIPART_FORM_DATA_TYPE).post(ClientResponse.class, multiPart);
	}

	/**
	 * HTTP Post Json
	 * 
	 * @param uri
	 * @param requestJson
	 * @return ClientResponse
	 */
	public static ClientResponse postJson(String uri, String requestJson) {

		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, null, null);
		} catch (NoSuchAlgorithmException | KeyManagementException e) {
			log.info(e.getMessage());
		}
		System.setProperty("https.protocols", UtilConstants.TLS_VERSION);
		Client client = Client.create();
		WebResource webResource = client.resource(uri);
		FormDataMultiPart multiPart = new FormDataMultiPart();
		multiPart.bodyPart(new FormDataBodyPart("summary", requestJson, MediaType.APPLICATION_JSON_TYPE));
		return webResource.type(MediaType.MULTIPART_FORM_DATA_TYPE).post(ClientResponse.class, multiPart);
	}

	/**
	 * HTTP Post json
	 * 
	 * @param uri
	 * @param requestJson
	 * @return ClientResponse
	 */
	public static ClientResponse postJsonData(String uri, String requestJson) {

		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, null, null);
		} catch (NoSuchAlgorithmException | KeyManagementException e) {
			log.info(e.getMessage());
		}
		System.setProperty("https.protocols", UtilConstants.TLS_VERSION);
		Client client = Client.create();
		WebResource webResource = client.resource(uri);
		FormDataMultiPart multiPart = new FormDataMultiPart();
		multiPart.bodyPart(new FormDataBodyPart("data", requestJson, MediaType.APPLICATION_JSON_TYPE));
		return webResource.type(MediaType.MULTIPART_FORM_DATA_TYPE).post(ClientResponse.class, multiPart);
	}

	/**
	 * HTTP Post file with dummy file
	 * 
	 * @param uri
	 * @param requestJson
	 * @param file
	 * @param headers
	 * @return ClientResposne
	 */
	public static ClientResponse postUploaddummyFile(String uri, String requestJson, String file,
			Map<String, String> headers) {

		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, null, null);
		} catch (NoSuchAlgorithmException | KeyManagementException e) {
			log.info("Exception while setting sslContext " + e.getMessage());
		}
		System.setProperty("https.protocols", UtilConstants.TLS_VERSION);
		Client client = Client.create();
		WebResource webResource = client.resource(uri);
		Builder builder = webResource.accept("application/json");
		for (String key : headers.keySet()) {
			builder = builder.header(key, headers.get(key));
		}
		FormDataMultiPart multiPart = new FormDataMultiPart();
		multiPart.bodyPart("fileName", MediaType.MULTIPART_FORM_DATA_TYPE);
		multiPart.bodyPart(new FormDataBodyPart("data", requestJson, MediaType.APPLICATION_JSON_TYPE));
		return builder.type(MediaType.MULTIPART_FORM_DATA_TYPE).post(ClientResponse.class, multiPart);
	}

	/**
	 * HTTP Post upload filename
	 * 
	 * @param uri
	 * @param requestJson
	 * @param fileName
	 * @param headers
	 * @return ClientResponse
	 */
	public static ClientResponse postUploadFileName(String uri, String requestJson, String fileName,
			Map<String, String> headers) {

		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, null, null);
		} catch (NoSuchAlgorithmException | KeyManagementException e) {
			log.info(e.getMessage());
		}
		System.setProperty("https.protocols", UtilConstants.TLS_VERSION);
		Client client = Client.create();
		WebResource webResource = client.resource(uri);
		Builder builder = webResource.accept("application/json");
		for (String key : headers.keySet()) {
			builder = builder.header(key, headers.get(key));
		}
		FormDataMultiPart multiPart = new FormDataMultiPart();
		multiPart.bodyPart(new FormDataBodyPart("fileName", fileName, MediaType.MULTIPART_FORM_DATA_TYPE));
		multiPart.bodyPart(new FormDataBodyPart("data", requestJson, MediaType.APPLICATION_JSON_TYPE));
		return builder.type(MediaType.MULTIPART_FORM_DATA_TYPE).post(ClientResponse.class, multiPart);
	}

}
