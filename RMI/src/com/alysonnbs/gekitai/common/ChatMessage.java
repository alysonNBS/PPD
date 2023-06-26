package com.alysonnbs.gekitai.common;

import java.io.Serializable;

public class ChatMessage implements Serializable {
    public final int player;
    public final String message;

    public ChatMessage(int player, String message) {
        this.player = player;
        this.message = message;
    }
}
