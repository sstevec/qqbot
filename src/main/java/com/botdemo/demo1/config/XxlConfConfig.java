package com.botdemo.demo1.config;

import com.xxl.conf.core.spring.XxlConfFactory;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.logging.Logger;

/**
 * xxl-conf config
 *
 * @author xuxueli 2017-04-28
 */
//@Configuration
//public class XxlConfConfig {
//    private Logger logger = Logger.getLogger("XxlConfConfig");
//
//
//    @Value("${xxl.conf.admin.address}")
//    private String adminAddress;
//
//    @Value("${xxl.conf.env}")
//    private String env;
//
//    @Value("${xxl.conf.access.token}")
//    private String accessToken;
//
//    @Value("${xxl.conf.mirrorfile}")
//    private String mirrorfile;
//
//    @Bean
//    public XxlConfFactory xxlConfFactory() {
//        XxlConfFactory xxlConf = new XxlConfFactory();
//        xxlConf.setAdminAddress(adminAddress);
//        xxlConf.setEnv(env);
//        xxlConf.setAccessToken(accessToken);
//        xxlConf.setMirrorfile(mirrorfile);
//        logger.info(">>>>>>>>>>> xxl-conf config init.");
//        return xxlConf;
//    }
//
//}