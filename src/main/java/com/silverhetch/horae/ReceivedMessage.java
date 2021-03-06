package com.silverhetch.horae;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

class ReceivedMessage implements Message {
    private final JsonObject jsonObject;

    public ReceivedMessage(String rawMessage) {
        this.jsonObject = new JsonParser().parse(rawMessage).getAsJsonObject();
    }

    @Override
    public String messageType() {
        return jsonObject.get("messageType").getAsString();
    }

    @Override
    public String content() {
        return jsonObject.get("content").getAsString();
    }
}
