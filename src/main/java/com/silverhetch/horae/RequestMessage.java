package com.silverhetch.horae;

import com.google.gson.JsonObject;

class RequestMessage {
    private final int identity;
    private final String content;

    public RequestMessage(int identity, String content) {
        this.identity = identity;
        this.content = content;
    }

    public String json() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("messageType", identity);
        jsonObject.addProperty("content", content);
        return jsonObject.toString();
    }
}
