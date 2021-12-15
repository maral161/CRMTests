package com.alinconsulting.common.crm;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.HttpClients;

public class KeapTest {

    private static final String URL = "https://infusionsoft.app/crm/rest/v2/contacts/";
    private static final String APIKEY = "KeapAK-03aefa105d9a9aa00fde1639789535d99b1ca39a4e6d78922f";

    public static void main(String[] args) {
        HttpClient client = HttpClients.custom().build();
        HttpUriRequest request = RequestBuilder.get()
                .setUri(URL)
                .setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .setHeader("X-Keap-API-Key", APIKEY)
                .build();
        try {
            HttpResponse resp = client.execute(request);
            System.out.println(resp.toString());
        }catch (Exception e){
            System.out.println("CHAOS");
        }
    }
}
