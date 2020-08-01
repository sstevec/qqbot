package com.botdemo.demo1.service;

public interface LocalFileSystem {

    public void loadLocalFile();

    public String fileToBase64(String path);

    public String generateCQForLocalFile(int type);
}
