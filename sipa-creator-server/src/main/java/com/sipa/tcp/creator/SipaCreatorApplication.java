package com.sipa.tcp.creator;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.sipa.boot.core.app.AppConstant;
import com.sipa.boot.core.app.SipaApplication4Java;

/**
 * @author caszhou
 * @date 2022/6/10
 */
@SpringBootApplication
public class SipaCreatorApplication {
    public static void main(String[] args) {
        SipaApplication4Java.run(AppConstant.TCP.CREATOR_NAME, SipaCreatorApplication.class, args);
    }
}
