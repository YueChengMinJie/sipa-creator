package com.hmev.tcp.creator;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.sipa.boot.core.app.AppConstant;
import com.sipa.boot.core.app.SipaApplication4Java;

/**
 * @author caszhou
 * @date 2023/4/19
 */
@SpringBootApplication
public class CreatorApplication {
    public static void main(String[] args) {
        SipaApplication4Java.run(AppConstant.TCP.CREATOR_NAME, CreatorApplication.class, args);
    }
}
