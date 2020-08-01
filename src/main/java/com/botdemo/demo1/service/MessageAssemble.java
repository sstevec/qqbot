package com.botdemo.demo1.service;

import com.botdemo.demo1.dao.Message;

public interface MessageAssemble {

    public Message assemblePersonalMessage(String userId, String message);

    public Message assembleGroupMessage(String groupId, String message);

    public Message messageAppend(Message m, String newMessage);
}
