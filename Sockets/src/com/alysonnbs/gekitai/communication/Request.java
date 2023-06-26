package com.alysonnbs.gekitai.communication;

import java.io.Serializable;

public class Request implements Serializable {    
    public enum RequestType {
        DRAW,
        MESSAGE,
        MOVE,
        RESIGN;
    }

    public final RequestType type;
    public final String data;

    public Request(RequestType type, String data) {
        this.type = type;
        this.data = data;
    }
}
