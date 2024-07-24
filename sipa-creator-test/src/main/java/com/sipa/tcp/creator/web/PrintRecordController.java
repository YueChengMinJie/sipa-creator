package com.sipa.tcp.creator.web;

import javax.annotation.Resource;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sipa.boot.core.pojo.form.IdsForm;
import com.sipa.boot.mvc.allinone.SipaRest;
import com.sipa.tcp.creator.api.PrintRecordApi;
import com.sipa.tcp.creator.form.PrintRecordForm;
import com.sipa.tcp.creator.form.PrintRecordPageForm;
import com.sipa.tcp.creator.service.PrintRecordService;
import com.sipa.tcp.creator.vo.PrintRecordVo;

import lombok.extern.slf4j.Slf4j;

/**
 * @author caszhou
 * @date 2024-07-24
 */
@Slf4j
@SipaRest(tag = "打印记录", path = PrintRecordApi.MappingConstant.PATH)
public class PrintRecordController implements PrintRecordApi {
    @Resource
    private PrintRecordService printRecordService;

    @Override
    public Page<PrintRecordVo> list(PrintRecordPageForm form) {
        return null;
    }

    @Override
    public PrintRecordVo getById(String id) {
        return null;
    }

    @Override
    public Object create(PrintRecordForm form) {
        return null;
    }

    @Override
    public Object delete(IdsForm form) {
        return null;
    }

    @Override
    public Object update(PrintRecordForm form) {
        return null;
    }
}
