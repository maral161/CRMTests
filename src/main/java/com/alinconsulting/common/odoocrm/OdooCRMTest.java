package com.alinconsulting.common.odoocrm;

import com.alinconsulting.common.odoocrm.model.ServerVersion;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import java.net.URL;
import java.util.HashMap;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;


public class OdooCRMTest {

        private static final String url = "https://allin-consulting1.odoo.com";
        private static final String db = "allin-consulting1";
        private static final String username = "markus@alinconsulting.com";
        private static final String password = "VgDy3p2i5ZUsxid";

        public static void main(String[] args){
                final XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
                final XmlRpcClient client = new XmlRpcClient();

                try {
                        config.setServerURL(new URL(String.format("%s/xmlrpc/2/common", url)));
                        ServerVersion version = new ServerVersion((HashMap) client.execute(config, "version", emptyList()));
                        System.out.println("Server Version:" + version.getServer_version());

                        int uid = (int)client.execute(
                                config, "authenticate", asList(
                                        db, username, password, emptyMap()));

                        

                        System.out.println("UUID: " + uid);
                } catch (Exception e){
                        System.out.println("Kuken!!");
                }
        }



}
