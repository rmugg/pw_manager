package com.janktank;

public class secretObject {
    private String id, domain, user, secret, iv, lastUpdate;

    public secretObject(String idIn, String domainIn, String userIn, String secretIn, String ivIn, String lastUpdateIn){
        id = idIn;
        domain = domainIn;
        user = userIn;
        secret = secretIn;
        iv = ivIn;
        lastUpdate = lastUpdateIn;
    }

    public String getId() {
        return id;
    }

    public String getDomain() {
        return domain;
    }

    public String getUser(){
        return user;
    }

    public String getSecret() {
        return secret;
    }

    public String getIv(){
        return iv;
    }

    public String getLastUpdate(){
        return lastUpdate;
    }
}