package com.botdemo.demo1.service;

import cn.hutool.http.HttpUtil;
import com.botdemo.demo1.dao.Message;
import com.xxl.conf.core.annotation.XxlConf;
import org.springframework.stereotype.Service;

import java.util.HashMap;

import static com.botdemo.demo1.enums.Params.*;
import static com.botdemo.demo1.enums.api.*;

@Service
public class MessageSenderImp implements MessageSender{

    private String botUrl = "http://127.0.0.1:5700";

    public void sendMessageToPerson(Message m){
        HashMap<String, Object> params = new HashMap<>();
        params.put(MESSAGE, m.getMessage());
        params.put(USERID,m.getUser_id());
        HttpUtil.post(botUrl+ SEND_PRIVATE_MSG, params);
    }

    @Override
    public void sendMessageToGroup(Message m) {
        HashMap<String, Object> params = new HashMap<>();
        params.put(MESSAGE, m.getMessage());
        params.put(GROUPID,m.getGroup_id());
        HttpUtil.post(botUrl+ SEND_GROUP_MSG, params);
    }
}
