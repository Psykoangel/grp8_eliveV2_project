package fr.group8.elive.utils;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by psyko on 08/02/16.
 */
public class WebService {
    private static WebService ourInstance = new WebService();

    public static WebService Instance() {
        return ourInstance;
    }

    private final String DOMAIN = "10.167.128.56";
    private final String PROTOCOLE = "http";
    private final String TLD = "fr";
    private final String APP_HIERARCHIY = "/elive/REST/";
    private final String urlUserPath = "USER/";

    private String serverUrl;

    public WebService() {
        defineServerUrl(PROTOCOLE, DOMAIN, TLD, APP_HIERARCHIY);
    }

    private InputStream sendRequest(URL url) {

        // Connection Opening
        HttpURLConnection urlConnection = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // URL Connection
        try {
            urlConnection.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            // If Server Response is OK
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                return urlConnection.getInputStream();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public InputStream getUserInfos(int patientId) {

        try {
            // Sending Request
            InputStream inputStream = sendRequest(new URL(this.getServerUrl()));

            // Check InputStream
            if(inputStream != null) {
                // Return raw InputStream from server answer
                return inputStream;
            }

        } catch (Exception e) {
            Log.e(this.getClass().getName(), e.getMessage());
        }
        return null;
    }

    public void defineServerUrl(String protocole, String domain, String tld, String appAddress) {
        setServerUrl(protocole + "://" + domain + "." + tld + (!appAddress.isEmpty() ? "" : appAddress));
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }
}
