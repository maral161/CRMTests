package com.alinconsulting.common.crm;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.HttpClients;

public class KeapTest {

    private static final String URL = "https://api-intg.infusiontest.com/crm/rest/v2/contacts/";
    private static final String APIKEY = "KeapAK-43d60d18d32f731381aed6a6431a7ba3551795356e5263acca";

    public static void main(String[] args) {
        HttpClient client = HttpClients.custom().build();
        HttpUriRequest request = RequestBuilder.get()
                .setUri(URL)
                .setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .setHeader("X-Keap-API-Key", APIKEY)
                .build();
        try {
            client.execute(request);
            HttpResponse resp = client.execute(request);
            System.out.println(resp.toString());
        }catch (Exception e){
            System.out.println("CHAOS");
        }
    }
}
