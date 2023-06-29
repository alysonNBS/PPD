package com.alysonnbs.javaspaces.models;

import net.jini.core.entry.Entry;

public class MyMessage implements Entry {
    public String content;
    public String from;
    public String to;
    public String environment;
    public String wasSpiedOn;

    public MyMessage() {}
}
