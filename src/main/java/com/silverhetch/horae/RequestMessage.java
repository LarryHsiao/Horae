package com.silverhetch.horae;

import com.google.gson.JsonObject;

class RequestMessage {
    private final String messageType;
    private final String content;

    public RequestMessage(String messageType, String content) {
        this.messageType = messageType;
        this.content = content;
    }

    public String json() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("messageType", messageType);
        jsonObject.addProperty("content", content);
        return jsonObject.toString();
    }
}
