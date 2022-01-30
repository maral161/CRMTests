package com.alinconsulting.common.crm;

import com.alinconsulting.common.crm.keapmodel.*;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class KeapTest {

    private static final String URL = "https://api.infusionsoft.com/crm/rest/";
    private static final String APIKEY = "KeapAK-712244d00eb086eed924b13e6e61c00ed8358154970cf8c742";
    //private static final String APIKEY = "KeapAK-0820aac121f0dc25d82334e6cf9e7d38d23811f34a797c2117";
    private static final String ENDPOINT_CONTACTS_LIST = "contacts?optional_properties=custom_fields";
    private static final String ENDPOINT_CONTACTS = "contacts/";
    private static final String ENDPOINT_CONTACTS_MODEL = "contacts/model/";

    public static void main(String[] args) {

       try {
           //Contacts kc = listContacts();
           //System.out.println(kc.toString());
           //String customFields = listContactsCustomFields();

           updateContactIfPersonalNumberUnique();

        }catch (Exception e){
            System.out.println("CHAOS: Exception message: " + e.getMessage());
        }
    }

    private static void updateContactIfPersonalNumberUnique() throws IOException{
        Contact contact = new Contact();
        String personalNumber = "19810914-2409";
        //String personalNumber = "19731023-5911";
        contact.setFamily_name("Alin");
        contact.setGiven_name("Frida");
        contact.email_opted_in=true;
        contact.owner_id=1;
        contact.id=4;
        contact.phone_numbers = new ArrayList<PhoneNumber>();
        contact.email_status="SingleOptIn";

        EmailAddress mail = new EmailAddress("frigu504@live.se","EMAIL1");
        ArrayList<EmailAddress> mailAddresses = new ArrayList<EmailAddress>();
        mailAddresses.add(mail);
        contact.setEmail_addresses(mailAddresses);
        PhoneNumber phone = new PhoneNumber("",null, "PHONE1", "Work");
        ArrayList<PhoneNumber> phoneNumbers = new ArrayList<PhoneNumber>();
        phoneNumbers.add(phone);
        contact.phone_numbers = phoneNumbers;
        ArrayList<CustomFields> cfList = new ArrayList<CustomFields>();
        CustomFields cf = new CustomFields("1", personalNumber);
        cfList.add(cf);
        contact.setCustom_fields(cfList);



        Contacts contacts = listContacts();
        boolean foundPNr = false;
        for (Contact c : contacts.getContacts()){
            String existPersonalNumber = c.getCustom_fields().get(0).getContent();
            if (existPersonalNumber.equals(personalNumber)){
                foundPNr = true;
                System.out.println("Client found! Do not create a new one!");
            }
        }
        if(!foundPNr){
            System.out.println("Client not found! Create a new one!");
            Type type = new TypeToken<Object>(){}.getType();
            Object s = postJson(ENDPOINT_CONTACTS,contact,type);
            System.out.println(s.toString());
        }
    }

    private static String listContactsCustomFields() throws IOException {
        Type type = new TypeToken<Contacts>(){}.getType();
        return getJson(ENDPOINT_CONTACTS_MODEL, type);
    }

    private static Contacts listContacts() throws IOException {
        Type type = new TypeToken<Contacts>(){}.getType();
        return getJson(ENDPOINT_CONTACTS_LIST, type);
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

    private static <T> T postJson(String endpoint, Object data, Type type) throws IOException{
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(30 * 1000)
                .build();

        CloseableHttpClient client = HttpClientBuilder.create()
                .setDefaultRequestConfig(requestConfig)
                .build();
        Gson gson = new Gson();
        String jsonReq = gson.toJson(data);
        System.out.println(jsonReq.toString());
        //StringEntity entity = new StringEntity(jsonReq, ContentType.APPLICATION_JSON);
        StringEntity entity = new StringEntity(jsonReq, ContentType.APPLICATION_JSON);
        HttpPost request = new HttpPost(URL + endpoint);
        request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        request.setHeader("X-Keap-API-Key", APIKEY);
        request.setEntity(entity);
        String jsonRsp;
        try (CloseableHttpResponse response = client.execute(request)) {
            System.out.println(response.toString());
            jsonRsp = readHttpResponse(response);
            System.out.println(jsonRsp.toString());
        }
        return gson.fromJson(jsonRsp, type);
    }

    private static String readHttpResponse(CloseableHttpResponse response) throws IOException {
        switch (response.getStatusLine().getStatusCode()) {
            case 204:
                EntityUtils.consume(response.getEntity());
                return "";
            case 200, 201:
                try (BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))) {
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = rd.readLine()) != null) {
                        result.append(line);
                    }
                    return result.toString();
                }
            case 400:
                try (BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))) {
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = rd.readLine()) != null) {
                        result.append(line);
                    }
                    return result.toString();
                } catch (Exception e) {
                    //EntityUtils.consume(response.getEntity());
                    throw new IOException("Returned error code 400 from API: Exception:" + e.getMessage());
                }
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
