package com.sipa.tcp.creator;

import com.sipa.boot.core.app.AppConstant;
import com.sipa.boot.core.app.SipaApplication4Java;
import com.sipa.boot.core.constant.SipaBootConstant;
import com.sipa.boot.core.env.EnvConstant;
import com.sipa.boot.testcontainer.TestContainer;

/**
 * @author caszhou
 * @date 2019/4/24
 */
public class CreatorTestContainer {
    public static void main(String[] args) {
        System.setProperty(SipaBootConstant.Core.SPRING_PROFILES_ACTIVE_KEY, EnvConstant.ENV_DEV);
        SipaApplication4Java.run(AppConstant.TCP.CREATOR_NAME, CreatorApplication.class, args);
        TestContainer.start();
    }
}
