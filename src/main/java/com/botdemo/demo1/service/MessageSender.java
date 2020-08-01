package com.botdemo.demo1.service;

import com.botdemo.demo1.dao.Message;

public interface MessageSender {

    public void sendMessageToPerson(Message m);

    public void sendMessageToGroup(Message m);
}
