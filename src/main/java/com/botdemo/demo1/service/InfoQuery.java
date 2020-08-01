package com.botdemo.demo1.service;

import com.botdemo.demo1.dao.PersonalInfo;

import java.util.List;

public interface InfoQuery {
    public PersonalInfo getGroupMember(String groupId, String userId);

    public List<PersonalInfo> getAllGroupMember(String groupId);
}
