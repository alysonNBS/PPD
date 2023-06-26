package com.alysonnbs.gekitai.game;

public class ChatMessage {
    public final int player;
    public final String message;

    public ChatMessage(int player, String message) {
        this.player = player;
        this.message = message;
    }
}
