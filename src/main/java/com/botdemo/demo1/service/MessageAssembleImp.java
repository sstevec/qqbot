package com.botdemo.demo1.service;

import com.botdemo.demo1.dao.Message;
import org.springframework.stereotype.Service;

@Service
public class MessageAssembleImp implements MessageAssemble{

    public Message assemblePersonalMessage(String userId, String message){
        Message m = new Message();
        m.setMessage(message);
        m.setUser_id(userId);
        return m;
    }

    public Message assembleGroupMessage(String groupId, String message){
        Message m = new Message();
        m.setMessage(message);
        m.setGroup_id(groupId);
        return m;
    }

    public Message messageAppend(Message m, String newMessage){
        m.setMessage(m.getMessage()+newMessage);
        return m;
    }
}
