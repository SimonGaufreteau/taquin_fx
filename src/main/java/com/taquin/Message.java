package com.taquin;

import java.util.Objects;

public class Message {
    private MessageType messageType;
    private Agent sender;
    private Agent recipient;
    private Direction direction;
    private int stepsToWait;

    public Message(MessageType messageType, Agent sender, Agent recipient, Direction direction,int stepsToWait) {
        this.messageType = messageType;
        this.sender = sender;
        this.recipient = recipient;
        this.direction = direction;
        this.stepsToWait = stepsToWait;
    }
    public Message(MessageType messageType, Agent sender, Agent recipient, Direction direction) {
        this(messageType,sender,recipient,direction,0);
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

    public Direction getDirection() {
        return direction;
    }

    public int getStepsToWait() {
        return stepsToWait;
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
                ", direction=" + direction +
                '}';
    }
}
