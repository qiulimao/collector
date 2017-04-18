package com.getqiu.event.utils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class WordService {

	private String scheme = "http";
	private String host = "localhost";
	private int port  = 8000;
	private String separatingPath = "/services/word/separating";
	private String labelingPath = "/services/word/labeling";
	private String analyzingPath = "/services/word/analyzing";
	
	private Logger logger = LoggerFactory.getLogger(WordService.class);
	
	HttpClient httpClient = HttpClients.createDefault();
	
	/**
	 * 本质上都是获取tag
	 * */
	private List<String> cuttingToGetTags(String query,String requestPath){
		URI url=null;
		try {
			url = new URIBuilder().setScheme(scheme).setHost(host)
					.setPort(port).setPath(requestPath)
					.setParameter("s", query).build();
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}
		HttpGet httpGetRequest = new HttpGet(url);
		List<String> wordList = new ArrayList<>();
		try {
			HttpResponse response =  httpClient.execute(httpGetRequest);
			if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				/* 将这个结果转化为 数组或者json */
				String plainResult = EntityUtils.toString(response.getEntity());
				Gson googleJson = new Gson();
				Type type =  new TypeToken<ArrayList<String>>(){}.getType();
				wordList = googleJson.fromJson(plainResult, type);
			}
			
		} catch (IOException e) {
			// IO访问失败
			logger.error("network error....");
			e.printStackTrace();
		}
		return wordList;
	}

	public List<String> separating(String query){
		return cuttingToGetTags(query, separatingPath);
	}
	
	public List<String> labeling(String query){
		return cuttingToGetTags(query, labelingPath);
	}
	
	public Map<String, String> analyzing(String query){
		URI url=null;
		try {
			url = new URIBuilder().setScheme(scheme).setHost(host)
					.setPort(port).setPath(analyzingPath)
					.setParameter("s", query).build();
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}
		HttpGet httpGetRequest = new HttpGet(url);
		Map<String,String> wordWithTagging = new HashMap<>();
		try {
			HttpResponse response =  httpClient.execute(httpGetRequest);
			if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				/* 将这个结果转化为 数组或者json */
				String plainResult = EntityUtils.toString(response.getEntity());
				Gson googleJson = new Gson();
				Type type =  new TypeToken<HashMap<String,String>>(){}.getType();
				wordWithTagging = googleJson.fromJson(plainResult, type);
			}
			
		} catch (IOException e) {
			// IO访问失败
			logger.error("network error....");
		}
		return wordWithTagging;
	}

	/*-------
	 *  getters and setters.
	 * -----*/
	public String getScheme() {
		return scheme;
	}

	public void setScheme(String scheme) {
		this.scheme = scheme;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getSeperatingPath() {
		return separatingPath;
	}

	public void setSeperatingPath(String seperatingPath) {
		this.separatingPath = seperatingPath;
	}

	public String getLabelingPath() {
		return labelingPath;
	}

	public void setLabelingPath(String labelingPath) {
		this.labelingPath = labelingPath;
	}

	public String getAnalyzingPath() {
		return analyzingPath;
	}

	public void setAnalyzingPath(String analyzingPath) {
		this.analyzingPath = analyzingPath;
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public HttpClient getHttpClient() {
		return httpClient;
	}

	public void setHttpClient(HttpClient httpClient) {
		this.httpClient = httpClient;
	}
}
