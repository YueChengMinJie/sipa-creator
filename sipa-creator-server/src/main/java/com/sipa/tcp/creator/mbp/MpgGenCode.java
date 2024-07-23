package com.sipa.tcp.creator.mbp;

import java.util.List;

import lombok.Data;

/**
 * @author caszhou
 * @date 2022/6/10
 */
@Data
public class MpgGenCode {
    private String tenant;

    private List<String> tables;

    private GenSetting genSetting;
}
