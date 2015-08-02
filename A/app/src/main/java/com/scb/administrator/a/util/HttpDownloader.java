package com.scb.administrator.a.util;

/**
 * Created by Administrator on 2015/6/23 0023.
 */

import java.net.URL;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;


public class HttpDownloader {

    private URL url = null;
    int i=0;



    public String download(String urlStr) {
        StringBuffer sb = new StringBuffer();
        String line = null;

        BufferedReader buffer = null;
        try {
            url = new URL(urlStr);
            HttpURLConnection urlConn = (HttpURLConnection) url
                    .openConnection();
            urlConn.setRequestMethod("GET");
            urlConn.setConnectTimeout(8000);
            urlConn.setReadTimeout(8000);
            buffer = new BufferedReader(new InputStreamReader(
                    urlConn.getInputStream()));
            while ((line = buffer.readLine()) != null) {
                sb.append(line);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (buffer != null) {
                    buffer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
              i = 1;
            }
        }
        if(i==0) return sb.toString();
        else return null;
    }
}
