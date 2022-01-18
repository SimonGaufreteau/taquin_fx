package com.taquin;

import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MailBox {
    private HashMap<Agent, ConcurrentLinkedQueue<Message>> box;

    public MailBox(Agent[] agentList){
        resetBox(agentList);
    }

    public void resetBox(Agent[] agentList){
        box = new HashMap<>();
        for (Agent agent: agentList){
            ConcurrentLinkedQueue<Message> tempList = new ConcurrentLinkedQueue<>();
            box.put(agent,tempList);
        }
    }

    /**
     * Main method to retrieve messages
     * @param agent Agent for which we want to retrieve the message
     * @return The first message in the queue for the agent (or null if none found)
     * @throws Exception If the agent wasn't found in the mailbox
     */
    public Message getMessage(Agent agent) throws Exception {
        if(box.containsKey(agent)){
            ConcurrentLinkedQueue<Message> mailQueue = box.get(agent);
            return mailQueue.poll();
        }
        throw new Exception(String.format("Agent %s not found in the mailbox",agent));
    }

    /**
     * Main method to send messages
     * @param agent Agent to send the message to
     * @param message The message to be sent
     * @throws Exception If the agent wasn't found in the mailbox
     */
    public void sendMessage(Agent agent, Message message) throws Exception {
        if(box.containsKey(agent)){
            //System.out.println("Sending message : "+message);
            ConcurrentLinkedQueue<Message> mailQueue = box.get(agent);
            mailQueue.add(message);
            return;
        }
        throw new Exception(String.format("Agent %s not found in the mailbox",agent));
    }

}
