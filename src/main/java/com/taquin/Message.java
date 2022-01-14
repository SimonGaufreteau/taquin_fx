package com.taquin;

import java.util.Objects;

public class Message {
    private MessageType messageType;
    private Agent sender;
    private Agent recipient;

    public Message(MessageType messageType, Agent sender, Agent recipient) {
        this.messageType = messageType;
        this.sender = sender;
        this.recipient = recipient;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public Agent getSender() {
        return sender;
    }

    public Agent getRecipient() {
        return recipient;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return getMessageType() == message.getMessageType()
                && Objects.equals(getSender(), message.getSender())
                && Objects.equals(getRecipient(), message.getRecipient());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMessageType(), getSender(), getRecipient());
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageType=" + messageType +
                ", sender=" + sender +
                ", recipient=" + recipient +
                '}';
    }
}
