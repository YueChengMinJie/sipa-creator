package com.sipa.tcp.creator.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sipa.tcp.creator.mbp.MbpGenerator;
import com.sipa.tcp.creator.mbp.MpgGenCode;
import com.sipa.tcp.creator.util.PathUtil;

import cn.hutool.core.lang.Tuple;
import lombok.AllArgsConstructor;

/**
 * @author caszhou
 * @date 2022/6/10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/mbp-generator")
public class MbpGeneratorController {
    private final MbpGenerator mbpGenerator;

    @PostMapping("/gen-code")
    public void genCode(@RequestBody MpgGenCode dto, HttpServletResponse response) {
        Tuple tuple = mbpGenerator.genCodeBatch(dto.getTenant(), dto.getGenSetting(), dto.getTables());
        PathUtil.downloadZip(response, tuple);
    }
}
