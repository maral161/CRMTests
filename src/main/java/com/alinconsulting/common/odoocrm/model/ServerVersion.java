package com.alinconsulting.common.odoocrm.model;

import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;

public class ServerVersion {
        public ServerVersion(HashMap map){
                server_version = (String) map.get("server_version");
        }

        @Getter private String server_version;
        @Getter private List<Object> server_version_info;
        @Getter private String server_serie;
        @Getter private int protocol_version;



}
