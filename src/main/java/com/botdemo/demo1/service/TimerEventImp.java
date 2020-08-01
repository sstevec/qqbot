package com.botdemo.demo1.service;


import com.botdemo.demo1.dao.Message;
import com.botdemo.demo1.mapper.SentenceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

@Service
public class TimerEventImp implements TimerEvent{

    @Autowired
    MessageAssemble messageAssemble;

    @Autowired
    MessageSender messageSender;

    @Autowired
    SentenceMapper sentenceMapper;

    @Override
    @Scheduled(cron="1 0 * * * * ")
    public void saySomethingHourly(){
        String[] dateTrans = {"零","一","二","三","四","五","六","七","八","九"};
        SimpleDateFormat df = new SimpleDateFormat("HH");//设置日期格式
        String date = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳

        String newTime = "";
        for(int i = 0; i<date.length(); i++){
            int num = date.charAt(i)-'0';
            newTime = newTime + dateTrans[num];
        }
        newTime = newTime + dateTrans[0] + dateTrans[0] + "~, ";

        int totalNumber = sentenceMapper.getTotalNumber();
        Random random = new Random();
        int id = random.nextInt(totalNumber) + 1;
        String sentence = sentenceMapper.getSentence(id);
        newTime = newTime+sentence;

        Message m = messageAssemble.assembleGroupMessage("791372943",newTime);
        messageSender.sendMessageToGroup(m);
    }
}
