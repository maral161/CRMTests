package com.alinconsulting.common.crm;

import com.alinconsulting.common.crm.keapmodel.Contacts;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

public class KeapTest {

    private static final String URL = "https://api.infusionsoft.com/crm/rest/";
    private static final String APIKEY = "KeapAK-712244d00eb086eed924b13e6e61c00ed8358154970cf8c742";
    private static final String ENDPOINT_CONTACTS = "contacts/";

    public static void main(String[] args) {

       try {
           Contacts kc = listKeapContacts();
           System.out.println(kc.toString());
            //KeapContact kc = new KeapContact("Alin", "Lasse", "VD");

        }catch (Exception e){
            System.out.println("CHAOS: Exception message: " + e.getMessage());
        }
    }


    private static Contacts listKeapContacts() throws IOException {
        Type type = new TypeToken<Contacts>(){}.getType();
        return getJson(ENDPOINT_CONTACTS, type);
    }

    private static <T> T getJson(String endpoint, Type type) throws IOException{
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(30 * 1000)
                    .build();

            CloseableHttpClient client = HttpClientBuilder.create()
                    .setDefaultRequestConfig(requestConfig)
                    .build();

            HttpGet request = new HttpGet(URL + endpoint);
            request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            request.setHeader("X-Keap-API-Key", APIKEY);
            String jsonRsp;
            try (CloseableHttpResponse response = client.execute(request)) {
                jsonRsp = readHttpResponse(response);
                System.out.println(jsonRsp.toString());
            }
            Gson gson = new Gson();
            return gson.fromJson(jsonRsp, type);
    }

    private static String readHttpResponse(CloseableHttpResponse response) throws IOException {
        switch (response.getStatusLine().getStatusCode()) {
            case 204:
                EntityUtils.consume(response.getEntity());
                return "";
            case 200:
                try (BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))) {
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = rd.readLine()) != null) {
                        result.append(line);
                    }
                    return result.toString();
                }
            case 400:
                EntityUtils.consume(response.getEntity());
                throw new IOException("Returned error code 400 from API");
            case 401:
                EntityUtils.consume(response.getEntity());
                throw new IOException("Returned error code 401 from API");
            case 403:
                EntityUtils.consume(response.getEntity());
                throw new IOException("Returned error code 403 from API");
            case 409:
                EntityUtils.consume(response.getEntity());
                throw new IOException("Returned error code 409 from API");
            default:
                EntityUtils.consume(response.getEntity());
                throw new IOException("Returned error code from API: " + response.getStatusLine().getStatusCode());
        }
    }
}
