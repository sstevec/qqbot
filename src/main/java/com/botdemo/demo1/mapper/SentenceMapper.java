package com.botdemo.demo1.mapper;

import org.apache.ibatis.annotations.Param;

public interface SentenceMapper {

    String getSentence(@Param("id") int id);

    Integer getTotalNumber();

    String getResponseSentence(@Param("id") int id);

    Integer getTotalResponseNumber();


    void storeFileCode(@Param("fileCode") String fileCode, @Param("name") String name);
}
