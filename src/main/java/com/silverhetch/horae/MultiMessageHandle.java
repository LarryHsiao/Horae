package com.silverhetch.horae;

class MultiMessageHandle implements MessageHandle {
    private final MessageHandle[] messageHandles;

    public MultiMessageHandle(MessageHandle... messageHandles) {
        this.messageHandles = messageHandles == null ? new MessageHandle[0] : messageHandles;
    }

    @Override
    public String messageType() {
        return "MultiMessageHandle";
    }

    @Override
    public void onReceive(String rawMessage) {
        Message jsonReceivedMessage = new ReceivedMessage(rawMessage);
        for (MessageHandle messageHandle : messageHandles) {
            if (messageHandle.messageType().equals(jsonReceivedMessage.messageType())) {
                messageHandle.onReceive(jsonReceivedMessage.content());
            }
        }
    }
}
