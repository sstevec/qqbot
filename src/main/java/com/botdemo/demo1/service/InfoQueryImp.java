package com.botdemo.demo1.service;

import com.alibaba.fastjson.JSON;
import com.botdemo.demo1.dao.PersonalInfo;
import com.xxl.conf.core.annotation.XxlConf;
import org.springframework.stereotype.Service;
import cn.hutool.http.HttpUtil;

import java.util.HashMap;
import java.util.List;

import static com.botdemo.demo1.enums.Params.*;
import static com.botdemo.demo1.enums.api.*;
@Service
public class InfoQueryImp implements InfoQuery{


    private String botUrl = "http://127.0.0.1:5700";

    // 获取群组内某个成员的信息
    @Override
    public PersonalInfo getGroupMember(String groupId, String userId) {
        PersonalInfo info;
        HashMap<String, Object> request = new HashMap<>();
        request.put(GROUPID, groupId);
        request.put(USERID, userId);
        HashMap result = JSON.parseObject(HttpUtil.post(botUrl + GET_GROUP_MEMBER_INFO, request), HashMap.class);
        info = JSON.parseObject(result.get("data").toString(), PersonalInfo.class);
        return info;
    }

    // 获取群组内所有成员的信息
    @Override
    public List<PersonalInfo> getAllGroupMember(String groupId) {
        HashMap<String, Object> request = new HashMap<>();
        request.put(GROUPID, groupId);
        HashMap result = JSON.parseObject(HttpUtil.post(botUrl + GET_GROUP_MEMBER_LIST, request), HashMap.class);
        List<PersonalInfo> info = JSON.parseArray(result.get("data").toString(), PersonalInfo.class);
        return info;
    }
}
