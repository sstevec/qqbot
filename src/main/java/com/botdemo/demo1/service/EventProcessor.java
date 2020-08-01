package com.botdemo.demo1.service;

import com.botdemo.demo1.dao.PickUpResult;
import com.botdemo.demo1.dao.Sender;

public interface EventProcessor {

    public void groupEventProcessor(String groupId, String message, Sender sender, String selfId);

    public void pcrEventProcessor(String groupId, String message, Sender sender, String selfId);

    public void pcrBattleEvent(String groupId, String message, Sender sender, String selfId);

    public String autoResponse(String groupId, String message, Sender sender, String selfId);


    // type = 0 normal, type = 1 pick up
    public String pickThreeHundred(int type);

    public PickUpResult pickTenNormal();

    public PickUpResult pickTenPickUp();

    public void sayHi(String user_id);
}
