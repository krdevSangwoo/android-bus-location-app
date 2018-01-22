package com.example.lgpc.mygooglemap1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpServerConnect {

    public String getData(String pUrl){

        BufferedReader bufferedReader = null;
        HttpURLConnection httpURLConnection = null;

        StringBuffer page = new StringBuffer();

        try{
            URL url = new URL(pUrl);
            httpURLConnection = (HttpURLConnection)url.openConnection();
            InputStream contentStream = httpURLConnection.getInputStream();

            bufferedReader = new BufferedReader(new InputStreamReader(
                    contentStream, "UTF-8"));
            String line = null;

            while((line = bufferedReader.readLine()) != null){
                page.append(line);
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
        finally {
            try{
                bufferedReader.close();
                httpURLConnection.disconnect();
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
        return page.toString();
    }
}