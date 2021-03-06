package com.qoobico.remindme.app;

/**
 * Created by Lincoln on 06/01/16.
 */
public class EndPoints {

    // localhost url
    // public static final String BASE_URL = "http://192.168.0.101/gcm_chat/v1";

    public static final String BASE_URL = "http://data.webber-group.com.ua/gcm_chat/v1";
    public static final String LOGIN = BASE_URL + "/user/login";
    public static final String USER = BASE_URL + "/user/_ID_";
    public static final String USERS = BASE_URL + "/user";
    public static final String CREW = BASE_URL + "/flights_crew/_ID_";
    public static final String FLIGHTS = BASE_URL + "/flights/_ID_";
    public static final String READINESS = BASE_URL + "/readiness/_ID_";
    public static final String CHAT_ROOMS = BASE_URL + "/chat_rooms";
    public static final String NEWS = BASE_URL + "/news";
    public static final String CODEID = BASE_URL + "/user_code/_ID_";
    public static final String ANAL = BASE_URL + "/flights_hour/_ID_";
    public static final String ANALCOST = BASE_URL + "/hour_cost/_ID_";
    public static final String CHAT_THREAD = BASE_URL + "/chat_rooms/_ID_";
    public static final String CHAT_ROOM_MESSAGE = BASE_URL + "/chat_rooms/_ID_/message";
}
