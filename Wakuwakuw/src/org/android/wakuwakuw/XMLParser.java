package org.android.wakuwakuw;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.content.Entity;
import android.util.Log;

public class XMLParser {

	// constructor
	public XMLParser() {

	}
	
	public String getXmlFromUrlMap(String url) {
		String xml = null;
		
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			
			//HttpPost = buat UPDATE...
			//HttpPost httpPost = new HttpPost(url);
			
			//HttpGet = buat ngambil...
			HttpGet httpGet = new HttpGet(url);
			//httpGet.setHeader("Content-Type", "application/xml");

			//HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpResponse httpResponse = httpClient.execute(httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();
			xml = EntityUtils.toString(httpEntity);
			
			System.out.println(xml);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// return XML
		return xml;
	}

	/**
	 * Getting XML from URL making HTTP request
	 * @param url string
	 * */
	public String getXmlFromUrl(String url, String username, String password) {
		String xml = null;
		String myUsername, myPassword;
		
		if (username.equalsIgnoreCase("") && password.equalsIgnoreCase("")) {
			myUsername = "rommy"; myPassword = "android";
		}
		else {
			myUsername = username; myPassword = password;
		}
		
		try {
			/*
			// ini masih belum pake HTTPHeader dan yg ini defaultnya
			// defaultHttpClient
			DefaultHttpClient httpClient = new DefaultHttpClient();
			
			//HttpPost = buat UPDATE...
			//HttpPost httpPost = new HttpPost(url);
			
			//HttpGet = buat ngambil...
			HttpGet httpGet = new HttpGet(url);
			//httpGet.setHeader("Content-Type", "application/xml");

			//HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpResponse httpResponse = httpClient.execute(httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();
			xml = EntityUtils.toString(httpEntity);
			*/
			
			
			// defaultHttpClient
			/*
			DefaultHttpClient httpClient = new DefaultHttpClient();
			
			AuthScope as = new AuthScope("api.wakuwakuw.com", 433);
			
			UsernamePasswordCredentials upc = new UsernamePasswordCredentials(username, password);
 
            ((AbstractHttpClient) httpClient).getCredentialsProvider().setCredentials(as, upc);
 
            BasicHttpContext localContext = new BasicHttpContext();
 
            BasicScheme basicAuth = new BasicScheme();
            localContext.setAttribute("preemptive-auth", basicAuth);
 
            HttpHost targetHost = new HttpHost("api.wakuwakuw.com", 433, "http");
 
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("Content-Type", "application/xml");
 
            HttpResponse httpResponse = httpClient.execute(targetHost, httpGet, localContext);
			
            
			httpClient.getCredentialsProvider().setCredentials(
	                new AuthScope(null, -1),
	                new UsernamePasswordCredentials(username, password));

			
			//HttpGet httpGet = new HttpGet(url);
			//httpGet.setHeader("Content-Type", "application/xml");

			//HttpResponse httpResponse = httpClient.execute(httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();
			xml = EntityUtils.toString(httpEntity); */
			
			
			//proses authorization saat user login
			HttpParams httpParameters = new BasicHttpParams();
            String auth = android.util.Base64.encodeToString(
                    (myUsername + ":" + myPassword).getBytes("UTF-8"), android.util.Base64.NO_WRAP);
			
			//String auth = Base64.encodeBytes((myUsername + ":" + myPassword).getBytes("UTF-8"), Base64.ENCODE);
			
            HttpGet httpGet = new HttpGet(url);

            //httpGet.addHeader("Authorization", "Basic " + auth);
            httpGet.setHeader("User-Agent", "Android");
            httpGet.setHeader("Authorization", "Basic " + auth);

            HttpConnectionParams.setSoTimeout(httpParameters, 0);
            DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);

            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            
            xml = EntityUtils.toString(httpEntity);

            System.out.println("Data. in login with " + myUsername + ", " + myPassword + ": " + xml);


		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// return XML
		return xml;
	}
	
	
	public void postSomething(String url, String data) {
		try {			
			HttpParams httpParameters = new BasicHttpParams();
            //String auth = android.util.Base64.encodeToString(
            //        (myUsername + ":" + myPassword).getBytes("UTF-8"), android.util.Base64.NO_WRAP);
			
			//String auth = Base64.encodeObject((myUsername + ":" + myPassword).getBytes("UTF-8"), Base64.ENCODE);

            HttpConnectionParams.setSoTimeout(httpParameters, 0);
            DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
            
            HttpPut httpPut = new HttpPut(url);
            
            //httpPut.addHeader("Authorization", "Basic " + auth);
            httpPut.setEntity(new StringEntity(data));
            
            HttpResponse httpResponse = httpClient.execute(httpPut);

            /*if (httpResponse.getStatusLine().getStatusCode()  == 200) {
            	is = httpResponse.getEntity().getContent();
            	int ch;
            	sb = new StringBuffer();
            	
            	while ((ch = is.read()) != -1) {
            		sb.append((char) ch);
            	}
            	
            	// Log sb . it prints the response you get.
            }*/

            int myResponseNumber = httpResponse.getStatusLine().getStatusCode();
            System.out.println("The Response Number: " + myResponseNumber);
            
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void editSomething (String url, String data, String username, String password) {
		String myUsername, myPassword;
		
		if (username.equalsIgnoreCase("") && password.equalsIgnoreCase("")) {
			myUsername = "rommy"; myPassword = "android";
		}
		else {
			myUsername = username; myPassword = password;
		}
		
		try {			
			HttpParams httpParameters = new BasicHttpParams();
			
            String auth = android.util.Base64.encodeToString(
                    (myUsername + ":" + myPassword).getBytes("UTF-8"), android.util.Base64.URL_SAFE | android.util.Base64.NO_WRAP);

            HttpConnectionParams.setSoTimeout(httpParameters, 0);
            DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
            
            HttpPost httpPost = new HttpPost(url);
            
            //httpPut.addHeader("Authorization", "Basic " + auth);
            httpPost.setHeader("User-Agent", "Android");
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            httpPost.setHeader("Authorization", "Basic " + auth);
            httpPost.setEntity(new StringEntity(data));
            
            HttpResponse httpResponse = httpClient.execute(httpPost);

            /*if (httpResponse.getStatusLine().getStatusCode()  == 200) {
            	is = httpResponse.getEntity().getContent();
            	int ch;
            	sb = new StringBuffer();
            	
            	while ((ch = is.read()) != -1) {
            		sb.append((char) ch);
            	}
            	
            	// Log sb . it prints the response you get.
            }*/

            int myResponseNumber = httpResponse.getStatusLine().getStatusCode();
            System.out.println("The Response Number: " + myResponseNumber);
            System.out.println("The Response Reason: " + httpResponse.getStatusLine().getReasonPhrase());
            System.out.println("The Response Reason: " + httpResponse.getEntity().getContent());
            
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Getting XML DOM element
	 * @param XML string
	 * */
	public Document getDomElement(String xml){
		Document doc = null;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); 
		
		//ini buat supaya bisa parsing <![CDATA[elemen sembarang]]> di XML nya
		dbf.setCoalescing(true);
		//////////////////////////////////////////////////////////////////////
		
		try {

			DocumentBuilder db = dbf.newDocumentBuilder();

			InputSource is = new InputSource();
		        is.setCharacterStream(new StringReader(xml));
		        doc = db.parse(is); 

			} catch (ParserConfigurationException e) {
				Log.e("Error: ", e.getMessage());
				return null;
			} catch (SAXException e) {
				Log.e("Error: ", e.getMessage());
	            return null;
			} catch (IOException e) {
				Log.e("Error: ", e.getMessage());
				return null;
			}

	        return doc;
	}
	
	/** Getting node value
	  * @param elem element
	  */
	 public final String getElementValue( Node elem ) {
	     Node child;
	     if( elem != null){
	         if (elem.hasChildNodes()){
	             for( child = elem.getFirstChild(); child != null; child = child.getNextSibling() ){
	                 if( child.getNodeType() == Node.TEXT_NODE  ){
	                     return child.getNodeValue();
	                 }
	             }
	         }
	     }
	     return "";
	 }
	 
	 /**
	  * Getting node value
	  * @param Element node
	  * @param key string
	  * */
	 /*public String getValue(Element item, String str) {		
			NodeList n = item.getElementsByTagName(str);		
			return this.getElementValue(n.item(0));
		}*/
	 
	public String getValue(Element item, String str) {		
			NodeList n = item.getElementsByTagName(str);		
			return this.getElementValue(n.item(0));
	}
}
