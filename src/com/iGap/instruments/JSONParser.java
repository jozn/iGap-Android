// Copyright (c) 2016, iGap - www.iGap.im
// iGap is a Hybrid instant messaging service .
// RooyeKhat Media Co . - www.RooyeKhat.co
// All rights reserved.

package com.iGap.instruments;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;


/**
 * 
 * for send and receive data to server
 *
 */

public class JSONParser {

    private InputStream is         = null;
    private JSONObject  jObj       = null;
    private String      json       = "";
    private String      statuscode = "";


    // constructor
    public JSONParser() {

    }


    /**
     * 
     * get json from server and prepration info for use
     * 
     * @param url address for get response from server
     * @param params for send multiple params
     * @param method for determination kind of request
     * @param basicAuth an authentication send to server for allow you send params and get response
     * @param paramsbody for send param
     * @return
     *         json object , contains statuscode and json
     */

    public JSONObject getJSONFromUrl(String url, List<NameValuePair> params, String method, String basicAuth, String paramsbody) {

        if (method.equals("GET")) {
            // Making HTTP request
            try {

                // defaultHttpClient
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(url);
                httpGet.setHeader("Authorization", basicAuth);
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
                int status = httpResponse.getStatusLine().getStatusCode();
                statuscode = String.valueOf(status);

            }
            catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            catch (ClientProtocolException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        } else if (method.equals("POST")) {
            // Making HTTP request
            try {
                // defaultHttpClient
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
                httpPost.setHeader("Authorization", basicAuth);
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
                int status = httpResponse.getStatusLine().getStatusCode();
                statuscode = String.valueOf(status);
            }
            catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            catch (ClientProtocolException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        } else if (method.equals("DELETE")) {
            // Making HTTP request
            try {
                // defaultHttpClient
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpDelete httpDelete = new HttpDelete(url);
                httpDelete.setHeader("Authorization", basicAuth);
                HttpResponse httpResponse = httpClient.execute(httpDelete);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
                int status = httpResponse.getStatusLine().getStatusCode();
                statuscode = String.valueOf(status);
            }
            catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            catch (ClientProtocolException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        } else if (method.equals("PUT")) {
            // Making HTTP request
            try {
                // defaultHttpClient
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPut httpput = new HttpPut(url);
                httpput.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
                httpput.setHeader("Authorization", basicAuth);
                HttpResponse httpResponse = httpClient.execute(httpput);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
                int status = httpResponse.getStatusLine().getStatusCode();
                statuscode = String.valueOf(status);
            }
            catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            catch (ClientProtocolException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        } else if (method.equals("DELETE-BODY")) {
            // Making HTTP request
            try {
                // defaultHttpClient
                HttpEntity entity = new StringEntity(paramsbody);
                HttpClient httpClient = new DefaultHttpClient();
                HttpDeleteWithBody httpDeleteWithBody = new HttpDeleteWithBody(url);
                httpDeleteWithBody.setEntity(entity);
                httpDeleteWithBody.setHeader("Authorization", basicAuth);
                HttpResponse httpResponse = httpClient.execute(httpDeleteWithBody);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
                int status = httpResponse.getStatusLine().getStatusCode();
                statuscode = String.valueOf(status);
            }
            catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            catch (ClientProtocolException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        } else if (method.equals("BODY")) {
            // Making HTTP request
            try {
                // defaultHttpClient
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(new StringEntity(paramsbody, "UTF8"));
                httpPost.setHeader("Authorization", basicAuth);
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();

            }
            catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            catch (ClientProtocolException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
            Log.i("JSON", "" + json);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject();
            jObj.put("json", json);
            jObj.put("statuscode", statuscode);

        }
        catch (JSONException e) {}

        // return JSON String
        return jObj;
    }
}