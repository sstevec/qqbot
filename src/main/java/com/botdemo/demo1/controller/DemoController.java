package com.botdemo.demo1.controller;


import com.botdemo.demo1.service.LocalFileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {


    @Autowired
    LocalFileSystem localFileSystem;

    @RequestMapping("/record")
    public void testMyDemo(){
        localFileSystem.loadLocalFile();
    }
}
