package fr.group8.elive.utils;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
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

    private final String DOMAIN = "10.167.50.53";
    private final String PORT = "8080";
    private final String PROTOCOLE = "http";
    private final String TLD = "fr";
    private final String APP_HIERARCHIY = "/elive-server-r2/API/user";

    private String serverUrl;

    public WebService() {
        defineServerUrl(PROTOCOLE, DOMAIN, null, PORT, APP_HIERARCHIY);
    }

    private static int count = 0;

    private InputStream sendRequest(URL url) {
/*
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
*/

        String CMatysiak = "{\"userId\":3,\"personaldataId\":2,\"userUniqId\":\"CHMATYSIAKGODNB\",\"userIsdeleted\":false,\"userCreationdate\":\"2016-02-01T00:00:00+01:00\",\"relationships\":[{\"userUniqSrcId\":\"CHMATYSIAKGODNB\",\"userUniqTargetId\":\"HMATYSIAKGODNB\",\"relationTypeId\":5}],\"personalData\":{\"bloodgroupId\":0,\"personaldataUserfirstname\":\"Charles\",\"personaldataUsername\":\"MATYSIAK\",\"personaldataBirthdate\":\"1950-03-07T00:00:00+01:00\",\"personaldataAddress\":\"27 Porte de Buhl\",\"cmas\":{\"entry\":[]}}}";

        String CaMatysiak = "{\"userId\":4,\"personaldataId\":3,\"userUniqId\":\"CMMATYSIAKGODNB\",\"userIsdeleted\":false,\"userCreationdate\":\"2016-02-01T00:00:00+01:00\",\"relationships\":[{\"userUniqSrcId\":\"CMMATYSIAKGODNB\",\"userUniqTargetId\":\"HMATYSIAKGODNB\",\"relationTypeId\":5}],\"personalData\":{\"bloodgroupId\":0,\"personaldataUserfirstname\":\"Carmen\",\"personaldataUsername\":\"MATYSIAK\",\"personaldataBirthdate\":\"1960-09-09T00:00:00+01:00\",\"personaldataAddress\":\"27 Porte de Buhl\",\"cmas\":{\"entry\":[]}}}";

        String HMatysiak = "{\"userId\":2,\"personaldataId\":1,\"userUniqId\":\"HMATYSIAKGODNB\",\"userIsdeleted\":false,\"userUpdatedate\":\"2016-02-01T00:00:00+01:00\",\"userCreationdate\":\"2016-02-01T00:00:00+01:00\",\"relationships\":[{\"userUniqSrcId\":\"HMATYSIAKGODNB\",\"userUniqTargetId\":\"CHMATYSIAKGODNB\",\"relationTypeId\":1},{\"userUniqSrcId\":\"HMATYSIAKGODNB\",\"userUniqTargetId\":\"CMMATYSIAKGODNB\",\"relationTypeId\":2}],\"personalData\":{\"bloodgroupId\":1,\"personaldataUserfirstname\":\"Hervé\",\"personaldataUsername\":\"MATYSIAK\",\"personaldataBirthdate\":\"1993-12-08T02:11:00+01:00\",\"personaldataPhonenumber\":\"0652532218\",\"personaldataAddress\":\"27 Porte de Buhl\r\n68530 BUHL\",\"personaldataAdditional\":\"Si la personne semble morte, merci de le ramener à la vie.\n\nUne fois mais pas deux.\n@Satan & @St-Pierre\n\nNB: T'aime la pluie le con ? @St-Pierre\",\"cmas\":{\"entry\":[{\"key\":50,\"value\":\"2016-02-02T00:00:00+01:00\"},{\"key\":2548,\"value\":\"2016-02-01T00:00:00+01:00\"}]}}}";

        String select = null;
        switch (++count) {
            case 1:
                select = HMatysiak;
                break;
            case 2:
                select = CMatysiak;
                break;
            case 3:
                select = CaMatysiak;
                count = 0;
                break;
            default:
                break;
        }

        InputStream is = new ByteArrayInputStream(select.getBytes());
        return is;
    }

    public InputStream getUserInfos(String patientId) {

        try {
            URL url = new URL(this.getServerUrl() + "/" + patientId);

            // Sending Request
            InputStream inputStream = sendRequest(url);

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

    public void defineServerUrl(String protocole, String domain, String tld, String port, String appAddress) {
        setServerUrl(protocole + "://"
                + domain + (tld == null || tld.isEmpty() ? "" : "." + tld)
                + (port == null || port.isEmpty() ? "" : ":" + port)
                + (appAddress == null || appAddress.isEmpty() ? "" : appAddress));
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }
}
