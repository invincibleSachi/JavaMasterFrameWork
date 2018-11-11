package com.inspire.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import com.inspire.constants.UtilConstants;

/**
 * @author sachi
 *
 */
public class RestUtils {

	HttpClient httpclient = null;

	public RestUtils() {
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(UtilConstants.HTTP_CONNECTION_TIMEOUT)
				.build();
		httpclient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
	}

	public HttpResponse getRequest(String uri, String request, String requestBody) {
		HttpGet getRequest = new HttpGet(uri);
		HttpResponse response = null;
		getRequest.addHeader(request, requestBody);
		try {
			response = httpclient.execute(getRequest);
			if (response.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return response;
	}

	public HttpResponse sendPutRequest(String data, String url) throws UnsupportedEncodingException {
		HttpPut request = new HttpPut(url);
		StringEntity params = new StringEntity(data, "UTF-8");
		params.setContentType("application/json");
		request.addHeader("content-type", "application/json");
		request.addHeader("Accept", "*/*");
		request.addHeader("Accept-Encoding", "gzip,deflate,sdch");
		request.addHeader("Accept-Language", "en-US,en;q=0.8");
		request.setEntity(params);
		HttpResponse response = null;
		try {
			response = httpclient.execute(request);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}

	public int getResponseCode(HttpResponse response) {
		int responseCode = response.getStatusLine().getStatusCode();
		return responseCode;
	}

}
