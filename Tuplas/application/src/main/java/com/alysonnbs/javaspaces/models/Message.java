package com.alysonnbs.javaspaces.models;

import net.jini.core.entry.Entry;

public class Message implements Entry {
    public String content;
    public String from;
    public String to;
    public String environment;

    public Message() {}
}
