package com.botdemo.demo1.controller;

import com.alibaba.fastjson.JSON;
import com.botdemo.demo1.dao.Sender;
import com.botdemo.demo1.service.EventProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class EventController {

    @Autowired
    EventProcessor eventProcessor;

    @RequestMapping("/botEvent")
    public void receiveMessage(@RequestHeader(name = "X-Self-ID", required = true) String botQQ, @RequestBody String request){
        HashMap<String, Object> requestMap = JSON.parseObject(request, HashMap.class);
        if(requestMap.get("post_type").toString().equals("message")){
            // message
            String message = requestMap.get("message").toString();
            Sender sender = JSON.parseObject(requestMap.get("sender").toString(), Sender.class);
            String userId = sender.getUser_id();
            String selfId = requestMap.get("self_id").toString();
            if(requestMap.get("message_type").toString().equals("group")){
                // 群消息
                String groupId = requestMap.get("group_id").toString();
                eventProcessor.groupEventProcessor(groupId,message,sender,selfId);
            }else if(requestMap.get("message_type").toString().equals("private")){
                // 私聊消息
                if(userId.equals("303983558")){
                    eventProcessor.sayHi(userId);
                }
            } else{
                // 讨论组
            }

        }else{
            // 其他事件
            System.out.println(requestMap.get("post_type").toString());
//            System.out.println(requestMap.get("message").toString());
            return;
        }
    }
}
